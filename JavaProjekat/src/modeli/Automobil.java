package modeli;

import enumeracije.Gorivo;
import enumeracije.Marka;
import enumeracije.Model;

public class Automobil {
	private Musterija vlasnik;
	private Marka marka;
	private Model model;
	private int godinaProizvodnje;
	private double zapreminaMotora;
	private int snagaMotora;
	private Gorivo gorivo;
	private String id;
	private String obrisan = "ne";
	
	// konstruktori
	public Automobil() {};
	public Automobil(Musterija vlasnik, Marka marka, Model model, int godinaProizvodnje,
			double zapreminaMotora, int snagaMotora, Gorivo gorivo, String id, String obrisan) {
		this.vlasnik = vlasnik;
		this.marka = marka;
		this.model = model;
		this.godinaProizvodnje = godinaProizvodnje;
		this.zapreminaMotora = zapreminaMotora;
		this.snagaMotora = snagaMotora;
		this.gorivo = gorivo;
		this.id = id;
		this.obrisan = obrisan;
	}
	public Automobil(Automobil automobil) {
		this.vlasnik = automobil.vlasnik;
		this.marka = automobil.marka;
		this.model = automobil.model;
		this.godinaProizvodnje = automobil.godinaProizvodnje;
		this.zapreminaMotora = automobil.zapreminaMotora;
		this.snagaMotora = automobil.snagaMotora;
		this.gorivo = automobil.gorivo;
		this.id = automobil.id;
		this.obrisan = automobil.obrisan;
	}
	
	
	//
	public String getObrisan() {
		return obrisan;
	}
	public void setObrisan(String obrisan) {
		this.obrisan = obrisan;
	}
	//
	public Marka getMarka() {
		return marka;
	}
	public void setMarka(Marka marka) {
		this.marka = marka;
	}
	//
	public Model getModel() {
		return model;
	}
	public void setModel(Model model) {
		this.model = model;
	}
	//
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	//
	public Musterija getVlasnik() {
		return vlasnik;
	}
	public void setVlasnik(Musterija vlasnik) {
		this.vlasnik = vlasnik;
	}
	//
	public int getGodinaProizvodnje() {
		return godinaProizvodnje;
	}
	public void setGodinaProizvodnje(int godinaProizvodnje) {
		this.godinaProizvodnje = godinaProizvodnje;
	}
	//
	public double getZapreminaMotora() {
		return zapreminaMotora;
	}
	public void setZapreminaMotora(double zapreminaMotora) {
		this.zapreminaMotora = zapreminaMotora;
	}
	//
	public int getSnagaMotora() {
		return snagaMotora;
	}
	public void setSnagaMotora(int snagaMotora) {
		this.snagaMotora = snagaMotora;
	}
	//
	public Gorivo getGorivo() {
		return gorivo;
	}
	public void setGorivo(Gorivo gorivo) {
		this.gorivo = gorivo;
	}
	
	
	@Override
	public String toString() {
		return this.vlasnik.getId()+","+this.marka.toString()+","+this.model.toString()+","+
				this.godinaProizvodnje+","+this.zapreminaMotora+","+this.snagaMotora+","+this.gorivo.toString()+","+this.id+","+this.obrisan;
	}
}
