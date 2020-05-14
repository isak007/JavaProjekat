package modeli;

import enumeracije.Uloga;
import enumeracije.Pol;

public abstract class Korisnik {
	protected String ime;
	protected String prezime;
	protected String jmbg;
	protected Pol pol;
	protected String adresa;
	protected String brojTelefona;
	protected String korisinickoIme;
	protected String lozinka;
	protected Uloga uloga;
	
	// konstruktori
	public Korisnik() {};
	public Korisnik(String ime, String prezime, String jmbg, Pol pol, String adresa, String brojTelefona,
			String korisnickoIme, String lozinka, Uloga uloga) {
		this.ime = ime;
		this.prezime = prezime;
		this.jmbg = jmbg;
		this.pol = pol;
		this.adresa = adresa;
		this.brojTelefona = brojTelefona;
		this.korisinickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.uloga = uloga;
	}
	public Korisnik(Korisnik korisnik) {
		this.ime = korisnik.ime;
		this.prezime = korisnik.prezime;
		this.jmbg = korisnik.jmbg;
		this.pol = korisnik.pol;
		this.adresa = korisnik.adresa;
		this.brojTelefona = korisnik.brojTelefona;
		this.korisinickoIme = korisnik.korisinickoIme;
		this.lozinka = korisnik.lozinka;
		this.uloga = korisnik.uloga;
	}
	
	//
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	
	//
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	
	//
	public String getJmbg() {
		return jmbg;
	}
	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}
	
	//
	public Pol getPol() {
		return pol;
	}
	public void setPol(Pol pol) {
		this.pol = pol;
	}
	
	//
	public String getAdresa() {
		return adresa;
	}
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
	
	//
	public String getBrojTelefona() {
		return brojTelefona;
	}
	public void setBrojTelefona(String brojTelefona) {
		this.brojTelefona = brojTelefona;
	}
	
	//
	public String getKorisinickoIme() {
		return korisinickoIme;
	}
	public void setKorisinickoIme(String korisinickoIme) {
		this.korisinickoIme = korisinickoIme;
	}
	
	//
	public String getLozinka() {
		return lozinka;
	}
	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}
	
	//
	public Uloga getUloga() {
		return uloga;
	}
	public void setUloga(Uloga uloga) {
		this.uloga = uloga;
	}
	
}
