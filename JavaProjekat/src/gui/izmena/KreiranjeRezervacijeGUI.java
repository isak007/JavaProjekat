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
import enumeracije.Marka;
import enumeracije.Model;
import funkcije.AdminFunkcije;
import modeli.Administrator;
import modeli.Automobil;
import modeli.Musterija;
import net.miginfocom.swing.MigLayout;

public class KreiranjeRezervacijeGUI extends JFrame{
	private JLabel lblOpis = new JLabel("Opis");
	private JTextField txtOpis = new JTextField(20);
	private JLabel lblAutomobil = new  JLabel("Automobil");
	private JComboBox<String> cbAutomobil;
	
	private JButton btnOk = new JButton("OK");
	private JButton btnCancel = new JButton("Cancel");
	
	private Musterija musterija;
	
	public KreiranjeRezervacijeGUI(String korisnikID) {
		this.musterija = Pronadji.pronadjiMusteriju(korisnikID, "", "");
		
		setTitle("Kreiranje rezervacije");
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
		ArrayList<Automobil> listaAutomobila = musterija.getListaAutomobila();
		String[] automobili = new String[musterija.getListaAutomobila().size()];
		for (Automobil auto : listaAutomobila) {
			automobili[listaAutomobila.indexOf(auto)] = auto.getId()+"-"+auto.getMarka()+" "+auto.getModel();
		}
		cbAutomobil = new JComboBox<String>(automobili);
		
		add(lblOpis);
		add(txtOpis);
		add(lblAutomobil);
		add(cbAutomobil);
		
		add(new JLabel());
		add(btnOk, "split 2");
		add(btnCancel);
	}
	
	private void initActions() {
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(validacija()) {
					String automobilID = cbAutomobil.getSelectedItem().toString().trim().split("-")[0];	
					String opis = txtOpis.getText().trim();
					String rezervacija = automobilID+","+opis;
					// dodavanje u listu
					AdminFunkcije af = new AdminFunkcije(null);
					af.dodajUkloniRez(rezervacija, "dodaj");
					// upisivanje u fajl
					CitanjePisanje.pisanjeUfajl(rezervacija, "rezervacije.txt");
					KreiranjeRezervacijeGUI.this.dispose();
					KreiranjeRezervacijeGUI.this.setVisible(false);
				}
			}
		});
		
		
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				KreiranjeRezervacijeGUI.this.dispose();
				KreiranjeRezervacijeGUI.this.setVisible(false);
			}
		});
	}
	
	private boolean validacija() {
		boolean ok = true;

		for (String rez : Administrator.getListaRezervacija()) {
			String unetiAutomobilID = cbAutomobil.getSelectedItem().toString().trim().split("-")[0];
			if (rez.split(",")[0].equals(unetiAutomobilID)) {
				ok = false;
			}
		}
		
		if(ok == false) {
			String poruka = "Rezervacija za ovaj automobil vec postoji.";
			JOptionPane.showMessageDialog(null, poruka, "Upozorenje", JOptionPane.WARNING_MESSAGE);
		}
		else { 
			if (txtOpis.getText().trim().contains(",")) {
				String ispravljenOpis = "";
				for(String opis : txtOpis.getText().trim().split(",")) {
					ispravljenOpis+=opis+" ";
				}
				txtOpis.setText(ispravljenOpis);
			}
		}
		return ok;
	}
}
