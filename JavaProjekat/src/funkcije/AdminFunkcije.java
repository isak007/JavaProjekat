package funkcije;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import modeli.*;
import citanjePisanje.CitanjePisanje;
import enumeracije.Marka;
import enumeracije.Model;
import enumeracije.Pol;
import enumeracije.Specijalizacija;
import enumeracije.StatusServisa;
import enumeracije.Uloga;

public class AdminFunkcije {
	//private Administrator admin;
	
	public AdminFunkcije (Administrator admin) {
		//this.admin = admin;
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
	
	
	public void dodavanjeServisnogDela(Marka marka, Model model, String naziv, double cena, String id, boolean dodajSimetricni) {
		String suprotnaStrana = "";
		String strana = "";
		// simetricni deo
		boolean mogucSimetricni = false;
		if(naziv.contains("Desna strana") || naziv.contains("desna strana") ||
				naziv.contains("Leva strana") || naziv.contains("leva strana")) {
			mogucSimetricni = true;
			if (dodajSimetricni) {
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
			}
		}
		ArrayList<ServisniDeo> listaServisnihDelova = Administrator.getListaSvihDelova();
		CitanjePisanje pisanje = new CitanjePisanje();
		// kreiranje objekta
		ServisniDeo servisniDeo = new ServisniDeo(marka,model,naziv,cena,id,"ne");
		if (mogucSimetricni) {
			String noviNaziv = naziv.replace(strana, suprotnaStrana);
			ServisniDeo servisniDeoSimetricni = new ServisniDeo(marka,model,noviNaziv,cena,id+"s","ne");
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
	

	// registracija MUSTERIJE
	public void registracijaMusterije(String ime, String prezime, String jmbg, Pol pol, String adresa, String brojTelefona,
			String korisnickoIme, String lozinka, Uloga uloga, String id) {
		ArrayList<Musterija> listaMusterija = Administrator.getListaMusterija();
		int brojSakupljenihBodova = 0;
		Musterija korisnik = new Musterija(ime,prezime,jmbg,pol,adresa,brojTelefona,korisnickoIme,
				lozinka,uloga,id,brojSakupljenihBodova,"ne");
		listaMusterija.add(korisnik);
		Administrator.setListaMusterija(listaMusterija);
		CitanjePisanje pisanje = new CitanjePisanje();
		pisanje.pisanjeUfajl(korisnik.toString(), "musterija.txt");
	}
			
	// registracija SERVISERA
	public void registracijaServisera(String ime, String prezime, String jmbg, Pol pol, String adresa, String brojTelefona,
			String korisnickoIme, String lozinka, Uloga uloga, String id, double plata, Specijalizacija specijalizacija) {
			
		ArrayList<Serviser> listaServisera = Administrator.getListaServisera();
		Serviser korisnik = new Serviser(ime,prezime,jmbg,pol,adresa,brojTelefona,korisnickoIme,
				lozinka,uloga,id,plata,specijalizacija,"ne");
		listaServisera.add(korisnik);
		Administrator.setListaServisera(listaServisera);
		CitanjePisanje pisanje = new CitanjePisanje();
		pisanje.pisanjeUfajl(korisnik.toString(), "serviser.txt");	
	}
	
	// registracija ADMINA
	public void registracijaAdmina(String ime, String prezime, String jmbg, Pol pol, String adresa, String brojTelefona,
			String korisnickoIme, String lozinka, Uloga uloga, String id, double plata) {
		uloga = Uloga.ADMINISTRATOR;
		
		ArrayList<Administrator> listaAdministratora = Administrator.getListaAdministratora();
		Administrator korisnik = new Administrator(ime,prezime,jmbg,pol,adresa,brojTelefona,korisnickoIme,
				lozinka,uloga,id,plata,"ne");
		listaAdministratora.add(korisnik);
		Administrator.setListaAdministratora(listaAdministratora);
		CitanjePisanje pisanje = new CitanjePisanje();
		pisanje.pisanjeUfajl(korisnik.toString(), "administrator.txt");
	}
	
	public void pregledRezervacija() {
		ArrayList<String> listaRezervacija = Administrator.getListaRezervacija();
		if (listaRezervacija.size() > 0) {
			for (int i = 0; i < listaRezervacija.size(); i++) {
				System.out.println(listaRezervacija.get(i).toString());
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
				System.out.println(i+") "+listaSvihServisa.get(i).toString());
			}
		}
		else {
			System.out.println("Trenutno nema servisa!");
		}
	}
	
	public void kreiranjeServisa(String automobilID, String opis, Serviser serviser, String termin,
			ArrayList<ServisniDeo> listaDelova, String id, String sifraServisneKnjizice) {
		ArrayList<Serviser> listaServisera = Administrator.getListaServisera();
		
		if(listaServisera.size() > 0) {	
			// pronalazenje automobila koji je postavljen za servis, preko toString metode
			ArrayList<Automobil> listaAutomobila = Administrator.getListaAutomobila();
			Automobil automobilObj = null;
			for (int i = 0; i < listaAutomobila.size(); i++) {
				if (listaAutomobila.get(i).getId().equals(automobilID)) {
					automobilObj = listaAutomobila.get(i);
					break;
				}
			}
			
			/*
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
			}*/
			
			ArrayList<Servis> listaOdredjenihServisa = serviser.getListaServisa(); // servisi odredjeni serviseru
			ArrayList<Servis> listaServisa = Administrator.getListaSvihServisa(); // svi servisi
			
			CitanjePisanje pisanje = new CitanjePisanje();
			Servis servis = new Servis(automobilObj, serviser, termin, opis, listaDelova, StatusServisa.ZAKAZAN, id, 0, "ne","ne");
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
			// SERVISNA KNJIZICA
			ArrayList<ServisnaKnjizica> listaServisnihKnjizica = Administrator.getListaServisnihKnjizica();
			if (!sifraServisneKnjizice.equals("")) {
				for (int i = 0; i < listaServisnihKnjizica.size(); i++) {
					// dodavanje servisa u servisnoj knjzici ako postoji servisna knjizica
					if (listaServisnihKnjizica.get(i).getAutomobil().getId().equals(automobilObj.getId())) {
						ArrayList<Servis> servisiKnjizice = listaServisnihKnjizica.get(i).getListaServisa();
						servisiKnjizice.add(servis);
						listaServisnihKnjizica.get(i).setListaServisa(servisiKnjizice);
						break;
					}
				}
			}
			// dodavanje nove servisne knjizice ako ne postoji i servisa
			else {
				ArrayList <Servis> servisiKnjizice = new ArrayList<Servis>();
				servisiKnjizice.add(servis);
				ServisnaKnjizica servisnaKnjizica = new ServisnaKnjizica(automobilObj,servisiKnjizice, id,"ne");
				listaServisnihKnjizica.add(servisnaKnjizica);
				Administrator.setListaServisnihKnjizica(listaServisnihKnjizica);
				pisanje.pisanjeUfajl(servisnaKnjizica.toString(), "servisnaKnjizica.txt");
			}
			
			pisanje.pisanjeUfajl(servis.toString(), "servis.txt");
			System.out.println("Servis je uspesno zakazan!");
		}
		else {
			System.out.println("Ne postoje serviseri u sistemu!");
		}		
	}
	
	public void izmenaServisa(Servis servis, String opcija, String noviSadrzaj) {
		this.inicijalizacijaPromjena();
		ArrayList<Servis> listaSvihServisa = Administrator.getListaSvihServisa();
		if(listaSvihServisa.size() > 0) {
			if (opcija.equals("1")) {
				servis.setOpis(noviSadrzaj);
			}
			else if(opcija.equals("2")) {
				servis.setCena(Integer.parseInt(noviSadrzaj));
			}
		}
		else {
			System.out.println("Trenutno nema zakazanih servisa!");
		}
	}
}
