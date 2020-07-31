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
	
	// musterija getter i setter
	public Musterija getMusterija() {
		return musterija;
	}

	public void setMusterija(Musterija musterija) {
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
			servis.setOtplacen("da");
		}
		// dodavanje izmena u servis fajlu
		ArrayList<String> listaServisaString = CitanjePisanje.citanjeFajla("servis.txt");
		CitanjePisanje.prazanFajl("servis.txt");
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
		CitanjePisanje.pisanjeUfajl(noviSadrzaj, "servis.txt");
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
	
	
	public void otkazivanjeServisa(Servis servis) {
		this.inicijalizacijaPromjena();
		ArrayList<Servis> listaZakazanihServisa = this.musterija.getListaZakazanihServisa();
		if(listaZakazanihServisa.size() > 0) {
			// dodavanje izmena u servis fajlu
			ArrayList<String> listaServisaString = CitanjePisanje.citanjeFajla("servis.txt");
			CitanjePisanje.prazanFajl("servis.txt");
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
			CitanjePisanje.pisanjeUfajl(noviSadrzaj, "servis.txt");
			
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
