package aplikacija;

import gui.LoginGUI;

public class ServisAutomobilaMain {
	public static void main(String[] args) {
		Inicijalizacija.init();
		LoginGUI lp = new LoginGUI();
		lp.setVisible(true);
	}
}
