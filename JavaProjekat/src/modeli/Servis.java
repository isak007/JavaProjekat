package modeli;
import java.util.ArrayList;
import enumeracije.StatusServisa;

public class Servis {
	private Automobil automobil;
	private Serviser serviser;
	private String termin;
	private String opis;
	private ArrayList<ServisniDeo> listaDelova = new ArrayList<ServisniDeo>();
	private String sifreDelova;
	private StatusServisa statusServisa;
	private String id;
	private double cena;
	private String namiren;
	
	// konstruktori
	public Servis() {};
	public Servis(Automobil automobil, Serviser serviser, String termin, String opis,
			ArrayList<ServisniDeo> listaDelova, StatusServisa statusServisa, String id, double cena, String namiren) {
		this.automobil = automobil;
		this.serviser = serviser;
		this.termin = termin;
		this.opis = opis;
		this.listaDelova = listaDelova;
		this.statusServisa = statusServisa;
		this.id = id;
		this.cena = cena;
		this.namiren = namiren;
	}
	public Servis(Servis servis) {
		this.automobil = servis.automobil;
		this.serviser = servis.serviser;
		this.termin = servis.termin;
		this.opis = servis.opis;
		this.listaDelova = servis.listaDelova;
		this.statusServisa = servis.statusServisa;
		this.id = servis.id;
		this.cena = servis.cena;
		this.namiren = servis.namiren;
	}
	

	
	//
	public String getNamiren() {
		return namiren;
	}
	public void setNamiren(String namiren) {
		this.namiren = namiren;
	}
	//
	public double getCena() {
		return cena;
	}
	public void setCena(double cena) {
		this.cena = cena;
	}
	//
	public String getSifreDelova() {
		sifreDelova = "";
		if(listaDelova.size() > 0) {
			for (int i = 0; i < listaDelova.size(); i++) {
				sifreDelova += listaDelova.get(i).getId() + ".";
			}
			sifreDelova = sifreDelova.substring(0, sifreDelova.length()-1);
		}
		return sifreDelova;
	}
	public void setSifreDelova (String sifreDelova) {
		this.sifreDelova = sifreDelova;
	}
	//
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	//
	public Automobil getAutomobil() {
		return automobil;
	}
	public void setAutomobil(Automobil automobil) {
		this.automobil = automobil;
	}
	//
	public Serviser getServiser() {
		return serviser;
	}
	public void setServiser(Serviser serviser) {
		this.serviser = serviser;
	}
	//
	public String getTermin() {
		return termin;
	}
	public void setTermin(String termin) {
		this.termin = termin;
	}
	//
	public String getOpis() {
		return opis;
	}
	public void setOpis(String opis) {
		this.opis = opis;
	}
	//
	public ArrayList<ServisniDeo> getListaDelova() {
		return listaDelova;
	}
	public void setListaDelova(ArrayList<ServisniDeo> listaDelova) {
		this.listaDelova = listaDelova;
	}
	//
	public StatusServisa getStatusServisa() {
		return statusServisa;
	}
	public void setStatusServisa(StatusServisa statusServisa) {
		this.statusServisa = statusServisa;
	}

	@Override
	public String toString() {
		return this.automobil.getId()+","+this.serviser.getId()+","+this.termin+","+this.opis+","+
	this.getSifreDelova()+","+this.statusServisa.toString()+","+this.id+","+this.cena+","+this.namiren;
	}
}
