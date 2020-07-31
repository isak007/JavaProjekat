package funkcije;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import citanjePisanje.CitanjePisanje;
import enumeracije.StatusServisa;
import modeli.Administrator;
import modeli.Automobil;
import modeli.Servis;
import modeli.Serviser;
import modeli.ServisnaKnjizica;
import modeli.ServisniDeo;

public class ServiserFunkcije {
	private Serviser serviser;
	
	public ServiserFunkcije(Serviser serviser) {
		this.serviser = serviser;
	}
	
	// serviser getter i setter
	public Serviser getServiser() {
		return serviser;
	}

	public void setServiser(Serviser serviser) {
		this.serviser = serviser;
	}



	public void zavrsiServis(Servis servis, double cena) {
		if (this.serviser.getListaNezavrsenihServisa().size() > 0) {
			this.inicijalizacijaPromjena();
			ArrayList<Servis> listaNezavrsenihServisa = this.serviser.getListaNezavrsenihServisa();
			servis.setStatusServisa(StatusServisa.ZAVRSEN);
			// modifikovanje tesktualnog fajla servisa 
			ArrayList<String> listaServisaString = CitanjePisanje.citanjeFajla("servis.txt");
			CitanjePisanje.prazanFajl("servis.txt");
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
			CitanjePisanje.pisanjeUfajl(noviSadrzaj, "servis.txt");
			// dodavanje cene svakog dela ukupnog ceni
			ArrayList<ServisniDeo> listaDelova = servis.getListaDelova();
			for (int j = 0; j < listaDelova.size(); j++) {
				cena += listaDelova.get(j).getCena();
			}
			servis.setCena(cena);
			// uklanjanje servisa iz liste nezavrsenih servisa(serviser)
			listaNezavrsenihServisa.remove(listaNezavrsenihServisa.indexOf(servis));
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
						listaServisa.get(i).setStatusServisa(StatusServisa.U_TOKU);
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
		ArrayList<Servis> listaServisa = this.serviser.getListaServisa();
		if (listaServisa.size() > 0) {
			for (int i = 0; i < listaServisa.size(); i++) {
				System.out.println(i+") "+listaServisa.get(i).toString());
			}
		}
		else {
			System.out.println("Niste jos opredeljeni ni za jedan servis!");
		}
	}
	
	
	
	///// SVIM OVIM FUNKCIJAMA MOGU PREKO ADMIN FUNCIJA DA PRISTUPIM
	/*
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
	}*/
	
	
	public void kreiranjeServisa(Servis servis) {		
		// pronalazenje automobila koji je postavljen za servis, preko toString metode
		ArrayList<Automobil> listaAutomobila = Administrator.getListaAutomobila();
		Automobil automobilObj = null;
		for (int i = 0; i < listaAutomobila.size(); i++) {
			if (listaAutomobila.get(i).getId().toString().equals(servis.getAutomobil().getId())) {
				automobilObj = listaAutomobila.get(i);
				break;
			}
		}
		
		Serviser serviser = this.serviser;
		
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
		ArrayList<Servis> listaOdredjenihServisa = serviser.getListaServisa(); // servisi odredjeni serviseru
		ArrayList<Servis> listaServisa = Administrator.getListaSvihServisa(); // svi servisi
		
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
		boolean postojiKnjizica = false;
		ServisnaKnjizica servisnaKnjizica = null;
		for (ServisnaKnjizica sk : Administrator.getListaServisnihKnjizica()) {
			if (sk.getAutomobil().equals(servis.getAutomobil())){
				postojiKnjizica = true;
				servisnaKnjizica = sk;
				break;
			}
		}
		// dodavanje servisa u servisnoj knjzici ako postoji servisna knjizica
		if (postojiKnjizica) {
			ArrayList<Servis> servisiKnjizice = servisnaKnjizica.getListaServisa();
			servisiKnjizice.add(servis);
			servisnaKnjizica.setListaServisa(servisiKnjizice);
		}
		// dodavanje nove servisne knjizice ako ne postoji i servisa
		else {
			ArrayList <Servis> servisiKnjizice = new ArrayList<Servis>();
			servisiKnjizice.add(servis);
			ServisnaKnjizica novaServisnaKnjizica = new ServisnaKnjizica(servis.getAutomobil(),servisiKnjizice, servis.getAutomobil().getId(),"ne");
			AdminFunkcije af = new AdminFunkcije(null);
			af.dodajUkloniSK(novaServisnaKnjizica, "dodaj");
		}
		CitanjePisanje.izmenaPodataka(CitanjePisanje.skZaUpis(), "servisnaKnjizica.txt");
		CitanjePisanje.izmenaPodataka(CitanjePisanje.servisZaUpis(), "servis.txt");
		System.out.println("Servis je uspesno zakazan!");
	}
	
}
