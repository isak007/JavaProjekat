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
import modeli.Administrator;
import modeli.Automobil;
import modeli.Musterija;
import modeli.Servis;
import modeli.Serviser;

public class KorisniciGUI extends JFrame{
	// samo admin ima pristup ovoj klasi
	
	private JToolBar mainToolbar = new JToolBar();
	private JButton btnAdd = new JButton();
	private JButton btnEdit = new JButton();
	private JButton btnDelete = new JButton();
	
	private DefaultTableModel tableModel;
	private JTable korisniciTabela;
	
	private AdminFunkcije adminFunkcije;
	
	public KorisniciGUI(AdminFunkcije adminFunkcije) {
		this.adminFunkcije = adminFunkcije;
		setTitle("Korisnici");
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
		mainToolbar.add(btnEdit);
		mainToolbar.add(btnDelete);
		add(mainToolbar, BorderLayout.NORTH);
	
		ArrayList<Musterija> listaMusterija = Administrator.getListaMusterija();
		ArrayList<Serviser> listaServisera = Administrator.getListaServisera();
		ArrayList<Administrator> listaAdministratora = Administrator.getListaAdministratora();
		
		
		int brojSvihKorisnika = listaMusterija.size() + listaServisera.size() +
				listaAdministratora.size();
		
		String[] zaglavlja = new String[] {"ID", "Ime", "Prezime", "Korisnicko ime", "Uloga"};
		Object[][] sadrzaj = new Object[brojSvihKorisnika][zaglavlja.length];
		
		
		// dodavanje svakog korisnika u tabeli od musteriji do admina
		int k = 0;
		
		for(int i = 0; i< listaMusterija.size(); i++) {
			Musterija musterija = listaMusterija.get(i);
			sadrzaj[k][0] = musterija.getId();
			sadrzaj[k][1] = musterija.getIme();
			sadrzaj[k][2] = musterija.getPrezime();
			sadrzaj[k][3] = musterija.getKorisinickoIme();
			sadrzaj[k][4] = musterija.getUloga().toString();
			k++;
		}
		
		
		for(int i = 0; i< listaServisera.size(); i++) {
			Serviser serviser = listaServisera.get(i);
			sadrzaj[k][0] = serviser.getId();
			sadrzaj[k][1] = serviser.getIme();
			sadrzaj[k][2] = serviser.getPrezime();
			sadrzaj[k][3] = serviser.getKorisinickoIme();
			sadrzaj[k][4] = serviser.getUloga().toString();
			k++;
		}
		
		
		for(int i = 0; i< listaAdministratora.size(); i++) {
			Administrator admin = listaAdministratora.get(i);
			sadrzaj[k][0] = admin.getId();
			sadrzaj[k][1] = admin.getIme();
			sadrzaj[k][2] = admin.getPrezime();
			sadrzaj[k][3] = admin.getKorisinickoIme();
			sadrzaj[k][4] = admin.getUloga().toString().substring(0,5);
			k++;
		}

		
		// pravljenje tabele od korisnika
		tableModel = new DefaultTableModel(sadrzaj, zaglavlja);
		korisniciTabela = new JTable(tableModel);
		
		korisniciTabela.setRowSelectionAllowed(true);
		korisniciTabela.setColumnSelectionAllowed(false);
		korisniciTabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		korisniciTabela.setDefaultEditor(Object.class, null);
		korisniciTabela.getTableHeader().setReorderingAllowed(false);
		
		JScrollPane scrollPane = new JScrollPane(korisniciTabela);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	
	
	
	// trenutno gotovo akcija za brisanje korisnika...
	private void initActions() {
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = korisniciTabela.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
				}
				else {
					String korisnikID = tableModel.getValueAt(red, 0).toString();

					if(Pronadji.pronadjiMusteriju(korisnikID, "", "") != null) {
						Musterija musterija = Pronadji.pronadjiMusteriju(korisnikID, "", "");				
						int izbor = JOptionPane.showConfirmDialog(null, 
								"Da li ste sigurni da zelite da obrisete korisnika?", 
								korisnikID + " - Porvrda brisanja", JOptionPane.YES_NO_OPTION);
						if(izbor == JOptionPane.YES_OPTION) {
							// uklanjanje automobila vezanog za musteriju
							ArrayList<Automobil> listaAutomobila = musterija.getListaAutomobila();
							for (int i = 0; i < listaAutomobila.size(); i++) {
								if (listaAutomobila.get(i).getObrisan().equals("ne")) {
									listaAutomobila.get(i).setObrisan("da");
									CitanjePisanje.izmenaPodataka(CitanjePisanje.automobiliZaUpis(), "automobil.txt");
									adminFunkcije.dodajUkloniAutomobil(listaAutomobila.get(i), "ukloni");
									// oduzimanje 1 od i jer se lista smanjila pa da se ne bi prekinula
									i -= 1;
								}
							}
							// uklanjanje servisa vezanog za musteriju
							ArrayList<Servis> listaServisa = musterija.getListaSvihServisa();
							for(int i = 0; i < listaServisa.size(); i++) {
								if(listaServisa.get(i).getObrisan().equals("ne")) {
									listaServisa.get(i).setObrisan("da");
									CitanjePisanje.izmenaPodataka(CitanjePisanje.servisZaUpis(), "servis.txt");
									adminFunkcije.dodajUkloniServis(listaServisa.get(i), "ukloni");
									// oduzimanje 1 od i jer se lista smanjila pa da se ne bi prekinula
									i -= 1;
								}
							}
							musterija.setObrisan("da");
							tableModel.removeRow(red);
							// azuriranje fajla
							CitanjePisanje.izmenaPodataka(CitanjePisanje.musterijaZaUpis(), "musterija.txt");
							// uklanjanje iz liste
							adminFunkcije.dodajUkloniMusteriju(musterija, "ukloni");
						}
					}
					
					else if(Pronadji.pronadjiServisera(korisnikID, "", "") != null) {
						Serviser serviser = Pronadji.pronadjiServisera(korisnikID, "", "");
						int izbor = JOptionPane.showConfirmDialog(null, 
								"Da li ste sigurni da zelite da obrisete korisnika?", 
								korisnikID + " - Porvrda brisanja", JOptionPane.YES_NO_OPTION);	
						if(izbor == JOptionPane.YES_OPTION) {
							// uklanjanje servisa vezanog za servisera
							ArrayList<Servis> listaServisa = serviser.getListaServisa();
							for(int i = 0; i < listaServisa.size(); i++) {
								if(listaServisa.get(i).getObrisan().equals("ne")) {
									listaServisa.get(i).setObrisan("da");
									CitanjePisanje.izmenaPodataka(CitanjePisanje.servisZaUpis(), "servis.txt");
									adminFunkcije.dodajUkloniServis(listaServisa.get(i), "ukloni");
									// oduzimanje 1 od i jer se lista smanjila pa da se ne bi prekinula
									i -= 1;
								}
							}
							serviser.setObrisan("da");
							tableModel.removeRow(red);
							// azuriranje fajla
							CitanjePisanje.izmenaPodataka(CitanjePisanje.serviserZaUpis(), "serviser.txt");
							// uklanjanje iz liste
							adminFunkcije.dodajUkloniServisera(serviser, "ukloni");
						}
					}
					
					else if(Pronadji.pronadjiAdmina(korisnikID, "", "") != null) {
						Administrator admin = Pronadji.pronadjiAdmina(korisnikID, "", "");
						int izbor = JOptionPane.showConfirmDialog(null, 
								"Da li ste sigurni da zelite da obrisete korisnika?", 
								korisnikID + " - Porvrda brisanja", JOptionPane.YES_NO_OPTION);
						if(izbor == JOptionPane.YES_OPTION) {
							admin.setObrisan("da");
							tableModel.removeRow(red);
							// azuriranje fajla
							CitanjePisanje.izmenaPodataka(CitanjePisanje.adminZaUpis(), "administrator.txt");
							// uklanjanje iz liste
							adminFunkcije.dodajUkloniAdmina(admin, "ukloni");
						}
					}
				}
			}
		});
		
		
		
		
		
		// dodavanje korisnika, otvara drugi GUI prozor namijenjen za dodavanje/izmenu
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IzmenaKorisnikaGUI ik = new IzmenaKorisnikaGUI(adminFunkcije,"",Uloga.MUSTERIJA);
				ik.setVisible(true);
				KorisniciGUI.this.dispose();
				KorisniciGUI.this.setVisible(false);
			}
		});
		
		
		
		// izmena korisnika, isti prozor samo za izmenu
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = korisniciTabela.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
				}
				else {
					String korisnikID = tableModel.getValueAt(red, 0).toString();
					IzmenaKorisnikaGUI ik = new IzmenaKorisnikaGUI(adminFunkcije, korisnikID, null);
					ik.setVisible(true);
					KorisniciGUI.this.dispose();
					KorisniciGUI.this.setVisible(false);
				}
			}
		});
	}
}
