package modeli;

import enumeracije.Uloga;
import enumeracije.Pol;
import java.util.ArrayList;

public class Musterija extends Korisnik {
	private String id;
	private int brojSakupljenihBodova;
	private ArrayList<Automobil> listaAutomobila = new ArrayList<Automobil>();
	private ArrayList<Servis> listaSvihServisa = new ArrayList<Servis>();
	private ArrayList<Servis> listaZakazanihServisa = new ArrayList<Servis>();
	private ArrayList<Servis> listaZavrsenihServisa = new ArrayList<Servis>();
	
	// konstruktori
	public Musterija() {};
	public Musterija(String ime, String prezime, String jmbg, Pol pol, String adresa, String brojTelefona, 
			String korisnickoIme, String lozinka, Uloga uloga, String id, int brojSakupljenihBodova, String obrisan) {
		super(ime, prezime, jmbg, pol, adresa, brojTelefona, korisnickoIme, lozinka, uloga, obrisan);
		this.id = id;
		this.brojSakupljenihBodova = brojSakupljenihBodova;
	}
	public Musterija(Musterija musterija) {
		this.id = musterija.id;
		this.brojSakupljenihBodova = musterija.brojSakupljenihBodova;
	}
	
	
	
	//
	public ArrayList<Servis> getListaZavrsenihServisa() {
		return listaZavrsenihServisa;
	}
	public void setListaZavrsenihServisa(ArrayList<Servis> listaZavrsenihServisa) {
		this.listaZavrsenihServisa = listaZavrsenihServisa;
	}
	//
	public ArrayList<Servis> getListaSvihServisa() {
		return listaSvihServisa;
	}
	public void setListaSvihServisa(ArrayList<Servis> listaSvihServisa) {
		this.listaSvihServisa = listaSvihServisa;
	}
	//
	public ArrayList<Servis> getListaZakazanihServisa() {
		return listaZakazanihServisa;
	}
	public void setListaZakazanihServisa(ArrayList<Servis> listaZakazanihServisa) {
		this.listaZakazanihServisa = listaZakazanihServisa;
	}
	//
	public ArrayList<Automobil> getListaAutomobila() {
		return listaAutomobila;
	}
	public void setListaAutomobila(ArrayList<Automobil> listaAutomobila) {
		this.listaAutomobila = listaAutomobila;
	}
	//
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	//
	public int getBrojSakupljenihBodova() {
		return brojSakupljenihBodova;
	}
	public void setBrojSakupljenihBodova(int brojSakupljenihBodova) {
		this.brojSakupljenihBodova = brojSakupljenihBodova;
	}
	
	
	@Override
	public String toString() {
		return this.ime+","+this.prezime+","+this.jmbg +","+this.pol+","+this.adresa+","+this.brojTelefona+
				","+this.korisinickoIme+","+this.lozinka+","+this.uloga+","+this.id+","+this.brojSakupljenihBodova+","+this.obrisan;
	}
	
}
