package gui.izmena;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import aplikacija.Pronadji;
import citanjePisanje.CitanjePisanje;
import enumeracije.Gorivo;
import enumeracije.Marka;
import enumeracije.Model;
import funkcije.AdminFunkcije;
import modeli.Administrator;
import modeli.Automobil;
import modeli.Musterija;
import net.miginfocom.swing.MigLayout;

public class IzmenaAutomobilaGUI extends JFrame{
	private JLabel lblID = new JLabel("ID");
	private JTextField txtID = new JTextField(20);
	private JLabel lblMarka = new  JLabel("Marka");
	private JComboBox<Marka> cbMarka = new JComboBox<Marka>(Marka.values());
	private JLabel lblModel = new  JLabel("Model");
	private JComboBox<Model> cbModel;
	private JLabel lblVlasnik = new  JLabel("Vlasnik");
	private JComboBox<String> cbVlasnik;
	private JLabel lblGorivo = new  JLabel("Gorivo");
	private JComboBox<Gorivo> cbGorivo = new JComboBox<Gorivo>(Gorivo.values());
	private JLabel lblGod = new JLabel("Godina proizvodnje");
	private JTextField txtGod = new JTextField(20);
	private JLabel lblZapremina = new JLabel("Zapremina motora");
	private JTextField txtZapremina = new JTextField(20);
	private JLabel lblSnaga = new JLabel("Snaga motora");
	private JTextField txtSnaga = new JTextField(20);
	
	private JButton btnOk = new JButton("OK");
	private JButton btnCancel = new JButton("Cancel");
	
	private String korisnikID;
	private Automobil auto;
	
	public IzmenaAutomobilaGUI(String korisnikID,String autoID,Marka marka) {
		this.korisnikID = korisnikID;
		this.auto = Pronadji.pronadjiAutomobil(autoID);
		if (auto != null) {
			setTitle("Izmena automobila - " + auto.getId());
		}
		else {
			setTitle("Dodavanje automobila");
		}
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		initGUI(marka);
		initActions();
		setResizable(false);
		pack();
	}
	
	private void initGUI(Marka marka) {
		MigLayout layout = new MigLayout("wrap 2", "[][]", "[][][][][]20[]");
		setLayout(layout);
		// pravljenje combo box modela od trenutnih musterija
		ArrayList<Musterija> listaMusterija = Administrator.getListaMusterija();
		String[] moguciVlasnici = new String[listaMusterija.size()];
		for (Musterija musterija : listaMusterija) {
			moguciVlasnici[listaMusterija.indexOf(musterija)] = musterija.getId()+"-"+musterija.getKorisinickoIme();
		}
		cbVlasnik = new JComboBox<String>(moguciVlasnici);
		
		if(auto != null) {
			txtID.setEditable(false);
			cbVlasnik.setEnabled(false);
			popuniPolja();
		}
		if (marka != null) {
			cbMarka.setSelectedItem(marka);
		}
		// pravljenje combo box modela u zavisnosti od unete marke
		int k = 0;
		Model[] modeli = new Model[Administrator.getMarkeModeli().get(cbMarka.getSelectedItem()).size()];
		for (Model model : Administrator.getMarkeModeli().get(cbMarka.getSelectedItem())) {
			modeli[k] = model;
			k++;
		}
		cbModel = new JComboBox<Model>(modeli);
		if(auto != null && marka == null) {
			cbModel.setSelectedItem(auto.getModel());
		}
		
		add(lblID);
		add(txtID);
		add(lblMarka);
		add(cbMarka);
		add(lblModel);
		add(cbModel);
		add(lblVlasnik);
		if (Pronadji.pronadjiMusteriju(korisnikID, "", "") != null) {
			for (String vlasnik : moguciVlasnici) {
				if (vlasnik.split("-")[0].equals(korisnikID)) {
					cbVlasnik.setSelectedItem(vlasnik);
					cbVlasnik.setEnabled(false);
					break;
				}
			}
		}
		add(cbVlasnik);
		add(lblGorivo);
		add(cbGorivo);
		add(lblGod);
		add(txtGod);
		add(lblZapremina);
		add(txtZapremina);
		add(lblSnaga);
		add(txtSnaga);	
		
		add(new JLabel());
		add(btnOk, "split 2");
		add(btnCancel);
	}
	
	
	private void popuniPolja() {
		txtID.setText(auto.getId());
		cbMarka.setSelectedItem(auto.getMarka());
		cbVlasnik.setSelectedItem(auto.getVlasnik());
		cbGorivo.setSelectedItem(auto.getGorivo());
		txtGod.setText(String.valueOf(auto.getGodinaProizvodnje()));
		txtZapremina.setText(String.valueOf(auto.getZapreminaMotora()));
		txtSnaga.setText(String.valueOf(auto.getSnagaMotora()));
		
	}
	
	private void initActions() {
		cbMarka.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IzmenaAutomobilaGUI.this.dispose();
				IzmenaAutomobilaGUI.this.setVisible(false);
				Marka marka = Marka.valueOf(cbMarka.getSelectedItem().toString());
				if (auto != null) {
					IzmenaAutomobilaGUI isd = new IzmenaAutomobilaGUI(korisnikID,auto.getId(),marka);
					isd.setVisible(true);
				}
				else {
					IzmenaAutomobilaGUI isd = new IzmenaAutomobilaGUI(korisnikID,"",marka);
					isd.setVisible(true);
				}
			}
		});
		
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(validacija()) {	
					String id = txtID.getText().trim();
					Marka marka = (Marka)cbMarka.getSelectedItem();
					Model model = (Model)cbModel.getSelectedItem();
					String vlasnikID = cbVlasnik.getSelectedItem().toString().split("-")[0];
					Musterija vlasnik = Pronadji.pronadjiMusteriju(vlasnikID, "", "");
					Gorivo gorivo = Gorivo.valueOf(cbGorivo.getSelectedItem().toString());
					int god = Integer.parseInt(txtGod.getText().trim());
					double zapremina = Double.parseDouble(txtZapremina.getText().trim());
					int snaga = Integer.parseInt(txtSnaga.getText().trim());
					
					if (auto != null) {	// IZMENA
						auto.setId(id);
						auto.setMarka(marka);
						auto.setModel(model);
						auto.setVlasnik(vlasnik);
						auto.setGorivo(gorivo);
						auto.setGodinaProizvodnje(god);
						auto.setZapreminaMotora(zapremina);
						auto.setSnagaMotora(snaga);
					}	
					else { // DODAVANJE
						Automobil automobil = new Automobil(vlasnik,marka,model,god,zapremina,snaga,gorivo,id,"ne");
						AdminFunkcije af = new AdminFunkcije(null);
						af.dodajUkloniAutomobil(automobil, "dodaj");
					}
					CitanjePisanje.izmenaPodataka(CitanjePisanje.automobiliZaUpis(), "automobil.txt");
					IzmenaAutomobilaGUI.this.dispose();
					IzmenaAutomobilaGUI.this.setVisible(false);
				}
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IzmenaAutomobilaGUI.this.dispose();
				IzmenaAutomobilaGUI.this.setVisible(false);
			}
		});
	}
	
	private boolean validacija() {
		boolean ok = true;
		String poruka = "Molimo popravite sledece greske u unosu:\n";
		
		if(txtID.getText().trim().equals("")) {
			poruka += "- Unesite ID\n";
			ok = false;
		}
		else {
			if(Pronadji.pronadjiAutomobil(txtID.getText().trim()) != null) {
				// ako automobil nije prosledjen znaci da je u pitanju dodavanje
				// automobila i ID ne moze da se postavi jer vec postoji
				// u suprotnom se postavlja iako postoji jer je to isti id zato
				// sto korisnik nema opciju da menja id tokom izmene automobila
				if (auto == null) {
					poruka += "- Ovaj ID vec postoji\n";
					ok = false;
				}
			}
			else if (txtID.getText().trim().contains(",")) {
				poruka += "- ID ne moze da sadrzi zarez\n";
				ok = false;
			}
		}
		
		if(txtGod.getText().trim().equals("")) {
			poruka += "- Unesite god. proizvodnje\n";
			ok = false;
		}
		else {
			if(txtGod.getText().trim().length() != 4) {
				poruka += "- Unesite ispravnu god. proizvodnje\n";
				ok = false;
			}
			else {
				try {
					Integer.parseInt(txtGod.getText().trim());
				}
				catch (NumberFormatException e) {
					poruka += "- God. proizvodnje mora biti broj\n";
					ok = false;
				}
			}
		}	
		
		if(txtZapremina.getText().trim().equals("")) {
			poruka += "- Unesite zapreminu motora\n";
			ok = false;
		}
		else {
			try {
				Double.parseDouble(txtZapremina.getText().trim());
			}
			catch (NumberFormatException e) {
				poruka += "- Zapremina mora biti broj\n";
				ok = false;
			}
		}	
		
		if(txtSnaga.getText().trim().equals("")) {
			poruka += "- Unesite snagu motora\n";
			ok = false;
		}
		else {
			try {
				Integer.parseInt(txtSnaga.getText().trim());
			}
			catch (NumberFormatException e) {
				poruka += "- Snaga motora biti broj\n";
				ok = false;
			}
		}	
		
		if(ok == false) {
			JOptionPane.showMessageDialog(null, poruka, "Neispravni podaci", JOptionPane.WARNING_MESSAGE);
		}
		
		return ok;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
