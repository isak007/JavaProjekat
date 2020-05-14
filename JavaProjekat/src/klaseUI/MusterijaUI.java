package klaseUI;

import modeli.Musterija;
import modeli.Servis;
import modeli.ServisniDeo;
import modeli.Administrator;
import modeli.Automobil;
import java.util.Scanner;

import citanjePisanje.CitanjePisanje;
import enumeracije.Gorivo;
import enumeracije.Marka;
import enumeracije.Model;
import enumeracije.StatusServisa;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MusterijaUI {
	private Musterija musterija;
	
	public MusterijaUI(Musterija musterija) {
		this.musterija = musterija;
		System.out.println("Dobrodosli, "+this.musterija.getIme());
		Scanner skener = new Scanner(System.in);
		String userInput;
		while(true) {
			double dug = this.dug();
			if (dug > 0) {
				System.out.println("Dug: "+dug);
				System.out.println("0. Otplacivanje duga");
			}
			System.out.println("1. Kreirajte rezervaciju za servis");
			System.out.println("2. Unos vaseg automobila u sistem");
			System.out.println("3. Otkazite servis");
			System.out.println("4. Pregled automobila");
			System.out.println("5. Pregled vasih servisa");
			System.out.println("6. Odjava sa sistema");
			System.out.print("> ");
			userInput = skener.nextLine();
			
			if (userInput.equals("0")&& dug > 0) {
				this.otplacivanjeDuga();
			}
			else if (userInput.equals("1")){
				this.kreiranjeRezervacije();
			}
			else if (userInput.equals("2")) {
				this.unosAutomobila();
			}
			else if (userInput.equals("3")) {
				this.otkazivanjeServisa();
			}
			else if (userInput.equals("4")) {
				this.pregledAutomobila();
			}
			else if (userInput.equals("5")) {
				this.pregledServisa();
			}
			else if (userInput.equals("6")) {
				break;
			}
			else {
				System.out.println("Unesite validnu opciju!");
			}
		}
		//skener.close();
	}
	
	public void inicijalizacijaPromjena() {
		ArrayList<Servis> listaSvihServisa = this.musterija.getListaSvihServisa();
		if (listaSvihServisa.size() > 0) {
			ArrayList<Servis> listaZakazanihServisa = this.musterija.getListaZakazanihServisa();	
			SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
			GregorianCalendar sad = new GregorianCalendar();
			GregorianCalendar termin = new GregorianCalendar();
			String datumString;
			for (int i = 0; i < listaSvihServisa.size(); i++) {
				for (int j = 0; j < listaZakazanihServisa.size(); j++) {
					if (listaSvihServisa.get(i).getId().equals(listaZakazanihServisa.get(j).getId())) {
						// provjera promjene servisa i modifikacija liste zakazanih servisa
						datumString = listaZakazanihServisa.get(j).getTermin();
						try {
							termin.setTime(format.parse(datumString));
							if (sad.after(termin)) {
								listaZakazanihServisa.get(j).setStatusServisa(StatusServisa.ZAVRSEN);
								listaZakazanihServisa.remove(j);
							}
						}
						catch(ParseException e) {
							e.printStackTrace();
							System.out.println("Invalidan format!");
						}
					}
				}
			}
		}
	}
	
	
	public void otplacivanjeDuga() {
		Scanner skener = new Scanner(System.in);
		double dug = this.dug();
		if (this.musterija.getBrojSakupljenihBodova() > 0) {
			while (true) {
				System.out.println("Da li zelite da iskoristite sakupljene bodove bodove?(da/ne): ");
				String userInput = skener.nextLine();			
				
				if (userInput.equalsIgnoreCase("da")) {
					double snizenje = this.musterija.getBrojSakupljenihBodova() * (dug*(2.0/100.0));
					dug -= snizenje;
					System.out.println("Uspesno ste iskoristili bodove, novi dug je: "+dug);
					this.musterija.setBrojSakupljenihBodova(0);
					break;
				}
				else if (userInput.equalsIgnoreCase("ne")) {
					break;
				}
				else {
					System.out.println("Unesite validnu opciju!");
				}
			}
			System.out.println("Dug("+dug+")"+" je uspesno otplacen!");
			
		}
		else {
			System.out.println("Dug("+dug+")"+" je uspesno otplacen!");
		}
		ArrayList<Servis> listaZavrsenihServisa = this.musterija.getListaZavrsenihServisa();
		for (Servis servis : listaZavrsenihServisa) {
			servis.setNamiren("da");
		}
		// dodavanje izmena u servis fajlu
		CitanjePisanje citanjePisanje = new CitanjePisanje();
		ArrayList<String> listaServisaString = citanjePisanje.citanjeFajla("servis.txt");
		citanjePisanje.prazanFajl("servis.txt");
		String [] servisString;
		String noviSadrzaj = "";
		for (int i = 0; i < listaServisaString.size(); i++) {
			servisString = listaServisaString.get(i).split(",");
			// servisString[6] = servisID;
			for (Servis servis : listaZavrsenihServisa) {
				if (servis.getId().equals(servisString[6])){
					// servisString[8] = servisNamiren
					servisString[8] = "da";
					break;
				}
			}
			for (int j = 0; j < servisString.length; j++) {
				noviSadrzaj += servisString[j] + ",";
			}
			noviSadrzaj = noviSadrzaj.substring(0, noviSadrzaj.length()-1);
			noviSadrzaj += "\r\n";
		}
		noviSadrzaj = noviSadrzaj.substring(0, noviSadrzaj.length()-2);
		citanjePisanje.pisanjeUfajl(noviSadrzaj, "servis.txt");
		// kreiranje prazne liste zavrsenih servisa
		this.musterija.setListaZavrsenihServisa(new ArrayList<Servis>());
		
	}
	
	
	
	public double dug() {
		ArrayList<Servis> listaZavrsenihServisa = this.musterija.getListaZavrsenihServisa();
		if (listaZavrsenihServisa.size() > 0) {
			double dug = 0;
			for (int i = 0; i < listaZavrsenihServisa.size(); i++) {
				dug += listaZavrsenihServisa.get(i).getCena();
			}
			return dug;
		}
		else {
			return 0;
		}
	}
	
	
	public void unosAutomobila() {
		Scanner skener = new Scanner(System.in);
		Musterija vlasnik = this.musterija;
		
		//
		HashMap<Marka, ArrayList<Model>> markeModeli = Administrator.getMarkeModeli();
		Marka marka = null;
		String userInput;
		while (true) {
			for (int i = 0; i < markeModeli.keySet().size(); i++) {
				for(Marka key: markeModeli.keySet()) {
					if (key.ordinal() == i) {
						System.out.println(i+") "+key);
						break;
					}
				}
			}
			System.out.print("Unesite marku automobila: ");
			userInput = skener.nextLine();
			try {
				int brojMarke = Integer.parseInt(userInput);
				if (brojMarke >= 0 && brojMarke < markeModeli.keySet().size()) {
					for(Marka key: markeModeli.keySet()) {
						if (key.ordinal() == brojMarke) {
							marka = key;
							break;
						}
					}
					break;
				}
				else {
					throw new NumberFormatException();
				}
			}
			catch(NumberFormatException e) {
				System.out.println("Unesite validnu opciju!");
			}
		}
		//
		ArrayList<Model> listaModela = markeModeli.get(marka);
		Model model = null;
		while(true) {
			for (int i = 0; i < listaModela.size(); i++) {
				System.out.println(i+") "+listaModela.get(i));
			}
			System.out.print("Unesite mmodel automobila: ");
			userInput = skener.nextLine();
			try {
				int brojModela = Integer.parseInt(userInput);
				if (brojModela >= 0 && brojModela < listaModela.size()) {
					model = listaModela.get(brojModela);
					break;
				}
				else {
					throw new NumberFormatException();
				}
			}
			catch(NumberFormatException e) {
				System.out.println("Unesite validnu opciju!");
			}	
		}
		
		//
		int godinaProizvodnje = 0;
		while (true) {
			System.out.print("Unesite godinu prozivodnje: ");
			userInput = skener.nextLine();
			
			try {
				godinaProizvodnje = Integer.parseInt(userInput);
				if (userInput.length() == 4) {
					break;
				}
				else {
					throw new NumberFormatException();
				}
			}
			catch(NumberFormatException e) {
				System.out.println("Unesite validnu godinu prozivodnje!");
			}
			
		}
		
		//
		double zapreminaMotora = 0;
		while (true) {
			System.out.print("Unesite zapreminu motora: ");
			userInput = skener.nextLine();
			
			try {
				zapreminaMotora = Double.parseDouble(userInput);
				break;
			}
			catch(NumberFormatException e) {
				System.out.println("Unesite validnu zapreminu motora!");
			}
			
		}
		
		//
		int snagaMotora = 0;
		while (true) {
			System.out.print("Unesite snagu motora: ");
			userInput = skener.nextLine();
			
			try {
				snagaMotora = Integer.parseInt(userInput);
				if (userInput.length() > 1 && userInput.length() < 4) {
					break;
				}
				else {
					throw new NumberFormatException();
				}
			}
			catch(NumberFormatException e) {
				System.out.println("Unesite validnu snagu motora!");
			}
			
		}
		
		//
		Gorivo gorivo = null;
		while (true) {
			System.out.println("1. Benzin");
			System.out.println("2. Dizel");
			System.out.print("Unesite tip goriva: ");
			userInput = skener.nextLine();
			
			if (userInput.equals("1")) {
				gorivo = Gorivo.BENZIN;
				break;
			}
			else if (userInput.equals("2")) {
				gorivo = Gorivo.DIZEL;
				break;
			}
			else {
				System.out.println("Unesite validnu opciju!");
			}
		}
		
		// dodavanje jedinstvenog ID-a
		ArrayList<Automobil> listaAutomobila = Administrator.getListaAutomobila();
		if (listaAutomobila.size() > 0) {
			boolean jedinstven = false;
			while (!jedinstven) {
				jedinstven = true;
				System.out.print("Unesite nov ID automobila: ");
				userInput = skener.nextLine();
				for (int i = 0; i < listaAutomobila.size(); i++) {
					if(listaAutomobila.get(i).getId() == userInput) {
						System.out.println("Ovaj ID vec postoji!");
						jedinstven = false;
						break;
					}
				}
			}
		}
		else {
			System.out.print("Unesite nov ID automobila: ");
			userInput = skener.nextLine();
		}
		String id = userInput;
		
		// pravljenje automobil objekta
		Automobil automobil = new Automobil(vlasnik,marka,model,godinaProizvodnje,
				zapreminaMotora,snagaMotora,gorivo,id);
		// dodavanje automobila musterijinoj listi automobila
		ArrayList<Automobil> listaOdredjenihAutomobila = this.musterija.getListaAutomobila();
		listaOdredjenihAutomobila.add(automobil);
		this.musterija.setListaAutomobila(listaOdredjenihAutomobila);
		// dodavanje automobila celoj listi automobila
		listaAutomobila.add(automobil);
		Administrator.setListaAutomobila(listaAutomobila);
		
		// upisivanje automobila u fajl
		CitanjePisanje pisanje = new CitanjePisanje();
		pisanje.pisanjeUfajl(automobil.toString(), "automobil.txt");
		System.out.println("Uspesno ste uneli automobil!");
		
	}
	
	
	public void kreiranjeRezervacije() {
		ArrayList<Automobil> listaAutomobila = this.musterija.getListaAutomobila();
		if (listaAutomobila.size() > 0) {
			Scanner skener = new Scanner(System.in);
			String userInput;
			int brojAutomobila;
			Automobil automobil = null;
			while(true) {
				this.pregledAutomobila();
				System.out.print("Unesite redni broj automobila koji zelite da zakazete za servis: ");
				userInput = skener.nextLine();
				
				try {
					brojAutomobila = Integer.parseInt(userInput);
					if (brojAutomobila < listaAutomobila.size() && brojAutomobila >= 0) {
						automobil = listaAutomobila.get(brojAutomobila);
						break;
					}
					else {
						System.out.println("Unesite validan broj automobila!");
					}
				}
				catch(NumberFormatException e) {
					System.out.println("Unesite validnu opciju!");
				}
			}
			
			if (automobil != null) {
				CitanjePisanje citanjePisanje = new CitanjePisanje();
				ArrayList<String> listaRezervacijaString = citanjePisanje.citanjeFajla("rezervacije.txt");
				String [] rezervacijaString;
				String automobilID;
				boolean rezervisan = false;
				for (int i = 0; i < listaRezervacijaString.size(); i++) {
					rezervacijaString = listaRezervacijaString.get(i).split(",");
					automobilID = rezervacijaString[0];
					if (automobil.getId().equals(automobilID)) {
						System.out.println("Ovaj automobil je vec poslat na rezervaciju!");
						rezervisan = true;
						break;
					}
				}
				
				ArrayList<Servis> listaSvihServisa = Administrator.getListaSvihServisa();
				for (int i = 0; i < listaSvihServisa.size(); i++) {
					if (listaSvihServisa.get(i).getAutomobil().getId().equals(automobil.getId()) && 
							listaSvihServisa.get(i).getStatusServisa().toString().equals("ZAKAZAN")){
						System.out.println("Ovaj automobil je vec zakazan za servis!");
						rezervisan = true;
						break;
					}
				}
				
				boolean postojeDelovi = false;
				ArrayList<ServisniDeo> listaServisnihDelova = Administrator.getListaSvihDelova();
				for (int i = 0; i < listaServisnihDelova.size(); i++) {
					String marka = listaServisnihDelova.get(i).getMarka().toString();
					String model = listaServisnihDelova.get(i).getModel().toString();
					if(automobil.getMarka().toString().equals(marka) &&
							automobil.getModel().toString().equals(model)) {
						postojeDelovi = true;
						break;
					}
				}
				if(!postojeDelovi) {
					System.out.println("Ne postoje servisni delovi za vas automobil!");
				}
				
				if (!rezervisan & postojeDelovi) {
					System.out.print("Unesite opis servisa: ");
					String opis = skener.nextLine();
					citanjePisanje.pisanjeUfajl(automobil.getId()+","+opis, "rezervacije.txt");
					System.out.println("Rezervacija je uspijesno poslata na obradu!");
				}
			}
			
			else {
				System.out.println("Ne postoji automobil!");
			}
		}
		else {
			System.out.println("Trenutno nemate automobila za rezervaciju!");
		}
		//skener.close();
	}
	
	public void pregledAutomobila() {
		ArrayList<Automobil> listaAutomobila = this.musterija.getListaAutomobila();
		if (listaAutomobila.size() > 0) {
			for(int i = 0; i < listaAutomobila.size(); i++) {
				System.out.println(i+") "+listaAutomobila.get(i));
			}
		}
		else {
			System.out.println("Trenutno nemate unetih automobila!");
		}
	}
	
	public void pregledServisa() {
		this.inicijalizacijaPromjena();
		ArrayList<Servis> listaSvihServisa = this.musterija.getListaSvihServisa();
		if (listaSvihServisa.size() > 0) {
			for (int i = 0; i < listaSvihServisa.size(); i++) {
				// ispis svih servisa
				System.out.println(i+") "+listaSvihServisa.get(i).toString());
			}
		}
		else {
			System.out.println("Jos uvek niste zakazali servis!");
		}
	}
	
	public void otkazivanjeServisa() {
		this.inicijalizacijaPromjena();
		ArrayList<Servis> listaZakazanihServisa = this.musterija.getListaZakazanihServisa();
		if(listaZakazanihServisa.size() > 0) {
			Scanner skener = new Scanner(System.in);
			String userInput;
			int brojServisa;
			Servis servis = null;
			while(true) {
				for (int i = 0; i < listaZakazanihServisa.size(); i++) {
					System.out.println(i+") "+listaZakazanihServisa.get(i));
				}
				System.out.print("Unesite broj servisa koji zelite da otkazete: ");
				userInput = skener.nextLine();
				
				try {
					brojServisa = Integer.parseInt(userInput);
					if (brojServisa < listaZakazanihServisa.size() && brojServisa >= 0) {
						servis = listaZakazanihServisa.get(brojServisa);
						listaZakazanihServisa.remove(brojServisa);
						break;
					}
					else {
						System.out.println("Unesite validan broj servisa!");
					}
				}
				catch(NumberFormatException e) {
					e.printStackTrace();
					System.out.println("Unesite validnu opciju!");
				}
			}
			//skener.close();
			
			if (servis != null) {
				// dodavanje izmena u servis fajlu
				CitanjePisanje citanjePisanje = new CitanjePisanje();
				ArrayList<String> listaServisaString = citanjePisanje.citanjeFajla("servis.txt");
				citanjePisanje.prazanFajl("servis.txt");
				String [] servisString;
				String noviSadrzaj = "";
				for (int i = 0; i < listaServisaString.size(); i++) {
					servisString = listaServisaString.get(i).split(",");
					// servisString[6] = servisID;
					if(servis.getId().equals(servisString[6])){
						// servisString[5] = statusServisa
						servisString[5] = "OTKAZAN";
					}
					for (int j = 0; j < servisString.length; j++) {
						noviSadrzaj += servisString[j] + ",";
					}
					noviSadrzaj = noviSadrzaj.substring(0, noviSadrzaj.length()-1);
					noviSadrzaj += "\r\n";
				}
				noviSadrzaj = noviSadrzaj.substring(0, noviSadrzaj.length()-2);
				citanjePisanje.pisanjeUfajl(noviSadrzaj, "servis.txt");
				
				// dodavanje izmena u objektu
				servis.setStatusServisa(StatusServisa.OTKAZAN);
				this.musterija.setListaZakazanihServisa(listaZakazanihServisa);
				System.out.println("Uspesno ste otkazali servis!");
			}
			else {
				System.out.println("Servis ne postoji!");
			}
		}
		else {
			System.out.println("Nemate zakazanih servisa!");
		}
	}
	
}
