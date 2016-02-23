package civilisation;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InterfaceClient extends JFrame implements MouseListener {
	
	private JPanel nomAcceuil = new JPanel();
	private JLabel nom = new JLabel();
	
	private JPanel adresseEtPort = new JPanel();
	private JLabel explications = new JLabel();
	private JPanel adresse = new JPanel();
	private JPanel port = new JPanel();	
	private JLabel adresseTitre = new JLabel();
	private JLabel portTitre = new JLabel();
	private JTextField adresseEntre = new JTextField();
	private JTextField portEntre = new JTextField();
	
	private JPanel boutons = new JPanel();
	private JButton connection = new JButton();
	private JButton annuler =  new JButton();
	
	private Font fontTitre = new Font("Colibri", Font.BOLD, 36);
	private Semaphore sema;
	
	public InterfaceClient(Semaphore s, int width, int height){
		sema = s;
		
		//La fenetre
		this.setSize(width, height);
		this.setTitle("CIVKRAFT");
		this.setVisible(true);
	
		//Nom
		nomAcceuil = new JPanel();
		nomAcceuil.setSize(width / 2, height / 10);
		
		nom = new JLabel("CIVKRAFT");
		nom.setSize(nomAcceuil.getHeight(),nomAcceuil.getWidth());
		nom.setFont(fontTitre);
		nomAcceuil.add(nom);
		nom.setVisible(true);
		
		this.add(nomAcceuil, BorderLayout.NORTH);		
		nomAcceuil.setVisible(true);
		
		//Adresses et port
		adresseEtPort = new JPanel();
		
		explications = new JLabel("<html><center>Veuillez entrer l'adresse IP du serveur à contacter ainsi que le port.<br>L'équipe Civkraft vous souhaite une agréable partie!</center></html>");
		Font font = new Font("Arial", Font.BOLD, 12);
		explications.setFont(font);
		explications.setVisible(true);
		
		adresseEtPort.add(explications, BorderLayout.NORTH);
		adresse = new JPanel();
		adresseTitre = new JLabel("Adresse : ");
		adresseEntre = new JTextField(15);
		
		adresse.add(adresseTitre, BorderLayout.WEST);
		adresse.add(adresseEntre, BorderLayout.EAST);
		adresse.setVisible(true);
		
		port = new JPanel();		
		portTitre = new JLabel("Port : ");
		portEntre = new JTextField(3);
		
		port.add(portTitre, BorderLayout.WEST);
		port.add(portEntre, BorderLayout.EAST);
		port.setVisible(true);
		
		adresseEtPort.add(adresse, BorderLayout.WEST);
		adresseEtPort.add(port, BorderLayout.EAST);
		adresseEtPort.setVisible(true);
		
		this.add(adresseEtPort);
			
		//Boutons
		boutons = new JPanel();
		boutons.setSize(width / 2, height / 10);
		connection = new JButton("Connection");		
		connection.addMouseListener(this);
		
		annuler = new JButton("Annuler");
		annuler.addMouseListener(this);
		
		boutons.add(annuler, BorderLayout.EAST);
		boutons.add(connection, BorderLayout.WEST);
		boutons.setVisible(true);
		
		this.add(boutons, BorderLayout.SOUTH);
		
		//Mettre la fenetre au centre
		this.setLocationRelativeTo(null);
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource().equals(connection)){
			String portFinal = portEntre.getText();
			String adresseFinale = adresseEntre.getText();
			System.out.println("Adresse : " + adresseFinale);
			System.out.println("Port : " + portFinal);
			if(!portFinal.isEmpty() && !adresseFinale.isEmpty()){
				//Connection
				
				this.setVisible(false);
			}			
		}
		if(arg0.getSource().equals(annuler)){
			System.out.println("Exit");
			System.exit(1);
		}
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
