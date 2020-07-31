package gui.izmena;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import aplikacija.Pronadji;
import citanjePisanje.CitanjePisanje;
import enumeracije.*;
import funkcije.AdminFunkcije;
import modeli.Administrator;
import modeli.ServisniDeo;
import net.miginfocom.swing.MigLayout;

public class IzmenaServisnogDelaGUI extends JFrame{
	private JLabel lblID = new JLabel("ID");
	private JTextField txtID = new JTextField(20);
	private JLabel lblNaziv = new JLabel("Naziv");
	private JTextField txtNaziv = new JTextField(20);
	private JLabel lblCena = new JLabel("Cena");
	private JTextField txtCena = new JTextField(20);
	private JLabel lblMarka = new  JLabel("Marka");
	private JComboBox<Marka> cbMarka = new JComboBox<Marka>(Marka.values());
	private JLabel lblModel = new  JLabel("Model");
	private JComboBox<Model> cbModel;
	
	private JButton btnOK = new JButton("OK");
	private JButton btnCancel = new JButton("Cancel");
	
	private AdminFunkcije adminFunkcije;
	private String sdID;
	
	public IzmenaServisnogDelaGUI(AdminFunkcije adminFunkcije, String sdID, Marka marka) {
		this.adminFunkcije = adminFunkcije;
		this.sdID = sdID;
		setTitle("Dodavanje servisnog dela");
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
		
		if(sdID != "") {
			txtID.setEnabled(false);
			ServisniDeo sd = Pronadji.pronadjiServnisniDeo(sdID);
			popuniPolja(sd);
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
		if(sdID != "" && marka == null) {
			ServisniDeo sd = Pronadji.pronadjiServnisniDeo(sdID);
			cbModel.setSelectedItem(sd.getModel());
		}
				
		add(lblID);
		add(txtID);
		add(lblNaziv);
		add(txtNaziv);
		add(lblCena);
		add(txtCena);
		add(lblMarka);
		add(cbMarka);
		add(lblModel);
		add(cbModel);
		
		add(new JLabel());
		add(btnOK, "split 2");
		add(btnCancel);
	}
	
	private void popuniPolja(ServisniDeo sd) {
		txtID.setText(sd.getId());
		txtNaziv.setText(sd.getNaziv());
		txtCena.setText(String.valueOf(sd.getCena()));
		cbMarka.setSelectedItem(sd.getMarka());
	}
	
	private void initActions() {
		cbMarka.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IzmenaServisnogDelaGUI.this.dispose();
				IzmenaServisnogDelaGUI.this.setVisible(false);
				Marka marka = Marka.valueOf(cbMarka.getSelectedItem().toString());
				IzmenaServisnogDelaGUI isd = new IzmenaServisnogDelaGUI(adminFunkcije,sdID,marka);
				isd.setVisible(true);
			}
		});
		
		
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IzmenaServisnogDelaGUI.this.dispose();
				IzmenaServisnogDelaGUI.this.setVisible(false);
			}
		});
		
		btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(validacija()) {
					String id = txtID.getText().trim();
					String naziv = txtNaziv.getText().trim();
					double cena = Double.parseDouble(txtCena.getText().trim());
					Marka marka = (Marka)cbMarka.getSelectedItem();
					Model model = (Model)cbModel.getSelectedItem();
					
					if(sdID != "") { // IZMENA:
						ServisniDeo sd = Pronadji.pronadjiServnisniDeo(sdID);
						sd.setId(id);
						sd.setNaziv(naziv);
						sd.setCena(cena);
						sd.setMarka(marka);
						sd.setModel(model);
					}
					else { // DODAVANJE:
						ServisniDeo sd = new ServisniDeo(marka,model,naziv,cena,id,"ne");
						if(naziv.contains("Desna strana") || naziv.contains("desna strana") ||
								naziv.contains("Leva strana") || naziv.contains("leva strana")) {
							int izbor = JOptionPane.showConfirmDialog(null, 
									"Da li zelite da dodate simetricni deo?", 
									"Simetricni deo" ,JOptionPane.YES_NO_OPTION);
							if(izbor == JOptionPane.YES_OPTION) {
								adminFunkcije.dodavanjeServisnogDela(sd, true);
							}
							else {
								adminFunkcije.dodavanjeServisnogDela(sd, false);
							}
						}
					}
					// upisivanje u fajl
					CitanjePisanje.izmenaPodataka(CitanjePisanje.sdZaUpis(), "servisniDelovi.txt");
					IzmenaServisnogDelaGUI.this.dispose();
					IzmenaServisnogDelaGUI.this.setVisible(false);
				}
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
			if(Pronadji.pronadjiServnisniDeo(txtID.getText().trim()) != null && !txtID.getText().trim().equals(sdID)) {
				poruka += "- ID vec postoji\n";
				ok = false;
			}
			else if (txtID.getText().trim().contains(",")) {
				poruka += "- ID ne moze da sadrzi zarez\n";
				ok = false;
			}
		}
		
		if(txtNaziv.getText().trim().equals("")) {
			poruka += "- Unesite naziv\n";
			ok = false;
		}
		else {
			if (txtNaziv.getText().trim().contains(",")) {
				poruka += "- Naziv ne moze da sadrzi zarez\n";
				ok = false;
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
