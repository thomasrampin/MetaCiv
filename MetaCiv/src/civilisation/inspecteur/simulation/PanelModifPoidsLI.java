package civilisation.inspecteur.simulation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ComponentListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import civilisation.Configuration;
import civilisation.DefineConstants;
import civilisation.individu.cognitons.LienPlan;
import civilisation.individu.cognitons.TypeCogniton;
import civilisation.inspecteur.simulation.dialogues.DialogueEditerLiensInfluence;
import civilisation.inspecteur.simulation.dialogues.DialogueEditionModifPoidsLI;

public class PanelModifPoidsLI extends JPanel{

	private JButton boutonMoins;
	private JButton boutonPlus;
	private JButton boutonEdit;
	private LienPlan lien;
	private GCogniton gc;
	private GPlan gp;
	
	// constantes concernant les dimensions
	public static Dimension dimensionPanelModifPoidsLI = new Dimension(110,45);
	public static Dimension dimensionPrefereeBoutonsPanelModifPoidsLI = new Dimension(20,20); 
	public static Dimension dimensionPrefereeBoutonEditPanelModifPoidsLI = new Dimension(50,(int)dimensionPrefereeBoutonsPanelModifPoidsLI.getHeight()); // 70

	
	//public int poidsMinLI = -10;

	public int valeurMin;
	public int valeurMax;
	public int pasModifPoidsLI = 1;
	
	public PanelModifPoidsLI(GCogniton gc, GPlan gp, double x, double y, LienPlan lien)
	{
		this.lien = lien;
		this.gc = gc;
		this.gp = gp;
		
		this.initialisationValeurs();
		this.verifierConcordance(lien.getPoids());
		
		// Positionne le composant au milieu verticalement (et horizontalement), collee aux autres boutons
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, (int)(dimensionPanelModifPoidsLI.getHeight()-dimensionPrefereeBoutonsPanelModifPoidsLI.getHeight())/2));
		this.setOpaque(false);
		this.setSize(dimensionPanelModifPoidsLI);
		this.setLocation((int)x-this.getWidth()/2, (int)y-this.getHeight()/2);
		
		ImageIcon iconeMoins = Configuration.getIcon("minus.png");	
		boutonMoins = new JButton(iconeMoins);
		boutonMoins.setToolTipText("Diminue le poids");
		ImageIcon iconePlus = Configuration.getIcon("plus.png");	
		boutonPlus = new JButton(iconePlus);
		boutonPlus.setToolTipText("Augmente le poids");
		boutonEdit = new JButton();
		boutonEdit.setMargin(new Insets(0,0,0,0));
		// Fond transparent
		boutonMoins.setFocusPainted(false);
		boutonEdit.setFocusPainted(false);
		boutonPlus.setFocusPainted(false);
		
		boutonPlus.setPreferredSize(dimensionPrefereeBoutonsPanelModifPoidsLI);
		//boutonPlus.setPreferredSize(new Dimension(boutonPlus.getIcon().getIconWidth(), boutonPlus.getIcon().getIconHeight()));
		boutonMoins.setPreferredSize(dimensionPrefereeBoutonsPanelModifPoidsLI);
		boutonEdit.setPreferredSize(dimensionPrefereeBoutonEditPanelModifPoidsLI);
		
		
		boutonMoins.addMouseListener(new MousePanelModifPoidsLIListener(this, 0));
		boutonPlus.addMouseListener(new MousePanelModifPoidsLIListener(this, 1));
		boutonEdit.addMouseListener(new MousePanelModifPoidsLIListener(this, 2));
		//this.addMouseListener(listener);
		
		this.add(boutonMoins);
		this.add(boutonEdit);
		this.add(boutonPlus);
		
		// On cache les bouton Plus et Moins pour l'instant
		boutonPlus.setVisible(false);
		boutonMoins.setVisible(false);		
	
		this.actualiserAffichagePoids();
		
		/*boutonEdit.addComponentListener(new ComponentListener(){

			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub

				//System.out.println("name : " + e.getComponent().getParent().getParent().getClass().getName());
				PanelModifPoidsLI p = (PanelModifPoidsLI)e.getComponent().getParent().getParent();
				double x = (p.getGc().getCentreX()+p.getGp().getCentreX())/2-p.getWidth()/2;
				double y = (p.getGc().getCentreY()+p.getGp().getCentreY())/2-p.getHeight()/2;
				System.out.println("\tX = " + p.getLocation().getX());
				System.out.println("\tY = " + p.getLocation().getY());
				System.out.println("\tlargeur = " + p.getWidth());
				System.out.println("\thauteur = " + p.getHeight());
				//this.setYy((a.getCentreY()+b.getCentreY())/2-hauteurItem/2);
				//p.setLocation((int)x, (int)y);
				System.out.println("\tX_ = " + p.getLocation().getX());
				System.out.println("\tY_ = " + p.getLocation().getY());
				System.out.println("\tlargeur = " + p.getWidth());
				System.out.println("\thauteur = " + p.getHeight());
				
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				System.out.println("on bouge");
				System.out.println("\tx = " + e.getComponent().getX());
				System.out.println("\ty = " + e.getComponent().getY());
				}

			@Override
			public void componentShown(ComponentEvent e) {
				System.out.println("on se montre");}

			@Override
			public void componentHidden(ComponentEvent e) {}
			});*/
	}
	
	public void initialisationValeurs()
	{
		this.valeurMin = (int)Configuration.SCEnCoursEdition.weightsBound*(-1);
		this.valeurMax = (int)Configuration.SCEnCoursEdition.weightsBound;
		
		switch(Configuration.SCEnCoursEdition.weightInterval)
		{
			case DefineConstants._WeightIntervalBoth :
					// par default
				break;
			case DefineConstants._WeightIntervalNegative :
				this.valeurMax = 0;
				break;
			case DefineConstants._WeightIntervalPositive :
				this.valeurMin = 0;
				break;
		}
	}
	

	public void actualiserValeurs()
	{
		this.initialisationValeurs();
		this.verifierConcordance(lien.getPoids());
		
		this.actualiserAffichagePoids();
	}
	
	public void actualiserPosition()
	{
		this.setLocation((int)(gc.getCentreX()+gp.getCentreX())/2-this.getWidth()/2,(int)(gc.getCentreY()+gp.getCentreY())/2-this.getHeight()/2);
	}
	
	public void actualiserAffichagePoids()
	{
		int poids = 0;
		if(Configuration.SCEnCoursEdition.weightsBound>0)
			poids = lien.getPoids()*100/(int)Configuration.SCEnCoursEdition.weightsBound;
		
		boutonEdit.setText(String.valueOf(poids)+"%");
	}
	
	public void modifierPoids(int poids)
	{
		this.verifierConcordance(poids);
		
		((PanelStructureCognitive) gc.getParent()).modifierLienInfluence(gc, gp, lien.getPoids());
		actualiserAffichagePoids();
	}
	
	public void verifierConcordance(int poids)
	{
		if(poids>this.valeurMax)
			lien.setPoids(this.valeurMax);
		else if(poids<this.valeurMin)
			lien.setPoids(this.valeurMin);
		else
			lien.setPoids(poids);
	}
	
	public void evenementModifierPoids(int indice)
	{
		if(indice==0)
		{
			modifierPoids(lien.getPoids()-pasModifPoidsLI);
		}
		else if(indice==1)
		{
			modifierPoids(lien.getPoids()+pasModifPoidsLI);
		}
	}
	
	public void creerPanelEdit()
	{
		((PanelStructureCognitive) gc.getParent()).creerPanelEdit(this, gc, gp, lien);
	}
	
	// Methode appelee quand on survole un composant écouté
	public void entre(MouseEvent e)
	{
		// Solution a�ameliorer
		/*if(!e.getSource().equals(boutonMoins)
			&&!e.getSource().equals(boutonEdit)
			&&!e.getSource().equals(boutonPlus))
		{
			((PanelStructureCognitive) gc.getParent()).enleverPanelModifPoidsLI(this);
		}*/
		
		// Si on survole le bouton -> On affiche les boutons Moins et Plus
		if(e.getSource().equals(boutonEdit))
		{
			boutonMoins.setVisible(true);
			boutonPlus.setVisible(true);
		}
		
	}
	
	// Retourne le GPlan associe
	public GPlan getGp()
	{
		return gp;
	}
	
	// Retourne le GCogniton associé
	public GCogniton getGc()
	{
		return gc;
	}
	
	// Enleve les boutons Moins et Plus de l'affichage
	public void enleverBoutonsMoinsPlus()
	{
			boutonMoins.setVisible(false);
			boutonPlus.setVisible(false);
	}
	
}

