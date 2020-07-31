package aplikacija;

import modeli.Administrator;
import modeli.Automobil;
import modeli.Musterija;
import modeli.Servis;
import modeli.Serviser;
import modeli.ServisnaKnjizica;
import modeli.ServisniDeo;

public class Pronadji {
	/* pretraga po listi admina da li postoji */
	public static Administrator pronadjiAdmina(String adminID, String korisnickoIme, String lozinka) {
		// pretrazivanje po ID-u, funkcionalnosti programa
		if (!adminID.equals("") && korisnickoIme.equals("") && lozinka.equals("")) {
			for(Administrator admin: Administrator.getListaAdministratora()) {
				if (admin.getId().equals(adminID)) {
					return admin;
				}
			}
			return null;
		}
		
		// pretrazivanje po korisnickom imenu i lozinci(LOGOVANJE)
		else if (adminID.equals("") && !korisnickoIme.equals("") && !lozinka.equals("")) {
			for(Administrator admin: Administrator.getListaAdministratora()) {
				if (admin.getKorisinickoIme().equals(korisnickoIme) && admin.getLozinka().equals(lozinka)) {
					return admin;
				}
			}
			return null;
		}
		
		// pretrazivanje samo po korisnickom imenu
		else if (adminID.equals("") && !korisnickoIme.equals("") && lozinka.equals("")) {
			for(Administrator admin: Administrator.getListaAdministratora()) {
				if (admin.getKorisinickoIme().equals(korisnickoIme)) {
					return admin;
				}
			}
			return null;
		}
				
		
		else {
			//System.out.println("Doslo je do greske u kodu!");
			return null;
		}
		
	}
	
	/* pretraga po listi servisera da li postoji */
	public static Serviser pronadjiServisera(String serviserID, String korisnickoIme, String lozinka) {
		// pretrazivanje po ID-u, funkcionalnosti programa
		if (!serviserID.equals("") && korisnickoIme.equals("") && lozinka.equals("")) {
			for(Serviser serviser: Administrator.getListaServisera()) {
				if (serviser.getId().equals(serviserID)) {
					return serviser;
				}
			}
			return null;
		}
		
		// pretrazivanje po korisnickom imenu i lozinci(LOGOVANJE)
		else if (serviserID.equals("") && !korisnickoIme.equals("") && !lozinka.equals("")) {
			for(Serviser serviser: Administrator.getListaServisera()) {
				if (serviser.getKorisinickoIme().equals(korisnickoIme) && serviser.getLozinka().equals(lozinka)) {
					return serviser;
				}
			}
			return null;
		}
		
		// pretrazivanje samo po korisnickom imenu
		else if (serviserID.equals("") && !korisnickoIme.equals("") && lozinka.equals("")) {
			for(Serviser serviser: Administrator.getListaServisera()) {
				if (serviser.getKorisinickoIme().equals(korisnickoIme)) {
					return serviser;
				}
			}
			return null;
		}
		
		else {
			//System.out.println("Doslo je do greske u kodu!");
			return null;
		}
		
	}
	
	/* pretraga po listi musterija da li postoji */
	public static Musterija pronadjiMusteriju(String musterijaID, String korisnickoIme, String lozinka) {
		// pretrazivanje po ID-u, funkcionalnosti programa
		if (!musterijaID.equals("") && korisnickoIme.equals("") && lozinka.equals("")) {
			for(Musterija musterija: Administrator.getListaMusterija()) {
				if (musterija.getId().equals(musterijaID)) {
					return musterija;
				}
			}
			return null;
		}
		
		// pretrazivanje po korisnickom imenu i lozinci(LOGOVANJE)
		else if (musterijaID.equals("") && !korisnickoIme.equals("") && !lozinka.equals("")) {
			for(Musterija musterija: Administrator.getListaMusterija()) {
				if (musterija.getKorisinickoIme().equals(korisnickoIme) && musterija.getLozinka().equals(lozinka)) {
					return musterija;
				}
			}
			return null;
		}
		
		// pretrazivanje samo po korisnickom imenu
		else if (musterijaID.equals("") && !korisnickoIme.equals("") && lozinka.equals("")) {
			for(Musterija musterija: Administrator.getListaMusterija()) {
				if (musterija.getKorisinickoIme().equals(korisnickoIme)) {
					return musterija;
				}
			}
			return null;
		}
		
		else {
			//System.out.println("Doslo je do greske u kodu!");
			return null;
		}
		
	}
	
	
	
	//// Pretraga automobila  /////
	public static Automobil pronadjiAutomobil(String automobilID) {
		for(Automobil automobil : Administrator.getListaAutomobila()) {
			if (automobil.getId().equals(automobilID)){
				return automobil;
			}
		}
		return null;
	}
	

	//// Pretraga rezervacija /////
	public static Servis pronadjiServis(String servisID) {
		for (Servis servis : Administrator.getListaSvihServisa()) {
			if (servis.getId().equals(servisID)) {
				return servis;
			}
		}
		return null;
	}
	
	
	//// Pretraga servisnih delova /////
	public static ServisniDeo pronadjiServnisniDeo(String sifraDela) {
		for (ServisniDeo sd : Administrator.getListaSvihDelova()) {
			if (sd.getId().equals(sifraDela)) {
				return sd;
			}
		}
		return null;
	}
	
	//// Pretraga servisnih knjizica
	public static ServisnaKnjizica pronadjiServisnuKnjizicu(String skID) {
		for (ServisnaKnjizica sk : Administrator.getListaServisnihKnjizica()) {
			if (sk.getId().equals(skID)) {
				return sk;
			}
		}
		return null;
	}
}
