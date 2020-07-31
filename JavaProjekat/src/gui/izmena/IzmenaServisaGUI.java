package gui.izmena;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import aplikacija.Pronadji;
import citanjePisanje.CitanjePisanje;
import enumeracije.StatusServisa;
import funkcije.AdminFunkcije;
import funkcije.ServiserFunkcije;
import modeli.Administrator;
import modeli.Automobil;
import modeli.Musterija;
import modeli.Servis;
import modeli.Serviser;
import modeli.ServisniDeo;
import gui.prikaz.ServisniDeloviGUI;
import net.miginfocom.swing.MigLayout;

public class IzmenaServisaGUI extends JFrame{
	private JLabel lblID = new JLabel("ID");
	private JTextField txtID = new JTextField(20);
	private JLabel lblServiser = new JLabel("Serviser");
	private JComboBox<String> cbServiser;
	private JLabel lblAutomobil = new JLabel("Automobil");
	private JTextField txtAutomobil = new JTextField(20);
	private JLabel lblOpis = new JLabel("Opis");
	private JTextField txtOpis = new JTextField(20);
	private JLabel lblTermin = new JLabel("Termin (yy-MM-dd HH:mm)");
	private JTextField txtTermin = new JTextField(20);
	private JLabel lblStatus = new JLabel("Status servisa");
	private JTextField txtStatus = new JTextField(20);
	private JLabel lblSifreDelova = new JLabel("Sifre ser. delova (odvojene tackom)");
	private JTextField txtSifreDelova = new JTextField(20);
	private JLabel lblCena= new JLabel("Cena");
	private JTextField txtCena = new JTextField(20);
	private JLabel lblOtplacen = new JLabel("Otplacen");
	private JTextField txtOtplacen = new JTextField(20);
	private JButton btnPregledDelova = new JButton("Pregled delova");
	private JButton btnOk = new JButton("OK");
	private JButton btnCancel = new JButton("Cancel");
	
	private Servis servis;
	private Automobil automobil;
	private String opis;
	private String servisID;
	private Administrator admin;
	private Serviser serviser;
	
	public IzmenaServisaGUI(String korisnikID, String servisID, String automobilID, String opis) {
		this.servisID = servisID;
		this.admin = Pronadji.pronadjiAdmina(korisnikID, "", "");
		this.serviser = Pronadji.pronadjiServisera(korisnikID, "", "");
		this.servis = Pronadji.pronadjiServis(servisID);
		this.automobil = Pronadji.pronadjiAutomobil(automobilID);
		this.opis = opis;
		if(servis == null) {
			setTitle("Dodavanje servisa");
		}
		else {
			setTitle("Izmena servisa - " + servis.getId());
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
		// pravljenje combo box modela od trenutnih musterija
		ArrayList<Serviser> listaServisera = Administrator.getListaServisera();
		String[] moguciServiseri = new String[listaServisera.size()];
		for (Serviser serviser : listaServisera) {
			moguciServiseri[listaServisera.indexOf(serviser)] = serviser.getId()+"-"+serviser.getKorisinickoIme();
		}
		cbServiser = new JComboBox<String>(moguciServiseri);
		
		// ako je samo servisID prosledjen znaci da se radi o izmeni servisa
		// pa se polja moraju popuniti
		if(servis != null) {
			popuniPolja();
			txtID.setEnabled(false);
			txtAutomobil.setEnabled(false);
			txtSifreDelova.setEnabled(false);
			cbServiser.setEnabled(false);
		}
		
		// ako se radi o dodavanju novog servisa
		else {
			txtAutomobil.setText(automobil.getId()+"-"+automobil.getVlasnik().getKorisinickoIme()+" "+
		automobil.getMarka()+" "+automobil.getModel());
			txtAutomobil.setEnabled(false);		
			txtOpis.setText(opis);
			txtOpis.setEnabled(false);
			txtOtplacen.setText("ne");
			txtOtplacen.setEnabled(false);
		}
		
		add(lblID);
		add(txtID);
		add(lblServiser);
		if (serviser != null) {
			for (String serviserString : moguciServiseri) {
				if (serviserString.split("-")[0].equals(serviser.getId())) {
					cbServiser.setSelectedItem(serviserString);
					break;
				}
			}
			cbServiser.setEnabled(false);
			if(servis != null) {
				txtTermin.setEnabled(false);
			}
		}
		add(cbServiser);
		add(lblAutomobil);
		add(txtAutomobil);
		add(lblOpis);
		add(txtOpis);
		add(lblTermin);
		add(txtTermin);
		if(servis != null) {
			add(lblStatus);
			add(txtStatus);
			txtStatus.setEnabled(false);
			add(lblOtplacen);
			add(txtOtplacen);
			txtOtplacen.setEnabled(false);
		}
		else {
			txtCena.setText("0.0");
			txtCena.setEnabled(false);
		}
		add(btnPregledDelova,"span 2");
		add(lblSifreDelova);
		add(txtSifreDelova);
		add(lblCena);
		add(txtCena);
		
		add(new JLabel());
		add(btnOk, "split 2");
		add(btnCancel);
	}
	
	private void initActions() {
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(validacija()) {		
					String id = txtID.getText().trim();
					String serviserID = cbServiser.getSelectedItem().toString().trim().split("-")[0];
					Serviser serviserObj = Pronadji.pronadjiServisera(serviserID, "", "");
					String termin = txtTermin.getText().trim();
					String opis = txtOpis.getText().trim();
					// kreiranje liste servisnih delova od sifre delova koje je korisnik uneo kao String
					String sifreDelova = txtSifreDelova.getText().trim();
					ArrayList<ServisniDeo> listaDelova = new ArrayList<ServisniDeo>();
					for (String sifra : sifreDelova.split(",")) {
						if (Pronadji.pronadjiServnisniDeo(sifra) != null) {
							listaDelova.add(Pronadji.pronadjiServnisniDeo(sifra));
						}
					}
					Automobil automobilObj = Pronadji.pronadjiAutomobil(txtAutomobil.getText().trim().split("-")[0]);
					double cena = Double.parseDouble(txtCena.getText());
					
					if(servis == null) { // DODAVANJE:
						// koriscenje prosledjenog automobila od strane korisnika
						// ako je u pitanju dodavanje servisa
						Servis noviServis = new Servis(automobilObj,serviserObj,termin,opis,listaDelova,StatusServisa.ZAKAZAN,
								id,cena,"ne","ne");
						if (admin!=null) {
							AdminFunkcije af = new AdminFunkcije(admin);
							af.kreiranjeServisa(noviServis);
						}
						else if (serviser!=null) {
							ServiserFunkcije sf = new ServiserFunkcije(serviser);
							sf.kreiranjeServisa(noviServis);
						}
						
						// uklanjanje rezervacije iz liste
						AdminFunkcije adminf = new AdminFunkcije(null);
						ArrayList<String> listaRez = Administrator.getListaRezervacija();
						for (int i = 0; i < listaRez.size(); i++) {
							if (listaRez.get(i).split(",")[0].equals(automobilObj.getId())) {
								adminf.dodajUkloniRez(listaRez.get(i), "ukloni");
								break;
							}
						}
						// uklanjanje rezervacije iz fajla
						CitanjePisanje.prazanFajl("rezervacije.txt");
						CitanjePisanje.pisanjeUfajl(CitanjePisanje.rezZaUpis(), "rezervacije.txt");
					}
					
					else { // IZMENA:
						// koriscenje starog automobila ako je u pitanju izmena
						servis.setOpis(opis);
						servis.setTermin(termin);
						servis.setCena(cena);
						CitanjePisanje.izmenaPodataka(CitanjePisanje.servisZaUpis(), "servis.txt");
					}
					IzmenaServisaGUI.this.dispose();
					IzmenaServisaGUI.this.setVisible(false);
				}
			}
		});
		
		btnPregledDelova.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (admin != null) {
					AdminFunkcije af = new AdminFunkcije(admin);
					ServisniDeloviGUI sd = new ServisniDeloviGUI(af);
					sd.setVisible(true);
				}
				else {
					ServisniDeloviGUI sd = new ServisniDeloviGUI(null);
					sd.setVisible(true);
				}
			}
		});
		
		
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IzmenaServisaGUI.this.dispose();
				IzmenaServisaGUI.this.setVisible(false);
			}
		});
		
	}
	
	private void popuniPolja() {	
		txtAutomobil.setText(servis.getAutomobil().getId() + 
				"-" + servis.getAutomobil().getVlasnik().getKorisinickoIme()+" "+
				servis.getAutomobil().getMarka()+" "+servis.getAutomobil().getModel());
		txtID.setText(servis.getId());
		txtOpis.setText(servis.getOpis());
		txtTermin.setText(servis.getTermin());
		txtOtplacen.setText(servis.getOtplacen());
		txtCena.setText(String.valueOf(servis.getCena()));
		txtSifreDelova.setText(servis.getSifreDelova());
		txtStatus.setText(servis.getStatusServisa().toString());
		
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
			if (postoji && !txtID.getText().trim().equals(servisID)) {
				poruka += "- ID vec postoji\n";
				ok = false;
			}
			else if (txtID.getText().trim().contains(",")) {
				poruka += "- ID ne moze da sadrzi zarez\n";
				ok = false;
			}
		}
		
		
		if(txtOpis.getText().trim().equals("")) {
			poruka += "- Unesite opis\n";
			ok = false;
		}
		else if (txtOpis.getText().trim().contains(",")) {
			String ispravljenOpis = "";
			for(String opis : txtOpis.getText().trim().split(",")) {
				ispravljenOpis+=opis+" ";
			}
			txtOpis.setText(ispravljenOpis);
		}
		
		if(txtTermin.getText().trim().equals("")) {
			poruka += "- Unesite termin\n";
			ok = false;
		}
		else {
			SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
			GregorianCalendar sad = new GregorianCalendar();
			GregorianCalendar termin = new GregorianCalendar();
			try {
				termin.setTime(format.parse(txtTermin.getText().trim()));
				if (sad.after(termin)) {
					throw new ParseException("",1);
				}
			}
			catch(ParseException e) {
				poruka += "- Invalidan format za termin\n";
				ok = false;
			}
		}
		
		if(!txtSifreDelova.getText().trim().equals("")) {
			String sifreDelova = txtSifreDelova.getText().trim();
			if(sifreDelova.contains(".")) {
				for (String sifra : sifreDelova.split("\\.")) {
					if(Pronadji.pronadjiServnisniDeo(sifra) == null) {
						poruka += "- Unesite ispravne sifre(ID) servisnih delova\n";
						ok = false;
						break;
					}
					else {
						ServisniDeo sd = Pronadji.pronadjiServnisniDeo(sifra);
						Automobil automobilObj = Pronadji.pronadjiAutomobil(txtAutomobil.getText().trim().split("-")[0]);
						if (automobilObj != null) {
							if(sd.getMarka() != automobilObj.getMarka() || sd.getModel() != automobilObj.getModel()) {
								poruka += "- Unesite odgovarajuce servisne delove\n";
								ok = false;
								break;
							}
						}
					}
				}
			}
			else {
				if(Pronadji.pronadjiServnisniDeo(sifreDelova) == null) {
					poruka += "- Unesite ispravne sifre(ID) servisnih delova\n";
					ok = false;
				}
				else {
					ServisniDeo sd = Pronadji.pronadjiServnisniDeo(sifreDelova);
					Automobil automobilObj = Pronadji.pronadjiAutomobil(txtAutomobil.getText().trim().split("-")[0]);
					if (automobilObj != null) {
						if(sd.getMarka() != automobilObj.getMarka() || sd.getModel() != automobilObj.getModel()) {
							poruka += "- Unesite odgovarajuce servisne delove\n";
							ok = false;
						}
					}
				}
			}
		}
		
		if(txtCena.getText().trim().equals("")) {
			poruka += "- Unesite cenu\n";
			ok = false;
		}
		else {
			try {
				Double.parseDouble(txtCena.getText().trim());
			}
			catch (NumberFormatException e) {
				poruka += "- Cena mora biti broj\n";
				ok = false;
			}
		}		
		
		if(ok == false) {
			JOptionPane.showMessageDialog(null, poruka, "Neispravni podaci", JOptionPane.WARNING_MESSAGE);
		}
		
		return ok;
	}
}
