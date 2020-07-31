package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import funkcije.AdminFunkcije;
import funkcije.ServiserFunkcije;
import gui.prikaz.RezervacijeGUI;
import gui.prikaz.ServisiGUI;

public class GlavniProzorServiserGUI extends JFrame{
	private JMenuBar mainMenu = new JMenuBar();
	private JMenu servisiMenu = new JMenu("Servisi");
	private JMenuItem pregledRezItem = new JMenuItem("Pregled rezervacija");
	private JMenuItem pregledServisaItem = new JMenuItem("Pregled servisa");
	
	private ServiserFunkcije serviserFunkcije;
	
	public GlavniProzorServiserGUI(ServiserFunkcije serviserf) {
		this.serviserFunkcije = serviserf;
		setTitle("Korisnik: " + this.serviserFunkcije.getServiser().getIme() + " - " + 
		this.serviserFunkcije.getServiser().getUloga().toString());
		setSize(500, 500);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		initMenu();
		initActions();
	}
	
	private void initMenu() {
		setJMenuBar(mainMenu);
		mainMenu.add(servisiMenu);
		servisiMenu.add(pregledRezItem);
		servisiMenu.add(pregledServisaItem);
	}
	
	private void initActions() {
		pregledRezItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RezervacijeGUI rez = new RezervacijeGUI(serviserFunkcije.getServiser().getId());
				rez.setVisible(true);
			}
		});
		
		pregledServisaItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ServisiGUI servisi = new ServisiGUI(serviserFunkcije.getServiser().getId());
				servisi.setVisible(true);
			}
		});
		
	}
}
