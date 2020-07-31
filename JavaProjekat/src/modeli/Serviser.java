package modeli;
import java.util.ArrayList;

import enumeracije.Specijalizacija;
import enumeracije.Uloga;
import enumeracije.Pol;

public class Serviser extends Korisnik {
	private String id;
	private double plata;
	private Specijalizacija specijalizacija;
	private ArrayList<Servis> listaServisa = new ArrayList<Servis>();
	private ArrayList<Servis> listaNezavrsenihServisa = new ArrayList<Servis>();
	
	// konstruktori
	public Serviser() {};
	public Serviser(String ime, String prezime, String jmbg, Pol pol, String adresa, String brojTelefona, 
			String korisnickoIme, String lozinka, Uloga uloga, String id, double plata,
			Specijalizacija specijalizacija, String obrisan) {
		super(ime, prezime, jmbg, pol, adresa, brojTelefona, korisnickoIme, lozinka, uloga, obrisan);
		this.id = id;
		this.plata = plata;
		this.specijalizacija = specijalizacija;
	}
	public Serviser(Serviser serviser) {
		this.id = serviser.id;
		this.plata = serviser.plata;
		this.specijalizacija = serviser.specijalizacija;
	}
	
	
	
	
	//
	public ArrayList<Servis> getListaNezavrsenihServisa() {
		return listaNezavrsenihServisa;
	}
	public void setListaNezavrsenihServisa(ArrayList<Servis> listaNezavrsenihServisa) {
		this.listaNezavrsenihServisa = listaNezavrsenihServisa;
	}
	//
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	//
	public ArrayList<Servis> getListaServisa() {
		return listaServisa;
	}
	public void setListaServisa(ArrayList<Servis> listaServisa) {
		this.listaServisa = listaServisa;
	}
	//
	public double getPlata() {
		return plata;
	}
	public void setPlata(double plata) {
		this.plata = plata;
	}
	
	//
	public Specijalizacija getSpecijalizacija() {
		return specijalizacija;
	}
	public void setSpecijalizacija(Specijalizacija specijalizacija) {
		this.specijalizacija = specijalizacija;
	}
	
	
	@Override
	public String toString() {
		return this.id+","+this.prezime+","+this.jmbg +","+this.pol+","+this.adresa+","+this.brojTelefona+
				","+this.korisinickoIme+","+this.lozinka+","+this.uloga+","+this.ime+","+this.plata+","+this.specijalizacija+","+this.obrisan;
	}
}
