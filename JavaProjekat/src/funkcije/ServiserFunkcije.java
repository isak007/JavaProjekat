package funkcije;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import citanjePisanje.CitanjePisanje;
import enumeracije.StatusServisa;
import modeli.Servis;
import modeli.Serviser;
import modeli.ServisniDeo;

public class ServiserFunkcije {
	private Serviser serviser;
	
	public ServiserFunkcije(Serviser serviser) {
		this.serviser = serviser;
	}
	
	public void zavrsiServis(Servis servis, double cena) {
		if (this.serviser.getListaNezavrsenihServisa().size() > 0) {
			this.inicijalizacijaPromjena();
			ArrayList<Servis> listaNezavrsenihServisa = this.serviser.getListaNezavrsenihServisa();
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
	
	
	/*
	public void kreiranjeServisa(String automobilID, String opis, String termin,
			ArrayList<ServisniDeo> listaDelova, String id, String sifraServisneKnjizice) {		
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
		}
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
			ServisnaKnjizica servisnaKnjizica = new ServisnaKnjizica(automobilObj,servisiKnjizice, id, "ne");
			listaServisnihKnjizica.add(servisnaKnjizica);
			Administrator.setListaServisnihKnjizica(listaServisnihKnjizica);
			pisanje.pisanjeUfajl(servisnaKnjizica.toString(), "servisnaKnjizica.txt");
		}
		pisanje.pisanjeUfajl(servis.toString(), "servis.txt");
		System.out.println("Servis je uspesno zakazan!");
	}*/
	
	
	/*
	public void izmenaServisa(Servis servis, String opcija, String noviSadrzaj) {
		this.inicijalizacijaPromjena();
		ArrayList<Servis> listaServisa = this.serviser.getListaServisa();
		if(listaServisa.size() > 0) {
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
	}*/
}
