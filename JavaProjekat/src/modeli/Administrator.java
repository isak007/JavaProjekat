package modeli;
import java.util.ArrayList;
import java.util.HashMap;
import enumeracije.Uloga;
import enumeracije.Pol;
import enumeracije.Marka;
import enumeracije.Model;

public class Administrator extends Korisnik {
	private String id;
	private double plata;
	private static ArrayList<Automobil> listaAutomobila = new ArrayList<Automobil>();
	private static ArrayList<String> listaRezervacija = new ArrayList<String>();
	private static ArrayList<Servis> listaSvihServisa = new ArrayList<Servis>();
	private static ArrayList<Musterija> listaMusterija = new ArrayList<Musterija>();
	private static ArrayList<Serviser> listaServisera = new ArrayList<Serviser>();
	private static ArrayList<Administrator> listaAdministratora = new ArrayList<Administrator>();
	private static ArrayList<ServisniDeo> listaSvihDelova = new ArrayList<ServisniDeo>();
	private static ArrayList<ServisnaKnjizica> listaServisnihKnjizica = new ArrayList<ServisnaKnjizica>();
	private static HashMap<Marka, ArrayList<Model>> markeModeli;

	
	// konstruktori
	public Administrator() {};
	public Administrator(String ime, String prezime, String jmbg, Pol pol, String adresa, String brojTelefona, 
			String korisnickoIme, String lozinka, Uloga uloga, String id, double plata, String obrisan) {
		super(ime, prezime, jmbg, pol, adresa, brojTelefona, korisnickoIme, lozinka, uloga, obrisan);
		this.id = id;
		this.plata = plata;
	}
	public Administrator(Administrator administrator) {
		this.id = administrator.id;
		this.plata = administrator.plata;
	}
	
	

	
	//
	public static ArrayList<String> getListaRezervacija() {
		return listaRezervacija;
	}
	public static void setListaRezervacija(ArrayList<String> listaRezervacija) {
		Administrator.listaRezervacija = listaRezervacija;
	}
	//
	public static HashMap<Marka, ArrayList<Model>> getMarkeModeli() {
		return markeModeli;
	}
	public static void setMarkeModeli(HashMap<Marka, ArrayList<Model>> markeModeli) {
		Administrator.markeModeli = markeModeli;
	}
	//
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	//
	public static ArrayList<ServisnaKnjizica> getListaServisnihKnjizica() {
		return listaServisnihKnjizica;
	}
	public static void setListaServisnihKnjizica(ArrayList<ServisnaKnjizica> listaServisnihKnjizica) {
		Administrator.listaServisnihKnjizica = listaServisnihKnjizica;
	}
	//
	public static ArrayList<ServisniDeo> getListaSvihDelova() {
		return listaSvihDelova;
	}
	public static void setListaSvihDelova(ArrayList<ServisniDeo> listaSvihDelova) {
		Administrator.listaSvihDelova = listaSvihDelova;
	}
	//
	public static ArrayList<Automobil> getListaAutomobila() {
		return listaAutomobila;
	}
	public static void setListaAutomobila(ArrayList<Automobil> listaAutomobila) {
		Administrator.listaAutomobila = listaAutomobila;
	}
	//
	public static ArrayList<Servis> getListaSvihServisa() {
		return listaSvihServisa;
	}
	public static void setListaSvihServisa(ArrayList<Servis> listaSvihServisa) {
		Administrator.listaSvihServisa = listaSvihServisa;
	}
	//
	public static ArrayList<Musterija> getListaMusterija() {
		return listaMusterija;
	}
	public static void setListaMusterija(ArrayList<Musterija> listaMusterija) {
		Administrator.listaMusterija = listaMusterija;
	}
	//
	public static ArrayList<Serviser> getListaServisera() {
		return listaServisera;
	}
	public static void setListaServisera(ArrayList<Serviser> listaServisera) {
		Administrator.listaServisera = listaServisera;
	}
	//
	public static ArrayList<Administrator> getListaAdministratora() {
		return listaAdministratora;
	}
	public static void setListaAdministratora(ArrayList<Administrator> listaAdministratora) {
		Administrator.listaAdministratora = listaAdministratora;
	}
	//
	public double getPlata() {
		return plata;
	}
	public void setPlata(double plata) {
		this.plata = plata;
	}	
	
	
	@Override
	public String toString() {
		return this.ime+","+this.prezime+","+this.jmbg +","+this.pol+","+this.adresa+","+this.brojTelefona+
				","+this.korisinickoIme+","+this.lozinka+","+this.uloga+","+this.id+","+this.plata+","+this.obrisan;
	}
}
