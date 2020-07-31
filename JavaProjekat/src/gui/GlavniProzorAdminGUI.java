package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import funkcije.AdminFunkcije;
import gui.prikaz.KorisniciGUI;
import gui.prikaz.RezervacijeGUI;
import gui.prikaz.ServisiGUI;
import gui.prikaz.ServisneKnjiziceGUI;
import gui.prikaz.ServisniDeloviGUI;
import modeli.Administrator;
import gui.prikaz.AutomobiliGUI;

public class GlavniProzorAdminGUI extends JFrame{
	private JMenuBar mainMenu = new JMenuBar();
	
	// Korinsici menu
	private JMenu korisniciMenu = new JMenu("Korisnici");
	private JMenuItem pregledKorisnika = new JMenuItem("Pregled korisnika");
	
	// Servisi menu
	private JMenu servisiMenu = new JMenu("Servisi");
	private JMenuItem pregledRezervacija = new JMenuItem("Pregled rezervacija");
	private JMenuItem pregledServisa = new JMenuItem("Pregled servisa");
	private JMenuItem pregledServisnihDelova = new JMenuItem("Pregled servisnih delova");
	private JMenuItem pregledServisnihKnjizica = new JMenuItem("Pregled servisnih knjizica");
	
	// Automobili menu
	private JMenu automobiliMenu = new JMenu("Automobili");
	private JMenuItem pregledAutomobila = new JMenuItem("Pregled automobila");
	
	private AdminFunkcije adminFunkcije;
	
	
	public GlavniProzorAdminGUI(AdminFunkcije adminf) {
		this.adminFunkcije = adminf;
		setTitle("Korisnik: " + this.adminFunkcije.getAdmin().getIme() + " - "+ 
				this.adminFunkcije.getAdmin().getUloga().toString());
		setSize(500, 500);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		initMenu();
		initActions();
	}
	
	private void initMenu() {
		setJMenuBar(mainMenu);
		mainMenu.add(korisniciMenu);
		korisniciMenu.add(pregledKorisnika);
		
		mainMenu.add(servisiMenu);
		servisiMenu.add(pregledRezervacija);
		servisiMenu.add(pregledServisa);
		servisiMenu.add(pregledServisnihDelova);
		servisiMenu.add(pregledServisnihKnjizica);
		
		mainMenu.add(automobiliMenu);
		automobiliMenu.add(pregledAutomobila);
	}
	
	private void initActions() {
		pregledKorisnika.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				KorisniciGUI pk = new KorisniciGUI(adminFunkcije);
				pk.setVisible(true);
			}
		});
		
		pregledRezervacija.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Administrator.getListaRezervacija().size() > 0) {
					RezervacijeGUI pr = new RezervacijeGUI(adminFunkcije.getAdmin().getId());
					pr.setVisible(true);
				}
				else {
					String poruka = "Trenutno nema rezervacija.";
					JOptionPane.showMessageDialog(null, poruka, "", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		pregledServisa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ServisiGUI ps = new ServisiGUI(adminFunkcije.getAdmin().getId());
				ps.setVisible(true);
			}
		});
		
		
		pregledServisnihDelova.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ServisniDeloviGUI sd = new ServisniDeloviGUI(adminFunkcije);
				sd.setVisible(true);
			}
		});
		
		pregledAutomobila.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AutomobiliGUI kp = new AutomobiliGUI(adminFunkcije.getAdmin().getId());
				kp.setVisible(true);
			}
		});
		
		pregledServisnihKnjizica.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ServisneKnjiziceGUI sk = new ServisneKnjiziceGUI(adminFunkcije);
				sk.setVisible(true);
			}
		});
	}
}
