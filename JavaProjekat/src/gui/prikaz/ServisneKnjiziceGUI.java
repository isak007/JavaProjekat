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
import gui.izmena.IzmenaServisnogDelaGUI;
import modeli.Administrator;
import modeli.ServisnaKnjizica;
import modeli.ServisniDeo;

public class ServisneKnjiziceGUI extends JFrame{
	private JToolBar mainToolbar = new JToolBar();
	private JButton btnDelete = new JButton();
	
	private DefaultTableModel tableModel;
	private JTable skTabela;
	
	private AdminFunkcije adminFunkcije;
	
	public ServisneKnjiziceGUI(AdminFunkcije af) {
		this.adminFunkcije = af;
		setTitle("Servisne knjizice");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		initGUI();
		initActions();
	}
	
	private void initGUI() {
		ImageIcon deleteIcon = new ImageIcon(getClass().getResource("/slike/remove.gif"));
		btnDelete.setIcon(deleteIcon);
		mainToolbar.add(btnDelete);
		add(mainToolbar, BorderLayout.NORTH);
	
		ArrayList<ServisnaKnjizica> listaKnjizica = Administrator.getListaServisnihKnjizica();
		
		String[] zaglavlja = new String[] {"ID", "Automobil", "Servisi (ID)"};
		Object[][] sadrzaj = new Object[listaKnjizica.size()][zaglavlja.length];
		
		
		// dodavanje svakog korisnika u tabeli od musteriji do admina
		for(int i = 0; i< listaKnjizica.size(); i++) {
			ServisnaKnjizica sk = listaKnjizica.get(i);
			sadrzaj[i][0] = sk.getId();
			sadrzaj[i][1] = sk.getAutomobil().getId()+"-"+sk.getAutomobil().getVlasnik().getKorisinickoIme()+
					" "+sk.getAutomobil().getMarka().toString()+" "+sk.getAutomobil().getModel().toString();
			sadrzaj[i][2] = sk.getSifreServisa();
		}
			
		// pravljenje tabele od korisnika
		tableModel = new DefaultTableModel(sadrzaj, zaglavlja);
		skTabela = new JTable(tableModel);
		
		skTabela.setRowSelectionAllowed(true);
		skTabela.setColumnSelectionAllowed(false);
		skTabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		skTabela.setDefaultEditor(Object.class, null);
		skTabela.getTableHeader().setReorderingAllowed(false);
		
		JScrollPane scrollPane = new JScrollPane(skTabela);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	private void initActions() {
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = skTabela.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
				}
				else {
					String skID = tableModel.getValueAt(red, 0).toString();
					ServisnaKnjizica sk = Pronadji.pronadjiServisnuKnjizicu(skID);
					int izbor = JOptionPane.showConfirmDialog(null, 
							"Da li ste sigurni da zelite da obrisete servisnu knjizicu?", 
							skID + " - Porvrda brisanja", JOptionPane.YES_NO_OPTION);
					if(izbor == JOptionPane.YES_OPTION) {
						sk.setObrisan("da");
						tableModel.removeRow(red);
						// azuriranje fajla
						CitanjePisanje.izmenaPodataka(CitanjePisanje.skZaUpis(), "servisnaKnjizica.txt");
						// uklanjanje iz liste
						adminFunkcije.dodajUkloniSK(sk, "ukloni");
					}
				}
			}
		});
		
	}
}
