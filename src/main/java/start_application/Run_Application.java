package start_application;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

import frontend_application_ui.LinkChecker_Frontend_ApplicationUI;

public class Run_Application {

	
	public static void main(String[] args) {
		
		
		SwingUtilities.invokeLater(() -> new LinkChecker_Frontend_ApplicationUI().setVisible(true));
	}
}
