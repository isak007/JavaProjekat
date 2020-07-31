package gui;

import aplikacija.*;
import funkcije.AdminFunkcije;
import funkcije.MusterijaFunkcije;
import funkcije.ServiserFunkcije;
import modeli.Administrator;
import modeli.Musterija;
import modeli.Serviser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class LoginGUI extends JFrame {

	private JLabel lblGreeting = new JLabel("Dobrodošli. Molimo da se prijavite.");
	private JLabel lblUsername = new JLabel("Korisničko ime");
	private JTextField txtKorisnickoIme = new JTextField(20);
	private JLabel lblPassword = new JLabel("Šifra");
	private JPasswordField pfPassword = new JPasswordField(20);
	private JButton btnOk = new JButton("OK");
	private JButton btnCancel = new JButton("Cancel");
	
	
	public LoginGUI() {
		setTitle("Prijava");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		initGUI();
		initActions();
		pack();
	}
	
	public void initGUI() {
		MigLayout mig = new MigLayout("wrap 2", "[][]", "[]10[][]10[]");
		setLayout(mig);
		
		add(lblGreeting, "span 2");
		add(lblUsername);
		add(txtKorisnickoIme);
		add(lblPassword);
		add(pfPassword);
		add(new JLabel());
		add(btnOk, "split 2");
		add(btnCancel);
		
		
		txtKorisnickoIme.setText("admin");
		pfPassword.setText("admin");
		getRootPane().setDefaultButton(btnOk);
	}
	
	public void initActions() {
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LoginGUI.this.dispose();
				LoginGUI.this.setVisible(false);
			}
		});
		
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String korisnickoIme = txtKorisnickoIme.getText().trim();
				String lozinka = new String(pfPassword.getPassword()).trim();
				
				if(korisnickoIme.equals("") || lozinka.equals("")) {
					JOptionPane.showMessageDialog(null, "Niste uneli sve podatke za prijavu.", "Greska", JOptionPane.WARNING_MESSAGE);
				}
				else {
					if (Pronadji.pronadjiMusteriju("", korisnickoIme, lozinka) != null) {
						Musterija musterija = Pronadji.pronadjiMusteriju("", korisnickoIme, lozinka);
						LoginGUI.this.dispose();
						LoginGUI.this.setVisible(false);
						GlavniProzorMusterijaGUI gp = new GlavniProzorMusterijaGUI(new MusterijaFunkcije(musterija));
						gp.setVisible(true);
					}
				
					else if (Pronadji.pronadjiServisera("", korisnickoIme, lozinka) != null) {
						Serviser serviser = Pronadji.pronadjiServisera("", korisnickoIme, lozinka);
						LoginGUI.this.dispose();
						LoginGUI.this.setVisible(false);
						GlavniProzorServiserGUI gp = new GlavniProzorServiserGUI(new ServiserFunkcije(serviser));
						gp.setVisible(true);
					}
				
					else if (Pronadji.pronadjiAdmina("", korisnickoIme, lozinka) != null) {
						Administrator admin = Pronadji.pronadjiAdmina("", korisnickoIme, lozinka);
						LoginGUI.this.dispose();
						LoginGUI.this.setVisible(false);
						GlavniProzorAdminGUI gp = new GlavniProzorAdminGUI(new AdminFunkcije(admin));
						gp.setVisible(true);
					}
					else {
						JOptionPane.showMessageDialog(null, "Pogrešni login podaci.", "Greška", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		
	}
}