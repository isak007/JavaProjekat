package funkcije;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import modeli.*;
import citanjePisanje.CitanjePisanje;
import enumeracije.Gorivo;
import enumeracije.Marka;
import enumeracije.Model;
import enumeracije.Pol;
import enumeracije.Specijalizacija;
import enumeracije.StatusServisa;
import enumeracije.Uloga;

public class AdminFunkcije {
	//private Administrator admin;
	
	public AdminFunkcije (/*Administrator admin*/) {
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
	
	
	public void dodavanjeServisnogDela(ServisniDeo servisniDeo, boolean dodajSimetricni) {
		String suprotnaStrana = "";
		String strana = "";
		String naziv = servisniDeo.getNaziv();
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
		if (mogucSimetricni) {
			String noviNaziv = naziv.replace(strana, suprotnaStrana);
			ServisniDeo servisniDeoSimetricni = new ServisniDeo(servisniDeo.getMarka(),servisniDeo.getModel(),noviNaziv,
					servisniDeo.getCena(),servisniDeo.getId()+"s","ne");
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
	public void registracijaMusterije(Musterija korisnik) {
		ArrayList<Musterija> listaMusterija = Administrator.getListaMusterija();
		listaMusterija.add(korisnik);
		Administrator.setListaMusterija(listaMusterija);
		CitanjePisanje pisanje = new CitanjePisanje();
		pisanje.pisanjeUfajl(korisnik.toString(), "musterija.txt");
	}
			
	// registracija SERVISERA
	public void registracijaServisera(Serviser korisnik) {
		ArrayList<Serviser> listaServisera = Administrator.getListaServisera();
		listaServisera.add(korisnik);
		Administrator.setListaServisera(listaServisera);
		CitanjePisanje pisanje = new CitanjePisanje();
		pisanje.pisanjeUfajl(korisnik.toString(), "serviser.txt");	
	}
	
	// registracija ADMINA
	public void registracijaAdmina(Administrator korisnik) {
		ArrayList<Administrator> listaAdministratora = Administrator.getListaAdministratora();
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
				if (listaSvihServisa.get(i).getObrisan().equals("ne")) {
					System.out.println(i+") "+listaSvihServisa.get(i).toString());
				}
			}
		}
		else {
			System.out.println("Trenutno nema servisa!");
		}
	}
	
	public void kreiranjeServisa(Automobil automobilObj, String opis, Serviser serviser, String termin,
			ArrayList<ServisniDeo> listaDelova, String id) {
		ArrayList<Serviser> listaServisera = Administrator.getListaServisera();
		
		if(listaServisera.size() > 0) {	
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
			boolean postojiKnjizica = false;
			ServisnaKnjizica servisnaKnjizica = null;
			for (ServisnaKnjizica sk : listaServisnihKnjizica) {
				if (sk.getAutomobil().equals(automobilObj)){
					postojiKnjizica = true;
					servisnaKnjizica = sk;
					break;
				}
			}
			if (postojiKnjizica) {
				// dodavanje servisa u servisnoj knjzici ako postoji servisna knjizica
				ArrayList<Servis> servisiKnjizice = servisnaKnjizica.getListaServisa();
				servisiKnjizice.add(servis);
				servisnaKnjizica.setListaServisa(servisiKnjizice);
			}
			// dodavanje nove servisne knjizice ako ne postoji i servisa
			else {
				ArrayList <Servis> servisiKnjizice = new ArrayList<Servis>();
				servisiKnjizice.add(servis);
				ServisnaKnjizica novaServisnaKnjizica = new ServisnaKnjizica(automobilObj,servisiKnjizice, id,"ne");
				pisanje.pisanjeUfajl(novaServisnaKnjizica.toString(), "servisnaKnjizica.txt");
			}
			listaServisnihKnjizica.add(servisnaKnjizica);
			Administrator.setListaServisnihKnjizica(listaServisnihKnjizica);
			
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
	
	
	///////////////////////////////////////////////////
	
	// CRUD korisnik
	public void izmenaMusterije(Musterija musterija, String ime, String prezime, String jmbg, Pol pol, String adresa, String brojTelefona,
			String korisnickoIme, String lozinka, Uloga uloga, int brojSakupljenihBodova) {
			musterija.setIme(ime);
			musterija.setPrezime(prezime);
			musterija.setJmbg(jmbg);
			musterija.setPol(pol);
			musterija.setAdresa(adresa);
			musterija.setBrojTelefona(brojTelefona);
			musterija.setKorisinickoIme(korisnickoIme);
			musterija.setLozinka(lozinka);
			musterija.setBrojSakupljenihBodova(brojSakupljenihBodova);
	}
	public void izmenaServisera(Serviser serviser, String ime, String prezime, String jmbg, Pol pol, String adresa, String brojTelefona,
			String korisnickoIme, String lozinka, Uloga uloga, double plata, Specijalizacija specijalizacija) {
			serviser.setIme(ime);
			serviser.setPrezime(prezime);
			serviser.setJmbg(jmbg);
			serviser.setPol(pol);
			serviser.setAdresa(adresa);
			serviser.setBrojTelefona(brojTelefona);
			serviser.setKorisinickoIme(korisnickoIme);
			serviser.setLozinka(lozinka);
			serviser.setPlata(plata);
			serviser.setSpecijalizacija(specijalizacija);
	}
	public void izmenaAdmina(Administrator admin, String ime, String prezime, String jmbg, Pol pol, String adresa, String brojTelefona,
			String korisnickoIme, String lozinka, Uloga uloga, double plata) {
			admin.setIme(ime);
			admin.setPrezime(prezime);
			admin.setJmbg(jmbg);
			admin.setPol(pol);
			admin.setAdresa(adresa);
			admin.setBrojTelefona(brojTelefona);
			admin.setKorisinickoIme(korisnickoIme);
			admin.setLozinka(lozinka);
			admin.setPlata(plata);
		
	}
	public void brisanjeMusterije(Musterija musterija) {
		musterija.setObrisan("da");
		for (Automobil automobil : musterija.getListaAutomobila()) {
			automobil.setObrisan("da");
		}
		for (Servis servis : musterija.getListaSvihServisa()) {
			servis.setObrisan("da");
		}
	}
	public void brisanjeServisera(Serviser serviser) {
		serviser.setObrisan("da");
		for (Servis servis : serviser.getListaServisa()) {
			servis.setObrisan("da");
		}
	}
	public void brisanjeAdmina(Administrator admin) {
		admin.setObrisan("da");
	}
	
	// CRUD automobil
	public void dodavanjeAutomobila(Automobil automobil) {
		ArrayList<Automobil> listaAutomobila = Administrator.getListaAutomobila();
		// dodavanje automobila musterijinoj listi automobila
		ArrayList<Automobil> listaOdredjenihAutomobila = automobil.getVlasnik().getListaAutomobila();
		listaOdredjenihAutomobila.add(automobil);
		automobil.getVlasnik().setListaAutomobila(listaOdredjenihAutomobila);
		// dodavanje automobila celoj listi automobila
		listaAutomobila.add(automobil);
		Administrator.setListaAutomobila(listaAutomobila);
		
		
		// upisivanje automobila u fajl
		CitanjePisanje pisanje = new CitanjePisanje();
		pisanje.pisanjeUfajl(automobil.toString(), "automobil.txt");
		System.out.println("Uspesno ste uneli automobil!"); 
		
		
	}
	public void izmenaAutomobila(Automobil automobil, Musterija vlasnik, Marka marka, Model model, int godinaProizvodnje,
			double zapreminaMotora, int snagaMotora, Gorivo gorivo) {
		automobil.setMarka(marka);
		automobil.setModel(model);
		automobil.setGodinaProizvodnje(godinaProizvodnje);
		automobil.setZapreminaMotora(zapreminaMotora);
		automobil.setSnagaMotora(snagaMotora);
		automobil.setGorivo(gorivo);
		if(automobil.getVlasnik() != vlasnik) {
			// uklanjanje iz liste starog vlasnika
			ArrayList<Automobil> novaListaStarogVlasnika = automobil.getVlasnik().getListaAutomobila();
			novaListaStarogVlasnika.remove(novaListaStarogVlasnika.indexOf(automobil));
			automobil.getVlasnik().setListaAutomobila(novaListaStarogVlasnika);
			// dodavanje automobila listi novog vlasnika
			ArrayList<Automobil> novaListaNovogVlasnika = vlasnik.getListaAutomobila();
			novaListaNovogVlasnika.add(automobil);
			vlasnik.setListaAutomobila(novaListaNovogVlasnika);
			automobil.setVlasnik(vlasnik);
		}
		
	}
	public void brisanjeAutomobila(Automobil automobil) {
		automobil.setObrisan("da");
	}
	
	// CRUD servis
	public void brisanjeServisa(Servis servis) {
		servis.setObrisan("da");
	}
	
	// CRUD servisni deo
	public void brisanjeSDela(ServisniDeo servisniDeo) {
		servisniDeo.setObrisan("da");
	}
	
	// CRUD servisna knjizica
	public void dodavanjeServisneKnjizice(Automobil automobil, String id) {
		ArrayList<ServisnaKnjizica> listaServisnihKnjizica = Administrator.getListaServisnihKnjizica();
		ServisnaKnjizica servisnaKnjizica = new ServisnaKnjizica(automobil,new ArrayList<Servis>(), id,"ne");
		listaServisnihKnjizica.add(servisnaKnjizica);
		Administrator.setListaServisnihKnjizica(listaServisnihKnjizica);
	}
	public void izmenaServisneKnjizice(ServisnaKnjizica sk, Automobil automobil, ArrayList <Servis> servisiKnjizice) {
		sk.setAutomobil(automobil);
		sk.setListaServisa(servisiKnjizice);
	}
	public void brisanjeServisneKnjizice(ServisnaKnjizica servisnaKnjizica) {
		servisnaKnjizica.setObrisan("da");
	}
	
	/////////////////////////////////////////////
	
}
