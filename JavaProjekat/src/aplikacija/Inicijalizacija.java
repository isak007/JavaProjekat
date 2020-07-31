package aplikacija;

import java.util.ArrayList;
import java.util.HashMap;
import modeli.*;

import enumeracije.Gorivo;
import enumeracije.Marka;
import enumeracije.Model;
import enumeracije.Pol;
import enumeracije.Specijalizacija;
import enumeracije.StatusServisa;
import enumeracije.Uloga;
import funkcije.AdminFunkcije;
//import funkcije.ServiserFunkcije;
import citanjePisanje.CitanjePisanje;

public class Inicijalizacija {

	public static void init() {
		inicijalizacijaObjekata();
		inicijalizacijaMarkiModela();
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
		AdminFunkcije af = new AdminFunkcije(null);
		// ADMINISTRATOR
		ArrayList<String> listaAdminStringova = CitanjePisanje.citanjeFajla("administrator.txt");
		if (listaAdminStringova.size() > 0) {
			String [] adminString;
			Administrator admin;
			String ime, prezime, jmbg, adresa, brojTelefona, korisnickoIme, lozinka, id, polString, ulogaString, obrisan;
			Pol pol;
			Uloga uloga;
			double plata;
			
			// ime,prezime,jmbg,pol,adresa,br.tel.,kor.ime,lozinka,uloga,id,plata
			for (int i = 0; i < listaAdminStringova.size(); i++) {
				adminString = listaAdminStringova.get(i).split(",");
				ime = adminString[9];
				prezime = adminString[1];
				jmbg = adminString[2];
				polString = adminString[3];
				adresa = adminString[4];
				brojTelefona = adminString[5];
				korisnickoIme = adminString[6];
				lozinka = adminString[7];
				ulogaString = adminString[8];
				id = adminString[0];
				plata = Double.parseDouble(adminString[10]);
				obrisan = adminString[11];
				
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
				if (obrisan.equals("ne")) {
					// kreiranje Administrator objekta i dodavanje listi administratora
					// ime,prezime,jmbg,pol,adresa,br.tel.,kor.ime,lozinka,uloga,id,plata
					admin = new Administrator(ime,prezime,jmbg,pol,adresa,brojTelefona,korisnickoIme,lozinka,uloga,id,plata,obrisan);
					af.dodajUkloniAdmina(admin, "dodaj");
				}
				
			}
		}
		
		// SERVISER
		ArrayList<String> listaServiserStringova = CitanjePisanje.citanjeFajla("serviser.txt");
		if (listaServiserStringova.size() > 0) {
			String [] serviserString;
			Serviser serviser;
			String ime, prezime, jmbg, adresa, brojTelefona, korisnickoIme, lozinka, id,
			polString, ulogaString, specijalizacijaString, obrisan;
			Pol pol;
			Uloga uloga;
			double plata;
			Specijalizacija specijalizacija;
			
			// ime,prezime,jmbg,pol,adresa,br.tel.,kor.ime,lozinka,uloga,id,plata
			for (int i = 0; i < listaServiserStringova.size(); i++) {
				serviserString = listaServiserStringova.get(i).split(",");
				ime = serviserString[9];
				prezime = serviserString[1];
				jmbg = serviserString[2];
				polString = serviserString[3];
				adresa = serviserString[4];
				brojTelefona = serviserString[5];
				korisnickoIme = serviserString[6];
				lozinka = serviserString[7];
				ulogaString = serviserString[8];
				id = serviserString[0];
				plata = Double.parseDouble(serviserString[10]);
				specijalizacijaString = serviserString[11];
				obrisan = serviserString[12];
				
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
				
				if (obrisan.equals("ne")) {
					// kreiranje Administrator objekta i dodavanje listi administratora
					// ime,prezime,jmbg,pol,adresa,br.tel.,kor.ime,lozinka,uloga,id,plata
					serviser = new Serviser(ime,prezime,jmbg,pol,adresa,brojTelefona,korisnickoIme,lozinka,
							uloga,id,plata,specijalizacija,obrisan);
					af.dodajUkloniServisera(serviser, "dodaj");
				}
			}
		}
		
		// MUSTERIJA
		ArrayList<String> listaMusterijaStringova = CitanjePisanje.citanjeFajla("musterija.txt");
		if (listaMusterijaStringova.size() > 0) {
			String [] musterijaString;
			Musterija musterija;
			String ime, prezime, jmbg, adresa, brojTelefona, korisnickoIme, lozinka, id, 
			polString, ulogaString, obrisan;
			Pol pol;
			Uloga uloga;
			int brojSakupljenihBodova;
			double dug;
			
			// ime,prezime,jmbg,pol,adresa,br.tel.,kor.ime,lozinka,uloga,id,plata
			for (int i = 0; i < listaMusterijaStringova.size(); i++) {
				musterijaString = listaMusterijaStringova.get(i).split(",");
				ime = musterijaString[9];
				prezime = musterijaString[1];
				jmbg = musterijaString[2];
				polString = musterijaString[3];
				adresa = musterijaString[4];
				brojTelefona = musterijaString[5];
				korisnickoIme = musterijaString[6];
				lozinka = musterijaString[7];
				ulogaString = musterijaString[8];
				id = musterijaString[0];
				brojSakupljenihBodova = Integer.parseInt(musterijaString[10]);
				dug = Double.parseDouble(musterijaString[11]);
				obrisan = musterijaString[12];
				
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
				
				if (obrisan.equals("ne")) {
					// kreiranje Administrator objekta i dodavanje listi administratora
					// ime,prezime,jmbg,pol,adresa,br.tel.,kor.ime,lozinka,uloga,id,plata
					musterija = new Musterija(ime,prezime,jmbg,pol,adresa,brojTelefona,korisnickoIme,lozinka,
							uloga,id,brojSakupljenihBodova,dug,obrisan);
					af.dodajUkloniMusteriju(musterija, "dodaj");
				}
				
			}
		}
		
		
		// AUTOMOBIL
		ArrayList<String> listaAutomobilaString = CitanjePisanje.citanjeFajla("automobil.txt");
		if (listaAutomobilaString.size() > 0) {
			ArrayList<Musterija> listaMusterija = Administrator.getListaMusterija();
			Marka [] listaMarki = Marka.values();
			Model [] listaModela = Model.values();
			
			String vlasnikString,markaString,modelString,gorivoString,id,obrisan;
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
				vlasnikString = automobilString[7];
				markaString = automobilString[1];
				modelString = automobilString[2];
				godinaProizvodnje = Integer.parseInt(automobilString[3]);
				zapreminaMotora = Double.parseDouble(automobilString[4]);
				snagaMotora = Integer.parseInt(automobilString[5]);
				gorivoString = automobilString[6];
				id = automobilString[0];
				obrisan = automobilString[8];
				
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
				
				if (obrisan.equals("ne")) {
					// kreiranje Automobil objekata
					Automobil automobil = new Automobil(vlasnik,marka,model,godinaProizvodnje,zapreminaMotora,
							snagaMotora,gorivo,id,obrisan);
					// dodavanje automobila listi sa svim automobilima
					af.dodajUkloniAutomobil(automobil, "dodaj");
				}
			}
			
		}
		
		// SERVISNI DELOVI
		ArrayList<String> listaSDString = CitanjePisanje.citanjeFajla("servisniDelovi.txt");
		if (listaSDString.size() > 0) {
			
			Marka[] listaMarki = Marka.values();
			Model[] listaModela = Model.values();
			Marka marka = null;
			Model model = null;
			String markaString,modelString,naziv,id,obrisan;
			double cena;
			String [] servisniDeoString;
			// marka,model,naziv,cena,id
			for (int i = 0; i < listaSDString.size(); i++) {
				servisniDeoString = listaSDString.get(i).split(",");
				markaString = servisniDeoString[4];
				modelString = servisniDeoString[1];
				naziv = servisniDeoString[2];
				cena = Double.parseDouble(servisniDeoString[3]);
				id = servisniDeoString[0];
				obrisan = servisniDeoString[5];
				
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
				
				if (obrisan.equals("ne")) {
					// kreiranje objekta
					ServisniDeo servisniDeo = new ServisniDeo(marka,model,naziv,cena,id,obrisan);
					// dodavanje servisnog dela listi servisnih delova
					af.dodajUkloniSD(servisniDeo, "dodaj");
				}
			}
		}
		
		// SERVIS
		ArrayList<String> listaServisaString = CitanjePisanje.citanjeFajla("servis.txt");
		
		if (listaServisaString.size() > 0) {
			
			String automobilString, serviserString, termin, opis, sifreDelova, statusServisaString, otplacen;
			
			Automobil automobil = null;
			Serviser serviser = null;
			StatusServisa statusServisa = null;
			String id, obrisan;
			double cena;	
			ArrayList<Automobil> listaAutomobila = Administrator.getListaAutomobila();
			ArrayList<Serviser> listaServisera = Administrator.getListaServisera();
			ArrayList<ServisniDeo> listaSvihDelova = Administrator.getListaSvihDelova();
			
			String [] servisString;
			for (int i = 0; i < listaServisaString.size(); i++) {
				servisString = listaServisaString.get(i).split(",");
				automobilString = servisString[6];
				serviserString = servisString[1];
				termin = servisString[2];
				opis = servisString[3];
				sifreDelova = servisString[4];
				statusServisaString = servisString[5];
				id = servisString[0];
				cena = Double.parseDouble(servisString[7]);
				otplacen = servisString[8];
				obrisan = servisString[9];
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
				ArrayList<ServisniDeo> listaDelova = new ArrayList<ServisniDeo>();
				if (sifreDelova.contains(".")) {
					for (String sifra : sifreDelova.split("\\.")) {
						for (ServisniDeo sd : listaSvihDelova) {
							if(sd.getId().equals(sifra)) {
								listaDelova.add(sd);
								break;
							}
						}
					}
				}
				else if (sifreDelova.length()>0){
					for (ServisniDeo sd : listaSvihDelova) {
						if(sd.getId().equals(sifreDelova)) {
							listaDelova.add(sd);
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
				
				if (obrisan.equals("ne")) {
					// kreiranje objekata
					Servis servis = new Servis(automobil,serviser,termin,opis,listaDelova,statusServisa,id,cena,otplacen,obrisan);
					// dodavanje servisa musteriji
					ArrayList<Servis> listaMServisa = automobil.getVlasnik().getListaSvihServisa();
					listaMServisa.add(servis);
					automobil.getVlasnik().setListaSvihServisa(listaMServisa);
					if(servis.getStatusServisa().toString().equals("ZAKAZAN")) {
						ArrayList<Servis> listaZakazanihMServisa = automobil.getVlasnik().getListaZakazanihServisa();
						listaZakazanihMServisa.add(servis);
						automobil.getVlasnik().setListaZakazanihServisa(listaZakazanihMServisa);
					}
					else if(servis.getStatusServisa().toString().equals("ZAVRSEN") && servis.getOtplacen().equals("ne")) {
						ArrayList<Servis> listaZavrsenihServisa = automobil.getVlasnik().getListaZavrsenihServisa();
						listaZavrsenihServisa.add(servis);
						automobil.getVlasnik().setListaZavrsenihServisa(listaZavrsenihServisa);
					}
					// dodavanje servisa serviseru odgovornom za taj servis
					ArrayList<Servis> listaOdredjenihServisa = serviser.getListaServisa();
					listaOdredjenihServisa.add(servis);
					serviser.setListaServisa(listaOdredjenihServisa);
					// dodavanje servisa u listi svih servisa
					af.dodajUkloniServis(servis, "dodaj");
				}
			}
			
		}
		
		// SERVISNA KNJIZICA
		ArrayList<String> listaServisnihKnjizicaString = CitanjePisanje.citanjeFajla("servisnaKnjizica.txt");
		if (listaServisnihKnjizicaString.size() > 0) {
			Automobil automobil = null;
			ArrayList<Servis> listaSvihServisa = Administrator.getListaSvihServisa();
			ArrayList<Automobil> listaAutomobila = Administrator.getListaAutomobila();
			String automobilString, sifreServisa, id, obrisan;
			String [] listaSifara;
			String[] servisnaKnjizicaString;

			for (int i = 0; i < listaServisnihKnjizicaString.size(); i++) {
				ArrayList<Servis> listaServisa = new ArrayList<Servis>();
				servisnaKnjizicaString = listaServisnihKnjizicaString.get(i).split(",");
				automobilString = servisnaKnjizicaString[2];
				sifreServisa = servisnaKnjizicaString[1];
				id = servisnaKnjizicaString[0];
				obrisan = servisnaKnjizicaString[3];
				
				// pronalazenje automobila
				for (int j = 0; j < listaAutomobila.size(); j++) {
					if (listaAutomobila.get(j).getId().equals(automobilString)) {
						automobil = listaAutomobila.get(j);
						break;
					}
				}
				
				//
				if(sifreServisa.contains(".")) {
					listaSifara = sifreServisa.split("\\.");
					for (int j = 0; j < listaSifara.length; j++) {
						for (int k = 0; k < listaSvihServisa.size(); k++) {
							if (listaSvihServisa.get(k).getId().equals(listaSifara[j])){
								listaServisa.add(listaSvihServisa.get(k));
								break;
							}
						}
					}
				}
				else {
					for (Servis servis : listaSvihServisa) {
						if (servis.getId().equals(sifreServisa)){
							listaServisa.add(servis);
							break;
						}
					}
				}
				
				if (obrisan.equals("ne")) {
					// kreiranje objekata
					ServisnaKnjizica servisnaKnjizica = new ServisnaKnjizica(automobil,listaServisa,id,obrisan);
					// dodavanje servisne knjizice u listi servisnih knjizica
					af.dodajUkloniSK(servisnaKnjizica, "dodaj");
				}
			}
			
		}
			
		// REZERVACIJE
		ArrayList<String> listaRezervacija = CitanjePisanje.citanjeFajla("rezervacije.txt");
		if (listaRezervacija.size() > 0) {
			Administrator.setListaRezervacija(listaRezervacija);
		}
	}
}
