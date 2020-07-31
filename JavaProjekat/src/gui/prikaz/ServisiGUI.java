package gui.prikaz;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import aplikacija.Pronadji;
import citanjePisanje.CitanjePisanje;
import enumeracije.StatusServisa;
import funkcije.AdminFunkcije;
import gui.izmena.IzmenaServisaGUI;
import modeli.Administrator;
import modeli.Musterija;
import modeli.Servis;
import modeli.Serviser;
import modeli.ServisniDeo;

public class ServisiGUI extends JFrame{
	// treba da dijele ovaj prikaz admin i serviser
	// id kao identifikator da bi znao GUI da li prikazuje za Admin ili za Servisera opcije
	private JToolBar mainToolbar = new JToolBar();
	private JButton btnAdd = new JButton();
	private JButton btnEdit = new JButton();
	private JButton btnDelete = new JButton();
	private JButton btnZavrsiServis = new JButton("Zavrsi servis");
	private JLabel lblBrojBodova;
	private JLabel lblDug;
	private DefaultTableModel tableModel;
	private JTable servisiTabela;
	
	
	private String korisnikID;
	private Administrator admin;
	private Serviser serviser;
	private Musterija musterija;
	
	public ServisiGUI(String korisnikID) {
		AdminFunkcije af = new AdminFunkcije(null);
		af.inicijalizacijaPromjena();
		this.korisnikID = korisnikID;
		this.admin = Pronadji.pronadjiAdmina(korisnikID, "", "");
		this.serviser = Pronadji.pronadjiServisera(korisnikID, "", "");
		this.musterija = Pronadji.pronadjiMusteriju(korisnikID, "", "");
		setTitle("Servisi");
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
		
		ArrayList<Servis> listaServisa = new ArrayList<Servis>();
		
		if (musterija != null) {
			listaServisa = musterija.getListaSvihServisa();
			mainToolbar.add(btnDelete);
			if(musterija.getListaZavrsenihServisa().size()>0) {
				String poruka = "Imate neotplacenih servisa. Da li zelite da iskoristite bodove za placanje?";
				int izbor = JOptionPane.showConfirmDialog(null, poruka, "Potvrda", JOptionPane.YES_NO_OPTION);
				double dug = musterija.getDug();
				double cenaServisa = 0;
				for (Servis servis : musterija.getListaZavrsenihServisa()) {
					servis.setOtplacen("da");
					cenaServisa += servis.getCena();
					for(ServisniDeo sd : servis.getListaDelova()) {
						cenaServisa += sd.getCena();
					}
				}
				if(izbor == JOptionPane.YES_OPTION) {
					if (musterija.getBrojSakupljenihBodova() > 0) {
						double pocetniDug = cenaServisa;
						for (int i = 0; i < musterija.getBrojSakupljenihBodova(); i++) {
							cenaServisa -= pocetniDug*(2.0/100.0);
						}
						musterija.setBrojSakupljenihBodova(0);
					}
					else {
						JOptionPane.showMessageDialog(null, "Trenutno nemate sakupljenih bodova.", "Greska", 
								JOptionPane.WARNING_MESSAGE);
						musterija.setBrojSakupljenihBodova(musterija.getBrojSakupljenihBodova()+1);
						if(musterija.getBrojSakupljenihBodova() > 10) {
							musterija.setBrojSakupljenihBodova(10);
						}
					}
				}
				else {
					musterija.setBrojSakupljenihBodova(musterija.getBrojSakupljenihBodova()+1);
					if(musterija.getBrojSakupljenihBodova() > 10) {
						musterija.setBrojSakupljenihBodova(10);
					}
				}
				JOptionPane.showMessageDialog(null, "Servisi otplaceni.\nCena servisa: "+cenaServisa, "Potvrda", 
						JOptionPane.INFORMATION_MESSAGE);
				dug += cenaServisa;
				musterija.setDug(dug);
				musterija.setListaZavrsenihServisa(new ArrayList<Servis>());
				CitanjePisanje.izmenaPodataka(CitanjePisanje.musterijaZaUpis(), "musterija.txt");
				CitanjePisanje.izmenaPodataka(CitanjePisanje.servisZaUpis(), "servis.txt");
			}
			lblBrojBodova = new JLabel(" Broj sakupljenih bodova: "+String.valueOf(musterija.getBrojSakupljenihBodova()));
			mainToolbar.add(lblBrojBodova);
			lblDug = new JLabel("          Dug: "+musterija.getDug());
			mainToolbar.add(lblDug);
		}
		
		else {
			mainToolbar.add(btnAdd);
			mainToolbar.add(btnEdit);
			if (admin != null) {
				mainToolbar.add(btnDelete);
				listaServisa = Administrator.getListaSvihServisa();
			}
			else if (serviser != null) {
				listaServisa = serviser.getListaServisa();
				for (Servis servis : listaServisa) {
					if (servis.getStatusServisa().equals(StatusServisa.U_TOKU)){
						mainToolbar.add(btnZavrsiServis);
						break;
					}
				}
			}
		}
		
		add(mainToolbar, BorderLayout.NORTH);
		String[] zaglavlja = new String[] {"ID", "Automobil ID", "Serviser", "Termin", "Opis","Status"};
		Object[][] sadrzaj = new Object[listaServisa.size()][zaglavlja.length];
		
		
		
		for(int i = 0; i< listaServisa.size(); i++) {
			Servis servis = listaServisa.get(i);
			sadrzaj[i][0] = servis.getId();
			sadrzaj[i][1] = servis.getAutomobil().getId();
			sadrzaj[i][2] = servis.getServiser().getKorisinickoIme();
			sadrzaj[i][3] = servis.getTermin();
			sadrzaj[i][4] = servis.getOpis();
			sadrzaj[i][5] = servis.getStatusServisa().toString();
		}
		
		
		// pravljenje tabele od korisnika
		tableModel = new DefaultTableModel(sadrzaj, zaglavlja);
		servisiTabela = new JTable(tableModel);
		
		servisiTabela.setRowSelectionAllowed(true);
		servisiTabela.setColumnSelectionAllowed(false);
		servisiTabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		servisiTabela.setDefaultEditor(Object.class, null);
		servisiTabela.getTableHeader().setReorderingAllowed(false);
		
		JScrollPane scrollPane = new JScrollPane(servisiTabela);
		add(scrollPane, BorderLayout.CENTER);
	}
	
	
	
	
	// trenutno gotovo akcija za brisanje korisnika...
	private void initActions() {
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = servisiTabela.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
				}
				else {
					String servisID = tableModel.getValueAt(red, 0).toString();
					Servis servis = Pronadji.pronadjiServis(servisID);

					if (musterija != null) {
						if (!servis.getStatusServisa().equals(StatusServisa.ZAKAZAN)) {
							JOptionPane.showMessageDialog(null, "Nije moguce otkazati ovaj servis.", "Greska", 
									JOptionPane.WARNING_MESSAGE);
						}
						else {
							int izbor = JOptionPane.showConfirmDialog(null, 
									"Da li ste sigurni da zelite da otkazete servis?", 
									servisID + " - Porvrda otkazivanja", JOptionPane.YES_NO_OPTION);
							if(izbor == JOptionPane.YES_OPTION) {
								servis.setStatusServisa(StatusServisa.OTKAZAN);
								CitanjePisanje.izmenaPodataka(CitanjePisanje.servisZaUpis(), "servis.txt");
								ServisiGUI.this.dispose();
								ServisiGUI.this.setVisible(false);
							}
						}
					}
					else {
						int izbor = JOptionPane.showConfirmDialog(null, 
								"Da li ste sigurni da zelite da obrisete servis?", 
								servisID + " - Porvrda brisanja", JOptionPane.YES_NO_OPTION);
						if(izbor == JOptionPane.YES_OPTION) {
							AdminFunkcije af = new AdminFunkcije(null);
							servis.setObrisan("da");
							tableModel.removeRow(red);
							// azuriranje fajla
							CitanjePisanje.izmenaPodataka(CitanjePisanje.servisZaUpis(), "servis.txt");
							// uklanjanje iz liste
							af.dodajUkloniServis(servis, "ukloni");
						}
					}
				}
			}
		});
		
		
		// zavrsavanje servisa
		btnZavrsiServis.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = servisiTabela.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
				}
				else {
					String servisID = tableModel.getValueAt(red, 0).toString();
					Servis servis = Pronadji.pronadjiServis(servisID);
					
					if(servis.getStatusServisa().equals(StatusServisa.U_TOKU)) {
						int izbor = JOptionPane.showConfirmDialog(null, 
								"Da li ste sigurni da zelite da zavrsite servis?", 
								"Potvrda", JOptionPane.YES_NO_OPTION);
						if(izbor == JOptionPane.YES_OPTION) {
							if (servis.getCena() == 0) {
								String input = JOptionPane.showInputDialog("Unesite cenu servisa");
								if (input != null) {
									try{
										double cena = Double.parseDouble(input);
										servis.setCena(cena);
										servis.setStatusServisa(StatusServisa.ZAVRSEN);
										ArrayList<Servis> listaNeotplacenihS = servis.getAutomobil().getVlasnik().getListaZavrsenihServisa();
										listaNeotplacenihS.add(servis);
										servis.getAutomobil().getVlasnik().setListaZavrsenihServisa(listaNeotplacenihS);
										CitanjePisanje.izmenaPodataka(CitanjePisanje.servisZaUpis(), "servis.txt");
										ServisiGUI.this.dispose();
										ServisiGUI.this.setVisible(false);
									}
									catch(NumberFormatException ex) {
										JOptionPane.showMessageDialog(null, "Niste uneli validnu cenu.",
												"Greska", JOptionPane.WARNING_MESSAGE);
									}
								}
							}
							else {
								servis.setStatusServisa(StatusServisa.ZAVRSEN);
								ArrayList<Servis> listaNeotplacenihS = servis.getAutomobil().getVlasnik().getListaZavrsenihServisa();
								listaNeotplacenihS.add(servis);
								servis.getAutomobil().getVlasnik().setListaZavrsenihServisa(listaNeotplacenihS);
								CitanjePisanje.izmenaPodataka(CitanjePisanje.servisZaUpis(), "servis.txt");
								ServisiGUI.this.dispose();
								ServisiGUI.this.setVisible(false);
							}
						}
					}
					else {
						String poruka = "Nije moguce zavrsiti servis koji nije u toku.";
						JOptionPane.showMessageDialog(null, poruka, "Greska", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		
		
		// dodavanje korisnika, otvara drugi GUI prozor namijenjen za dodavanje/izmenu
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RezervacijeGUI rez = new RezervacijeGUI(korisnikID);
				rez.setVisible(true);
				ServisiGUI.this.dispose();
				ServisiGUI.this.setVisible(false);
			}
		});
		
		
		
		// izmena korisnika, isti prozor samo za izmenu
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = servisiTabela.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
				}
				else {
					String servisID = tableModel.getValueAt(red, 0).toString();
					Servis servis = Pronadji.pronadjiServis(servisID);
					if(servis.getStatusServisa().equals(StatusServisa.ZAKAZAN)) {
						IzmenaServisaGUI is = new IzmenaServisaGUI(korisnikID, servisID,"","");
						is.setVisible(true);
						ServisiGUI.this.dispose();
						ServisiGUI.this.setVisible(false);
					}
					else {
						String poruka = "Nije moguca izmena ovog servisa.";
						JOptionPane.showMessageDialog(null, poruka, "Greska", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
	}
}
