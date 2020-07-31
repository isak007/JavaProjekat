package gui.prikaz;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import aplikacija.Pronadji;
import citanjePisanje.CitanjePisanje;
import funkcije.AdminFunkcije;
import gui.izmena.IzmenaServisaGUI;
import modeli.Administrator;
import modeli.Serviser;



public class RezervacijeGUI extends JFrame{
	// treba da dijele ovaj prikaz admin i serviser
	private JToolBar mainToolbar = new JToolBar();
	private JButton btnConfirm = new JButton();
	private JButton btnDelete = new JButton();
	
	private DefaultTableModel tableModel;
	private JTable rezervacijeTabela;
	
	private String korisnikID;

	public RezervacijeGUI(String korisnikID) {
		this.korisnikID = korisnikID;
		setTitle("Rezervacije - kreiranje servisa");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		initGUI();
		initActions();
	}
	
	private void initGUI() {
		ImageIcon okIcon = new ImageIcon(getClass().getResource("/slike/ok.png"));
		btnConfirm.setIcon(okIcon);
		ImageIcon deleteIcon = new ImageIcon(getClass().getResource("/slike/remove.gif"));
		btnDelete.setIcon(deleteIcon);
		
		mainToolbar.add(btnConfirm);
		if(Pronadji.pronadjiAdmina(korisnikID, "", "")!= null) {
			mainToolbar.add(btnDelete);
		}
		add(mainToolbar, BorderLayout.NORTH);
		
		ArrayList<String> listaRezervacija = Administrator.getListaRezervacija();
		String[] zaglavlja = new String[] {"Automobil ID","Opis"};
		Object[][] sadrzaj = new Object[listaRezervacija.size()][zaglavlja.length];
		
		// dodavanje svakog korisnika u tabeli od musteriji do admina
		for(int i = 0; i < listaRezervacija.size(); i++) {
			sadrzaj[i][0] = listaRezervacija.get(i).split(",")[0];
			sadrzaj[i][1] = listaRezervacija.get(i).split(",")[1];
		}
		
		// pravljenje tabele od rezervacija
		tableModel = new DefaultTableModel(sadrzaj, zaglavlja);
		rezervacijeTabela = new JTable(tableModel);
		
		rezervacijeTabela.setRowSelectionAllowed(true);
		rezervacijeTabela.setColumnSelectionAllowed(false);
		rezervacijeTabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rezervacijeTabela.setDefaultEditor(Object.class, null);
		rezervacijeTabela.getTableHeader().setReorderingAllowed(false);
		
		JScrollPane scrollPane = new JScrollPane(rezervacijeTabela);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	
	
	
	// trenutno gotova akcija za brisanje rezervacije
	private void initActions() {
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = rezervacijeTabela.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
				}
				else {					
					int izbor = JOptionPane.showConfirmDialog(null, 
							"Da li ste sigurni da zelite da obrisete rezervaciju?", 
							"Porvrda brisanja", JOptionPane.YES_NO_OPTION);
					if(izbor == JOptionPane.YES_OPTION) {
						String rezervacija = tableModel.getValueAt(red, 0).toString()+","+tableModel.getValueAt(red, 1).toString();
						// uklanjanje iz liste
						AdminFunkcije af = new AdminFunkcije(null);
						af.dodajUkloniRez(rezervacija, "ukloni");
						tableModel.removeRow(red);
						// upisivanje promena u fajl
						// pisanje u fajl dodaje sadrzaj pa sam zbog toga napravio prazan fajl
						// ne zelim da dodam nego da unesem skroz novi sadrzaj sa izbrisanom rezervacijom
						CitanjePisanje.prazanFajl("rezervacije.txt");
						CitanjePisanje.pisanjeUfajl(CitanjePisanje.rezZaUpis(), "rezervacije.txt");
					}
				}
			}
		});
		
		
		// dodavanje servisa, otvara drugi GUI prozor namijenjen za dodavanje/izmenu
		btnConfirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = rezervacijeTabela.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
				}
				else {
					String automobilID = tableModel.getValueAt(red, 0).toString();
					String opis = tableModel.getValueAt(red, 1).toString();
					if (Administrator.getListaServisera().size() > 0) {
						IzmenaServisaGUI is = new IzmenaServisaGUI(korisnikID,"",automobilID,opis);
						is.setVisible(true);
						RezervacijeGUI.this.dispose();
						RezervacijeGUI.this.setVisible(false);
					}
					else {
						JOptionPane.showMessageDialog(null, "Trenutno ne postoje serviseri u sistemu.", "Greska",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		
	}
}
