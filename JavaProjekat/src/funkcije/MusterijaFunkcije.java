package funkcije;

import modeli.Musterija;
import modeli.Servis;
import modeli.ServisniDeo;
import modeli.Administrator;
import modeli.Automobil;

import citanjePisanje.CitanjePisanje;
import enumeracije.Gorivo;
import enumeracije.Marka;
import enumeracije.Model;
import enumeracije.StatusServisa;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MusterijaFunkcije {
	private Musterija musterija;
	
	public MusterijaFunkcije(Musterija musterija) {
		this.musterija = musterija;
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
	
	
	public void otplacivanjeDuga(boolean koriscenjeBodova) {
		double dug = this.dug();
		if (this.musterija.getBrojSakupljenihBodova() > 0) {				
			if (koriscenjeBodova) {
				double snizenje = this.musterija.getBrojSakupljenihBodova() * (dug*(2.0/100.0));
				dug -= snizenje;
				System.out.println("Uspesno ste iskoristili bodove!");
				this.musterija.setBrojSakupljenihBodova(0);
			}
		}
		System.out.println("Dug("+dug+")"+" je uspesno otplacen!");
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
	
	
	public void unosAutomobila(Marka marka, Model model, int godinaProizvodnje, double zapreminaMotora, int snagaMotora,
			Gorivo gorivo, String id) {
		Musterija vlasnik = this.musterija;
		ArrayList<Automobil> listaAutomobila = Administrator.getListaAutomobila();
		// pravljenje automobil objekta
		Automobil automobil = new Automobil(vlasnik,marka,model,godinaProizvodnje,
				zapreminaMotora,snagaMotora,gorivo,id,"ne");
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
	
	
	public void kreiranjeRezervacije(Automobil automobil, String opis) {
		ArrayList<Automobil> listaAutomobila = this.musterija.getListaAutomobila();
		if (listaAutomobila.size() > 0) {
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
				String rezervacija = automobil.getId()+","+opis;
				ArrayList<String> listaRezervacija = Administrator.getListaRezervacija();
				listaRezervacija.add(rezervacija);
				Administrator.setListaRezervacija(listaRezervacija);
				System.out.println("Rezervacija je uspijesno poslata na obradu!");
			}
		}
		else {
			System.out.println("Trenutno nemate automobila za rezervaciju!");
		}
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
				System.out.println(i+") "+listaSvihServisa.get(i).toString());
			}
		}
		else {
			System.out.println("Jos uvek niste zakazali servis!");
		}
	}
	
	public void otkazivanjeServisa(Servis servis) {
		this.inicijalizacijaPromjena();
		ArrayList<Servis> listaZakazanihServisa = this.musterija.getListaZakazanihServisa();
		if(listaZakazanihServisa.size() > 0) {
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
			System.out.println("Nemate zakazanih servisa!");
		}
	}
}
