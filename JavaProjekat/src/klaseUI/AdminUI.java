package klaseUI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import modeli.*;
import citanjePisanje.CitanjePisanje;
import enumeracije.Marka;
import enumeracije.Model;
import enumeracije.Pol;
import enumeracije.Specijalizacija;
import enumeracije.StatusServisa;
import enumeracije.Uloga;

public class AdminUI {
	private Administrator admin;
	
	public AdminUI (Administrator admin) {
		this.admin = admin;
		System.out.println("Dobrodosli, "+this.admin.getIme());
		Scanner skener = new Scanner(System.in);
		String userInput;
		while (true) {
			System.out.println("1. Pregled rezervacija");
			System.out.println("2. Pregled svih servisa");
			System.out.println("3. Izmena servisa");
			System.out.println("4. Registracija novog korisnika");
			System.out.println("5. Dodavanje novog servisnog dela");
			System.out.println("6. Odjava sa sistema");
			System.out.print("> ");
			
			userInput = skener.nextLine();
		
			if (userInput.equals("1")){
				this.pregledRezervacija();
			}
			else if (userInput.equals("2")) {
				this.pregledServisa();
			}
			else if (userInput.equals("3")) {
				this.izmenaServisa();
			}
			else if (userInput.equals("4")) {
				this.registracijaKorisnika();
			}
			else if (userInput.equals("5")) {
				this.dodavanjeServisnogDela();
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
	
	// metode
	
	public void inicijalizacijaPromjena() {
		ArrayList<Servis> listaSvihServisa = Administrator.getListaSvihServisa();
		if(listaSvihServisa.size() > 0) {
			SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
			GregorianCalendar sad = new GregorianCalendar();
			GregorianCalendar termin = new GregorianCalendar();
			String datumString;
			for (int i = 0; i < listaSvihServisa.size(); i++) {
				// provjera promjene servisa
				datumString = listaSvihServisa.get(i).getTermin();
				try {
					termin.setTime(format.parse(datumString));
					if (sad.after(termin)) {
						listaSvihServisa.get(i).setStatusServisa(StatusServisa.ZAVRSEN);
					}
				}
				catch(ParseException e) {
					e.printStackTrace();
					System.out.println("Invalidan format!");
				}
			}
		}
	}
	
	
	public void dodavanjeServisnogDela() {
		Scanner skener = new Scanner(System.in);
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
			System.out.print("Unesite marku automobila kome je deo namenjen: ");
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
			System.out.print("Unesite model automobila kome je deo namenjen: ");
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
		// naziv
		System.out.print("Unesite naziv dela: ");
		String naziv = skener.nextLine();
		
		// cena
		double cena = 0;
		while(true) {
			System.out.print("Unesite cenu dela: ");
			userInput = skener.nextLine();
			
			try {
				cena = Double.parseDouble(userInput);
				break;
			}
			catch(NumberFormatException e) {
				System.out.println("Unesite validnu cenu dela!");
			}
		}
		
		// id
		ArrayList<ServisniDeo> listaServisnihDelova = Administrator.getListaSvihDelova();
		String id = "";
		boolean jedinstven = false;
		while (!jedinstven) {
			jedinstven = true;
			System.out.println("Unesite nov ID servisnog dela: ");
			id = skener.nextLine();
			for (int i = 0; i < listaServisnihDelova.size(); i++) {
				if (listaServisnihDelova.get(i).getId().equals(id)){
					System.out.println("Ovaj ID vec postoji!");
					jedinstven = false;
				}
			}
		}
		String suprotnaStrana = "";
		String strana = "";
		// simetricni deo
		boolean postojiSimetricni = false;
		if(naziv.contains("Desna strana") || naziv.contains("desna strana") ||
				naziv.contains("Leva strana") || naziv.contains("leva strana")) {
			postojiSimetricni = true;
			while(true) {
				System.out.print("Da li zelite da dodate simetricni deo?(da/ne): ");
				userInput = skener.nextLine();
				
				if(userInput.equalsIgnoreCase("da")) {
					if(naziv.contains("Desna strana")) {
						strana = "Desna strana";
						suprotnaStrana = "Leva strana";
					}
					else if(naziv.contains("desna strana")) {
						strana = "desna strana";
						suprotnaStrana = "leva strana";
					}
						
					else if(naziv.contains("Leva strana")){
						strana = "Leva strana";
						suprotnaStrana = "Desna strana";
					}
					else if(naziv.contains("leva strana")) {
						strana = "leva strana";
						suprotnaStrana = "desna strana";
					}
					
					break;
				}
				else if(userInput.equalsIgnoreCase("ne")){
					postojiSimetricni = false;
					break;
				}
				else{
					System.out.println("Unesite validnu opciju!");
				}
			}
			
		}
		
		CitanjePisanje pisanje = new CitanjePisanje();
		// kreiranje objekta
		ServisniDeo servisniDeo = new ServisniDeo(marka,model,naziv,cena,id);
		if (postojiSimetricni) {
			String noviNaziv = naziv.replace(strana, suprotnaStrana);
			ServisniDeo servisniDeoSimetricni = new ServisniDeo(marka,model,noviNaziv,cena,id+"s");
			listaServisnihDelova.add(servisniDeoSimetricni);
			pisanje.pisanjeUfajl(servisniDeoSimetricni.toString(), "servisniDelovi.txt");
		}
		// dodavanje servisnog dela u listi servisnih delova
		listaServisnihDelova.add(servisniDeo);
		Administrator.setListaSvihDelova(listaServisnihDelova);
		// upisivanje dela u fajl
		pisanje.pisanjeUfajl(servisniDeo.toString(), "servisniDelovi.txt");
		System.out.println("Uspesno ste dodali servisni deo!");
		
	}
	
	
	
	
	//
	public void registracijaKorisnika() {
		
		Scanner skener = new Scanner(System.in);
		System.out.print("Unesite ime: ");
		String ime = skener.nextLine();
		System.out.print("Unesite prezime: ");
		String prezime = skener.nextLine();

		String jmbg;
		while (true) {
			System.out.print("Unesite JMBG: ");
			jmbg = skener.nextLine();
			try {
				Long.parseLong(jmbg);
				if (jmbg.length() != 13) {
					throw new NumberFormatException();
				}
				break;
			}
			catch (NumberFormatException ex) {
				System.out.println("Unesite validan JMBG!");
			}
		}
		Pol pol;
		String userChoice;
		while(true) {
			System.out.println("1. Muski");
			System.out.println("2. Zenski");
			System.out.print("Unesite pol: ");
			userChoice = skener.nextLine();
			if (userChoice.equals("1")) {
				pol = Pol.MUSKO;
				break;
			}
			else if (userChoice.equals("2")) {
				pol = Pol.ZENSKO;
				break;
			}
			else {
				System.out.println("Unesite validnu opciju!");
			}
		}
		System.out.print("Unesite adresu: ");
		String adresa = skener.nextLine();
		String brojTelefona;
		while (true) {
			System.out.print("Unesite broj telefona: ");
			brojTelefona = skener.nextLine();
			try {
				Integer.parseInt(brojTelefona);
				if (brojTelefona.length() != 10) {
					throw new NumberFormatException();
				}
				break;
			}
			catch (NumberFormatException ex) {
				System.out.println("Unesite validan broj telefona!");
			}
		}
		System.out.print("Unesite korisnicko ime: ");
		String korisnickoIme = skener.nextLine();
		System.out.print("Unesite lozinku: ");
		String lozinka = skener.nextLine();
		Uloga uloga;
		String fajl;
		while(true) {
			System.out.println("1. Musterija");
			System.out.println("2. Serviser");
			System.out.println("3. Administrator");
			System.out.print("Unesite ulogu: ");
			userChoice = skener.nextLine();
			
			// musterija
			if (userChoice.equals("1")) {
				fajl = "musterija.txt";
				uloga = Uloga.MUSTERIJA;
				
				ArrayList<Musterija> listaMusterija = Administrator.getListaMusterija();
				String id = "";
				boolean jedinstven = false;
				// provjera i dodavanje novog jedinstvenog ID-a
				while (!jedinstven) {
					jedinstven = true;
					System.out.print("Unesite nov ID musterije: ");
					id = skener.nextLine();
					for (int i = 0; i < listaMusterija.size(); i++) {
						if (id.equals(listaMusterija.get(i).getId())) {
							jedinstven = false;
							System.out.println("Ovaj ID vec postoji!");
							break;
						}
					}
				}
				
				int brojSakupljenihBodova = 0;
				Musterija korisnik = new Musterija(ime,prezime,jmbg,pol,adresa,brojTelefona,korisnickoIme,
						lozinka,uloga,id,brojSakupljenihBodova);
				listaMusterija.add(korisnik);
				Administrator.setListaMusterija(listaMusterija);
				CitanjePisanje pisanje = new CitanjePisanje();
				pisanje.pisanjeUfajl(korisnik.toString(), fajl);
				break;
			}
			
			// serviser
			else if (userChoice.equals("2")) {
				fajl = "serviser.txt";
				uloga = Uloga.SERVISER;
				
				ArrayList<Serviser> listaServisera = Administrator.getListaServisera();
				String id = "";
				boolean jedinstven = false;
				// provjera i dodavanje novog jedinstvenog ID-a
				while (!jedinstven) {
					jedinstven = true;
					System.out.print("Unesite nov ID servisera: ");
					id = skener.nextLine();
					for (int i = 0; i < listaServisera.size(); i++) {
						if (id.equals(listaServisera.get(i).getId())) {
							jedinstven = false;
							System.out.println("Ovaj ID vec postoji!");
							break;
						}
					}
				}
				String plataString;
				double plata;
				while (true) {
					System.out.print("Unesite platu: ");
					plataString = skener.nextLine();
					try {
						plata = Double.parseDouble(plataString);
						break;
					}
					catch (NumberFormatException ex) {
						System.out.println("Unesite validnu platu!");
					}
				}
				Specijalizacija specijalizacija;
				String userSpec;
				while(true) {
					System.out.println("1. Automehanicar");
					System.out.println("2. Auto-elektricar");
					System.out.println("3. Vulkanizer");
					System.out.println("4. Limar");
					System.out.print("Unesite specijalizaciju: ");
					userSpec = skener.nextLine();
					
					if (userSpec.equals("1")) {
						specijalizacija = Specijalizacija.AUTOMEHANICAR;
						break;
					}
					else if (userSpec.equals("2")) {
						specijalizacija = Specijalizacija.AUTOELEKTRICAR;
						break;
					}
					else if (userSpec.equals("3")) {
						specijalizacija = Specijalizacija.VULKANIZER;
						break;
					}
					else if (userSpec.equals("4")) {
						specijalizacija = Specijalizacija.LIMAR;
						break;
					}
					else {
						System.out.println("Unesite validnu opciju!");
					}
					
				}
				Serviser korisnik = new Serviser(ime,prezime,jmbg,pol,adresa,brojTelefona,korisnickoIme,
						lozinka,uloga,id,plata,specijalizacija);
				listaServisera.add(korisnik);
				Administrator.setListaServisera(listaServisera);
				CitanjePisanje pisanje = new CitanjePisanje();
				pisanje.pisanjeUfajl(korisnik.toString(), fajl);
				break;
			}
			
			// administrator
			else if (userChoice.equals("3")) {
				fajl = "administrator.txt";
				uloga = Uloga.ADMINISTRATOR;
				
				ArrayList<Administrator> listaAdministratora = Administrator.getListaAdministratora();
				String id = "";
				boolean jedinstven = false;
				// provjera i dodavanje novog jedinstvenog ID-a
				while (!jedinstven) {
					jedinstven = true;
					System.out.print("Unesite nov ID admina: ");
					id = skener.nextLine();
					for (int i = 0; i < listaAdministratora.size(); i++) {
						if (id.equals(listaAdministratora.get(i).getId())) {
							jedinstven = false;
							System.out.println("Ovaj ID vec postoji!");
							break;
						}
					}
				}
				
				String plataString;
				double plata;
				while (true) {
					System.out.print("Unesite platu: ");
					plataString = skener.nextLine();
					try {
						plata = Double.parseDouble(plataString);
						break;
					}
					catch (NumberFormatException ex) {
						System.out.println("Unesite validnu platu!");
					}
				}
				Administrator korisnik = new Administrator(ime,prezime,jmbg,pol,adresa,brojTelefona,korisnickoIme,
						lozinka,uloga,id,plata);
				listaAdministratora.add(korisnik);
				Administrator.setListaAdministratora(listaAdministratora);
				CitanjePisanje pisanje = new CitanjePisanje();
				pisanje.pisanjeUfajl(korisnik.toString(), fajl);
				break;
			}
			else {
				System.out.println("Unesite validnu opciju!");
			}
		}
		//skener.close();
	}
	
	public void pregledRezervacija() {
		CitanjePisanje citanjePisanje = new CitanjePisanje() {};
		ArrayList<String> listaRezervacija = citanjePisanje.citanjeFajla("rezervacije.txt");
		if (listaRezervacija.size() > 0) {
			Scanner skener = new Scanner(System.in);
			String userChoice;
			for (int i = 0; i < listaRezervacija.size(); i++) {
				System.out.println(listaRezervacija.get(i));
				while (true) {
					System.out.print("Da li zelite da zakazete servis?(da/ne): ");
					userChoice = skener.nextLine();
					if (userChoice.equalsIgnoreCase("da")){
						String[] rezervacija = listaRezervacija.get(i).split(",");
						String automobilID = rezervacija[0];
						String opis = rezervacija[1];
						if (this.kreiranjeServisa(automobilID, opis)) {
							listaRezervacija.remove(i);
						}
						break;
					}
					else if (userChoice.equalsIgnoreCase("ne")) {
						break;
					}
					else {
						System.out.println("Unesite validnu opciju!");
					}
				}
			}
			//skener.close();
			citanjePisanje.prazanFajl("rezervacije.txt");
			for (int i = 0; i < listaRezervacija.size(); i++) {
				citanjePisanje.pisanjeUfajl(listaRezervacija.get(i), "rezervacije.txt");
			}
		}
		else {
			System.out.println("Trenutno nema rezervacija!");
		}
	}
	
	public void pregledServisa() {
		this.inicijalizacijaPromjena();
		ArrayList<Servis> listaSvihServisa = Administrator.getListaSvihServisa();
		if(listaSvihServisa.size() > 0) {
			for (int i = 0; i < listaSvihServisa.size(); i++) {
				// ispis servisa
				System.out.println(i+") "+listaSvihServisa.get(i).toString());
			}
		}
		else {
			System.out.println("Trenutno nema servisa!");
		}
	}
	
	public boolean kreiranjeServisa(String automobilID, String opis) {
		ArrayList<Serviser> listaServisera = Administrator.getListaServisera();
		if(listaServisera.size() > 0) {
			Scanner skener = new Scanner(System.in);
		
			// pronalazenje automobila koji je postavljen za servis, preko toString metode
			ArrayList<Automobil> listaAutomobila = Administrator.getListaAutomobila();
			Automobil automobilObj = null;
			for (int i = 0; i < listaAutomobila.size(); i++) {
				if (listaAutomobila.get(i).getId().equals(automobilID)) {
					automobilObj = listaAutomobila.get(i);
					break;
				}
			}
			
			// odredjivanje servisera koji ce obaviti dati servis
			
			Serviser serviser = null;
			String userChoice;
			while(true) {
				for (int i = 0; i < listaServisera.size(); i++) {
					System.out.println(listaServisera.get(i).toString());
					while (true) {
						System.out.print("Zakazite servisera?(da/ne): ");
						userChoice = skener.nextLine();
						if (userChoice.equalsIgnoreCase("da") || userChoice.equalsIgnoreCase("ne")) {
							break;
						}
						else {
							System.out.println("Ünesite validnu opciju!");
						}
					}
					if (userChoice.equalsIgnoreCase("da")){
						serviser = listaServisera.get(i);
						break;
					}
				}
				if (serviser == null) {
					System.out.println("Morate zakazati servisera da bi ste nastavili!");
				}
				else {
					break;
				}
			}
			
			
			// odredjivanje datuma
			GregorianCalendar sad;
			GregorianCalendar datum = new GregorianCalendar();
			String userInput;
			String termin;
			while(true) {
				try {
					sad = new GregorianCalendar();
					SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
					System.out.print("Unesite datum i vreme servisa(format year-month-day hour:minute): ");
					userInput = skener.nextLine();
					datum.setTime(format.parse(userInput));
					
					if (sad.before(datum)) {
						termin = userInput;
						break;
					}
					else {
						System.out.println("Unesite validno vreme!");
					}
					 
				}
				catch(InputMismatchException e) {
					e.printStackTrace();
				}
				catch (ParseException e) {
					System.out.println("Unesite validan format!");
				}
			}
			
			
			
			
			// dodavanje delova za servis
			ArrayList<ServisniDeo> listaOdgovarajucihDelova = new ArrayList<ServisniDeo>();
			ArrayList<ServisniDeo> listaSvihDelova = Administrator.getListaSvihDelova();
			for (int i = 0; i < listaSvihDelova.size(); i++) {
				String marka = listaSvihDelova.get(i).getMarka().toString();
				String model = listaSvihDelova.get(i).getModel().toString();
				if (automobilObj.getMarka().toString().equals(marka) && 
						automobilObj.getModel().toString().equals(model)) {
					listaOdgovarajucihDelova.add(listaSvihDelova.get(i));
				}
			}
			// servisni delovi
			ArrayList<ServisniDeo> listaDelova = new ArrayList<ServisniDeo>();
			while (true) {
				if (listaOdgovarajucihDelova.size() > 0) {
					for(int i = 0; i < listaOdgovarajucihDelova.size(); i++) {
						System.out.println(i+") "+listaOdgovarajucihDelova.get(i).toString());
					}
					System.out.print("Unesite broj servisnog dela koji zelite da dodate za servis: ");
					userInput = skener.nextLine();
					
					try {
						int brojSD = Integer.parseInt(userInput);
						if(brojSD >= 0 && brojSD < listaOdgovarajucihDelova.size()) {
							listaDelova.add(listaOdgovarajucihDelova.get(brojSD));
							listaOdgovarajucihDelova.remove(brojSD);
						}
						else {
							throw new NumberFormatException();
						}
						
					}
					catch(NumberFormatException e) {
						System.out.println("Unesite validnu opciju!");
						continue;
					}
				}
				else {
					System.out.println("Ne postoji vise servisnih delova za dodavanje!");
					break;
				}
				
				boolean kraj = true;
				while(true) {
					System.out.print("Da li zelite da dodate jos delova za servis?(da/ne): ");
					userInput = skener.nextLine();
					if (userInput.equalsIgnoreCase("da")) {
						kraj = false;
						break;
					}
					else if(userInput.equalsIgnoreCase("ne")) {
						kraj = true;
						break;
					}
					else {
						System.out.println("Unesite validnu opciju!");
					}
					
				}
				if (kraj) {
					break;
				}
				
			}
			
			ArrayList<Servis> listaOdredjenihServisa = serviser.getListaServisa(); // servisi odredjeni serviseru
			ArrayList<Servis> listaServisa = Administrator.getListaSvihServisa(); // svi servisi
			String id = "";
			boolean jedinstven = false;
			// provjera i dodavanje novog jedinstvenog ID-a
			while (!jedinstven) {
				jedinstven = true;
				System.out.print("Unesite nov ID servisa: ");
				id = skener.nextLine();
				for (int i = 0; i < listaServisa.size(); i++) {
					if (id.equals(listaServisa.get(i).getId())) {
						jedinstven = false;
						System.out.println("Ovaj ID vec postoji!");
						break;
					}
				}
			}			
			
			//skener.close();
			
			CitanjePisanje pisanje = new CitanjePisanje();
			Servis servis = new Servis(automobilObj, serviser, termin, opis, listaDelova, StatusServisa.ZAKAZAN, id, 0, "ne");
			// dodavanje servisa musteriji
			ArrayList<Servis> listaMServisa = automobilObj.getVlasnik().getListaSvihServisa();
			ArrayList<Servis> listaZakazanihMServisa = automobilObj.getVlasnik().getListaZakazanihServisa();
			listaMServisa.add(servis);
			listaZakazanihMServisa.add(servis);
			automobilObj.getVlasnik().setListaSvihServisa(listaMServisa);
			automobilObj.getVlasnik().setListaZakazanihServisa(listaZakazanihMServisa);
			// dodavanje servisa serviseru zaduzenom za taj servis
			listaOdredjenihServisa.add(servis);
			serviser.setListaServisa(listaOdredjenihServisa);
			// dodavanje servisa u listi svih servisa(administratoru)
			listaServisa.add(servis);
			Administrator.setListaSvihServisa(listaServisa);
			//
			ArrayList<ServisnaKnjizica> listaServisnihKnjizica = Administrator.getListaServisnihKnjizica();
			boolean postojiServisnaKnjizica = false;
			for (int i = 0; i < listaServisnihKnjizica.size(); i++) {
				// dodavanje servisa u servisnoj knjzici ako postoji servisna knjizica
				if (listaServisnihKnjizica.get(i).getAutomobil().getId().equals(automobilObj.getId())) {
					ArrayList<Servis> servisiKnjizice = listaServisnihKnjizica.get(i).getListaServisa();
					servisiKnjizice.add(servis);
					listaServisnihKnjizica.get(i).setListaServisa(servisiKnjizice);
					postojiServisnaKnjizica = true;
					break;
				}
			}
			// dodavanje nove servisne knjizice ako ne postoji i servisa
			if (!postojiServisnaKnjizica) {
				id = "";
				jedinstven = false;
				while(!jedinstven) {
					jedinstven = true;
					System.out.print("Unesite nov ID servisne knjizice: ");
					id = skener.nextLine();
					for (int i = 0; i < listaServisnihKnjizica.size(); i++) {
						if (listaServisnihKnjizica.get(i).getId().equals(id)){
							jedinstven = false;
							System.out.println("Ovaj ID vec postoji!");
							break;
						}
					}
				}
				ArrayList <Servis> servisiKnjizice = new ArrayList<Servis>();
				servisiKnjizice.add(servis);
				ServisnaKnjizica servisnaKnjizica = new ServisnaKnjizica(automobilObj,servisiKnjizice, id);
				listaServisnihKnjizica.add(servisnaKnjizica);
				Administrator.setListaServisnihKnjizica(listaServisnihKnjizica);
				pisanje.pisanjeUfajl(servisnaKnjizica.toString(), "servisnaKnjizica.txt");
			}
			
			pisanje.pisanjeUfajl(servis.toString(), "servis.txt");
			System.out.println("Servis je uspesno zakazan!");
			return true;
		}
		else {
			System.out.println("Ne postoje serviseri u sistemu!");
			return false;
		}
			
			
			
	}
	
	public void izmenaServisa() {
		this.inicijalizacijaPromjena();
		ArrayList<Servis> listaSvihServisa = Administrator.getListaSvihServisa();
		if(listaSvihServisa.size() > 0) {
			Scanner skener = new Scanner(System.in);
			String userInput;
			int brojServisa;
			this.pregledServisa();
			Servis servis;
			
			// biranje servisa koji zelimo da izmenimo na osnovu rednog broja
			while(true) {
				System.out.print("Unesite broj servisa koji zelite da izmenite: ");
				userInput = skener.nextLine();
				
				try {
					brojServisa = Integer.parseInt(userInput);
					if (brojServisa < listaSvihServisa.size() && brojServisa >= 0) {
						servis = listaSvihServisa.get(brojServisa);
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
			
			while(true) {
				System.out.println("Da li zelite da izmenite:");
				System.out.println("1. Opis");
				System.out.println("2. Cenu");
				System.out.print("> ");
				userInput = skener.nextLine();
				if (userInput.equals("1")) {
					System.out.print("Unesite novi opis: ");
					String opis = skener.nextLine();
					servis.setOpis(opis);
					while(true) {
						System.out.print("Da li zelite da izmenite jos nesto? (da/ne): ");
						userInput = skener.nextLine();
						if (userInput.equalsIgnoreCase("da") || userInput.equalsIgnoreCase("ne")) {
							break;
						}
						else {
							System.out.println("Unesite validnu opciju!");
						}
					}
					if (userInput.equalsIgnoreCase("ne")) {
						break;
					}
				}
				else if(userInput.equals("2")) {
					ArrayList<ServisniDeo> listaServisnihDelova = servis.getListaDelova();
					for (int i = 0; i < listaServisnihDelova.size(); i++) {
						System.out.println(i+") "+ listaServisnihDelova.get(i));
					}
					while(true) {
						System.out.print("Unesite broj dela ciju cenu zelite da izmenite: ");
						userInput = skener.nextLine();
						
						try {
							int brojDela = Integer.parseInt(userInput);
							if (brojDela < listaServisnihDelova.size() && brojDela >= 0) {
								System.out.print("Unesite novu cenu dela: ");
								userInput = skener.nextLine();
								int novaCena = Integer.parseInt(userInput);
								listaServisnihDelova.get(brojDela).setCena(novaCena);
								break;
							}
							else {
								System.out.println("Unesite validni broj dela!");
							}
						}
						catch(NumberFormatException e) {
							e.printStackTrace();
							System.out.println("Unesite validnu opciju!");
						}
					}
					while(true) {
						System.out.print("Da li zelite da izmenite jos nesto? (da/ne): ");
						userInput = skener.nextLine();
						if (userInput.equalsIgnoreCase("da") || userInput.equalsIgnoreCase("ne")) {
							break;
						}
						else {
							System.out.println("Unesite validnu opciju!");
						}
					}
					if (userInput.equalsIgnoreCase("ne")) {
						break;
					}
				}
			}
		}
		else {
			System.out.println("Trenutno nema zakazanih servisa!");
		}
		//skener.close();
	}
}
