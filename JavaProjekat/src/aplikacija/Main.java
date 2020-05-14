package aplikacija;
import klaseUI.*;

import java.util.ArrayList;
import java.util.HashMap;
import modeli.*;
import java.util.Scanner;

import enumeracije.Gorivo;
import enumeracije.Marka;
import enumeracije.Model;
import enumeracije.Pol;
import enumeracije.Specijalizacija;
import enumeracije.StatusServisa;
import enumeracije.Uloga;
import citanjePisanje.CitanjePisanje;

public class Main {

	public static void main(String[] args) {
		inicijalizacijaObjekata();
		inicijalizacijaMarkiModela();
		
		Scanner skener = new Scanner(System.in);
		String userInput = "";
		String korisnickoIme;
		String lozinka;
		while (true) {
			System.out.println("1. Musterija");
			System.out.println("2. Serviser");
			System.out.println("3. Administrator");
			System.out.println("4. IZLAZ iz aplikacije");
			System.out.print("> ");
			userInput = skener.nextLine();
			
			if (userInput.equals("1")) {
				System.out.print("Unesite korisnicko ime: ");
				korisnickoIme = skener.nextLine();
				System.out.print("Unesite lozinku: ");
				lozinka = skener.nextLine();
				if (prijava(korisnickoIme,lozinka,userInput)) {
					ArrayList<Musterija> listaMusterija = Administrator.getListaMusterija();
					Musterija musterija = null;
					for (int i = 0; i < listaMusterija.size(); i++) {
						if (listaMusterija.get(i).getKorisinickoIme().equals(korisnickoIme) &&
								listaMusterija.get(i).getLozinka().equals(lozinka)) {
							musterija = listaMusterija.get(i);
							break;
						}
					}
					// musterijaUI
					MusterijaUI musterijaUI = new MusterijaUI(musterija);

				}
				else {
					System.out.println("Neispravno korisnicko ime ili lozinka!");
				}
				
			}
			
			
			else if (userInput.equals("2")) {
				System.out.print("Unesite korisnicko ime: ");
				korisnickoIme = skener.nextLine();
				System.out.print("Unesite lozinku: ");
				lozinka = skener.nextLine();
				if (prijava(korisnickoIme,lozinka,userInput)) {
					ArrayList<Serviser> listaServisera = Administrator.getListaServisera();
					Serviser serviser = null;
					for (int i = 0; i < listaServisera.size(); i++) {
						if (listaServisera.get(i).getKorisinickoIme().equals(korisnickoIme) &&
								listaServisera.get(i).getLozinka().equals(lozinka)) {
							serviser = listaServisera.get(i);
							break;
						}
					}
					// seviserUI
					ServiserUI serviserUI = new ServiserUI(serviser);
				}
				else {
					System.out.println("Neispravno korisnicko ime ili lozinka!");
				}
				
			}
			
			
			else if (userInput.equals("3")) {
				System.out.print("Unesite korisnicko ime: ");
				korisnickoIme = skener.nextLine();
				System.out.print("Unesite lozinku: ");
				lozinka = skener.nextLine();
				if (prijava(korisnickoIme,lozinka,userInput)) {
					ArrayList<Administrator> listaAdministratora = Administrator.getListaAdministratora();
					Administrator admin = null;
					for (int i = 0; i < listaAdministratora.size(); i++) {
						if (listaAdministratora.get(i).getKorisinickoIme().equals(korisnickoIme) &&
								listaAdministratora.get(i).getLozinka().equals(lozinka)) {
							admin = listaAdministratora.get(i);
							break;
						}
					}
					// adminUI
					AdminUI adminUI = new AdminUI(admin);
				}
				else {
					System.out.println("Neispravno korisnicko ime ili lozinka!");
				}
				
			}

			
			else if (userInput.equals("4")) {
				break;
			}
			
			
			else {
				System.out.println("Unesite validnu opciju!");
			}
		}
		skener.close();
	}
	
	public static boolean prijava(String korisnickoIme, String lozinka, String uloga) {
		
		// musterija
		if (uloga.equals("1")) {
			ArrayList<Musterija> listaMusterija = Administrator.getListaMusterija();
			if (listaMusterija.size() > 0) {
				for (int i = 0; i < listaMusterija.size(); i++) {
					if (listaMusterija.get(i).getKorisinickoIme().equals(korisnickoIme) &&
							listaMusterija.get(i).getLozinka().equals(lozinka)) {
						return true;
					}
				}
				return false;
			}
			else {
				return false;
			}
		}
		
		// serviser
		else if (uloga.equals("2")) {
			ArrayList<Serviser> listaServisera = Administrator.getListaServisera();
			if (listaServisera.size() > 0) {
				for (int i = 0; i < listaServisera.size(); i++) {
					if (listaServisera.get(i).getKorisinickoIme().equals(korisnickoIme) &&
							listaServisera.get(i).getLozinka().equals(lozinka)) {
						return true;
					}
				}
				return false;
			}
			else {
				return false;
			}
		}
		
		// admin
		else {
			ArrayList<Administrator> listaAdmina = Administrator.getListaAdministratora();
			if (listaAdmina.size() > 0) {
				for (int i = 0; i < listaAdmina.size(); i++) {
					if (listaAdmina.get(i).getKorisinickoIme().equals(korisnickoIme) &&
							listaAdmina.get(i).getLozinka().equals(lozinka)) {
						return true;
					}
				}
				return false;
			}
			else {
				return false;
			}
		}
		
	}
	
	
	public static void inicijalizacijaMarkiModela() {
		HashMap<Marka, ArrayList<Model>> markeModeli = new HashMap<Marka, ArrayList<Model>>();
		Marka [] marke = Marka.values();
		ArrayList<Model> modeli;
		
		// kreiranje mape marke i modela, dodeljivanje svakoj marki po 5 modela
		int k = 0;
		for (int i = 0; i < marke.length; i++) {
			modeli = new ArrayList<Model>();
			for (int j = k; j < k+5; j++) {
				modeli.add(Model.values()[j]);
			}
			markeModeli.put(marke[i], modeli);
			k = k + 5;			
		}
		Administrator.setMarkeModeli(markeModeli);
	}
	
	public static void inicijalizacijaObjekata() {
		CitanjePisanje citanje = new CitanjePisanje();
		
		// ADMINISTRATOR
		ArrayList<String> listaAdminStringova = citanje.citanjeFajla("administrator.txt");
		if (listaAdminStringova.size() > 0) {
			ArrayList<Administrator> listaAdmina = Administrator.getListaAdministratora();
			String [] adminString;
			Administrator admin;
			String ime, prezime, jmbg, adresa, brojTelefona, korisnickoIme, lozinka, id, polString, ulogaString;
			Pol pol;
			Uloga uloga;
			double plata;
			
			// ime,prezime,jmbg,pol,adresa,br.tel.,kor.ime,lozinka,uloga,id,plata
			for (int i = 0; i < listaAdminStringova.size(); i++) {
				adminString = listaAdminStringova.get(i).split(",");
				ime = adminString[0];
				prezime = adminString[1];
				jmbg = adminString[2];
				polString = adminString[3];
				adresa = adminString[4];
				brojTelefona = adminString[5];
				korisnickoIme = adminString[6];
				lozinka = adminString[7];
				ulogaString = adminString[8];
				id = adminString[9];
				plata = Double.parseDouble(adminString[10]);
				
				// postavljanje pola
				if (Pol.values()[0].toString().equals(polString)) {
					pol = Pol.values()[0];
				}
				else {
					pol = Pol.values()[1];
				}
				
				// postavljanje uloge
				if(Uloga.values()[0].toString().equals(ulogaString)) {
					uloga = Uloga.values()[0];
				}
				else if(Uloga.values()[1].toString().equals(ulogaString)) {
					uloga = Uloga.values()[1];
				}
				else {
					uloga = Uloga.values()[2];
				}
				
				// kreiranje Administrator objekta i dodavanje listi administratora
				// ime,prezime,jmbg,pol,adresa,br.tel.,kor.ime,lozinka,uloga,id,plata
				admin = new Administrator(ime,prezime,jmbg,pol,adresa,brojTelefona,korisnickoIme,lozinka,uloga,id,plata);
				listaAdmina.add(admin);
				Administrator.setListaAdministratora(listaAdmina);
				
			}
		}
		
		// SERVISER
		ArrayList<String> listaServiserStringova = citanje.citanjeFajla("serviser.txt");
		if (listaServiserStringova.size() > 0) {
			ArrayList<Serviser> listaServisera = Administrator.getListaServisera();
			String [] serviserString;
			Serviser serviser;
			String ime, prezime, jmbg, adresa, brojTelefona, korisnickoIme, lozinka, id,
			polString, ulogaString, specijalizacijaString;
			Pol pol;
			Uloga uloga;
			double plata;
			Specijalizacija specijalizacija;
			
			// ime,prezime,jmbg,pol,adresa,br.tel.,kor.ime,lozinka,uloga,id,plata
			for (int i = 0; i < listaServiserStringova.size(); i++) {
				serviserString = listaServiserStringova.get(i).split(",");
				ime = serviserString[0];
				prezime = serviserString[1];
				jmbg = serviserString[2];
				polString = serviserString[3];
				adresa = serviserString[4];
				brojTelefona = serviserString[5];
				korisnickoIme = serviserString[6];
				lozinka = serviserString[7];
				ulogaString = serviserString[8];
				id = serviserString[9];
				plata = Double.parseDouble(serviserString[10]);
				specijalizacijaString = serviserString[11];
				
				// postavljanje POLA
				if (Pol.values()[0].toString().equals(polString)) {
					pol = Pol.values()[0];
				}
				else {
					pol = Pol.values()[1];
				}
				
				// postavljanje ULOGA
				if(Uloga.values()[0].toString().equals(ulogaString)) {
					uloga = Uloga.values()[0];
				}
				else if(Uloga.values()[1].toString().equals(ulogaString)) {
					uloga = Uloga.values()[1];
				}
				else {
					uloga = Uloga.values()[2];
				}
				
				// postavljanje SPECIJALIZACIJA
				if(Specijalizacija.values()[0].toString().equals(specijalizacijaString)) {
					specijalizacija = Specijalizacija.values()[0];
				}
				else if(Specijalizacija.values()[1].toString().equals(specijalizacijaString)) {
					specijalizacija = Specijalizacija.values()[1];
				}
				else if(Specijalizacija.values()[2].toString().equals(specijalizacijaString)) {
					specijalizacija = Specijalizacija.values()[2];
				}
				else {
					specijalizacija = Specijalizacija.values()[3];
				}
				
				
				// kreiranje Administrator objekta i dodavanje listi administratora
				// ime,prezime,jmbg,pol,adresa,br.tel.,kor.ime,lozinka,uloga,id,plata
				serviser = new Serviser(ime,prezime,jmbg,pol,adresa,brojTelefona,korisnickoIme,lozinka,
						uloga,id,plata,specijalizacija);
				listaServisera.add(serviser);
				Administrator.setListaServisera(listaServisera);
				
			}
		}
		
		// ADMINISTRATOR
		ArrayList<String> listaMusterijaStringova = citanje.citanjeFajla("musterija.txt");
		if (listaMusterijaStringova.size() > 0) {
			ArrayList<Musterija> listaMusterija = Administrator.getListaMusterija();
			String [] musterijaString;
			Musterija musterija;
			String ime, prezime, jmbg, adresa, brojTelefona, korisnickoIme, lozinka, id, 
			polString, ulogaString;
			Pol pol;
			Uloga uloga;
			int brojSakupljenihBodova;
			
			// ime,prezime,jmbg,pol,adresa,br.tel.,kor.ime,lozinka,uloga,id,plata
			for (int i = 0; i < listaMusterijaStringova.size(); i++) {
				musterijaString = listaMusterijaStringova.get(i).split(",");
				ime = musterijaString[0];
				prezime = musterijaString[1];
				jmbg = musterijaString[2];
				polString = musterijaString[3];
				adresa = musterijaString[4];
				brojTelefona = musterijaString[5];
				korisnickoIme = musterijaString[6];
				lozinka = musterijaString[7];
				ulogaString = musterijaString[8];
				id = musterijaString[9];
				brojSakupljenihBodova = Integer.parseInt(musterijaString[10]);
				
				// postavljanje pola
				if (Pol.values()[0].toString().equals(polString)) {
					pol = Pol.values()[0];
				}
				else {
					pol = Pol.values()[1];
				}
				
				// postavljanje uloge
				if(Uloga.values()[0].toString().equals(ulogaString)) {
					uloga = Uloga.values()[0];
				}
				else if(Uloga.values()[1].toString().equals(ulogaString)) {
					uloga = Uloga.values()[1];
				}
				else {
					uloga = Uloga.values()[2];
				}
				
				// kreiranje Administrator objekta i dodavanje listi administratora
				// ime,prezime,jmbg,pol,adresa,br.tel.,kor.ime,lozinka,uloga,id,plata
				musterija = new Musterija(ime,prezime,jmbg,pol,adresa,brojTelefona,korisnickoIme,lozinka,
						uloga,id,brojSakupljenihBodova);
				listaMusterija.add(musterija);
				Administrator.setListaMusterija(listaMusterija);
				
			}
		}
		
		
		// AUTOMOBIL
		ArrayList<String> listaAutomobilaString = citanje.citanjeFajla("automobil.txt");
		if (listaAutomobilaString.size() > 0) {
			ArrayList<Musterija> listaMusterija = Administrator.getListaMusterija();
			Marka [] listaMarki = Marka.values();
			Model [] listaModela = Model.values();
			
			String vlasnikString,markaString,modelString,gorivoString,id;
			//
			Musterija vlasnik = null;
			//
			Marka marka = null;
			//
			Model model = null;
			//
			int godinaProizvodnje;
			double zapreminaMotora;
			int snagaMotora;
			//
			Gorivo gorivo;
			//
			String [] automobilString;
			for (int i = 0; i < listaAutomobilaString.size(); i++) {
				automobilString = listaAutomobilaString.get(i).split(",");
				
				vlasnikString = automobilString[0];
				markaString = automobilString[1];
				modelString = automobilString[2];
				godinaProizvodnje = Integer.parseInt(automobilString[3]);
				zapreminaMotora = Double.parseDouble(automobilString[4]);
				snagaMotora = Integer.parseInt(automobilString[5]);
				gorivoString = automobilString[6];
				id = automobilString[7];
				
				// pronalazenje vlasnika
				for (int j = 0; j < listaMusterija.size(); j++) {
					if(listaMusterija.get(j).getId().equals(vlasnikString)) {
						vlasnik = listaMusterija.get(j);
						break;
					}
				}
				
				//
				for (int j = 0; j < listaMarki.length; j++) {
					if (listaMarki[j].toString().equals(markaString)) {
						marka = listaMarki[j];
						break;
					}
				}
				//
				for (int j = 0; j < listaModela.length; j++) {
					if (listaModela[j].toString().equals(modelString)) {
						model = listaModela[j];
						break;
					}
				}
				
				if(gorivoString.equals(Gorivo.values()[0].toString())) {
					gorivo = Gorivo.values()[0];
				}
				else {
					gorivo = Gorivo.values()[1];
				}
				
				// kreiranje Automobil objekata
				Automobil automobil = new Automobil(vlasnik,marka,model,godinaProizvodnje,zapreminaMotora,
						snagaMotora,gorivo,id);
				// dodavanje automobila odredjenoj musteriji
				ArrayList<Automobil> listaOdredjenihAutomobila = vlasnik.getListaAutomobila();
				listaOdredjenihAutomobila.add(automobil);
				vlasnik.setListaAutomobila(listaOdredjenihAutomobila);
				// dodavanje automobila listi sa svim automobilima
				ArrayList<Automobil> listaSvihAutomobila = Administrator.getListaAutomobila();
				listaSvihAutomobila.add(automobil);
				Administrator.setListaAutomobila(listaSvihAutomobila);
				
			}
			
		}
		
		
		// SERVIS
		ArrayList<String> listaServisaString = citanje.citanjeFajla("servis.txt");
		
		if (listaServisaString.size() > 0) {
			
			String automobilString, serviserString, termin, opis, sifreDelova, statusServisaString, namiren;
			
			Automobil automobil = null;
			Serviser serviser = null;
			String[] listaSifara;
			ArrayList<ServisniDeo> listaDelova = new ArrayList<ServisniDeo>();
			StatusServisa statusServisa = null;
			String id;
			double cena;
			
			ArrayList<Automobil> listaAutomobila = Administrator.getListaAutomobila();
			ArrayList<Serviser> listaServisera = Administrator.getListaServisera();
			ArrayList<ServisniDeo> listaSvihDelova = Administrator.getListaSvihDelova();
			
			String [] servisString;
			for (int i = 0; i < listaServisaString.size(); i++) {
				servisString = listaServisaString.get(i).split(",");
				
				automobilString = servisString[0];
				serviserString = servisString[1];
				termin = servisString[2];
				opis = servisString[3];
				sifreDelova = servisString[4];
				statusServisaString = servisString[5];
				id = servisString[6];
				cena = Double.parseDouble(servisString[7]);
				namiren = servisString[8];
				
				// pronalazenje automobila
				for (int j = 0; j < listaAutomobila.size(); j++) {
					if (listaAutomobila.get(j).getId().equals(automobilString)) {
						automobil = listaAutomobila.get(j);
						break;
					}
				}
				
				// pronalazenje servisera
				for (int j = 0; j < listaServisera.size(); j++) {
					if (listaServisera.get(j).getId().equals(serviserString)) {
						serviser = listaServisera.get(j);
						break;
					}
				}
				
				// pronalazenje delova sa istom sifrom
				listaSifara = sifreDelova.split(".");
				for (int j = 0; j < listaSifara.length; j++) {
					for (int k = 0; k < listaSvihDelova.size(); i++) {
						if (listaSifara[j].equals(listaSvihDelova.get(k).getId())) {
							listaDelova.add(listaSvihDelova.get(k));
							break;
						}
					}
				}
				
				
				// pronalazenje statusa servisa
				for(int j = 0; j < StatusServisa.values().length; j++) {
					if (StatusServisa.values()[j].toString().equals(statusServisaString)) {
						statusServisa = StatusServisa.values()[j];
						break;
					}
				}
				
				
				// kreiranje objekata
				Servis servis = new Servis(automobil,serviser,termin,opis,listaDelova,statusServisa,id,cena,namiren);
				// dodavanje servisa musteriji
				ArrayList<Servis> listaMServisa = automobil.getVlasnik().getListaSvihServisa();
				listaMServisa.add(servis);
				automobil.getVlasnik().setListaSvihServisa(listaMServisa);
				if(servis.getStatusServisa().toString().equals("ZAKAZAN")) {
					ArrayList<Servis> listaZakazanihMServisa = automobil.getVlasnik().getListaZakazanihServisa();
					listaZakazanihMServisa.add(servis);
					automobil.getVlasnik().setListaZakazanihServisa(listaZakazanihMServisa);
				}
				else if(servis.getStatusServisa().toString().equals("ZAVRSEN") && servis.getNamiren().equals("ne")) {
					ArrayList<Servis> listaZavrsenihServisa = automobil.getVlasnik().getListaZavrsenihServisa();
					listaZavrsenihServisa.add(servis);
					automobil.getVlasnik().setListaZavrsenihServisa(listaZavrsenihServisa);
				}
				// dodavanje servisa serviseru odgovornom za taj servis
				ArrayList<Servis> listaOdredjenihServisa = serviser.getListaServisa();
				listaOdredjenihServisa.add(servis);
				serviser.setListaServisa(listaOdredjenihServisa);
				// dodavanje servisa u listi svih servisa
				ArrayList<Servis> listaSvihServisa = Administrator.getListaSvihServisa();
				listaSvihServisa.add(servis);
				Administrator.setListaSvihServisa(listaSvihServisa);
				
			}
			
		}
		
		// SERVISNA KNJIZICA
		ArrayList<String> listaServisnihKnjizicaString = citanje.citanjeFajla("servisnaKnjizica.txt");
		if (listaServisnihKnjizicaString.size() > 0) {
			Automobil automobil = null;
			ArrayList<Servis> listaServisa = new ArrayList<Servis>();
			ArrayList<Servis> listaSvihServisa = Administrator.getListaSvihServisa();
			ArrayList<Automobil> listaAutomobila = Administrator.getListaAutomobila();
			String automobilString, sifreServisa, id;
			String [] listaSifara;
			String[] servisnaKnjizicaString;

			for (int i = 0; i < listaServisnihKnjizicaString.size(); i++) {
				servisnaKnjizicaString = listaServisnihKnjizicaString.get(i).split(",");
				automobilString = servisnaKnjizicaString[0];
				sifreServisa = servisnaKnjizicaString[1];
				id = servisnaKnjizicaString[2];
				
				// pronalazenje automobila
				for (int j = 0; j < listaAutomobila.size(); j++) {
					if (listaAutomobila.get(i).getId().equals(automobilString)) {
						automobil = listaAutomobila.get(i);
						break;
					}
				}
				
				//
				listaSifara = sifreServisa.split(".");
				for (int j = 0; j < listaSifara.length; j++) {
					for (int k = 0; k < listaSvihServisa.size(); k++) {
						if (listaSvihServisa.get(k).getId().equals(listaSifara[j])){
							listaServisa.add(listaSvihServisa.get(i));
							break;
						}
					}
				}
				
				// kreiranje objekata
				ServisnaKnjizica servisnaKnjizica = new ServisnaKnjizica(automobil,listaServisa,id);
				// dodavanje servisne knjizice u listi servisnih knjizica
				ArrayList<ServisnaKnjizica> listaServisnihKnjizica = Administrator.getListaServisnihKnjizica();
				listaServisnihKnjizica.add(servisnaKnjizica);
				Administrator.setListaServisnihKnjizica(listaServisnihKnjizica);
				
			}
			
		}
		
		// SERVISNI DELOVI
		ArrayList<String> listaSDString = citanje.citanjeFajla("servisniDelovi.txt");
		if (listaSDString.size() > 0) {
			
			Marka[] listaMarki = Marka.values();
			Model[] listaModela = Model.values();
			Marka marka = null;
			Model model = null;
			String markaString,modelString,naziv,id;
			double cena;
			String [] servisniDeoString;
			// marka,model,naziv,cena,id
			for (int i = 0; i < listaSDString.size(); i++) {
				servisniDeoString = listaSDString.get(i).split(",");
				markaString = servisniDeoString[0];
				modelString = servisniDeoString[1];
				naziv = servisniDeoString[2];
				cena = Double.parseDouble(servisniDeoString[3]);
				id = servisniDeoString[4];
				
				
				//
				for (int j = 0; j < listaMarki.length; j++) {
					if (listaMarki[j].toString().equals(markaString)) {
						marka = listaMarki[j];
						break;
					}
				}
				//
				for (int j = 0; j < listaModela.length; j++) {
					if (listaModela[j].toString().equals(modelString)) {
						model = listaModela[j];
						break;
					}
				}
				
				// kreiranje objekta
				ServisniDeo servisniDeo = new ServisniDeo(marka,model,naziv,cena,id);
				// dodavanje servisnog dela listi servisnih delova
				ArrayList<ServisniDeo> listaServisnihDelova = Administrator.getListaSvihDelova();
				listaServisnihDelova.add(servisniDeo);
				Administrator.setListaSvihDelova(listaServisnihDelova);
				
			}
		}
		
		
		
	}

}
