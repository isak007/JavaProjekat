package citanjePisanje;
import java.io.*;
import java.util.ArrayList;
import modeli.*;

public class CitanjePisanje {
	public static String root = "JavaProjekat/src/fajlovi/";
	
	public CitanjePisanje() {};
	
	public static void prazanFajl(String fajl) {
		File file = new File(root + fajl);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write("");
			writer.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void izmenaPodataka(String sadrzaj, String fajl) {
		ArrayList<String> stariSadrzaj = CitanjePisanje.citanjeFajla(fajl);
		String[] izmene = sadrzaj.split("\r\n");
		// izmena objekata
		for (String linijaSS : stariSadrzaj) {
			// id objekta starog sadrzaja
			String linijaSSid = linijaSS.split(",")[0];
			
			// ako je id isti iz novog sadrzaja i starog, znaci da se radi o promenama
			// i treba ih dodati
			for (String linijaIZ : izmene) {
				String linijaIZid = linijaIZ.split(",")[0];
				if (linijaSSid.equals(linijaIZid)) {
					stariSadrzaj.set(stariSadrzaj.indexOf(linijaSS), linijaIZ);
					break;
				}
			}
		}
		// dodavanje objekata
		for (String linijaIZ : izmene) {
			String linijaIZid = linijaIZ.split(",")[0];
			boolean postoji = false;
			for (String linijaSS : stariSadrzaj) {
				String linijaSSid = linijaSS.split(",")[0];
				if(linijaIZid.equals(linijaSSid)) {
					postoji = true;
					break;
				}
			}
			// ako u cijeloj listi ne postoji trenutni novi id znaci da se radi o dodavanju objekta
			// i treba ga upisati
			if (!postoji) {
				stariSadrzaj.add(linijaIZ);
			}
		}
		
		// kreiranje string novog sadrzaja radi upisa
		String noviSadrzaj = "";
		for (String objekat : stariSadrzaj) {
			noviSadrzaj += objekat + "\r\n";
		}
		
		File file = new File(root + fajl);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(noviSadrzaj);
			writer.close();
		}
		catch (IOException e) {	
			e.printStackTrace();
		}
	}
	
	public static void pisanjeUfajl(String sadrzaj, String fajl) {
		File file = new File(root + fajl);
		ArrayList<String> prosliSadrzaj = CitanjePisanje.citanjeFajla(fajl);
		String noviSadrzaj = "";
		for (int i = 0; i < prosliSadrzaj.size(); i++) {
			noviSadrzaj += prosliSadrzaj.get(i) + "\r\n";
		}
		noviSadrzaj += sadrzaj;
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(noviSadrzaj);
			writer.close();
		}
		catch (IOException e) {	
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> citanjeFajla(String fajl) {
		File file = new File(root + fajl);
		ArrayList<String> lista = new ArrayList<String>();
		BufferedReader reader;
		String linija;
		try {
			reader = new BufferedReader(new FileReader(file));
			while(true) {
				linija = reader.readLine();
				if(linija.length() > 0) {
					lista.add(linija);
				}
				else {
					break;
				}
			}
			reader.close();
			return(lista);
		}
		catch (IOException e) {	
			//e.printStackTrace();
			CitanjePisanje.prazanFajl(fajl);
			return (lista);
		}
		catch (NullPointerException e) {
			return(lista);
		}
	}
	
	// rezervacije za upis
	public static String rezZaUpis() {
		String sadrzajZaUpis = "";
		for (String rezervacija : Administrator.getListaRezervacija()) {
			sadrzajZaUpis += rezervacija + "\r\n";
		}
		sadrzajZaUpis = sadrzajZaUpis.substring(0, sadrzajZaUpis.length());
		return sadrzajZaUpis;
	}
	
	// servisna knjizica za upis
	public static String skZaUpis() {
		String zaUpisUFajl = "";
		for(ServisnaKnjizica sk : Administrator.getListaServisnihKnjizica()) {
			zaUpisUFajl += sk.toString() + "\r\n";
		}
		return zaUpisUFajl;
	}
	
	// automobili za upis
	public static String automobiliZaUpis() {
		String zaUpisUFajl = "";
		for(Automobil auto : Administrator.getListaAutomobila()) {
			zaUpisUFajl += auto.toString() + "\r\n";
		}
		return zaUpisUFajl;
	}
	
	// servisi za upis
	public static String servisZaUpis() {
		String zaUpisUFajl = "";
		for(Servis servis : Administrator.getListaSvihServisa()) {
			zaUpisUFajl += servis.toString() + "\r\n";
		}
		return zaUpisUFajl;
	}
	
	// servisni delovi za upis
	public static String sdZaUpis() {
		String zaUpisUFajl = "";
		for(ServisniDeo sd : Administrator.getListaSvihDelova()) {
			zaUpisUFajl += sd.toString() + "\r\n";
		}
		return zaUpisUFajl;
	}
	
	
	// korisnici za upis
	public static String adminZaUpis() {
		String zaUpisUFajl = "";
		for(Administrator admin : Administrator.getListaAdministratora()) {
			zaUpisUFajl += admin.toString() + "\r\n";
		}
		return zaUpisUFajl;
	}
	
	public static String serviserZaUpis() {
		String zaUpisUFajl = "";
		for(Serviser serviser : Administrator.getListaServisera()) {
			zaUpisUFajl += serviser.toString() + "\r\n";
		}
		return zaUpisUFajl;
	}
	
	public static String musterijaZaUpis() {
		String zaUpisUFajl = "";
		for(Musterija musterija : Administrator.getListaMusterija()) {
			zaUpisUFajl += musterija.toString() + "\r\n";
		}
		return zaUpisUFajl;
	}
	
}
