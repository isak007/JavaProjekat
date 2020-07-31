package funkcije;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import modeli.*;
import citanjePisanje.CitanjePisanje;
import enumeracije.StatusServisa;

public class AdminFunkcije {
	private Administrator admin;
	
	public AdminFunkcije (Administrator admin) {
		this.admin = admin;
	}
	
	public Administrator getAdmin() {
		return admin;
	}
	public void setAdmin(Administrator admin) {
		this.admin = admin;
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
					if (sad.after(termin) && listaSvihServisa.get(i).getStatusServisa().equals(StatusServisa.ZAKAZAN)) {
						listaSvihServisa.get(i).setStatusServisa(StatusServisa.U_TOKU);
						CitanjePisanje.izmenaPodataka(CitanjePisanje.servisZaUpis(), "servis.txt");
					}
				}
				catch(ParseException e) {
					e.printStackTrace();
					System.out.println("Invalidan format!");
				}
			}
		}
	}
	
	
	// dodavanje rezervacije
	public void dodajUkloniRez(String rezervacija, String opcija) {
		ArrayList<String> listaRezervacija = Administrator.getListaRezervacija();
		if (opcija.equals("dodaj")) {
			listaRezervacija.add(rezervacija);
		}
		else {
			listaRezervacija.remove(rezervacija);
		}
		Administrator.setListaRezervacija(listaRezervacija);
	}
	
	// dodavanje servisne knjizice
	public void dodajUkloniSK(ServisnaKnjizica sk, String opcija) {
		ArrayList<ServisnaKnjizica> listaKnjizica = Administrator.getListaServisnihKnjizica();
		if(opcija.equals("dodaj")) {
			listaKnjizica.add(sk);
		}
		else {
			listaKnjizica.remove(sk);
		}
		Administrator.setListaServisnihKnjizica(listaKnjizica);
	}
	
	// dodavanje automobila
	public void dodajUkloniAutomobil(Automobil auto,String opcija) {
		ArrayList<Automobil> listaAutomobila = Administrator.getListaAutomobila();
		ArrayList<Automobil> listaOdredjenihAutomobila = auto.getVlasnik().getListaAutomobila();
		if (opcija.equals("dodaj")) {
			// dodavanje automobila musterijinoj listi automobila
			listaOdredjenihAutomobila.add(auto);
			// dodavanje automobila celoj listi automobila
			listaAutomobila.add(auto);
		}
		else {
			// uklanjanje automobila iz musterijine liste automobila
			listaOdredjenihAutomobila.remove(auto);
			// uklanjanje automobila iz liste automobila
			listaAutomobila.remove(auto);
		}
		auto.getVlasnik().setListaAutomobila(listaOdredjenihAutomobila);
		Administrator.setListaAutomobila(listaAutomobila);
	}
	
	
	// dodavanje servisnog dela
	public void dodajUkloniSD(ServisniDeo sd,String opcija) {
		ArrayList<ServisniDeo> listaDelova = Administrator.getListaSvihDelova();
		if (opcija.equals("dodaj")) {
			listaDelova.add(sd);
		}
		else {
			listaDelova.remove(sd);
		}
		Administrator.setListaSvihDelova(listaDelova);
	}
	
	// dodavanje servisa
	public void dodajUkloniServis(Servis servis, String opcija) {
		ArrayList<Servis> listaServisa = Administrator.getListaSvihServisa();
		if (opcija.equals("dodaj")) {
			listaServisa.add(servis);
		}
		else {
			listaServisa.remove(servis);
		}
		Administrator.setListaSvihServisa(listaServisa);
	}
	
	
	//////////////////////////////////////////
	//     dodavanje korisnika u liste
	public void dodajUkloniAdmina(Administrator admin, String opcija) {
		ArrayList<Administrator> listaA = Administrator.getListaAdministratora();
		if (opcija.equals("dodaj")) {
			listaA.add(admin);
		}
		else {
			listaA.remove(admin);
		}
		Administrator.setListaAdministratora(listaA);
	}
	
	public void dodajUkloniMusteriju(Musterija musterija, String opcija) {
		ArrayList<Musterija> listaM = Administrator.getListaMusterija();
		if (opcija.equals("dodaj")) {
			listaM.add(musterija);
		}
		else {
			listaM.remove(musterija);
		}
		Administrator.setListaMusterija(listaM);
	}
	
	public void dodajUkloniServisera(Serviser serviser, String opcija) {
		ArrayList<Serviser> listaS = Administrator.getListaServisera();
		if (opcija.equals("dodaj")) {
			listaS.add(serviser);
		}
		else {
			listaS.remove(serviser);
		}
		Administrator.setListaServisera(listaS);
	}
	//////////////////////////////////////////
	
	
	public void dodavanjeServisnogDela(ServisniDeo servisniDeo, boolean dodajSimetricni) {
		String suprotnaStrana = "";
		String strana = "";
		String naziv = servisniDeo.getNaziv();
		// simetricni deo
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
		// kreiranje objekta
		if (dodajSimetricni) {
			String noviNaziv = naziv.replace(strana, suprotnaStrana);
			ServisniDeo servisniDeoSimetricni = new ServisniDeo(servisniDeo.getMarka(),servisniDeo.getModel(),noviNaziv,
					servisniDeo.getCena(),servisniDeo.getId()+"s","ne");
			this.dodajUkloniSD(servisniDeoSimetricni, "dodaj");
		}
		// dodavanje servisnog dela u listi servisnih delova
		this.dodajUkloniSD(servisniDeo, "dodaj");
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
	
	
	public void kreiranjeServisa(Servis servis) {
		ArrayList<Serviser> listaServisera = Administrator.getListaServisera();
		if(listaServisera.size() > 0) {	
			ArrayList<Servis> listaOdredjenihServisa = servis.getServiser().getListaServisa(); // servisi odredjeni serviseru
			ArrayList<Servis> listaServisa = Administrator.getListaSvihServisa(); // svi servisi
			// dodavanje servisa musteriji
			ArrayList<Servis> listaMServisa = servis.getAutomobil().getVlasnik().getListaSvihServisa();
			ArrayList<Servis> listaZakazanihMServisa = servis.getAutomobil().getVlasnik().getListaZakazanihServisa();
			listaMServisa.add(servis);
			listaZakazanihMServisa.add(servis);
			servis.getAutomobil().getVlasnik().setListaSvihServisa(listaMServisa);
			servis.getAutomobil().getVlasnik().setListaZakazanihServisa(listaZakazanihMServisa);
			// dodavanje servisa serviseru zaduzenom za taj servis
			listaOdredjenihServisa.add(servis);
			servis.getServiser().setListaServisa(listaOdredjenihServisa);
			// dodavanje servisa u listi svih servisa(administratoru)
			listaServisa.add(servis);
			Administrator.setListaSvihServisa(listaServisa);
			// SERVISNA KNJIZICA
			boolean postojiKnjizica = false;
			ServisnaKnjizica servisnaKnjizica = null;
			for (ServisnaKnjizica sk : Administrator.getListaServisnihKnjizica()) {
				if (sk.getAutomobil().equals(servis.getAutomobil())){
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
				ServisnaKnjizica novaServisnaKnjizica = new ServisnaKnjizica(servis.getAutomobil(),servisiKnjizice, servis.getAutomobil().getId(),"ne");
				this.dodajUkloniSK(novaServisnaKnjizica, "dodaj");
			}
			CitanjePisanje.izmenaPodataka(CitanjePisanje.skZaUpis(), "servisnaKnjizica.txt");
			CitanjePisanje.izmenaPodataka(CitanjePisanje.servisZaUpis(), "servis.txt");
			System.out.println("Servis je uspesno zakazan!");
		}
		else {
			System.out.println("Ne postoje serviseri u sistemu!");
		}		
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
