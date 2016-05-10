package civilisation.inspecteur.simulation;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.lang.model.element.TypeParameterElement;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import I18n.I18nList;

import civilisation.Civilisation;
import civilisation.Configuration;
import civilisation.SchemaCognitif;
import civilisation.group.Group;
import civilisation.individu.Human;
import civilisation.individu.cognitons.Cogniton;
import civilisation.individu.cognitons.LienPlan;
import civilisation.individu.cognitons.TypeCulturon;
import civilisation.individu.cognitons.TypeCogniton;
import civilisation.individu.cognitons.TypeDeCogniton;
import civilisation.individu.plan.NPlan;
import civilisation.individu.plan.NPlanPondere;
import civilisation.inspecteur.animations.JJAnimationOpacite;
import civilisation.inspecteur.animations.JJAnimationRotation;
import civilisation.inspecteur.animations.JJAnimationTranslation;
import civilisation.inspecteur.animations.JJAnimationTranslationEntreDeuxObjets;
import civilisation.inspecteur.animations.JJComponent;
import civilisation.inspecteur.animations.JJPanel;
import civilisation.inspecteur.simulation.dialogues.DialogueEditerCogniton;

public class PanelStructureCognitive extends JJPanel{

	protected ArrayList<GCogniton> gCognitons;
	protected ArrayList<GPlan> gPlan;

	//protected ArrayList<GModifPoidsLI> gModifPoidsLI;
	protected ArrayList<PanelModifPoidsLI> panelModifPoidsLI;
	protected ArrayList<PanelEditionModifPoidsLI> panelEdition;

	protected ArrayList<GTrigger> gTriggers;
	protected ArrayList<GLien> gLiens;
	protected ArrayList<GLien> gLiensConditionnels;
	protected ArrayList<GLien> gLinksTrigger;
	protected ArrayList<GGroupForCognitonGraph> gGroup;

	protected ArrayList<TypeCogniton> allCognitons;
	protected ArrayList<NPlan> plans;
	protected ArrayList<Group> groups;
	
	protected double espacement = 40;
	protected double espaceCognitonsPlans = 350;
	int tempsEntreApparitionSpheresLiens = 100;
	int compteur = 0;
	int initXTrigger = 12;
	
	PanelModificationSimulation panelPrincipal;
	
	JPopupMenu popupGPlans;
	JPopupMenu popupGCognitons;
	
	protected boolean showGroup = false;
	
	protected Human h;

	private boolean animeIndicateurDeLiens=true;

	

	
	public PanelStructureCognitive()
	{
		super();
		this.setDelay(5);
		
		initializeArray();
		initializeItemsToDraw();
		initializeDrawing();

		MousePanelStructureCognitiveListener listener = new MousePanelStructureCognitiveListener(this);
		this.addMouseListener(listener);
		this.addKeyListener(listener);
	}
	
	public PanelStructureCognitive(PanelModificationSimulation panelPrincipal) {
		this();
		this.panelPrincipal = panelPrincipal;
	}
	public PanelStructureCognitive(boolean b) {
		//Workaround to allow initialization as we want for child class
	}
	
	protected void initializeArray() {
		gCognitons = new ArrayList<GCogniton>();
		gPlan = new ArrayList<GPlan>();

		panelModifPoidsLI = new ArrayList<PanelModifPoidsLI>();
		panelEdition = new ArrayList<PanelEditionModifPoidsLI>();

		gLiens = new ArrayList<GLien>();
		gLiensConditionnels = new ArrayList<GLien>();
		gLinksTrigger = new ArrayList<GLien>();
		gTriggers = new ArrayList<GTrigger>();
		gGroup = new ArrayList<GGroupForCognitonGraph>();
		groups = new ArrayList<Group>();

	}
	
	/**
	 * Override this function to draw only some items
	 */
	protected void initializeItemsToDraw() {

		allCognitons = Configuration.getSchemaCognitifEnCourEdition().getCognitons();
		plans = Configuration.getSchemaCognitifEnCourEdition().getPlans();	
	}
	
	protected void initializeDrawing() {
	for (int i = 0; i < allCognitons.size(); i++){		
			if (allCognitons.get(i).getType() != TypeDeCogniton.CULTURON) {
				afficherCogniton(allCognitons.get(i),80,40+espacement*i);
			}
		}

		
		for (int i = 0; i < plans.size(); i++){	
			afficherPlan(plans.get(i), espaceCognitonsPlans,40+espacement*i,null);
		}
		
	/*	for (int i = 0; i < groups.size(); i++){	
			showGroup(groups.get(i), espaceCognitonsPlans * 3,40+espacement*i);
		}
	*/	
		creerLiensInfluence();
		creerLiensConditionnels();
		createTriggerLink();
	}
	
	

	

	public void selectionnerPlan(NPlan plan){
		if (panelPrincipal != null)
		panelPrincipal.changerArbreActions(plan);
	}
	
	
	public void appliquerTransparence(ArrayList<JJComponent> liste){
		
		boolean dansLaListe;
		
        for (int i = 0; i < this.getComponentCount() ; i++){
        	if (this.getComponent(i) instanceof JJComponent){
        		dansLaListe = false;
	        	for (int j = 0; j < liste.size(); j++){
	        		if (liste.get(j).equals(this.getComponent(i)))
	        		{
	        			dansLaListe = true;
	        		}
	        	}
	        	if (!dansLaListe){
	        		((JJComponent) this.getComponent(i)).addAnimation(new JJAnimationOpacite(35, (JJComponent) this.getComponent(i), -0.02, false));
	        	}
        	}
        }
	}
	
	@Override
	public void animate(){
		super.animate();

		// Si l'animation est active
		if(animeIndicateurDeLiens)
		{
			compteur++;
			if (compteur == tempsEntreApparitionSpheresLiens){
				compteur = 0;
				for (int i = 0 ; i < gLiens.size(); i++){
					for (int j = 0 ; j < Math.abs(gLiens.get(i).getPoids()) ; j++){
						GIndicateurDeLiens gIndicLien;
						gIndicLien = new GIndicateurDeLiens(this, -500, -500, 16, 16, gLiens.get(i).getPoids() > 0);
	
						this.add(gIndicLien);
						JJAnimationTranslationEntreDeuxObjets anim = new JJAnimationTranslationEntreDeuxObjets(250, gIndicLien, gLiens.get(i).getA(),gLiens.get(i).getB(), true); 
						gIndicLien.addAnimation(anim);
						anim.decalerPas(j*(-5));
					}
	
					//gIndicLien.addAnimation(new JJAnimationTranslation(2000, gIndicLien, 1.,0.1, true));
				}
			}
		}
		// sinon on garde le compteur � 0
		else{
			compteur=0;
		}		
	}
	
	/*public void afficherPopupPanel (MouseEvent e)
	{
		popupCouleur = new JPopupMenu("Couleur");
		JMenuItem parType = new JMenuItem(I18nList.CheckLang("Couleurs par Type"));
		//parType.addActionListener(new ActionsMenuCouleur());
		
		popupCouleur.add(parType);
		
		popupCouleur.show(this, e.getX(), e.getY());
	}*/
	
	public void afficherPopupPlan(MouseEvent e , GPlan p){
		final NPlan refP = p.getPlan(); 

		popupGPlans = new JPopupMenu(I18nList.CheckLang("Plan"));
		JMenuItem editerPlan = new JMenuItem(I18nList.CheckLang("Edit Plan"));
		editerPlan.addActionListener(new ActionsMenuGPlan(p,0));
		editerPlan.setIcon(new ImageIcon(System.getProperty("user.dir") + Configuration.pathToIcon + "/pencil.png"));
		popupGPlans.add(editerPlan);
		JMenuItem affichageCustom = new JMenuItem(I18nList.CheckLang("Display Custom colors"));
		affichageCustom.addActionListener(new ActionListener () {
			
			public void actionPerformed(ActionEvent e) {
				for( GPlan gc : gPlan)
				{
					gc.displayCustomColor();
				}
				
			}
		});
		affichageCustom.setIcon(new ImageIcon(System.getProperty("user.dir") + Configuration.pathToIcon + "/lock--arrow.png"));
		popupGPlans.add(affichageCustom);
		
		JMenuItem affichageParType = new JMenuItem(I18nList.CheckLang("Display Type colors"));
		affichageParType.addActionListener(new ActionListener () {
			
			public void actionPerformed(ActionEvent e) {
				for( GPlan gc : gPlan)
				{
					gc.displayTypeColor();
				}
				
			}
		});
		affichageParType.setIcon(new ImageIcon(System.getProperty("user.dir") + Configuration.pathToIcon + "/lock--arrow.png"));
		popupGPlans.add(affichageParType);
		
		JMenuItem supprimerPlan = new JMenuItem(I18nList.CheckLang("Remove"));

		supprimerPlan.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				for(GCogniton cog : gCognitons)
				{
					cog.typeCogniton.removePlan(refP);
				}
				supprimerLiensConditionnels();
				supprimerLiensInfluence();
				clearTriggerLink();
				
				Configuration.getSchemaCognitifEnCourEdition().removePlan(refP);
				removePlan(refP);
				plans.remove(refP);
				creerLiensInfluence();
				creerLiensConditionnels();
				createTriggerLink();
			}
		});
		supprimerPlan.setIcon(new ImageIcon(System.getProperty("user.dir") + Configuration.pathToIcon + "/cross.png"));
		popupGPlans.add(supprimerPlan);
		
		popupGPlans.show(this, (int)p.getXx() + e.getX(), (int)p.getYy() + e.getY());
	}
	
	public void afficherPopupCogniton(MouseEvent e , GCogniton c){
		final GCogniton refCogniton =c;
		final TypeCogniton refType = c.getCogniton();
		
		System.out.println(Configuration.pathToIcon + "/pencil.png");
		System.out.println(this.getClass().getResource("../icones/arrow-out.png"));

		popupGCognitons = new JPopupMenu(I18nList.CheckLang("Cogniton"));
		JMenuItem editerCogniton = new JMenuItem(I18nList.CheckLang("Edit Cogniton"));
		editerCogniton.addActionListener(new ActionsMenuGCogniton(c,0));
		editerCogniton.setIcon(new ImageIcon(System.getProperty("user.dir") + Configuration.pathToIcon + "/pencil.png"));
		popupGCognitons.add(editerCogniton);
		JMenuItem editerInfluences = new JMenuItem(I18nList.CheckLang("Edit influence links"));
		editerInfluences.addActionListener(new ActionsMenuGCogniton(c,1));
		editerInfluences.setIcon(new ImageIcon(System.getProperty("user.dir") + Configuration.pathToIcon + "/arrow-out.png"));
		popupGCognitons.add(editerInfluences);
		JMenuItem editerConditions = new JMenuItem(I18nList.CheckLang("Edit conditional links"));
		editerConditions.addActionListener(new ActionsMenuGCogniton(c,2));
		editerConditions.setIcon(new ImageIcon(System.getProperty("user.dir") + Configuration.pathToIcon + "/lock--arrow.png"));
		popupGCognitons.add(editerConditions);
		JMenuItem editTriggeringAttributes = new JMenuItem(I18nList.CheckLang("Edit triggering attributes"));

		editTriggeringAttributes.addActionListener(new ActionsMenuGCogniton(c,3));
		editTriggeringAttributes.setIcon(new ImageIcon(System.getProperty("user.dir") + Configuration.pathToIcon + "/lock--arrow.png"));
		popupGCognitons.add(editTriggeringAttributes);
		
		JMenuItem affichageCustom = new JMenuItem(I18nList.CheckLang("Display Custom colors"));
		affichageCustom.addActionListener(new ActionListener () {
			
			public void actionPerformed(ActionEvent e) {
				for( GCogniton gc : gCognitons)
				{
					gc.displayCustomColor();
				}
				
			}
		});
		affichageCustom.setIcon(new ImageIcon(System.getProperty("user.dir") + Configuration.pathToIcon + "/lock--arrow.png"));
		popupGCognitons.add(affichageCustom);
		
		JMenuItem affichageParType = new JMenuItem(I18nList.CheckLang("Display Type colors"));
		affichageParType.addActionListener(new ActionListener () {
			
			public void actionPerformed(ActionEvent e) {
				for( GCogniton gc : gCognitons)
				{
					gc.displayTypeColor();
				}
				
			}
		});
		affichageParType.setIcon(new ImageIcon(System.getProperty("user.dir") + Configuration.pathToIcon + "/lock--arrow.png"));
		popupGCognitons.add(affichageParType);
		//JMenuItem editerChaine = new JMenuItem("Edit inter-cogniton links");
		//editerChaine.setIcon(new ImageIcon(System.getProperty("user.dir") + Configuration.pathToIcon + "/arrow-in-out.png"));
		//popupGCognitons.add(editerChaine);

		JMenuItem supprimerCogniton = new JMenuItem(I18nList.CheckLang("Remove"));

		supprimerCogniton.setIcon(new ImageIcon(System.getProperty("user.dir") + Configuration.pathToIcon + "/cross.png"));
		supprimerCogniton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				supprimerLiensConditionnels();
				supprimerLiensInfluence();
				clearTriggerLink();
				
				for(Iterator<GTrigger> it = gTriggers.iterator(); it.hasNext();)
				{
					GTrigger gt = it.next();
					if(gt.getgCogniton() == refCogniton)
					{
						remove(gt);
						it.remove();
					}
				}
				Configuration.getSchemaCognitifEnCourEdition().removeTriggersOfCogniton(refType);
				Configuration.getSchemaCognitifEnCourEdition().removeCogniton(refType);
				removeCogniton(refType);
				gCognitons.remove(refCogniton);

				System.out.println("refCogniton : " + refCogniton.getCogniton().getNom());

				creerLiensInfluence();
				creerLiensConditionnels();
				createTriggerLink();
			}
		});
		popupGCognitons.add(supprimerCogniton);
		popupGCognitons.show(this, (int)c.getXx() + e.getX(), (int)c.getYy() + e.getY());
		// On supprime les panelModifPoidsLI pr�sent
		this.enleverPanelsModifPoidsLI();

	}


	public void afficherCogniton(TypeCogniton c , double posX , double posY){
		gCognitons.add(new GCogniton(this,posX,posY,60,25, c));
		//gCognitons.get(gCognitons.size()-1).addAnimation(new JJAnimationTranslation(-1, gCognitons.get(gCognitons.size()-1), 0.05, 0.05, false));
		this.add(gCognitons.get(gCognitons.size()-1));
		this.setComponentZOrder(gCognitons.get(gCognitons.size()-1), gCognitons.size()-1);
		this.showTrigger(gCognitons.get(gCognitons.size()-1));
	}
	
	public void afficherPlan(NPlan p , double posX , double posY, NPlanPondere pp){
		gPlan.add(new GPlan(this,posX,posY,60,25, p));
		//gPlan.get(gPlan.size()-1).addAnimation(new JJAnimationRotation(-1, gPlan.get(gPlan.size()-1), 0.01, false));
		this.add(gPlan.get(gPlan.size()-1));
		this.setComponentZOrder(gPlan.get(gPlan.size()-1), 0);
		if (pp != null) {
			gPlan.get(gPlan.size()-1).setConcretePlan(pp);
		}
	}
	

	// Methode appelee quand un clic est effectue sur un GCogniton
	// Permet d'afficher les boutons de modifications de poids de chaque lien d'influence
	// associe au cogniton.
	public void modifierAffichagePanelModifPoidsLI(GCogniton gC){

		// test
		GCogniton gCTemp=null;
		// On test si on a deja active le changement
		boolean dejaActive = false;
		for(int i=panelModifPoidsLI.size()-1;i>=0;i--)
		{
			if(gC.equals(panelModifPoidsLI.get(i).getGc()))
			{
				// to do
				// On fait disparaitre les sliders/boutons
				//////this.remove(panelModifPoidsLI.get(i));
				//////this.panelModifPoidsLI.remove(i);
				// test
				if(panelModifPoidsLI.get(i).isVisible())
					this.panelModifPoidsLI.get(i).setVisible(false);
				else
					this.panelModifPoidsLI.get(i).setVisible(true);
				dejaActive = true;
			}
		}
		if(!dejaActive)
		{
			this.afficherPanelModifPoidsLI(gC);
		}
		
	}
	
	public void afficherPanelsModifPoidsLI()
	{
		for(GCogniton gC : gCognitons)
		{
			afficherPanelModifPoidsLI(gC);
		}
	}
	
	public void afficherPanelModifPoidsLI(GCogniton gC)
	{
		// On parcourt tous les liens d'influence du cogniton
		for(int i=0;i<gC.getCogniton().getLiensPlans().size();i++)
		{
			// On parcours tous les plans
			for(int j=0;j<gPlan.size();j++)
			{
				// Si les plans correspondent
				if(gC.getCogniton().getLiensPlans().get(i).getP().equals(plans.get(j)))
				{
					// On calcule la position finale
					double positionX = (gC.getCentreX()+gPlan.get(j).getCentreX())/2;
					double positionY = (gC.getCentreY()+gPlan.get(j).getCentreY())/2;
					panelModifPoidsLI.add(new PanelModifPoidsLI(gC, gPlan.get(j), positionX,positionY,gC.getCogniton().getLiensPlans().get(i)));
					this.add(panelModifPoidsLI.get(panelModifPoidsLI.size()-1));
				}
			}
		}
					
		// On met les gItem au dernier Plan pour laisser le panelModifPoidsLI au premier plan
		this.metreGItemDernierPlan();
	}

	public void showGroup(Group gr, String role , double posX , double posY){
		System.out.println("show group");
		gGroup.add(new GGroupForCognitonGraph(this,posX,posY,60,25, gr, role));
		//gCognitons.get(gCognitons.size()-1).addAnimation(new JJAnimationTranslation(-1, gCognitons.get(gCognitons.size()-1), 0.05, 0.05, false));
		this.add(gGroup.get(gGroup.size()-1));
		this.setComponentZOrder(gGroup.get(gGroup.size()-1), gGroup.size()-1);
	}

	public void showCloudCogniton(TypeCulturon c , double posX , double posY){
		gCognitons.add(new GCloudCogniton(this,posX,posY,60,25, c));
		//gCognitons.get(gCognitons.size()-1).addAnimation(new JJAnimationTranslation(-1, gCognitons.get(gCognitons.size()-1), 0.05, 0.05, false));
		this.add(gCognitons.get(gCognitons.size()-1));
		this.setComponentZOrder(gCognitons.get(gCognitons.size()-1), gCognitons.size()-1);
	}
	
	public void showTrigger(GCogniton c) {
		for (int i = 0 ; i < gTriggers.size(); i++) {
			
			GTrigger gt = gTriggers.get(i);

			if (gt.getgCogniton() == c) {
				this.remove(gt);
				gTriggers.remove(i);
			}			
			
		}
		
		// If the GTrigger doesn't exist, create it
		for (int i = 0; i < c.getCogniton().getTriggeringAttributes().size();i++){
			Object[] trigInfo = c.getCogniton().getTriggeringAttributes().get(i);
			GTrigger newGt = new GTrigger(this,initXTrigger,c.getYy());


			//System.out.println((String)trigInfo[0]);
			//System.out.println(Configuration.getSchemaCognitifEnCourEdition().getAttributeIndexByName((String)trigInfo[0]));

			newGt.setAttributesIndex(Configuration.getSchemaCognitifEnCourEdition().getAttributeIndexByName((String)trigInfo[0]));
			newGt.setValue((Double)trigInfo[1]);
			newGt.setComparator((Integer)trigInfo[2]);
			newGt.setgCogniton(c);
			gTriggers.add(newGt);
			this.add(newGt);

		}
		
	}

	public TypeCogniton creerCogniton() {
		TypeCogniton nouveauCogniton = new TypeCogniton();
		Configuration.getSchemaCognitifEnCourEdition().addCogniton(nouveauCogniton);
		nouveauCogniton.creerCognitonLambda(Configuration.getSchemaCognitifEnCourEdition());
		afficherCogniton(nouveauCogniton, 100,100);
		return nouveauCogniton;
	}
	
	public void creerPlan() {
		NPlan nouveauPlan = new NPlan();
		nouveauPlan.setNom("Nouveau plan");
		Configuration.getSchemaCognitifEnCourEdition().addPlan(nouveauPlan);
		afficherPlan(nouveauPlan,100,100,null);
	}
	
	public void creerLiensInfluence(){
		
		for (int i = 0; i < gCognitons.size(); i++){
			for (int j = 0; j < gCognitons.get(i).getCogniton().getLiensPlans().size(); j++){
				int k = 0;
				while(k < gPlan.size() && !gCognitons.get(i).getCogniton().getLiensPlans().get(j).getP().equals(gPlan.get(k).getPlan())){
					k++;
				}
				if (k < gPlan.size()) {
					gLiens.add(new GLien(this,gCognitons.get(i),gPlan.get(k),gCognitons.get(i).getCogniton().getLiensPlans().get(j).getPoids() , Color.BLACK));
				}
				//this.add(gLiens.get(gLiens.size()-1));
			}

		}
		for (int i = 0; i < gCognitons.size(); i++){
			this.setComponentZOrder(gCognitons.get(i), this.getComponentCount()-1);
		}
		
		for (int i = 0; i < gPlan.size(); i++){
			this.setComponentZOrder(gPlan.get(i), this.getComponentCount()-1);
		}
	}


	public void modifierLienInfluence(GCogniton a, GPlan b, int poids)
	{
		for(int i=0;i<gLiens.size();i++)
		{
			if(gLiens.get(i).getA().equals(a)&&gLiens.get(i).getB().equals(b))
				gLiens.get(i).setPoids(poids);
		}
	}
	
	public void creerLiensConditionnels(){
		for (int i = 0; i < gCognitons.size(); i++){
			for (int j = 0; j < gCognitons.get(i).getCogniton().getPlansAutorises().size(); j++){
				int k = 0;
				while(k < gPlan.size() && !gCognitons.get(i).getCogniton().getPlansAutorises().get(j).equals(gPlan.get(k).getPlan())){
					k++;
				}
				if (k < gPlan.size()) {
				gLiensConditionnels.add(new GLien(this,gCognitons.get(i),
						gPlan.get(k),
						-1 , Color.BLUE));
				}
				}

		}
		for (int i = 0; i < gCognitons.size(); i++){
			this.setComponentZOrder(gCognitons.get(i), this.getComponentCount()-1);
		}
		
		for (int i = 0; i < gPlan.size(); i++){
			this.setComponentZOrder(gPlan.get(i), this.getComponentCount()-1);
		}
	}
	
	public void createTriggerLink(){
		for (int i = 0; i < gTriggers.size(); i++){
				this.gLinksTrigger.add(new GLien(this,gTriggers.get(i),
						gTriggers.get(i).getgCogniton(),
						-1 , Color.ORANGE));
				//System.out.println(gCognitons.get(i).getCogniton().getNom() +" i");
		}
		
		for (int i = 0; i < gCognitons.size(); i++){
			this.setComponentZOrder(gCognitons.get(i), this.getComponentCount()-1);
		}
		
		for (int i = 0; i < gPlan.size(); i++){
			this.setComponentZOrder(gPlan.get(i), this.getComponentCount()-1);
		}
	}
	
	
	public void clearTriggerLink(){
		for (int i = 0 ; i < gLinksTrigger.size(); i++){
			this.remove(gLinksTrigger.get(i));
		}
		gLinksTrigger.clear();
	}
	
	public void supprimerLiensInfluence(){
		for (int i = 0 ; i < gLiens.size(); i++){
			this.remove(gLiens.get(i));
		}
		gLiens.clear();
	}
	
	public void supprimerLiensConditionnels(){
		for (int i = 0 ; i < gLiensConditionnels.size(); i++){
			this.remove(gLiensConditionnels.get(i));
		}
		gLiensConditionnels.clear();
	}
	
	public void removeCogniton(TypeCogniton cognitonToRemove) {

		System.out.println("remove cogniton");
		for(Iterator<GCogniton> it = gCognitons.iterator(); it.hasNext();)
		{
			GCogniton cog = it.next();
			if(cog.getCogniton() == cognitonToRemove)
			{
				this.remove(cog);
				it.remove();
				allCognitons.remove(cog.getCogniton());
			}
		}
	}
	
	public void removePlan(NPlan planToRemove) {
		
		System.out.println("remove plan");
		for(Iterator<GPlan> it = gPlan.iterator(); it.hasNext();)
		{
			GPlan pl = it.next();
			if(pl.getPlan() == planToRemove)
			{
				this.remove(pl);
				it.remove();
				plans.remove(pl.getPlan());
			}
		}
	}
	
    @Override
	public void paintComponent(Graphics g) {
    	Graphics2D g2d = (Graphics2D) g;
    	super.paintComponent(g);

    	/*TODO solution pas terrible pour les GLiens qui servent un peu ___ rien___*/
    	for (int i = 0; i < gLiens.size(); i++){
            g2d.setStroke(new BasicStroke(2));
    		g2d.drawLine((int)gLiens.get(i).getA().getCentreX(), (int)gLiens.get(i).getA().getCentreY(),(int) gLiens.get(i).getB().getCentreX(), (int)gLiens.get(i).getB().getCentreY());
    	}
    	
    	for (int i = 0; i < gLiensConditionnels.size(); i++){
    		g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(2));
    		g2d.drawLine((int)gLiensConditionnels.get(i).getA().getCentreX()-2, (int)gLiensConditionnels.get(i).getA().getCentreY()-2,(int) gLiensConditionnels.get(i).getB().getCentreX()-2, (int)gLiensConditionnels.get(i).getB().getCentreY()-2);
    	}
    	
    	for (int i = 0; i < this.gLinksTrigger.size(); i++){
    		g2d.setColor(Color.ORANGE);
            g2d.setStroke(new BasicStroke(3));
    		g2d.drawLine((int)gLinksTrigger.get(i).getA().getCentreX()-2, (int)gLinksTrigger.get(i).getA().getCentreY()-2,(int) gLinksTrigger.get(i).getB().getCentreX()-2, (int)gLinksTrigger.get(i).getB().getCentreY()-2);
    	}

    	// to do
    	for (int i = 0; i < this.panelModifPoidsLI.size(); i++){
    		
    		if(animeIndicateurDeLiens){
    		this.panelModifPoidsLI.get(i).actualiserPosition();
    		this.panelModifPoidsLI.get(i).repaint();}
    	}
	}



	public void createCloudCogniton() {

		TypeCulturon newCloudCogniton = new TypeCulturon();
		Configuration.getSchemaCognitifEnCourEdition().addCloudCogniton(newCloudCogniton);
		newCloudCogniton.creerCognitonLambda(Configuration.getSchemaCognitifEnCourEdition());
		afficherCogniton(newCloudCogniton, 100,100);		
	}
	
	public boolean planIsDrawn(NPlan p) {
		for (int i = 0; i < gPlan.size(); i++) {
			if (gPlan.get(i).getPlan() == p) {
				return true;
			}
		}	
		return false;
	}
	
	public boolean cognitonIsDrawn(TypeCogniton c) {
		for (int i = 0; i < gCognitons.size(); i++) {
			if (gCognitons.get(i).getCogniton() == c) {
				return true;
			}
		}	
		return false;
	}

	public void displayCognitiveScheme(SchemaCognitif civ) {
		clearDisplay();
		initializeItemsToDraw();
		initializeDrawing();
	}

	private void clearDisplay() {

		clearTriggerLink();
		gTriggers.clear();
		gCognitons.clear();
		gPlan.clear();
		panelModifPoidsLI.clear();
		panelEdition.clear();
		this.removeAll();
		supprimerLiensConditionnels();
		supprimerLiensInfluence();
		
	}
	
	// to do
	// A enlever surement (commentaire pour l'instant)
	/*public void afficherPanelModifPoidsLI(GCogniton gc, GPlan gp, double x, double y, LienPlan lien)
	{
		//this.add(new PanelModifPoidsLI(gc, gp, x, y, lien));
		this.add(new PanelModifPoidsLI(gc, gp, x, y, lien));
	}
	*/
	
	
	// Methode appelee quand on clique sur le panel principal
	// Permet  d'enlever tous les panels de modifications et le panel d'edition si y a
	public void enleverPanelsModifPoidsLI()
	{
		for(int i=panelModifPoidsLI.size()-1;i>=0;i--)
		{
			//this.remove(panelModifPoidsLI.get(i));
			//panelModifPoidsLI.remove(i);
			panelModifPoidsLI.get(i).actualiserValeurs();
			panelModifPoidsLI.get(i).setVisible(false);
		}
		// to do
		// si il y a un panel d'edition (ou plusieurs ?), l'enlever
	}
	
	// Methode appelee quand on rentre dans le panel principal (et quand on sorts des boutons + et - donc)
	// Permet d'enlever tous les boutons + et -
	public void enleverBoutonsModifRapide()
	{
		for(int i=0;i<panelModifPoidsLI.size();i++)
		{
			panelModifPoidsLI.get(i).enleverBoutonsMoinsPlus();
		}
	}
	
	// Methode appelee quand on ouvre et ferme l'editeur des modifications du poids du lien d'influence
	// Permet d'afficher/cacher les panels au premier plan
	/*public void modifierVisibilitePanelsModifPoidsLI(boolean visible)
	{
		for(int i=0;i<panelModifPoidsLI.size();i++)
		{
			panelModifPoidsLI.get(i).setVisible(visible);
		}
	}*/
	
	public void enleverPanelEdit()
	{
		for(int i=panelEdition.size()-1;i>=0;i--)
		{
			this.remove(panelEdition.get(i));
			panelEdition.remove(i);
		}
		// On r�anime les indicateurs de poids des liens
		this.setAnimeIndicateurDeLiens(true);
	}
	
	// Active/Desactive l'animation d'indicateur de poids des liens d'influence
	public void setAnimeIndicateurDeLiens(boolean active)
	{
		this.animeIndicateurDeLiens=active;
	}
	
	public void creerPanelEdit(PanelModifPoidsLI parent, GCogniton gc, GPlan gp, LienPlan lien)
	{
		// Appelee au clic sur le bouton central
		// Affiche les options concernant le poids du lien
		panelEdition.add(new PanelEditionModifPoidsLI(parent, gc, gp, lien));
		this.add(panelEdition.get(panelEdition.size()-1));
		panelEdition.get(panelEdition.size()-1).actualiserPosition();
		// test
		//panelEdition.get(panelEdition.size()-1).grabFocus();
		//panelEdition.get(panelEdition.size()-1).setFocusable(true);
		// On met les panelModifPoidsLI dessous le panel Edition (dernier plan)
		for(int i=panelModifPoidsLI.size()-1;i>=0;i--)
		{
			this.remove(panelModifPoidsLI.get(i));
			this.add(panelModifPoidsLI.get(i));
		}
		
		this.metreGItemDernierPlan();
		// On enleve les indicateurs de poids des liens
		this.animeIndicateurDeLiens=false;
		
	}
	
	// Methode qui met les gItem (gCogniton et gPlan) au dernier plan, pour pas qu'il genent � l'affichage
	public void metreGItemDernierPlan()
	{
		// On met les gPlan dessous le panel Edition (dernier plan)
		for(int i=gPlan.size()-1;i>=0;i--)
		{
			this.remove(gPlan.get(i));
			this.add(gPlan.get(i));
		}
				
		// On met les gCognitons desous le panel Edition (dernier plan)
		for(int i=gCognitons.size()-1;i>=0;i--)
		{
			this.remove(gCognitons.get(i));
			this.add(gCognitons.get(i));
		}
	}
	}
