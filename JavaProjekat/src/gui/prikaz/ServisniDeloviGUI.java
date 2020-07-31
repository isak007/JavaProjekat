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
import enumeracije.Uloga;
import funkcije.AdminFunkcije;
import gui.izmena.IzmenaKorisnikaGUI;
import gui.izmena.IzmenaServisnogDelaGUI;
import modeli.Administrator;
import modeli.Musterija;
import modeli.Serviser;
import modeli.ServisniDeo;

public class ServisniDeloviGUI extends JFrame{
	private JToolBar mainToolbar = new JToolBar();
	private JButton btnAdd = new JButton();
	private JButton btnEdit = new JButton();
	private JButton btnDelete = new JButton();
	
	private DefaultTableModel tableModel;
	private JTable sdTabela;
	
	private AdminFunkcije adminFunkcije;
	
	public ServisniDeloviGUI(AdminFunkcije adminFunkcije) {
		this.adminFunkcije = adminFunkcije;
		setTitle("Servisni delovi");
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
		if(adminFunkcije != null) {
			mainToolbar.add(btnAdd);
			mainToolbar.add(btnEdit);
			mainToolbar.add(btnDelete);
		}
		add(mainToolbar, BorderLayout.NORTH);
	
		ArrayList<ServisniDeo> listaDelova = Administrator.getListaSvihDelova();
		
		String[] zaglavlja = new String[] {"ID", "Marka", "Model", "Naziv", "Cena"};
		Object[][] sadrzaj = new Object[listaDelova.size()][zaglavlja.length];
		
		
		// dodavanje svakog korisnika u tabeli od musteriji do admina
		for(int i = 0; i< listaDelova.size(); i++) {
			ServisniDeo sd = listaDelova.get(i);
			sadrzaj[i][0] = sd.getId();
			sadrzaj[i][1] = sd.getMarka();
			sadrzaj[i][2] = sd.getModel();
			sadrzaj[i][3] = sd.getNaziv();
			sadrzaj[i][4] = sd.getCena();
		}
		
		
		// pravljenje tabele od korisnika
		tableModel = new DefaultTableModel(sadrzaj, zaglavlja);
		sdTabela = new JTable(tableModel);
		
		sdTabela.setRowSelectionAllowed(true);
		sdTabela.setColumnSelectionAllowed(false);
		sdTabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sdTabela.setDefaultEditor(Object.class, null);
		sdTabela.getTableHeader().setReorderingAllowed(false);
		
		JScrollPane scrollPane = new JScrollPane(sdTabela);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	// trenutno gotovo akcija za brisanje korisnika...
	private void initActions() {
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = sdTabela.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
				}
				else {
					String sdID = tableModel.getValueAt(red, 0).toString();
					ServisniDeo sd = Pronadji.pronadjiServnisniDeo(sdID);
					int izbor = JOptionPane.showConfirmDialog(null, 
							"Da li ste sigurni da zelite da obrisete servisni deo?", 
							sdID + " - Porvrda brisanja", JOptionPane.YES_NO_OPTION);
					if(izbor == JOptionPane.YES_OPTION) {
						sd.setObrisan("da");
						tableModel.removeRow(red);
						// azuriranje fajla
						CitanjePisanje.izmenaPodataka(CitanjePisanje.sdZaUpis(), "servisniDelovi.txt");
						// uklanjanje iz liste
						adminFunkcije.dodajUkloniSD(sd, "ukloni");
					}
				}
			}
		});
		
		
		// dodavanje korisnika, otvara drugi GUI prozor namijenjen za dodavanje/izmenu
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IzmenaServisnogDelaGUI isd = new IzmenaServisnogDelaGUI(adminFunkcije,"",null);
				isd.setVisible(true);
				ServisniDeloviGUI.this.dispose();
				ServisniDeloviGUI.this.setVisible(false);
			}
		});
		
		
		
		// izmena korisnika, isti prozor samo za izmenu
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = sdTabela.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
				}
				else {
					String sdID = tableModel.getValueAt(red, 0).toString();
					IzmenaServisnogDelaGUI isd = new IzmenaServisnogDelaGUI(adminFunkcije,sdID,null);
					isd.setVisible(true);
					ServisniDeloviGUI.this.dispose();
					ServisniDeloviGUI.this.setVisible(false);
				}
			}
		});
	
	}	
}
