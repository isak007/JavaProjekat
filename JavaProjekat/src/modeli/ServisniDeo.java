package modeli;
import enumeracije.Marka;
import enumeracije.Model;

public class ServisniDeo {
	private Marka marka;
	private Model model;
	private String naziv;
	private double cena;
	private String id;
	
	// konstruktori
	public ServisniDeo() {};
	public ServisniDeo(Marka marka, Model model, String naziv, double cena, String id) {
		this.marka = marka;
		this.model = model;
		this.naziv = naziv;
		this.cena = cena;
		this.id = id;
	}
	public ServisniDeo(ServisniDeo servisniDeo) {
		this.marka = servisniDeo.marka;
		this.model = servisniDeo.model;
		this.naziv = servisniDeo.naziv;
		this.cena = servisniDeo.cena;
		this.id = servisniDeo.id;
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
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	//
	public double getCena() {
		return cena;
	}
	public void setCena(double cena) {
		this.cena = cena;
	}
	
	@Override
	public String toString() {
		return this.marka.toString()+","+this.model.toString()+","+this.naziv+","+this.cena+","+this.id;
	}
}
