package klaseUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.Scanner;

import citanjePisanje.CitanjePisanje;
import enumeracije.StatusServisa;
import modeli.Administrator;
import modeli.Automobil;
import modeli.Servis;
import modeli.Serviser;
import modeli.ServisnaKnjizica;
import modeli.ServisniDeo;

public class ServiserUI {
	private Serviser serviser;
	
	public ServiserUI(Serviser serviser) {
		this.serviser = serviser;
		System.out.println("Dobrodosli, "+this.serviser.getIme());
		Scanner skener = new Scanner(System.in);
		String userInput;
		while (true) {
			this.inicijalizacijaPromjena();
			System.out.println("1. Pregled rezervacija");
			System.out.println("2. Pregled svih servisa");
			System.out.println("3. Zavrsi servis");
			System.out.println("4. Izmena servisa");
			System.out.println("5. Odjava sa sistema");
			System.out.print("> ");
			
			userInput = skener.nextLine();
		
			if (userInput.equals("1")){
				this.pregledRezervacija();
			}
			else if (userInput.equals("2")) {
				this.pregledServisa();
			}
			else if (userInput.equals("3")) {
				this.zavrsiServis();
			}
			else if (userInput.equals("4")) {
				this.izmenaServisa();
			}
			else if (userInput.equals("5")) {
				break;
			}
			else {
				System.out.println("Unesite validnu opciju!");
			}
		}
		//skener.close();
	}
	
	public void zavrsiServis() {
		if (this.serviser.getListaNezavrsenihServisa().size() > 0) {
			this.inicijalizacijaPromjena();
			Scanner skener = new Scanner(System.in);
			ArrayList<Servis> listaNezavrsenihServisa = this.serviser.getListaNezavrsenihServisa();
			while(true) {
				for (int i = 0; i < listaNezavrsenihServisa.size(); i++) {
					System.out.println(i+") "+listaNezavrsenihServisa.get(i).toString());
				}
				System.out.print("Unesite broj servisa koji zelite da zavrsite: ");
				String userInput = skener.nextLine();
			
				try {
					int brojServisa = Integer.parseInt(userInput);
					if (brojServisa >= 0 && brojServisa < listaNezavrsenihServisa.size()) {
						System.out.print("Unesite cenu troskova servisa: ");
						userInput = skener.nextLine();
						double cena = Double.parseDouble(userInput);
						Servis servis = listaNezavrsenihServisa.get(brojServisa);
						servis.setStatusServisa(StatusServisa.ZAVRSEN);
						// modifikovanje tesktualnog fajla servisa 
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
								servisString[5] = "ZAVRSEN";
							}
							for (int j = 0; j < servisString.length; j++) {
								noviSadrzaj += servisString[j] + ",";
							}
							noviSadrzaj = noviSadrzaj.substring(0, noviSadrzaj.length()-1);
							noviSadrzaj += "\r\n";
						}
						noviSadrzaj = noviSadrzaj.substring(0, noviSadrzaj.length()-2);
						citanjePisanje.pisanjeUfajl(noviSadrzaj, "servis.txt");
						// dodavanje cene svakog dela ukupnog ceni
						ArrayList<ServisniDeo> listaDelova = servis.getListaDelova();
						for (int j = 0; j < listaDelova.size(); j++) {
							cena += listaDelova.get(j).getCena();
						}
						servis.setCena(cena);
						// uklanjanje servisa iz liste nezavrsenih servisa(serviser)
						listaNezavrsenihServisa.remove(brojServisa);
						this.serviser.setListaNezavrsenihServisa(listaNezavrsenihServisa);
						// uklanjanje servisa iz liste zakazanih servisa(musterija)
						ArrayList<Servis>listaZakazanihServisa = servis.getAutomobil().getVlasnik().getListaZakazanihServisa();
						listaZakazanihServisa.remove(servis);
						servis.getAutomobil().getVlasnik().setListaZakazanihServisa(listaZakazanihServisa);
						// dodavanje zavrsenog servisa listi zavrsenih servisa(musterija)
						ArrayList<Servis>listaZavrsenihServisa = servis.getAutomobil().getVlasnik().getListaZavrsenihServisa();
						listaZavrsenihServisa.add(servis);
						servis.getAutomobil().getVlasnik().setListaZavrsenihServisa(listaZavrsenihServisa);
						// dodavanje boda musteriji zbog zavrsenog servisa
						int brojBodova = servis.getAutomobil().getVlasnik().getBrojSakupljenihBodova() + 1;
						if (brojBodova > 10) {
							brojBodova = 10;
						}
						servis.getAutomobil().getVlasnik().setBrojSakupljenihBodova(brojBodova);
						//
						System.out.println("Ukupna cena servisa je: "+cena);
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
		}
		else {
			System.out.println("Nemate servisa za zavrsavanje!");
		}
	}
	
	
	public void inicijalizacijaPromjena() {
		ArrayList<Servis> listaServisa = this.serviser.getListaServisa();
		if (listaServisa.size() > 0) {
			for (int i = 0; i < listaServisa.size(); i++) {
				SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
				GregorianCalendar sad = new GregorianCalendar();
				GregorianCalendar termin = new GregorianCalendar();
				String datumString;
				// provjera promjene servisa
				datumString = listaServisa.get(i).getTermin();
				try {
					termin.setTime(format.parse(datumString));
					if (sad.after(termin)) {
						ArrayList<Servis> listaNezavrsenihServisa = this.serviser.getListaNezavrsenihServisa();
						if (!listaNezavrsenihServisa.contains(listaServisa.get(i)) && 
								listaServisa.get(i).getStatusServisa().toString().equals("ZAKAZAN")) {
							listaNezavrsenihServisa.add(listaServisa.get(i));
						}
					}
				}
				catch(ParseException e) {
					e.printStackTrace();
					System.out.println("Invalidan format!");
				}
			}
		}
	}
	
	
	public void pregledServisa() {
		/*Serviser serviser = null;
		for (int i = 0; i < Administrator.getListaServisera().size(); i++) {
			if (this.serviser.getId() == Administrator.getListaServisera().get(i).getId()) {
				serviser = Administrator.getListaServisera().get(i);
			}
		}*/
		//if (serviser != null) {
		ArrayList<Servis> listaServisa = this.serviser.getListaServisa();
		if (listaServisa.size() > 0) {
			for (int i = 0; i < listaServisa.size(); i++) {
				System.out.println(i+") "+listaServisa.get(i).toString());
			}
		}
		else {
			System.out.println("Niste jos opredeljeni ni za jedan servis!");
		}
		/*else {
			System.out.println("Ne postoji serviser!");
		}*/
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
						this.kreiranjeServisa(automobilID, opis);
						listaRezervacija.remove(i);
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
	
	
	
	public void kreiranjeServisa(String automobilID, String opis) {
		Scanner skener = new Scanner(System.in);
		
		// pronalazenje automobila koji je postavljen za servis, preko toString metode
		ArrayList<Automobil> listaAutomobila = Administrator.getListaAutomobila();
		Automobil automobilObj = null;
		for (int i = 0; i < listaAutomobila.size(); i++) {
			if (listaAutomobila.get(i).getId().toString().equals(automobilID)) {
				automobilObj = listaAutomobila.get(i);
				break;
			}
		}
		
		Serviser serviser = this.serviser;
		
		
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
			
			ArrayList<Servis> servisiKnjizice = new ArrayList<Servis>();
			servisiKnjizice.add(servis);
			ServisnaKnjizica servisnaKnjizica = new ServisnaKnjizica(automobilObj,servisiKnjizice, id);
			listaServisnihKnjizica.add(servisnaKnjizica);
			Administrator.setListaServisnihKnjizica(listaServisnihKnjizica);
			pisanje.pisanjeUfajl(servisnaKnjizica.toString(), "servisnaKnjizica.txt");
		}
		
		pisanje.pisanjeUfajl(servis.toString(), "servis.txt");
		System.out.println("Servis je uspesno zakazan!");
	}
	
	
	
	public void izmenaServisa() {
		this.inicijalizacijaPromjena();
		ArrayList<Servis> listaServisa = this.serviser.getListaServisa();
		if (listaServisa.size() > 0) {
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
					if (brojServisa < listaServisa.size() && brojServisa >= 0) {
						servis = listaServisa.get(brojServisa);
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
					if(listaServisnihDelova.size() > 0) {
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
					else {
						System.out.println("Servisni delovi jos uvek nisu definisani!");
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
