package I18n;

import java.util.ListResourceBundle;

public class I18nListRessources_en_EN extends ListResourceBundle{ 
	  public Object[][] getContents() { 
		    return contents; 
		  } 

		  //Tableau des mots cl�s et des valeurs 

		  static final Object[][] contents = { 
			//civkraft
			{"Annuler","Cancel"},
			{"Solo / Editeur","Solo / Editor"},
			{"Solo","Solo"}, 
		    {"Editeur","Editor "},
		    {"Heberger","Host"},
		    {"Heberger une partie","Host the game"},
		    {"Rejoindre une partie", "Join the game"},
		    {"Civkraft : Jeu multijoueur", "Civkraft : Multiplayer game"}, 
		    {"Choix de Langues", "Choose language"},
		    {"Scratch","Scratch"},
		    {"<html>Recherche de Serveurs Civkraft dans le réseau local ...<br>Vous pouvez lancer un serveur en choisissant l'option héberger partie" , "<html>Search Civkraft Servers in the local network ...<br>You can start a server by choosing the host party option"},
		    {"Recherche...", "Search..."},
		    {"Envoi Schéma Cognitif", "Sending Cognitive Scheme"},
		    {"<html>Vous êtes actuellement connecté au serveur civkraft suivant.<br>En attente du lancement de la partie côté serveur...</html>", "<html>You are currently connected to the civkraft server .<br>Pending the launch of the game ...</html>"},
		    {"<html><center>Veuillez sélectionnez le schéma cognitif <br> que vous désirez envoyer au serveur d'adresse <br>","<html><center>Please select the cognitive schema <br> that you want to send to the address server <br>"},
		    {"Serveur", "Server"},
		    {"Serveur démarré avec succès, en attente de joueurs...", "Successfully started the server, waiting for players..."},
		    {"Emplacement Vide","Empty location"},
		    {"Recherche en cours...", "Searching..."},
		    //turtlekit.agr
		    {"Kernel","Kernel"},
			{"Nom","Nam"},
			{"Lancer la partie","Start the game"},
			{"model","model"},
			{"engine", "engine"},
			{"turtles", "turtles"},
			{"scheduler","scheduler"},
			{"viewer","viewer"},
	  		{"launch", "launch" },
	  		//turtlekit.gui
	  		{"Config","Config"},
	  		{"Models","Models"},
	  		{"Configs","Configs"},
	  		{"XML Models","XML Models"},
	  		//civilisation.inspecteur
	  			//FenetreInspecteur.java
	  			{"Agents","Agents"},
	  			{"Options","Options"},
	  			//PanelAgentData.java
	  			{"Bar Chart Demo" , "Bar Chart Demo"},
	  			{"Category", "Category"},
	  			{"Value", "Value"},
	  			//PanelAgentLog.java ------------------------------------------Test
	  			{"tick" , "tick"},
	  			//PanelGroupViewer.java
	  			{"Groups","Groups"},
	  			{"Show in world view","Show in world view"},
	  			//PanelInspecteur.java ------------------------------------------Test
	  			{"Variable","Variable"},
	  			//PanelInventaire.java
	  			{"Inventaire","Inventory"},
	  			{"Objet","Object"},
	  			{"Nombre","Number"},
	  			//PanelListeCognitons.java
	  			{"Cognitons","Cognitons"},
	  			//PanelListePlans.java
	  			{"Plans","Plans"},
	  			{"poids" , "weight"},
	  			//PanelMindData.java
	  			{"Plans weight","Plans weight"},
	  			{"Cogni weight","Cogni weight"},
	  			//PanelOptions.java
	  			{"Affichage des plans : ", "View plans : " },
	  			{"Phero map : ","Phero map : "},
	  			{"Show specific group and role :","Show specific group and role :"},
	  			{"Aucun patch selectionne", "No patch selects"},
	  			{"Attribut","Attribute"},
	  			{"Valeur", "Value"},
	  			//PanelPerformances
	  			{"OS : ","OS : "},
	  			{"Version : ", "Version : "},
	  			{"Architecture : ", "Architecture : "},
	  			{"Total memory: ","Total memory: "},
	  			{"Used memory : ","Used memory : "},
	  		//civilisation.inspecteur.advancedcharts
	  			//AdvSubPanelDisplaySelection.java
	  			{"Civilisations" ,"Civilisations"},
	  			{"choose Civilisations","choose Civilisations"},
	  			//ChartWindow.java
	  			{"Population","Population"},
	  			{"Mind","Mind"},
	  			{"Selection","Selection"},
	  		//civilisation.inspecteur.simulation
	  			//ActionStructureCognitive
	  			{"NewCognitiveScheme","NewCognitiveScheme"},
	  			{"Copy This Cognitive Scheme","Copy This Cognitive Scheme"},
	  			{"Name :" ,"Name :"},
	  			{"Create Cognitive Scheme","Create Cognitive Scheme"},
	  			{"Are you sure you want to delete this cognitive scheme?","Are you sure you want to delete this cognitive scheme?"},
	  			{"DELETE COGNITIVE SCHEME","DELETE COGNITIVE SCHEME"},
	  			//PanelArbreActions.java
	  			{"Action", "Action"},
	  			{"Edit", "Edit"},
	  			{"Add after", "Add after"},
	  			{"Add before", "Add before"},
	  			{"Add internal", "Add internal"},
	  			{"Remove action", "Remove action"},
	  			//PanelModificationSimulation.java
	  			{"Save", "Save"},
	  			{"Make a copy of current save", "Make a copy of current save"},
	  			{"Edit cognitive scheme", "Edit cognitive scheme"},
	  			{"Edit environment","Edit environment"},
	  			{"Edit item","Edit item"},
	  			{"Edit civilizations","Edit civilizations"},
	  			{"Edit attributes","Edit attributes"},
	  			{"Manage group","Manage group"},
	  			{"Edit amenagement","Edit amenagement"},
	  			{"Edit configuration","Edit configuration"},
	  			{"Edit constants","Edit constants"},
	  			{"quitter","exit"},
	  			{"Action tree", "Action tree"},
	  			{"Cognitons and plans", "Cognitons and plans"},
	  			{"Environment","Environment"},
	  			{"Patch type","Patch type"},
	  			{"Group manager","Group manager"},
	  			{"Group tree","Group tree"},
	  			{"create cognitive scheme", "create cognitive scheme"},
	  			{"import cognitive scheme","import cognitive scheme"},
	  			{"delete cognitive scheme","delete cognitive scheme"},
	  			{"Save this environment", "Save this environment"},
	  			{"Load an environment","Load an environment"},
	  			{"Set environment bounds", "Set environment bounds"},
	  			{"Generate an environment from an existing picture", "Generate an environment from an existing picture"},
	  			{"Pen","Pen"},
	  			{"Pinceau","Brush"},
	  			{"Paint bucket","Paint bucket"},
	  			{"Zoom","Zoom"},
	  			{"Dezoom","Dezoom"},
	  			{"Choose environment to use for simulation","Choose environment to use for simulation"},
	  			{"Manage pheromon","Manage pheromon"},
	  			{"Add a new action at the start of the plan","Add a new action at the start of the plan"},
	  			{"Create new item","Create new item"},
	  			{"Create new amenagement","Create new amenagement"},
	  			{"Create new civilization", "Create new civilization"},
	  			//PanelStructureCognitive.java
	  			{"Plan","Plan"},
	  			{"Edit Plan","Edit Plan"},
	  			{"Remove","Remove"},
	  			{"Cogniton","Cogniton"},
	  			{"Edit Cogniton","Edit Cogniton"},
	  			{"Edit influence links","Edit influence links"},
	  			{"Edit conditional links","Edit conditional links"},
	  			{"Edit triggering attributes","Edit triggering attributes"},
	  		//civilisation.inspecteur.simulation.amenagement
	  			//PanelAmenagement.java
	  			{"Facility editor","Facility editor"},
	  			{"Add effect : ","Add effect : "},
	  			{"Add this effect","Add this effect"},
	  			{"Amenagement name : ","Amenagement name : "},
	  			{"Icon :","Icon :"},
	  			{"Description :","Description :"},
	  			{"Enter facility Description here","Enter facility Description here"},
	  			{"Facility color : ","Facility color : "},
	  			{"Change color","Change color"},
	  			{"New effect","New effect"},
	  			{"save amenagement","save amenagement"},
	  			{"Recipe","Recipe"},
	  			//PanelEffectA.java
	  			{"Effect Name : ","Effect Name : "},
	  			{"Target type : ","Target type : "},
	  			{"Attributes","Attributes"},
	  			{"Objets","Objets"},
	  			{"Ressource/tick","Ressource/tick"},
	  			{"Effect Type : ","Effect Type : "},
	  			{"Set","Set"},
	  			{"Add","Add"},
	  			{"Value : ","Value : "},
	  			{"Activation : ","Activation : "},
	  			{"Possession","Possession"},
	  			{"Use","Use"},
	  			{"Permanent ? : ","Permanent ? : "},
	  			{"Yes","Yes"},
	  			{"No","No"},
	  			{"Save Effect","Save Effect"},
	  			{"Remove Effect","Remove Effect"},
	  			{"Ressources/tick","Ressources/tick"},
	  			{"Target Name : ","Target Name : "},
	  			//PanelListeAmenagement.java
	  			{"Amenagement list","Amenagement list"},
	  			//PanelRecetteA.java
	  			{"Add item for craft","Add item for craft"},
	  			{"Save Recipe","Save Recipe"},
	  		//civilisation.inspecteur.simulation.dialogues
	  			//DialogChangeGroupName.java
	  			{"group_name","group_name"},
	  			{"Rename a group","Rename a group"},
	  			//DialogChangeRoleName.java
	  			{"role_name", "role_name"},
	  			{"Rename a role","Rename a role"},
	  			//DialogCreatePheromon.java
	  			{"Attribute Name : ","Attribute Name : "},
	  			{"Start Value : ", "Start Value : " },
	  			{"Growing value (units/tick) : ","Growing value (units/tick) : "},
	  			//DialogCreateRole.Java
	  			{"Create a role","Create a role"},
	  			//DialogEditBounds.java
	  			{"Edit environment bounds","Edit environment bounds"},
	  			//DialogEditPheromon.java
	  			{"Edit pheromons","Edit pheromons"},
	  			{"Constant","Constant"},
	  			{"Edit triggering attributes","Edit triggering attributes"},
	  			//DialogSelectEnvironmentToLoad.java
	  			{"Load environment","Load environment"},
	  			//DialogueAjouterAction.java
	  			{"Add new action","Add new action"},
	  			//DialogueChoisirCouleurAmenagement.java
	  			{"Choisir la couleur", "Choose color"},
	  			//DialogueEditerCogniton.java
	  			{"Cogniton type :","Cogniton type :"},
	  			{"Cogniton name :","Cogniton name :"},
	  			{"Starting cogniton","Starting cogniton"},
	  			{"Give this cogniton to new agents?","Give this cogniton to new agents?"},
	  			{"Starting apparition chance :","Starting apparition chance :"},
	  			{"Cogniton hues : ","Cogniton hues : "},
	  			{"Editer un cogniton","Edit cogniton"},
	  			//DialogueEditerLiensConditionnels.java
	  			{"Conditionned plan :","Conditionned plan :"},
	  			//DialogueEditerPlan.java
	  			{"Auto-Plan","Auto-Plan"},
	  			{"Every agents will run this plan every tick if this box is checked. You could use this features to create automatic cognitons transmissions," ,"Every agents will run this plan every tick if this box is checked. You could use this features to create automatic cognitons transmissions,"},
	  			{" or change attributes (need for food...)"," or change attributes (need for food...)"},
	  			{"Birth-Plan","Birth-Plan"},
	  			{"Every agents will run this plan at birth.","Every agents will run this plan at birth."},
	  			{"Edite plan","Edite plan"},
	  			//DialogueEditerTerrain.java
	  			{"Editer le terrain","Edit field"},
	  			{"Passability :","Passability :"},
	  			{"Impassable?","Impassable?"},
	  			{"Starting value of ","Starting value of "},
	  			{"Starting value : ","Starting value : "},
	  			{"Growth value of ","Growth value of " },
	  			{"Growth value : ","Growth value : "},
	  			//DialogueEnregistrerEnvironnement.java
	  			{"nom","nam"},	  			
	  			{"Enregistrer un environnement","Save an environment"},
	  			//DialoguePlacerCivilisation.java
	  			{"Choisissez une civilisation","Choose a civilization"},
	  			//DialogueSelectionnerEnvironnementActif.java
	  			{"Enregistrer un environnement","Save an environment"},
	  		//civilisation.inspecteur.simulation.environnement
	  			//PanelTerrains.java
	  			{"Terrain","Ground"},
	  			{"Edit terrain", "Edit ground " },
	  			{"Change colour","Change colour"},
	  		//civilisation.inspecteur.simulation.groupManager
	  			//GroupToolBar.java
	  			{"Add an existing culturon","Add an existing culturon"},
	  			{"Create a new culturon","Create a new culturon"},
	  			{"Remove currently selected role (can't remove the last role)","Remove currently selected role (can't remove the last role)"},
	  			{"Add a new role to this group","Add a new role to this group"},
	  			{"Rename the current role","Rename the current role"},
	  			{"Rename the current group","Rename the current group"},
	  		//civilisation.inspecteur.simulation.objets
	  			//PanelEffect.java
	  			{"Actions","Actions"},
	  			//PanelListeObjets.java
	  			{"Item list","Item list"},
	  			//PanelObjets.java
	  			{"Item editor","Item editor"},
	  			{"Item name : ", "Item name : "},
	  			{"Enter item Description here","Enter item Description here"},
	  			{"save object","save object"},
	  		//civilisation.inspecteur.viewer
	  			//ViewerTabbed.java
	  			{"Simulation","Simulation"},
	  			{"Agent","Agent"},
	  			{"Options","Options"},
	  			{"Performances","Performances"},
	  			{"Tableau de bord","Dashboard"},
	  			{"Charts","Charters"},
	  			{"Legend","Legend"}, 	
	  		//java.awt.event.KeyEvent
	  			//TKMenujava
	  			{"TurtleKit","TurtleKit"},
		  }; 
		
}
