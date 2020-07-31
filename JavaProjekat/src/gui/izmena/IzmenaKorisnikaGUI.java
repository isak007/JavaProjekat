package gui.izmena;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import aplikacija.Pronadji;
import citanjePisanje.CitanjePisanje;
import funkcije.AdminFunkcije;
import modeli.Administrator;
import modeli.Musterija;
import modeli.Serviser;
import net.miginfocom.swing.MigLayout;
import enumeracije.*;


public class IzmenaKorisnikaGUI extends JFrame{
	private JLabel lblID = new JLabel("ID");
	private JTextField txtID = new JTextField(20);
	private JLabel lblIme = new  JLabel("Ime");
	private JTextField txtIme = new JTextField(20);
	private JLabel lblPrezime = new JLabel("Prezime");
	private JTextField txtPrezime = new JTextField(20);
	private JLabel lblJMBG = new  JLabel("JMBG");
	private JTextField txtJMBG = new JTextField(20);
	private JLabel lblPol = new  JLabel("Pol");
	private JComboBox<Pol> cbPol = new JComboBox<Pol>(Pol.values());
	private JLabel lblAdresa = new JLabel("Adresa");
	private JTextField txtAdresa = new JTextField(20);
	private JLabel lblBrTel = new JLabel("Broj telefona");
	private JTextField txtBrTel = new JTextField(20);
	private JLabel lblKorIme = new JLabel("Korisnicko ime");
	private JTextField txtKorIme = new JTextField(20);
	private JLabel lblLozinka = new JLabel("Lozinka");
	private JTextField txtLozinka = new JTextField(20);
	private JLabel lblUloga = new JLabel("Uloga");
	private JComboBox<Uloga> cbUloga = new JComboBox<Uloga>(Uloga.values());
	private JButton btnOK = new JButton("OK");
	private JButton btnCancel = new JButton("Cancel");
	private JLabel lblPlata = new JLabel("Plata");
	private JTextField txtPlata = new JTextField(20);
	private JLabel lblSpec = new JLabel("Specijalizacija");
	private JComboBox<Specijalizacija> cbSpec = new JComboBox<Specijalizacija>(Specijalizacija.values());
	private JLabel lblBrBod = new JLabel("Broj bodova");
	private JTextField txtBrBod = new JTextField(20);
	private JLabel lblDug = new JLabel("Dug");
	private JTextField txtDug = new JTextField(20);
	
	private String korisnikID;
	private AdminFunkcije adminFunkcije;
	private Uloga uloga;
	
	public IzmenaKorisnikaGUI(AdminFunkcije adminFunkcije, String korisnikID, Uloga uloga) {
		this.uloga = uloga;
		this.adminFunkcije = adminFunkcije;
		this.korisnikID = korisnikID;
		if(korisnikID == "") {
			setTitle("Dodavanje korisnika");
		}else {
			setTitle("Izmena korisnika - " + korisnikID);
		}
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		initGUI();
		initActions();
		setResizable(false);
		pack();
	}
	
	private void initGUI() {
		MigLayout layout = new MigLayout("wrap 2", "[][]", "[][][][][]20[]");
		setLayout(layout);
		
		if(korisnikID != "") {
			// za postojeceg korisnika prosledjen je null kao uloga pa moram da nadjem i postavim ulogu
			if (Pronadji.pronadjiAdmina(korisnikID, "", "")!= null) {
				uloga = Uloga.ADMINISTRATOR;
			}
			else if (Pronadji.pronadjiServisera(korisnikID, "", "")!= null) {
				uloga = Uloga.SERVISER;
			}
			else if (Pronadji.pronadjiMusteriju(korisnikID, "", "")!= null) {
				uloga = Uloga.MUSTERIJA;
			}
			// Ne zelimo da moze da se promeni ID diska, pa je polje disable-ovano:
			txtID.setEnabled(false);
			cbUloga.setEnabled(false);
			popuniPolja();
		}
		cbUloga.setSelectedItem(uloga);
		
		add(lblID);
		add(txtID);
		add(lblIme);
		add(txtIme);
		add(lblPrezime);
		add(txtPrezime);
		add(lblJMBG);
		add(txtJMBG);
		add(lblPol);
		add(cbPol);
		add(lblAdresa);
		add(txtAdresa);
		add(lblBrTel);
		add(txtBrTel);
		add(lblKorIme);
		add(txtKorIme);
		add(lblLozinka);
		add(txtLozinka);
		add(lblUloga);
		add(cbUloga);
		if (uloga.equals(Uloga.ADMINISTRATOR)) {
			if(Pronadji.pronadjiAdmina(korisnikID, "", "") != null) {
				Administrator admin = Pronadji.pronadjiAdmina(korisnikID, "", "");
				Double plata = admin.getPlata();
				txtPlata.setText(plata.toString());
			}
			add(lblPlata);
			add(txtPlata);
		}
		if (uloga.equals(Uloga.SERVISER)) {
			if(Pronadji.pronadjiServisera(korisnikID, "", "") != null) {
				Serviser serviser = Pronadji.pronadjiServisera(korisnikID, "", "");
				Double plata = serviser.getPlata();
				txtPlata.setText(plata.toString());
				cbSpec.setSelectedItem(serviser.getSpecijalizacija());
			}
			add(lblPlata);
			add(txtPlata);
			add(lblSpec);
			add(cbSpec);
		}
		if (uloga.equals(Uloga.MUSTERIJA)){
			if(Pronadji.pronadjiMusteriju(korisnikID, "", "") != null) {
				Musterija musterija = Pronadji.pronadjiMusteriju(korisnikID, "", "");
				int brojBodova = musterija.getBrojSakupljenihBodova();
				txtBrBod.setText(String.valueOf(brojBodova));
				double dug = musterija.getDug();
				txtDug.setText(String.valueOf(dug));
			}
			else {
				txtBrBod.setText("0");
				txtDug.setText("0");
			}
			txtBrBod.setEnabled(false);
			add(lblBrBod);
			add(txtBrBod);
			txtDug.setEnabled(false);
			add(lblDug);
			add(txtDug);
		}
		add(new JLabel());
		add(btnOK, "split 2");
		add(btnCancel);
	}
	
	private void initActions() {
		
		cbUloga.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(cbUloga.getSelectedItem().equals(Uloga.ADMINISTRATOR)) {
					IzmenaKorisnikaGUI.this.dispose();
					IzmenaKorisnikaGUI.this.setVisible(false);
					IzmenaKorisnikaGUI ik = new IzmenaKorisnikaGUI(adminFunkcije,korisnikID,Uloga.ADMINISTRATOR);
					ik.setVisible(true);
				}	
					
				else if(cbUloga.getSelectedItem().equals(Uloga.SERVISER)) {
					IzmenaKorisnikaGUI.this.dispose();
					IzmenaKorisnikaGUI.this.setVisible(false);
					IzmenaKorisnikaGUI ik = new IzmenaKorisnikaGUI(adminFunkcije,korisnikID,Uloga.SERVISER);
					ik.setVisible(true);
				}
					
				else if(cbUloga.getSelectedItem().equals(Uloga.MUSTERIJA)) {
					IzmenaKorisnikaGUI.this.dispose();
					IzmenaKorisnikaGUI.this.setVisible(false);
					IzmenaKorisnikaGUI ik = new IzmenaKorisnikaGUI(adminFunkcije,korisnikID,Uloga.MUSTERIJA);
					ik.setVisible(true);
				}
			}
		});
		
		
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IzmenaKorisnikaGUI.this.dispose();
				IzmenaKorisnikaGUI.this.setVisible(false);
			}
		});
		
		btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(validacija()) {
					String ime = txtIme.getText().trim();
					String prezime = txtPrezime.getText().trim();
					String jmbg = txtJMBG.getText().trim();
					Pol pol = (Pol)cbPol.getSelectedItem();
					String adresa = txtAdresa.getText().trim();
					String brTel = txtBrTel.getText().trim();
					String korisnickoIme = txtKorIme.getText().trim();
					String sifra = txtLozinka.getText();
					String id = txtID.getText().trim();
					
					
					if(korisnikID != "") { // IZMENA:
						if (Pronadji.pronadjiAdmina(korisnikID, "", "") != null) {
							double plata = Double.parseDouble(txtPlata.getText());
							Administrator admin = Pronadji.pronadjiAdmina(korisnikID, "", "");
							admin.setIme(ime);
							admin.setPrezime(prezime);
							admin.setJmbg(jmbg);
							admin.setPol(pol);
							admin.setAdresa(adresa);
							admin.setBrojTelefona(brTel);
							admin.setKorisinickoIme(korisnickoIme);
							admin.setLozinka(sifra);
							admin.setPlata(plata);
							// upisivanje izmena u fajlu
							CitanjePisanje.izmenaPodataka(CitanjePisanje.adminZaUpis(), "administrator.txt");
						}
						else if (Pronadji.pronadjiServisera(korisnikID, "", "") != null) {
							double plata = Double.parseDouble(txtPlata.getText());
							Specijalizacija spec = (Specijalizacija)cbSpec.getSelectedItem();
							Serviser serviser = Pronadji.pronadjiServisera(korisnikID, "", "");
							serviser.setIme(ime);
							serviser.setPrezime(prezime);
							serviser.setJmbg(jmbg);
							serviser.setPol(pol);
							serviser.setAdresa(adresa);
							serviser.setBrojTelefona(brTel);
							serviser.setKorisinickoIme(korisnickoIme);
							serviser.setLozinka(sifra);	
							serviser.setSpecijalizacija(spec);
							serviser.setPlata(plata);
							// upisivanje izmena u fajlu
							CitanjePisanje.izmenaPodataka(CitanjePisanje.serviserZaUpis(), "serviser.txt");
							
						}
						else if (Pronadji.pronadjiMusteriju(korisnikID, "", "") != null) {
							int brojBodova = Integer.parseInt(txtBrBod.getText());
							Musterija musterija = Pronadji.pronadjiMusteriju(korisnikID, "", "");
							musterija.setIme(ime);
							musterija.setPrezime(prezime);
							musterija.setJmbg(jmbg);
							musterija.setPol(pol);
							musterija.setAdresa(adresa);
							musterija.setBrojTelefona(brTel);
							musterija.setKorisinickoIme(korisnickoIme);
							musterija.setLozinka(sifra);
							musterija.setBrojSakupljenihBodova(brojBodova);
							// upisivanje izmena u fajlu
							CitanjePisanje.izmenaPodataka(CitanjePisanje.musterijaZaUpis(), "musterija.txt");
						}
						
						
					}
					else { // DODAVANJE:
						if (uloga.equals(Uloga.ADMINISTRATOR)) {
							double plata = Double.parseDouble(txtPlata.getText());
							Administrator admin = new Administrator(ime, prezime, jmbg, pol, adresa, brTel, korisnickoIme, 
									sifra, uloga, id, plata, "ne");
							// dodavanje u listu
							adminFunkcije.dodajUkloniAdmina(admin,"dodaj");
							// upisivanje u fajl
							CitanjePisanje.izmenaPodataka(CitanjePisanje.adminZaUpis(), "administrator.txt");
						}
						else if (uloga.equals(Uloga.SERVISER)) {
							double plata = Double.parseDouble(txtPlata.getText());
							Specijalizacija spec = (Specijalizacija)cbSpec.getSelectedItem();
							Serviser serviser = new Serviser(ime, prezime, jmbg, pol, adresa, brTel, korisnickoIme,
									sifra, uloga, id, plata, spec, "ne");
							// dodavanje u listu
							adminFunkcije.dodajUkloniServisera(serviser,"dodaj");
							// upisivanje u fajl
							CitanjePisanje.izmenaPodataka(CitanjePisanje.serviserZaUpis(), "serviser.txt");
						}
						else if (uloga.equals(Uloga.MUSTERIJA)) {
							int brojBodova = Integer.parseInt(txtBrBod.getText());
							double dug = Double.parseDouble(txtDug.getText());
							Musterija musterija = new Musterija (ime, prezime, jmbg, pol, adresa, brTel, korisnickoIme,
									sifra, uloga, id, brojBodova, dug,"ne");
							// dodavanje u listu
							adminFunkcije.dodajUkloniMusteriju(musterija,"dodaj");
							// upisivanje u fajl
							CitanjePisanje.izmenaPodataka(CitanjePisanje.musterijaZaUpis(), "musterija.txt");
						}
						
					}
					IzmenaKorisnikaGUI.this.dispose();
					IzmenaKorisnikaGUI.this.setVisible(false);
				}
			}
		});
	};
	
	
	private void popuniPolja() {
		if(Pronadji.pronadjiAdmina(korisnikID, "", "") != null) {
			Administrator admin = Pronadji.pronadjiAdmina(korisnikID, "", "");
			txtID.setText(admin.getId());
			txtIme.setText(admin.getIme());
			txtPrezime.setText(admin.getPrezime());
			txtJMBG.setText(admin.getJmbg());
			cbPol.setSelectedItem(admin.getPol());
			txtAdresa.setText(admin.getAdresa());
			txtBrTel.setText(admin.getBrojTelefona());
			txtKorIme.setText(admin.getKorisinickoIme());
			txtLozinka.setText(admin.getLozinka());
			cbUloga.setSelectedItem(admin.getUloga());
		}
		else if(Pronadji.pronadjiServisera(korisnikID, "", "") != null) {
			Serviser serviser = Pronadji.pronadjiServisera(korisnikID, "", "");
			txtID.setText(serviser.getId());
			txtIme.setText(serviser.getIme());
			txtPrezime.setText(serviser.getPrezime());
			txtJMBG.setText(serviser.getJmbg());
			cbPol.setSelectedItem(serviser.getPol());
			txtAdresa.setText(serviser.getAdresa());
			txtBrTel.setText(serviser.getBrojTelefona());
			txtKorIme.setText(serviser.getKorisinickoIme());
			txtLozinka.setText(serviser.getLozinka());
			cbUloga.setSelectedItem(serviser.getUloga());
		}
		else if(Pronadji.pronadjiMusteriju(korisnikID, "", "") != null) {
			Musterija musterija = Pronadji.pronadjiMusteriju(korisnikID, "", "");
			txtID.setText(musterija.getId());
			txtIme.setText(musterija.getIme());
			txtPrezime.setText(musterija.getPrezime());
			txtJMBG.setText(musterija.getJmbg());
			cbPol.setSelectedItem(musterija.getPol());
			txtAdresa.setText(musterija.getAdresa());
			txtBrTel.setText(musterija.getBrojTelefona());
			txtKorIme.setText(musterija.getKorisinickoIme());
			txtLozinka.setText(musterija.getLozinka());
			cbUloga.setSelectedItem(musterija.getUloga());
		}
	}
	
	private boolean validacija() {
		boolean ok = true;
		String poruka = "Molimo popravite sledece greske u unosu:\n";
		
		if(txtID.getText().trim().equals("")) {
			poruka += "- Unesite ID\n";
			ok = false;
		}
		else {
			boolean postoji = false;
			if(Pronadji.pronadjiAdmina(txtID.getText().trim(), "", "") != null) {
				postoji = true;
			}
			else if(Pronadji.pronadjiServisera(txtID.getText().trim(), "", "") != null) {
				postoji = true;
			}
			else if(Pronadji.pronadjiMusteriju(txtID.getText().trim(), "", "") != null) {
				postoji = true;
			}
			if (postoji && !txtID.getText().trim().equals(korisnikID)) {
				poruka += "- ID vec postoji\n";
				ok = false;
			}
			else if (txtID.getText().trim().contains(",")) {
				poruka += "- ID ne moze da sadrzi zarez\n";
				ok = false;
			}
		}
		
		if(txtIme.getText().trim().equals("")) {
			poruka += "- Unesite ime\n";
			ok = false;
		}
		else {
			if (txtIme.getText().trim().contains(",")) {
				poruka += "- Ime ne moze da sadrzi zarez\n";
				ok = false;
			}
		}
		if(txtPrezime.getText().trim().equals("")) {
			poruka += "- Unesite prezime\n";
			ok = false;
		}
		else {
			if (txtPrezime.getText().trim().contains(",")) {
				poruka += "- Prezime ne moze da sadrzi zarez\n";
				ok = false;
			}
		}
		if(txtJMBG.getText().trim().equals("")) {
			poruka += "- Unesite JMBG\n";
			ok = false;
		}
		else {
			if (txtJMBG.getText().trim().length() != 13) {
				poruka += "- Unesite ispravan JMBG\n";
				ok = false;
			}
			else {
				try {
					Double.parseDouble(txtJMBG.getText().trim());
				}
				catch (NumberFormatException e) {
					poruka += "- JMBG mora biti broj\n";
					ok = false;
				}
			}
		}
		if(txtAdresa.getText().trim().equals("")) {
			poruka += "- Unesite adresu\n";
			ok = false;
		}
		else {
			if (txtAdresa.getText().trim().contains(",")) {
				String ispravljenaAdresa = "";
				for(String adresa : txtAdresa.getText().trim().split(",")) {
					ispravljenaAdresa+=adresa+" ";
				}
				txtAdresa.setText(ispravljenaAdresa);
			}
		}
		if(txtBrTel.getText().trim().equals("")) {
			poruka += "- Unesite broj telefona\n";
			ok = false;
		}
		else {
			try {
				Double.parseDouble(txtBrTel.getText().trim());
			}
			catch (NumberFormatException e) {
				poruka += "- Br. tel. mora biti broj\n";
				ok = false;
			}
		}
		if(txtKorIme.getText().trim().equals("")) {
			poruka += "- Unesite korisnicko ime\n";
			ok = false;
		}
		else {
			String korisnickoIme = txtKorIme.getText().trim();
			boolean postoji = false;
			if (Pronadji.pronadjiAdmina("", korisnickoIme, "") != null) {
				if(!Pronadji.pronadjiAdmina("", korisnickoIme, "").getId().equals(korisnikID)) {
					postoji = true;
				}
			}
			else if (Pronadji.pronadjiServisera("", korisnickoIme, "") != null) {
				if(!Pronadji.pronadjiServisera("", korisnickoIme, "").getId().equals(korisnikID)) {
					postoji = true;
				}
			}
			else if (Pronadji.pronadjiMusteriju("", korisnickoIme, "") != null) {
				if(!Pronadji.pronadjiServisera("", korisnickoIme, "").getId().equals(korisnikID)) {
					postoji = true;
				}
			}
			
			if(postoji) {
				poruka += "- Korisnicko ime vec postoji\n";
				ok = false;
			}
			else {
				if (txtKorIme.getText().trim().contains(",")) {
					poruka += "- Kor. ime ne moze da sadrzi zarez\n";
					ok = false;
				}
			}
		}
		
		String sifra = txtLozinka.getText();
		if(sifra.equals("")) {
			poruka += "- Unesite sifru\n";
			ok = false;
		}
		else {
			if (txtLozinka.getText().trim().contains(",")) {
				poruka += "- Lozinka ne moze da sadrzi zarez\n";
				ok = false;
			}
		}
		if (cbUloga.getSelectedItem().equals(Uloga.ADMINISTRATOR) || cbUloga.getSelectedItem().equals(Uloga.SERVISER)) {
			if (txtPlata.getText().trim().equals("")) {
				poruka += "- Unesite platu\n";
				ok = false;
			}
			else {
				try {
					Double.parseDouble(txtPlata.getText().trim());
				}
				catch (NumberFormatException e) {
					poruka += "- Plata mora biti broj\n";
					ok = false;
				}
			}
		}
		if(ok == false) {
			JOptionPane.showMessageDialog(null, poruka, "Neispravni podaci", JOptionPane.WARNING_MESSAGE);
		}
		
		return ok;
	}
}