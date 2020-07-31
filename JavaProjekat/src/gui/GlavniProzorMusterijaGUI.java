package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import funkcije.MusterijaFunkcije;
import gui.izmena.KreiranjeRezervacijeGUI;
import gui.prikaz.AutomobiliGUI;
import gui.prikaz.ServisiGUI;

public class GlavniProzorMusterijaGUI extends JFrame{
	private JMenuBar mainMenu = new JMenuBar();
	private JMenu servisiMenu = new JMenu("Servisi");
	private JMenuItem kreiranjeRezItem = new JMenuItem("Kreiranje rezervacije");
	private JMenuItem pregledServisaItem = new JMenuItem("Pregled servisa");
	private JMenu automobiliMenu = new JMenu("Automobili");
	private JMenuItem pregledAutoItem = new JMenuItem("Pregled automobila");
	
	private MusterijaFunkcije musterijaFunkcije;
	
	public GlavniProzorMusterijaGUI(MusterijaFunkcije musterijaf) {
		this.musterijaFunkcije = musterijaf;
		setTitle("Korisnik: " + this.musterijaFunkcije.getMusterija().getKorisinickoIme() + " - " + 
				this.musterijaFunkcije.getMusterija().getUloga().toString());
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
		servisiMenu.add(kreiranjeRezItem);
		servisiMenu.add(pregledServisaItem);
		mainMenu.add(automobiliMenu);
		automobiliMenu.add(pregledAutoItem);
	}
	
	private void initActions() {
		kreiranjeRezItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (musterijaFunkcije.getMusterija().getListaAutomobila().size() > 0) {
					KreiranjeRezervacijeGUI kr = new KreiranjeRezervacijeGUI(musterijaFunkcije.getMusterija().getId());
					kr.setVisible(true);
				}
				else {
					String poruka = "Morate prvo uneti automobil u sistem.";
					JOptionPane.showMessageDialog(null, poruka, "Greska", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		pregledServisaItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ServisiGUI ps = new ServisiGUI(musterijaFunkcije.getMusterija().getId());
				ps.setVisible(true);
			}
		});
		
		pregledAutoItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AutomobiliGUI pa = new AutomobiliGUI(musterijaFunkcije.getMusterija().getId());
				pa.setVisible(true);
			}
		});
	}
	
}
