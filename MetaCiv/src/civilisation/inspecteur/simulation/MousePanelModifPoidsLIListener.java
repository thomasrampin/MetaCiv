package civilisation.inspecteur.simulation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class MousePanelModifPoidsLIListener implements MouseListener {

	private PanelModifPoidsLI panelModifPoidsLI;
	

	// timer permettant l'augmentation/diminution des poids en restant clique
	private Timer timer;
	// garde en memoire la reference de l'evenement
	private MouseEvent refEvent;
	// Indice du bouton : Moins - 0 , Plus - 1 , Edition - 2
	private int indice;
	//private panelPr
	
	public MousePanelModifPoidsLIListener(PanelModifPoidsLI panelModifPoidsLI, int indice)
	{
		this.panelModifPoidsLI = panelModifPoidsLI;
		this.indice = indice;
		//this.panelPrincipal = panelModifPoidsLI;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//if(e.getSource().getClass().equals(JButton.class))
		//((JButton)e.getSource()).setSelected(true);
		//System.out.println("pressed");
		
		if(SwingUtilities.isLeftMouseButton(e)){
			panelModifPoidsLI.evenementModifierPoids(indice);
			this.refEvent = e;
			// Si on a appuye sur + ou -
			if(indice==0 || indice==1)
			{
				
				timer = createTimer ();
				timer.setInitialDelay(500);	
				timer.start ();
			}
			// Si on a appuye sur le bouton d'edition
			else if(indice==2)
			{
				panelModifPoidsLI.creerPanelEdit();
			}
			// Création et lancement du timer
		   
		}	
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		//if(e.getSource().getClass().equals(JButton.class))
		//((JButton)e.getSource()).setSelected(false);

		//System.out.println("released");
		if(indice==0 || indice==1)
		{
			if(timer.isRunning())
				timer.stop();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

		panelModifPoidsLI.entre(e);
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
		//panelModifPoidsLI.exit(e);
	}
	
	 private Timer createTimer ()
	  {
	    // Création d'une instance de listener 
	    // associée au timer
	    ActionListener action = new ActionListener ()
	      {
	        // Méthode appelée à chaque tic du timer
	        public void actionPerformed (ActionEvent event)
	        {
	        	System.out.println("----");
	        	panelModifPoidsLI.evenementModifierPoids(indice);
	        }
	      };
	      
	    // Création d'un timer qui génère un tic
	    // chaque 500 millième de seconde
	    return new Timer (200, action);
	  }  

}
