package citanjePisanje;
import java.io.*;
import java.util.ArrayList;

public class CitanjePisanje {
	public CitanjePisanje() {};
	
	public void prazanFajl(String fajl) {
		File file = new File(fajl);
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write("");
			writer.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void pisanjeUfajl(String sadrzaj, String fajl) {
		File file = new File(fajl);
		ArrayList<String> prosliSadrzaj = this.citanjeFajla(fajl);
		String noviSadrzaj = "";
		for (int i = 0; i < prosliSadrzaj.size(); i++) {
			noviSadrzaj += prosliSadrzaj.get(i) + "\r\n";
		}
		noviSadrzaj += sadrzaj + "\r\n";
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(noviSadrzaj);
			writer.close();
		}
		catch (IOException e) {	
			e.printStackTrace();
		}
	}
	
	public ArrayList<String> citanjeFajla(String fajl) {
		File file = new File(fajl);
		ArrayList<String> lista = new ArrayList<String>();
		BufferedReader reader;
		String linija;
		try {
			reader = new BufferedReader(new FileReader(file));
			while(true) {
				linija = reader.readLine();
				if(linija.length() > 0) {
					lista.add(linija);
				}
				else {
					break;
				}
			}
			reader.close();
			return(lista);
		}
		catch (IOException e) {	
			//e.printStackTrace();
			this.prazanFajl(fajl);
			return (lista);
		}
		catch (NullPointerException e) {
			return(lista);
		}
	}
	
}
