package civkraft;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import civilisation.DefineConstants;
import civilisation.inspecteur.viewer.ViewerAgent;
import civilisation.inspecteur.viewer.ViewerTabbed;
import civilisation.world.WorldViewer;
import turtlekit.kernel.Patch;
import turtlekit.kernel.TKEnvironment;
import turtlekit.kernel.TKScheduler;

import I18n.I18nList;

import turtlekit.kernel.TurtleKit;
import madkit.kernel.AbstractAgent;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Probe;
import madkit.message.ObjectMessage;
import madkit.simulation.probe.SingleAgentProbe;

public class InterfaceServeur extends JFrame implements MouseListener {
		
	private Serveur dad;

	private JPanel nomAcceuil = new JPanel();
	private JLabel nom = new JLabel();
	
	private JPanel acceuil = new JPanel();

	private JTable clients = new JTable();
	private List<AgentAddress> clientsObtenus;
	private JLabel explications = new JLabel();
	
	private JPanel boutons = new JPanel();
	private JButton connection = new JButton();
	private JButton annuler =  new JButton();
	
	private Font fontTitre = new Font("Colibri", Font.BOLD, 36);
	private Semaphore sema;

	private Boolean tempsReel = false;

	
	public InterfaceServeur(Semaphore s, int width, int height, Serveur d){
		sema = s;
		dad = d;
		
		//La fenetre
		this.setSize(width, height);
		this.setTitle(I18nList.CheckLang("Serveur"));

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
		
		//Serveur
		
		
		explications = new JLabel(I18nList.CheckLang("Serveur démarré avec succès, en attente de joueurs..."),JLabel.CENTER);

		Font font = new Font("Arial", Font.BOLD, 12);
		explications.setFont(font);
		explications.setVisible(true);
		

		Object[][] data = {{I18nList.CheckLang("Emplacement Vide"), I18nList.CheckLang("Recherche en cours...")},
	            {I18nList.CheckLang("Emplacement Vide"), I18nList.CheckLang("Recherche en cours...")}};
		String[] columnNames = {I18nList.CheckLang("Kernel"), I18nList.CheckLang("Nom")};

		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		

		clients = new JTable(model);
		clients.setRowSelectionAllowed(true);
		clients.setPreferredSize(new Dimension(280, 280));
		
		acceuil = new JPanel();
		acceuil.add(explications, BorderLayout.NORTH);
		acceuil.add(clients, BorderLayout.SOUTH);

		acceuil.setVisible(true);
		
		this.add(acceuil);
		//Boutons
		boutons = new JPanel();
		boutons.setSize(width / 2, height / 10);

		connection = new JButton(I18nList.CheckLang("Lancer la partie"));		
		connection.addMouseListener(this);
		
		annuler = new JButton(I18nList.CheckLang("Annuler"));

		annuler.addMouseListener(this);
		
		boutons.add(annuler, BorderLayout.EAST);
		boutons.add(connection, BorderLayout.WEST);
		boutons.setVisible(true);
		
		this.add(boutons, BorderLayout.SOUTH);
		
		//Mettre la fenetre au centre
		this.setLocationRelativeTo(null);
		
	}

	
	public void actualiser(List<AgentAddress> list){

		Object[][] data = {{I18nList.CheckLang("Emplacement Vide"), I18nList.CheckLang("Recherche en cours...")},
	            {I18nList.CheckLang("Emplacement Vide"), I18nList.CheckLang("Recherche en cours...")}};

		clientsObtenus = null;
		if(list != null){
			clientsObtenus = list;
			data = null;
			data = new Object[clientsObtenus.size()+1][2];
			for(int i = 0; i < clientsObtenus.size();i++){
				data[i][0] = "Joueur " + (i+1);
				data[i][1] = clientsObtenus.get(i).getSimpleAgentNetworkID();
				data[clientsObtenus.size()][0] = I18nList.CheckLang("Emplacement Vide");
				data[clientsObtenus.size()][1] = I18nList.CheckLang("Recherche en cours...");
			}	
		}
		String[] columnNames = {I18nList.CheckLang("Kernel"), I18nList.CheckLang("Nom")};

		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		acceuil.remove(clients);
		acceuil.revalidate();
		clients = new JTable(model);
		clients.setPreferredSize(new Dimension(280, 280));
		clients.revalidate();
		acceuil.add(clients, BorderLayout.SOUTH);
		acceuil.revalidate();	
	}
	
	public Boolean getTempsReel() {
		return tempsReel;
	}


	public void setTempsReel(Boolean tempsReel) {
		this.tempsReel = tempsReel;
	}

	public void envoiViewers(List<AgentAddress> clients){
		
		if(clients != null && clients.size() > 0){
			
			//SingleAgentProbe<TKScheduler,Double> tP = probes.
			/*WorldViewer aa = (WorldViewer) dad.launchAgent(WorldViewer.class.getName());
			Set<Probe<? extends AbstractAgent>> probes = aa.getProbes();
			System.out.println("La probe du serveur est " + aa.getProbes());*/
				/*Iterator<Probe<? extends AbstractAgent>> it = probes.iterator();
				//La probe contenant les patch
				SingleAgentProbe<TKEnvironment, Patch[]> gp = (SingleAgentProbe<TKEnvironment, Patch[]>) it.next();
				//la probe contenant le scheduler
				SingleAgentProbe<TKScheduler,Double> tP = (SingleAgentProbe<TKScheduler, Double>) it.next();
				*/
				
				//Transfert
				Transfert t = new Transfert();
				t.setChoix(DefineConstants.LANCER_VIEWER);
				t.setHauteurSimu(WorldViewer.getInstance().getDisplayPane().getHeight());
				t.setLargeurSimu(WorldViewer.getInstance().getDisplayPane().getWidth());
				//WorldViewer.getInstance().getDisplayPane().getPreferredSize();
				t.setGrille(WorldViewer.getInstance().getPatchGrid());
				
				
				ObjectMessage<Transfert> om = new ObjectMessage<Transfert>(t);
				dad.broadcastMessage(DefineConstants.RESEAU, DefineConstants.GROUPE_RESEAU, DefineConstants.ROLE_CLIENT, om);	
		}else{
			//System.out.println("Il n'y a pas de client connecté.");
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource().equals(connection)){

			this.setTempsReel(true);
			dad.setEnvoiContinuViewer(true);
			envoiViewers(clientsObtenus);			
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