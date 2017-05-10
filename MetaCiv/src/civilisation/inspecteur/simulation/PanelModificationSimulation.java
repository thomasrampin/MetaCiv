package civilisation.inspecteur.simulation;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import I18n.I18nList;

import civilisation.Civilisation;
import civilisation.Configuration;
import civilisation.SchemaCognitif;
import civilisation.individu.decisionMaking.DecisionMaker;
import civilisation.individu.plan.NPlan;
import civilisation.inspecteur.simulation.amenagement.ActionsToolBarListeAmenagement;
import civilisation.inspecteur.simulation.amenagement.PanelAmenagement;
import civilisation.inspecteur.simulation.amenagement.PanelListeAmenagement;
import civilisation.inspecteur.simulation.attributes.PanelAttributes;
import civilisation.inspecteur.simulation.civilisations.ActionsToolBarListeCivilisations;
import civilisation.inspecteur.simulation.civilisations.PanelCivilisations;
import civilisation.inspecteur.simulation.civilisations.PanelListeCivilisations;
import civilisation.inspecteur.simulation.configuration.PanelConfiguration;

import civilisation.inspecteur.simulation.constants.PanelConstants;
import civilisation.inspecteur.simulation.environnement.ActionsToolBarEnvironnement;
import civilisation.inspecteur.simulation.environnement.ActionsToolBarTerrains;
import civilisation.inspecteur.simulation.environnement.PanelEnvironnement;
import civilisation.inspecteur.simulation.environnement.PanelTerrains;
import civilisation.inspecteur.simulation.groupManager.GroupToolBar;
import civilisation.inspecteur.simulation.groupManager.GroupTreeToolBar;
import civilisation.inspecteur.simulation.groupManager.PanelGroupManager;
import civilisation.inspecteur.simulation.groupManager.PanelGroupTree;
import civilisation.inspecteur.simulation.objets.ActionsToolBarListeObjets;
import civilisation.inspecteur.simulation.objets.PanelListeObjets;
import civilisation.inspecteur.simulation.objets.PanelObjets;

public class PanelModificationSimulation extends JPanel{

	JToolBar toolBar;
	
	JButton boutonSauvegarder;
	JButton boutonArchiver;
	JButton boutonStructureCognitive;
	JButton boutonEnvironnement;
	JButton boutonObjets;
	JButton boutonCivilisations;
	JButton boutonAttribute;
	JButton boutonGroupManager;
	JButton boutonAmenagement;
	JButton boutonConfiguration;
	JButton boutonConstants;

	PanelStructureCognitive panelStructureCognitive;
	PanelEnvironnement panelEnvironnement;
	PanelArbreActions panelArbreActions;
	PanelTerrains panelTerrains;
	PanelObjets panelObjets;
	PanelListeObjets panelListeObjets;
	PanelListeAmenagement panelListeAmenagement;
	PanelCivilisations panelCivilisations;
	PanelListeCivilisations panelListeCivilisations;
	PanelAttributes panelAttributes;

	JScrollPane scrollAttributes;
	PanelConstants panelConstants;
	JScrollPane scrollConstants;

	PanelGroupTree panelGroupTree;
	PanelGroupManager panelGroupManager;
	PanelAmenagement panelAmenagement;
	PanelConfiguration panelConfiguration;
	
	JPanel panelCentral;
	JPanel panelEast;
	JSplitPane splitPane;

	JToolBar toolBarStructureCognitive;
	JButton boutonAjouterCogniton;
	JButton boutonAjouterPlan;
	JComboBox<DecisionMaker> selectDecisionMaker;

	JToolBar toolBarArbreActions;
	JButton ajouterAction;
	JButton ajouterSousAction;
	
	JToolBar toolBarSchemaCognitif;
	JButton boutonCreerSchemaCognitif;
	JComboBox<SchemaCognitif> selectSchemaCognitif;
	JButton boutonImporterSchemaCognitif;
	JButton boutonOptionsSchemaCognitif;
	JButton boutonSupprimerSchemaCognitif;
	
	JToolBar toolBarEnvironnement;
	JButton enregistrerEnvironnement;
	JButton limitesEnvironnement;
	JButton chargerEnvironnement;
	JButton genererEnvironnement;
	JButton crayon;
	JButton Pinceau;
	JButton potPeinture;
	JButton zoomer;
	JButton dezoomer;
	JButton choisirEnvironnementActif;
	JButton pheromone;
	JButton setting3D; 
	
	GroupToolBar toolBarGroupManager;
	
	GroupTreeToolBar toolBarGroupTree;

	JToolBar toolBarCivilisations;
	
	JToolBar toolBarListeCivilisations;
	JButton createCivilization;

	
	JToolBar toolBarListeTerrains;
	JButton ajouterTerrain;
	
	JToolBar toolBarListeObjets;
	JButton createItem;
	
	JToolBar toolBarListeAmenagement;
	JButton createAmenagement;
	
	JToolBar toolBarObjets;
	
	JToolBar toolBarAmenagement;
	
	JToolBar toolBarConfiguration;
	
	int agentID = 0;
	
	
	
	public PanelModificationSimulation()
	{
		super();
		
		this.setLayout(new BorderLayout());
		
		/*Create main toolBar*/
		toolBar = new JToolBar(SwingConstants.VERTICAL);
		
		ImageIcon iconeSauvegarder = Configuration.getIcon("disk-black.png");	
		boutonSauvegarder = new JButton(iconeSauvegarder);

		boutonSauvegarder.setToolTipText(I18nList.CheckLang("Save"));

		boutonSauvegarder.addActionListener(new ActionPanelCognitonsGraphiques(this, 0));
		toolBar.add(boutonSauvegarder);
		
		ImageIcon iconeArchiver = Configuration.getIcon("disks-black.png");	
		boutonArchiver = new JButton(iconeArchiver);

		boutonArchiver.setToolTipText(I18nList.CheckLang("Make a copy of current save"));

		boutonArchiver.addActionListener(new ActionPanelCognitonsGraphiques(this, 1));
		toolBar.add(boutonArchiver);
		
		toolBar.addSeparator();

		ImageIcon iconeStructureCognitive = Configuration.getIcon("brain.png");	
		boutonStructureCognitive = new JButton(iconeStructureCognitive);

		boutonStructureCognitive.setToolTipText(I18nList.CheckLang("Edit cognitive scheme"));

		boutonStructureCognitive.addActionListener(new ActionPanelCognitonsGraphiques(this, 2));
		toolBar.add(boutonStructureCognitive);

		ImageIcon iconeEnvironnement = Configuration.getIcon("globe.png");	
		boutonEnvironnement = new JButton(iconeEnvironnement);

		boutonEnvironnement.setToolTipText(I18nList.CheckLang("Edit environment"));

		boutonEnvironnement.addActionListener(new ActionPanelCognitonsGraphiques(this, 3));
		toolBar.add(boutonEnvironnement);
		
		ImageIcon iconeObjets = Configuration.getIcon("briefcase.png");	
		boutonObjets = new JButton(iconeObjets);

		boutonObjets.setToolTipText(I18nList.CheckLang("Edit item"));

		boutonObjets.addActionListener(new ActionPanelCognitonsGraphiques(this, 4));
		toolBar.add(boutonObjets);
		
		ImageIcon iconeCivilisations = Configuration.getIcon("bank.png");	
		boutonCivilisations = new JButton(iconeCivilisations);

		boutonCivilisations.setToolTipText(I18nList.CheckLang("Edit civilizations"));

		boutonCivilisations.addActionListener(new ActionPanelCognitonsGraphiques(this, 5));
		toolBar.add(boutonCivilisations);
		
		ImageIcon iconeAttributes = Configuration.getIcon("blue-document-attribute.png");	
		boutonAttribute = new JButton(iconeAttributes);

		boutonAttribute.setToolTipText(I18nList.CheckLang("Edit attributes"));

		boutonAttribute.addActionListener(new ActionPanelCognitonsGraphiques(this, 6));
		toolBar.add(boutonAttribute);
		
		ImageIcon iconeGroupManager = Configuration.getIcon("foaf.png");	
		boutonGroupManager = new JButton(iconeGroupManager);

		boutonGroupManager.setToolTipText(I18nList.CheckLang("Manage group"));

		boutonGroupManager.addActionListener(new ActionPanelCognitonsGraphiques(this, 7));
		toolBar.add(boutonGroupManager);
		
		ImageIcon iconeAmenagement = Configuration.getIcon("target.png");	
		boutonAmenagement = new JButton(iconeAmenagement);

		boutonAmenagement.setToolTipText(I18nList.CheckLang("Edit amenagement"));

		boutonAmenagement.addActionListener(new ActionPanelCognitonsGraphiques(this, 8));
		toolBar.add(boutonAmenagement);
		
		ImageIcon iconeConfiguration = Configuration.getIcon("compass.png");	
		boutonConfiguration = new JButton(iconeConfiguration);

		boutonConfiguration.setToolTipText(I18nList.CheckLang("Edit configuration"));

		boutonConfiguration.addActionListener(new ActionPanelCognitonsGraphiques(this, 9));
		toolBar.add(boutonConfiguration);
		
		ImageIcon iconeConstants = Configuration.getIcon("marker.png");	
		boutonConstants = new JButton(iconeConstants);

		boutonConstants.setToolTipText(I18nList.CheckLang("Edit constants"));
		boutonConstants.addActionListener(new ActionPanelCognitonsGraphiques(this, 10));
		toolBar.add(boutonConstants);

		/*// tenter de quitter proprement
		
		ImageIcon iconeQuitter = Configuration.getIcon("prohibition.png");	
		boutonQuitter = new JButton(iconeQuitter);

		boutonQuitter.setToolTipText(I18nList.CheckLang("quitter"));
		boutonQuitter.addActionListener(new ActionPanelCognitonsGraphiques(this, 11));

		toolBar.add(boutonQuitter);*/
		
		//

		panelArbreActions = new PanelArbreActions(null);
		panelStructureCognitive = new PanelStructureCognitive(this);
		panelEnvironnement = new PanelEnvironnement(this);
		panelGroupManager = new PanelGroupManager(this , null);
		panelGroupTree = new PanelGroupTree(panelGroupManager);

		panelTerrains = new PanelTerrains();
		panelObjets = new PanelObjets(this , panelListeObjets);
		panelListeObjets = new PanelListeObjets(panelObjets);
		panelObjets.setPanelListeObjets(panelListeObjets);
		
		panelAmenagement = new PanelAmenagement(this , panelListeAmenagement);
		panelListeAmenagement = new PanelListeAmenagement(panelAmenagement);
		panelAmenagement.setPanelListeAmenagement(panelListeAmenagement);
		
		panelConfiguration = new PanelConfiguration(this);
		
		panelCivilisations = new PanelCivilisations(this , panelListeCivilisations);
		panelListeCivilisations = new PanelListeCivilisations(panelCivilisations);
		panelCivilisations.setPanelListeCivilisations(panelListeCivilisations);
		panelAttributes = new PanelAttributes(this);

		panelConstants = new PanelConstants(this);

		TitledBorder bordure = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), I18nList.CheckLang("Action tree"));
		bordure.setTitleJustification(TitledBorder.LEFT);
		panelArbreActions.setBorder(bordure);
			
		bordure = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), I18nList.CheckLang("Cognitons and plans"));
		bordure.setTitleJustification(TitledBorder.LEFT);
		panelStructureCognitive.setBorder(bordure);
		
		bordure = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), I18nList.CheckLang("Environment"));
		bordure.setTitleJustification(TitledBorder.LEFT);
		panelEnvironnement.setBorder(bordure);
		
		bordure = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), I18nList.CheckLang("Patch type"));
		bordure.setTitleJustification(TitledBorder.LEFT);
		panelTerrains.setBorder(bordure);
		
		bordure = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), I18nList.CheckLang("Group manager"));
		bordure.setTitleJustification(TitledBorder.LEFT);
		panelGroupManager.setBorder(bordure);
		 
		bordure = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), I18nList.CheckLang("Group tree"));

		bordure.setTitleJustification(TitledBorder.LEFT);
		panelGroupTree.setBorder(bordure);

		/*Creation de la toolBar pour la structure cognitive*/
		toolBarStructureCognitive = new JToolBar();
		
		ImageIcon iconeAjouterCogniton = Configuration.getIcon("brain--plus.png");
		boutonAjouterCogniton = new JButton(iconeAjouterCogniton);
		boutonAjouterCogniton.addActionListener(new ActionStructureCognitive(this,0));
		toolBarStructureCognitive.add(boutonAjouterCogniton);
		
		ImageIcon iconeAjouterPlan = Configuration.getIcon("hammer--plus.png");
		boutonAjouterPlan = new JButton(iconeAjouterPlan);
		boutonAjouterPlan.addActionListener(new ActionStructureCognitive(this,1));
		toolBarStructureCognitive.add(boutonAjouterPlan);
		
		this.selectDecisionMaker = new JComboBox<DecisionMaker>((DecisionMaker[]) Configuration.allDecisionMakers.toArray(new DecisionMaker[0]));
		selectDecisionMaker.setSelectedItem(Configuration.decisionMaker);
		toolBarStructureCognitive.add(selectDecisionMaker);
		
		/*Creation de la toolBar pour le choix du schéma cognitif*/
		
		toolBarSchemaCognitif = new JToolBar();
		
		ImageIcon iconeCreerSchemaCognitif = Configuration.getIcon("plus.png");
		boutonCreerSchemaCognitif = new JButton(iconeCreerSchemaCognitif);
		boutonCreerSchemaCognitif.addActionListener(new ActionStructureCognitive(this,4));

		boutonCreerSchemaCognitif.setToolTipText(I18nList.CheckLang("create cognitive scheme"));

		toolBarSchemaCognitif.add(boutonCreerSchemaCognitif);
		
		
		ImageIcon iconeImporterSchemaCognitif = Configuration.getIcon("disk--arrow.png");
		boutonImporterSchemaCognitif = new JButton(iconeImporterSchemaCognitif);
		boutonImporterSchemaCognitif.addActionListener(new ActionStructureCognitive(this,5));

		boutonImporterSchemaCognitif.setToolTipText(I18nList.CheckLang("import cognitive scheme"));

		toolBarSchemaCognitif.add(boutonImporterSchemaCognitif);

		ImageIcon iconeOptionsSchemaCognitif = Configuration.getIcon("compass.png");
		boutonOptionsSchemaCognitif = new JButton(iconeOptionsSchemaCognitif);
		boutonOptionsSchemaCognitif.addActionListener(new ActionStructureCognitive(this,8));
		boutonOptionsSchemaCognitif.setToolTipText("cognitive scheme options");
		toolBarSchemaCognitif.add(boutonOptionsSchemaCognitif);

		ImageIcon iconeSupprimerSchemaCognitif = Configuration.getIcon("minus.png");
		boutonSupprimerSchemaCognitif = new JButton(iconeSupprimerSchemaCognitif);
		boutonSupprimerSchemaCognitif.addActionListener(new ActionStructureCognitive(this,6));

		boutonSupprimerSchemaCognitif.setToolTipText(I18nList.CheckLang("delete cognitive scheme"));

		toolBarSchemaCognitif.add(boutonSupprimerSchemaCognitif);
			
		this.selectSchemaCognitif = new JComboBox<SchemaCognitif>((SchemaCognitif[])Configuration.SchemasCognitifs.toArray(new SchemaCognitif[Configuration.SchemasCognitifs.size()]));
		selectSchemaCognitif.setSelectedItem(Configuration.getSchemaCognitifEnCourEdition());
		selectSchemaCognitif.addActionListener(new ActionStructureCognitive(this,7));
		toolBarSchemaCognitif.add(selectSchemaCognitif);
		
		/*Creation de la toolBar pour l'environnement*/
		toolBarEnvironnement = new JToolBar();
		
		ImageIcon iconeEnregistrerEnvironnement = Configuration.getIcon("disk.png");
		enregistrerEnvironnement = new JButton(iconeEnregistrerEnvironnement);
		enregistrerEnvironnement.addActionListener(new ActionsToolBarEnvironnement(panelEnvironnement,0));

		enregistrerEnvironnement.setToolTipText(I18nList.CheckLang("Save this environment"));

		toolBarEnvironnement.add(enregistrerEnvironnement);
		
		ImageIcon iconeChargerEnvironnement = Configuration.getIcon("disk--arrow.png");
		chargerEnvironnement = new JButton(iconeChargerEnvironnement);
		chargerEnvironnement.addActionListener(new ActionsToolBarEnvironnement(panelEnvironnement,1));

		chargerEnvironnement.setToolTipText(I18nList.CheckLang("Load an environment"));

		toolBarEnvironnement.add(chargerEnvironnement);
		
		ImageIcon iconeLimitesEnvironnement = Configuration.getIcon("compass.png");
		limitesEnvironnement = new JButton(iconeLimitesEnvironnement);
		limitesEnvironnement.addActionListener(new ActionsToolBarEnvironnement(panelEnvironnement,2));

		limitesEnvironnement.setToolTipText(I18nList.CheckLang("Set environment bounds"));

		toolBarEnvironnement.add(limitesEnvironnement);
		
		ImageIcon iconeGenererEnvironnement = Configuration.getIcon("picture--arrow.png");
		genererEnvironnement = new JButton(iconeGenererEnvironnement);
		genererEnvironnement.addActionListener(new ActionsToolBarEnvironnement(panelEnvironnement,3));

		genererEnvironnement.setToolTipText(I18nList.CheckLang("Generate an environment from an existing picture"));

		toolBarEnvironnement.add(genererEnvironnement);
		
		toolBarEnvironnement.addSeparator();

		ImageIcon iconeCrayon = Configuration.getIcon("pencil.png");
		crayon = new JButton(iconeCrayon);
		crayon.addActionListener(new ActionsToolBarEnvironnement(panelEnvironnement,4));

		crayon.setToolTipText(I18nList.CheckLang("Pen"));

		toolBarEnvironnement.add(crayon);
		
		ImageIcon iconePinceau = Configuration.getIcon("pinceau.png");
		Pinceau = new JButton(iconePinceau);
		Pinceau.addActionListener(new ActionsToolBarEnvironnement(panelEnvironnement,11));

		Pinceau.setToolTipText(I18nList.CheckLang("Pinceau"));

		toolBarEnvironnement.add(Pinceau);
		
		ImageIcon iconePotPeinture = Configuration.getIcon("paint-can.png");
		potPeinture = new JButton(iconePotPeinture);
		potPeinture.addActionListener(new ActionsToolBarEnvironnement(panelEnvironnement,5));

		potPeinture.setToolTipText(I18nList.CheckLang("Paint bucket"));

		toolBarEnvironnement.add(potPeinture);
		
		toolBarEnvironnement.addSeparator();

		ImageIcon iconeZoomer = Configuration.getIcon("plus.png");
		zoomer = new JButton(iconeZoomer);
		zoomer.addActionListener(new ActionsToolBarEnvironnement(panelEnvironnement,6));

		zoomer.setToolTipText(I18nList.CheckLang("Zoom"));

		toolBarEnvironnement.add(zoomer);
		
		ImageIcon iconeDezoomer = Configuration.getIcon("minus.png");
		dezoomer = new JButton(iconeDezoomer);
		dezoomer.addActionListener(new ActionsToolBarEnvironnement(panelEnvironnement,7));

		dezoomer.setToolTipText(I18nList.CheckLang("Dezoom"));

		toolBarEnvironnement.add(dezoomer);
		
		toolBarEnvironnement.addSeparator();

		ImageIcon iconeChoisirEnv = Configuration.getIcon("ui-color-picker-switch.png");
		choisirEnvironnementActif = new JButton(iconeChoisirEnv);
		choisirEnvironnementActif.addActionListener(new ActionsToolBarEnvironnement(panelEnvironnement,8));

		choisirEnvironnementActif.setToolTipText(I18nList.CheckLang("Choose environment to use for simulation"));

		toolBarEnvironnement.add(choisirEnvironnementActif);

		ImageIcon iconePheromone = Configuration.getIcon("marker.png");
		pheromone = new JButton(iconePheromone);
		pheromone.addActionListener(new ActionsToolBarEnvironnement(panelEnvironnement,9));

		pheromone.setToolTipText(I18nList.CheckLang("Manage pheromon"));

		toolBarEnvironnement.add(pheromone);
		
		toolBarEnvironnement.addSeparator();
		
		ImageIcon iconeSetting3D = Configuration.getIcon("setting3D.png");
		setting3D = new JButton(iconeSetting3D);
		setting3D.addActionListener(new ActionsToolBarEnvironnement(panelEnvironnement,10));

		setting3D.setToolTipText(I18nList.CheckLang("Setting 3D"));

		toolBarEnvironnement.add(setting3D);
		
		/*Creation de la toolBar pour l'arbre d'actions*/	
		ImageIcon icone = Configuration.getIcon("lightning--plus.png");
		ajouterAction = new JButton(icone);

		ajouterAction.setToolTipText(I18nList.CheckLang("Add a new action at the start of the plan"));

		ajouterAction.addActionListener(new ActionsToolBarArbreActions(panelArbreActions , 0));

		toolBarArbreActions = new JToolBar();
		toolBarArbreActions.add(ajouterAction);
		
		
		
		/*Creation de la toolBar pour la liste des terrains*/	
		toolBarListeTerrains = new JToolBar();

		icone = Configuration.getIcon("tree--plus.png");
		ajouterTerrain = new JButton(icone);
		ajouterTerrain.addActionListener(new ActionsToolBarTerrains(panelTerrains,0));
		toolBarListeTerrains.add(ajouterTerrain);

		/*Creation de la toolBar pour la liste des objets*/	
		toolBarListeObjets = new JToolBar();
		
		icone = Configuration.getIcon("ui-color-picker-switch.png");
		createItem = new JButton(icone);
		createItem.addActionListener(new ActionsToolBarListeObjets(panelListeObjets,0));

		createItem.setToolTipText(I18nList.CheckLang("Create new item"));

		toolBarListeObjets.add(createItem);
		
		/*Creation de la toolBar pour le panel des objets*/	
		toolBarObjets = new JToolBar();
		
		/*Creation de la toolBar pour la liste des amenagements*/	
		toolBarListeAmenagement = new JToolBar();
		
		icone = Configuration.getIcon("ui-color-picker-switch.png");
		createAmenagement = new JButton(icone);
		createAmenagement.addActionListener(new ActionsToolBarListeAmenagement(panelListeAmenagement,0));

		createAmenagement.setToolTipText(I18nList.CheckLang("Create new amenagement"));

		toolBarListeAmenagement.add(createAmenagement);
		
		/*Creation de la toolBar pour le panel des amenagement*/	
		toolBarAmenagement = new JToolBar();
		
		/*Creation toolBar pour le panel de configuration*/
		toolBarConfiguration = new JToolBar();
		
		/*ToolBar for group manager*/	
		toolBarGroupManager = new GroupToolBar(panelGroupManager);
		toolBarGroupTree = new GroupTreeToolBar(panelGroupTree);
		panelGroupManager.setToolBar(toolBarGroupManager);

		/*Creation de la toolBar pour la liste des civilisations*/	
		toolBarListeCivilisations = new JToolBar();
		
		icone = Configuration.getIcon("bank.png");
		createCivilization = new JButton(icone);
		createCivilization.addActionListener(new ActionsToolBarListeCivilisations(panelListeCivilisations,0));

		createCivilization.setToolTipText(I18nList.CheckLang("Create new civilization"));

		toolBarListeCivilisations.add(createCivilization);
		
		/*Creation de la toolBar pour le panel des civilisations*/	
		toolBarCivilisations = new JToolBar();
		
		this.add(toolBar, BorderLayout.WEST);
		afficherStructureCognitive();


		
	}
	
	public void afficherEnvironnement(){
		
		setPanelButtonAvailable();
		this.boutonEnvironnement.setEnabled(false);

		clearPanel();
		
		panelCentral = new JPanel();
		panelCentral.setLayout(new BorderLayout());
		
		panelCentral.add(new JScrollPane(panelEnvironnement,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.CENTER);
		panelCentral.add(toolBarEnvironnement, BorderLayout.NORTH);

		panelEast = new JPanel();
		panelEast.setLayout(new BorderLayout());
		
		panelEast.add(panelTerrains, BorderLayout.CENTER);
		panelEast.add(toolBarListeTerrains, BorderLayout.NORTH);
		
		this.add(panelCentral, BorderLayout.CENTER);
		this.add(panelEast, BorderLayout.EAST);
		
	}
	
	
	public void afficherStructureCognitive(){
		
		setPanelButtonAvailable();
		this.boutonStructureCognitive.setEnabled(false);
		
		clearPanel();
		
		panelCentral = new JPanel();
		panelCentral.setLayout(new BorderLayout());
		
		JPanel panelNord = new JPanel();
		panelNord.setLayout(new BoxLayout(panelNord,BoxLayout.PAGE_AXIS));

		panelNord.add(toolBarSchemaCognitif);
		panelNord.add(toolBarStructureCognitive);		
 
		panelCentral.add(panelNord,BorderLayout.NORTH);

		panelCentral.add(new JScrollPane(panelStructureCognitive,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.CENTER);		

		panelEast = new JPanel();
		panelEast.setLayout(new BorderLayout());
		
		panelEast.add(panelArbreActions, BorderLayout.CENTER);
		panelEast.add(toolBarArbreActions, BorderLayout.NORTH);
		
		//panelCentral.setMinimumSize(new Dimension(0,0));
		//panelEast.setMinimumSize(new Dimension(0,0));
		panelEast.setMinimumSize(new Dimension(300,300));
		/////////////////MARCHE PA !!!!
		
		
		//splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelCentral, panelEast);
		this.add(panelCentral, BorderLayout.CENTER);
		this.add(panelEast, BorderLayout.EAST);

		
	}
	

	public void afficherObjets() {
		
		setPanelButtonAvailable();
		this.boutonObjets.setEnabled(false);
		clearPanel();
		
		panelCentral = new JPanel();
		panelCentral.setLayout(new BorderLayout());
		
		panelCentral.add(new JScrollPane(panelObjets,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.CENTER);		
		panelCentral.add(toolBarObjets, BorderLayout.NORTH);

		panelEast = new JPanel();
		panelEast.setLayout(new BorderLayout());
		
		panelEast.add(panelListeObjets, BorderLayout.CENTER);
		panelEast.add(toolBarListeObjets, BorderLayout.NORTH);
		
		this.add(panelCentral, BorderLayout.CENTER);
		this.add(panelEast, BorderLayout.EAST);	
	}
	
	public void afficherAmenagement() {
		
		setPanelButtonAvailable();
		this.boutonAmenagement.setEnabled(false);
		clearPanel();
		
		panelCentral = new JPanel();
		panelCentral.setLayout(new BorderLayout());
		
		panelCentral.add(new JScrollPane(panelAmenagement,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.CENTER);		
		panelCentral.add(toolBarAmenagement, BorderLayout.NORTH);

		panelEast = new JPanel();
		panelEast.setLayout(new BorderLayout());
		panelEast.add(panelListeAmenagement, BorderLayout.CENTER);
		panelEast.add(toolBarListeAmenagement, BorderLayout.NORTH);
		
		this.add(panelCentral, BorderLayout.CENTER);
		this.add(panelEast, BorderLayout.EAST);	
	}
	
	public void afficherConfiguration() {
		// TODO Auto-generated method stub
		setPanelButtonAvailable();
		this.boutonConfiguration.setEnabled(false);
		clearPanel();
		
		panelCentral = new JPanel();
		panelCentral.setLayout(new BorderLayout());
		
		panelCentral.add(new JScrollPane(panelConfiguration,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.CENTER);		
		panelCentral.add(toolBarAmenagement, BorderLayout.NORTH);

		panelEast = new JPanel();
		panelEast.setLayout(new BorderLayout());
		

		
		this.add(panelCentral, BorderLayout.CENTER);
		this.add(panelEast, BorderLayout.EAST);	
	}

	
	
	public void afficherCivilisations() {
		
		setPanelButtonAvailable();
		this.boutonCivilisations.setEnabled(false);
		clearPanel();
		
		panelCentral = new JPanel();
		panelCentral.setLayout(new BorderLayout());
		
		panelCentral.add(panelCivilisations, BorderLayout.CENTER);
		panelCentral.add(toolBarCivilisations, BorderLayout.NORTH);

		panelEast = new JPanel();
		panelEast.setLayout(new BorderLayout());
		
		panelEast.add(panelListeCivilisations, BorderLayout.CENTER);
		panelEast.add(toolBarListeCivilisations, BorderLayout.NORTH);
		
		this.add(panelCentral, BorderLayout.CENTER);
		this.add(panelEast, BorderLayout.EAST);	
	}

public void afficherAttributes() {

		setPanelButtonAvailable();
		this.boutonAttribute.setEnabled(false);

		clearPanel();
		panelCentral = new JPanel();
		panelCentral.setLayout(new BorderLayout());
		
		panelCentral.add(toolBarSchemaCognitif,BorderLayout.NORTH);

		panelCentral.add(new JScrollPane(panelAttributes), BorderLayout.CENTER);



		this.add(panelCentral, BorderLayout.CENTER);
	}


	public void afficherConstantes() {
	
	setPanelButtonAvailable();
	this.boutonConstants.setEnabled(false);

	clearPanel();
	panelCentral = new JPanel();
	panelCentral.setLayout(new BorderLayout());
	
	panelCentral.add(toolBarSchemaCognitif,BorderLayout.NORTH);
	panelCentral.add(new JScrollPane(panelConstants), BorderLayout.CENTER);


	this.add(panelCentral, BorderLayout.CENTER);
}
	
	private void setPanelButtonAvailable(){
		this.boutonStructureCognitive.setEnabled(true);
		this.boutonObjets.setEnabled(true);
		this.boutonEnvironnement.setEnabled(true);
		this.boutonCivilisations.setEnabled(true);
		this.boutonAttribute.setEnabled(true);
		this.boutonConstants.setEnabled(true);
		this.boutonGroupManager.setEnabled(true);
		this.boutonAmenagement.setEnabled(true);
		this.boutonConfiguration.setEnabled(true);
	}
	
	public void changerArbreActions(NPlan plan){
		System.out.println("change to "+plan.getNom());
		panelArbreActions.changePlan(plan);

		TitledBorder bordure = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), plan.getNom());
		bordure.setTitleJustification(TitledBorder.LEFT);
		panelArbreActions.setBorder(bordure);

	}

	public PanelStructureCognitive getPanelStructureCognitive() {
		return panelStructureCognitive;
	}

	public PanelTerrains getPanelTerrains() {
		return panelTerrains;
	}

	public void setOutilDeDessinEnvironnement(int i) {
		if (i == 0){
			crayon.setEnabled(false);
			Pinceau.setEnabled(false);
			potPeinture.setEnabled(true);
		}
		else if (i == 1){
			crayon.setEnabled(true);
			potPeinture.setEnabled(false);
			Pinceau.setEnabled(false);
		}
		else
		{
			if(i == 2)
			{
				crayon.setEnabled(false);
				potPeinture.setEnabled(false);
				Pinceau.setEnabled(true);
			}
		}
	}

	public PanelListeObjets getPanelListeObjets() {
		return panelListeObjets;
	}

	public void showGroupManager() {
		//TODO
		setPanelButtonAvailable();
		this.boutonGroupManager.setEnabled(false);

		clearPanel();

		panelCentral = new JPanel();
		panelCentral.setLayout(new BorderLayout());
		
		JPanel panelNord = new JPanel();
		panelNord.setLayout(new BoxLayout(panelNord,BoxLayout.PAGE_AXIS));

		panelNord.add(toolBarSchemaCognitif);
		panelNord.add(toolBarGroupManager);		
 
		panelCentral.add(panelNord,BorderLayout.NORTH);

		
		panelCentral.add(new JScrollPane(panelGroupManager,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.CENTER);		

		panelEast = new JPanel();
		panelEast.setLayout(new BorderLayout());
		
		panelEast.add(panelGroupTree, BorderLayout.CENTER);
		panelEast.add(toolBarGroupTree, BorderLayout.NORTH);
		
		this.add(panelCentral, BorderLayout.CENTER);
		this.add(panelEast, BorderLayout.EAST);			
	}
	
	public String getSelectedDecisionMaker() {
		return selectDecisionMaker.getSelectedItem().toString();
	}
	
	private void clearPanel() {
		if (panelCentral != null){
			this.remove(panelCentral);
		}
		if (panelEast != null){
			this.remove(panelEast);
		}

	}
	public int tailleSchemaCognitif(){
		return selectSchemaCognitif.getItemCount();
	}

	public void refreshSelectSchemaCognitif(){
		//toolBarSchemaCognitif.remove(selectSchemaCognitif);
		selectSchemaCognitif.addItem(Configuration.SchemasCognitifs.get(Configuration.SchemasCognitifs.size()-1));
		//selectSchemaCognitif = new JComboBox<SchemaCognitif>((SchemaCognitif[])Configuration.SchemasCognitifs.toArray(new SchemaCognitif[Configuration.SchemasCognitifs.size()]));
		//selectSchemaCognitif.setSelectedItem(Configuration.getSchemaCognitifEnCourEdition());
		//selectSchemaCognitif.addActionListener(new ActionStructureCognitive(this,7));	
		//toolBarSchemaCognitif.add(selectSchemaCognitif);
		toolBarSchemaCognitif.revalidate();
	}
	

}
