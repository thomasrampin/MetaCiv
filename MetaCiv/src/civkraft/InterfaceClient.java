package civkraft;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import I18n.I18nList;

import civilisation.DefinePath;
import turtlekit.kernel.TurtleKit;
import madkit.kernel.AbstractAgent;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.KernelAddress;
import madkit.kernel.Madkit;

public class InterfaceClient extends JFrame implements MouseListener {
	
	
	private Client dad;
	
	private List<AgentAddress> serveursObtenus;
	
	private JPanel nomAcceuil = new JPanel();
	private JLabel nom = new JLabel();
	
	private JPanel listeServeurs = new JPanel();
	private JComboBox serveurs = new JComboBox();
	private JLabel explications = new JLabel();
	//private JLabel explicationsConnect = new JLabel();
	
	
	private JPanel boutons = new JPanel();
	private JButton connection = new JButton();
	private JButton annuler =  new JButton();
	private JButton envoiScCognitif = new JButton();
	
	private Font fontTitre = new Font("Colibri", Font.BOLD, 36);
	private Semaphore sema;
	
	//1 pour envoi ScCognitif
	private int actionAFaire = 0;
	
	
	public InterfaceClient(Semaphore s, int width, int height, Client d){
		dad = d;
		sema = s;
		
		//La fenetre
		this.setSize(width, height);
		this.setTitle("CIVKRAFT");
		this.setVisible(true);
	
		//Nom
		nomAcceuil = new JPanel();
		nomAcceuil.setSize(width / 2, height / 10);
		
		nom = new JLabel("CIVKRAFT", JLabel.CENTER);
		nom.setSize(nomAcceuil.getHeight(),nomAcceuil.getWidth());
		nom.setFont(fontTitre);
		nomAcceuil.add(nom);
		nom.setVisible(true);
		
		this.add(nomAcceuil, BorderLayout.NORTH);		
		nomAcceuil.setVisible(true);
		
		//Serveurs
		listeServeurs = new JPanel();
		
		explications = new JLabel(I18nList.CheckLang("<html>Recherche de Serveurs Civkraft dans le réseau local ...<br>Vous pouvez lancer un serveur en choisissant l'option héberger partie"),JLabel.CENTER);
		Font font = new Font("Arial", Font.BOLD, 12);
		explications.setFont(font);
		explications.setVisible(true);
		
		Object[] data = {I18nList.CheckLang("Recherche...")};

		serveurs = new JComboBox(data);
		
		listeServeurs.add(explications, BorderLayout.NORTH);
		listeServeurs.add(serveurs, BorderLayout.SOUTH);
		listeServeurs.setVisible(true);
		
		this.add(listeServeurs);
			
		//Boutons
		boutons = new JPanel();
		boutons.setSize(width / 2, height / 10);
		//connection = new JButton("Connection");		
		//connection.addMouseListener(this);
		
		annuler = new JButton(I18nList.CheckLang("Annuler"));
		annuler.addMouseListener(this);
		
		envoiScCognitif = new JButton(I18nList.CheckLang("Envoi Schéma Cognitif"));
		envoiScCognitif.addMouseListener(this);
		
		boutons.add(annuler, BorderLayout.CENTER);
		boutons.add(envoiScCognitif, BorderLayout.CENTER);
		//boutons.add(connection, BorderLayout.WEST);
		boutons.setVisible(true);
		
		this.add(boutons, BorderLayout.SOUTH);
		
		//Mettre la fenetre au centre
		this.setLocationRelativeTo(null);
	}

	
	
	public void actualiser(List<AgentAddress> list){

		Object[] data = {I18nList.CheckLang("Recherche...")};
		serveursObtenus = null;
		if(list != null){
			serveursObtenus = list;
			data = null;
			data = new Object[serveursObtenus.size()];
			for(int i = 0; i < serveursObtenus.size();i++){
				//data[i][1] = serveursObtenus.get(i).getAgentNetworkID();
				data[i] = serveursObtenus.get(i);//.getSimpleAgentNetworkID();
			}
			explications.setText(I18nList.CheckLang("<html>Vous êtes actuellement connecté au serveur civkraft suivant.<br>En attente du lancement de la partie côté serveur...</html>"));
			
		}else{
			explications.setText(I18nList.CheckLang("<html>Rechercher de Serveurs Civkraft dans le réseau local ...<br>Vous pouvez lancer un serveur en choisissant l'option héberger partie"));
		}
		listeServeurs.remove(serveurs);
		//listeServeurs.revalidate();
		serveurs = new JComboBox(data);
		serveurs.revalidate();
		listeServeurs.add(serveurs, BorderLayout.SOUTH);
		listeServeurs.revalidate();	
	}
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource().equals(envoiScCognitif)){
			int cpt=0;
			while(!serveursObtenus.get(cpt).toString().equals(serveurs.getSelectedItem().toString())){
				cpt++;
			}
			
			FenetreEnvoiScCognitif fESC = new FenetreEnvoiScCognitif(serveursObtenus.get(cpt), dad);
			fESC.setVisible(true);
		
			
			
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
	
	public int getActionAFaire(){
		return actionAFaire;
	}
	
	public void setActionAFaire(int a){
		actionAFaire = a;
	}
	
	
}
