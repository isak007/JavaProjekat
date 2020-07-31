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
import gui.izmena.IzmenaAutomobilaGUI;
import gui.izmena.IzmenaServisaGUI;
import modeli.*;

public class AutomobiliGUI extends JFrame{
	// samo admin ima pristup ovoj klasi
	
	private JToolBar mainToolbar = new JToolBar();
	private JButton btnAdd = new JButton();
	private JButton btnEdit = new JButton();
	private JButton btnDelete = new JButton();
	
	private DefaultTableModel tableModel;
	private JTable automobiliTabela;
	
	private String korisnikID;
	private Administrator admin;
	private Musterija musterija;
	
	public AutomobiliGUI(String korisnikID) {
		this.korisnikID = korisnikID;
		this.admin = Pronadji.pronadjiAdmina(korisnikID, "", "");
		this.musterija = Pronadji.pronadjiMusteriju(korisnikID, "", "");
		setTitle("Automobili");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		initGUI();
		initActions();
	}
	
	private void initGUI() {
		ImageIcon addIcon = new ImageIcon(getClass().getResource("/slike/add.gif"));
		btnAdd.setIcon(addIcon);
		ImageIcon editIcon = new ImageIcon(getClass().getResource("/slike/edit.gif"));
		btnEdit.setIcon(editIcon);
		ImageIcon deleteIcon = new ImageIcon(getClass().getResource("/slike/remove.gif"));
		btnDelete.setIcon(deleteIcon);
		
		mainToolbar.add(btnAdd);
		if (admin != null) {
			// samo admin moze da menja i brise automobile
			mainToolbar.add(btnEdit);
			mainToolbar.add(btnDelete);
		}
		add(mainToolbar, BorderLayout.NORTH);
		
		ArrayList<Automobil> listaAutomobila = new ArrayList<Automobil>();
		if (admin != null) {
			listaAutomobila = Administrator.getListaAutomobila();
		}
		else if (musterija != null) {
			listaAutomobila = musterija.getListaAutomobila();
		}
		else {
			System.out.println("Greska u kodu.");
		}
		
		String[] zaglavlja = new String[] {"ID", "Vlasnik", "Marka", "Model", "God. proizvodnje"};
		Object[][] sadrzaj = new Object[listaAutomobila.size()][zaglavlja.length];
		
		for(int i=0; i<listaAutomobila.size() ; i++) {
			Automobil auto = listaAutomobila.get(i);
			sadrzaj[i][0] = auto.getId();
			sadrzaj[i][1] = auto.getVlasnik().getKorisinickoIme();
			sadrzaj[i][2] = auto.getMarka();
			sadrzaj[i][3] = auto.getModel();
			sadrzaj[i][4] = auto.getGodinaProizvodnje();
		}
		
		tableModel = new DefaultTableModel(sadrzaj, zaglavlja);
		automobiliTabela = new JTable(tableModel);
		
		automobiliTabela.setRowSelectionAllowed(true);
		automobiliTabela.setColumnSelectionAllowed(false);
		automobiliTabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		automobiliTabela.setDefaultEditor(Object.class, null);
		automobiliTabela.getTableHeader().setReorderingAllowed(false);
		
		JScrollPane scrollPane = new JScrollPane(automobiliTabela);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	private void initActions() {
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = automobiliTabela.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
				}
				else {
					String automobilID = tableModel.getValueAt(red, 0).toString();
					Automobil auto = Pronadji.pronadjiAutomobil(automobilID);
					int izbor = JOptionPane.showConfirmDialog(null, 
							"Da li ste sigurni da zelite da obrisete disk?", 
							automobilID + " - Porvrda brisanja", JOptionPane.YES_NO_OPTION);
					if(izbor == JOptionPane.YES_OPTION) {
						// uklanjanje servisne knjizice vezane za automobil
						ArrayList<ServisnaKnjizica> listaSK = Administrator.getListaServisnihKnjizica();
						for(int i = 0; i < listaSK.size(); i++) {
							if(listaSK.get(i).getAutomobil().getId().equals(automobilID)){
								listaSK.get(i).setObrisan("da");
								CitanjePisanje.izmenaPodataka(CitanjePisanje.skZaUpis(), "servisnaKnjizica.txt");
								AdminFunkcije af = new AdminFunkcije(null);
								af.dodajUkloniSK(listaSK.get(i), "ukloni");
								break;
							}
						}
						auto.setObrisan("da");
						tableModel.removeRow(red);
						AdminFunkcije af = new AdminFunkcije(null);
						CitanjePisanje.izmenaPodataka(CitanjePisanje.automobiliZaUpis(), "automobil.txt");
						// uklanjanje iz korisnikove liste kao i adminove
						af.dodajUkloniAutomobil(auto, "ukloni");
					}
				}
			}
		});
		
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Administrator.getListaMusterija().size()>0) {
					IzmenaAutomobilaGUI ia = new IzmenaAutomobilaGUI(korisnikID, "", null);
					ia.setVisible(true);
					AutomobiliGUI.this.dispose();
					AutomobiliGUI.this.setVisible(false);
				}
				else {
					JOptionPane.showMessageDialog(null, "Ne postoje musterije u sistemu.", "Greska", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = automobiliTabela.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
				}else {
					String autoID = tableModel.getValueAt(red, 0).toString();
					IzmenaAutomobilaGUI ia = new IzmenaAutomobilaGUI(korisnikID, autoID, null);
					ia.setVisible(true);
					AutomobiliGUI.this.dispose();
					AutomobiliGUI.this.setVisible(false);
				}
			}
		});
	}
}
