package I18n;

import java.util.ListResourceBundle;

public class I18nListRessources_fr_FR extends ListResourceBundle { 
	  public Object[][] getContents() { 
		    return contents; 
		  } 

		  //tableau des mots cl�s et des valeurs 

		  static final Object[][] contents = {
			  {"Snow","Neige"},
			  {"Tiling","Repetition"},
			  {"Road Tiling : ","Route Repetition : "},
			  {"Cliff Tiling : ","Falaise Repetition : "},
			  {"Snow Height : ","Hauteur de neige : "},
			  {"Snow Distance Attenuation : ","Distance d'attenuation de la neige : "},
              {"Snow Density : ","Densite de la neige : "},
			  {"Setting 3D","Parametre 3D"},
			  {"Tiling : ","Repetition : "},
			  {"Textures Folder : ","Dossier des textures : "},
			  {"Height : ","Hauteur : "},
			  {"Erosion : ","Erosion : "},
			  {"Annuler","Annuler"},
			  {"Solo 3D/ Editeur","Solo 3D/ Editeur"},
			  {"Solo / Editeur","Solo / Editeur"},
			  {"Solo", "Solo"}, 
			  {"Editeur", "Editeur"},
			  {"Heberger une partie", "Heberger une partie"},
			  {"Rejoindre une partie", "Rejoindre une partie"},
			  {"Civkraft : Jeu multijoueur", "Civkraft : Jeu multijoueur"}, 
			  {"Choix de Langues", "Choix de Langues"},
			  {"Scratch" , "Depuis zero" },
			  {"<html>Recherche de Serveurs Civkraft dans le réseau local ...<br>Vous pouvez lancer un serveur en choisissant l'option héberger partie" , "<html>Recherche de Serveurs Civkraft dans le réseau local ...<br>Vous pouvez lancer un serveur en choisissant l'option héberger partie"}, 
			  {"Recherche...", "Recherche..."},
			  {"Envoi Schéma Cognitif", "Envoi Schéma Cognitif"},
			  {"<html>Vous êtes actuellement connecté au serveur civkraft suivant.<br>En attente du lancement de la partie côté serveur...</html>", "<html>Vous êtes actuellement connecté au serveur civkraft suivant.<br>En attente du lancement de la partie côté serveur...</html>"},
			  {"<html><center>Veuillez sélectionnez le schéma cognitif <br> que vous désirez envoyer au serveur d'adresse <br>","<html><center>Veuillez sélectionnez le schéma cognitif <br> que vous désirez envoyer au serveur d'adresse <br>"},
			  {"Serveur", "Serveur"},
			  {"Serveur démarré avec succès, en attente de joueurs...", "Serveur démarré avec succès, en attente de joueurs..."},
			  {"Emplacement Vide","Emplacement Vide"},
			  {"Recherche en cours...", "Recherche en cours..."},
			  //turtlekit.agr
			  {"Kernel","Kernel"},
			  {"Nom","Nom"},
			  {"Lancer la partie","Lancer la partie"},
			  {"model","modele"},
			  {"engine", "moteur"},
			  {"turtles", "tortues"},
			  {"scheduler","ordonnanceur"},
			  {"viewer","spectateur"},
			  {"launch", "lancer" },
			  //turtlekit.gui
			  {"Config","Config"},
			  {"Models","Modeles"},
			  {"Configs","Configs"},
			  {"XML Models","Modeles XML"},
			//civilisation.inspecteur
	  			//FenetreInspecteur.java
	  			{"Agents","Agents"},
	  			{"Options","Options"},
	  			//PanelAgentData.java
	  			{"Bar Chart Demo" , "Bar Graphique Demo"},
	  			{"Category", "Categorie"},
	  			{"Value", "Valeur"},
	  			//PanelAgentLog.java ------- test
	  			{"tick" , "tick"},
	  			//PanelGroupViewer.java
	  			{"Groups","Groupes"},
	  			{"Show in world view","Afficher vue du monde"},
	  			//PanelInspecteur.java ------------------------------------------Test
	  			{"Variable","Variable"},
	  			//PanelInventaire.java
	  			{"Inventaire","Inventaire"},
	  			{"Objet","Objet"},
	  			{"Nombre","Nombre"},
	  			//PanelListeCognitons.java
	  			{"Cognitons","Cognitons"},
	  			//PanelListePlans.java
	  			{"Plans","Plans"},
	  			{"poids" , "poids"},
	  			//PanelMindData.java
	  			{"Plans weight","Poids plans"},
	  			{"Cogni weight","Poids cognitons"},
	  			//PanelOptions.java
	  			{"Affichage des plans : ", "Affichage des plans : " },
	  			{"Phero map : ","Phero map : "},
	  			{"Show specific group and role :","Voir le groupe et le r�le sp�cifique :"},
	  			{"Aucun patch selectionne", "Aucun patch s�lectionne"},
	  			{"Attribut","Attribut"},
	  			{"Valeur", "Valeur"},
	  			//PanelPerformances
	  			{"OS : ","OS : "},
	  			{"Version : ", "Version : "},
	  			{"Architecture : ", "Architecture : "},
	  			{"Total memory: ","Mémoire totale :"},
	  			{"Used memory : ","Mémoire utiliser : "},
	  		//civilisation.inspecteur.advancedcharts
	  			//AdvSubPanelDisplaySelection.java
	  			{"Civilisations" ,"Civilisations"},
	  			{"choose Civilisations","choisir Civilisations"},
	  			//ChartWindow.java
	  			{"Population","Population"},
	  			{"Mind","Esprit"},
	  			{"Selection","Selection"},
	  		//civilisation.inspecteur.simulation
	  			//ActionStructureCognitive
	  			{"NewCognitiveScheme","Nouveauschemacognitif"},
	  			{"Copy This Cognitive Scheme","Copiez ce schéma cognitif"},
	  			{"Name :" ,"Nom :"},
	  			{"Create Cognitive Scheme","Creer Schema Cognitive "},
	  			{"Are you sure you want to delete this cognitive scheme?","êtes-vous sûr de vouloir supprimer ce schéma cognitif?"},
	  			{"DELETE COGNITIVE SCHEME","SUPPRIMER SCHEMA COGNITIVE"},
	  			//PanelArbreActions.java
	  			{"Action", "Action"},
	  			{"Edit", "Editer"},
	  			{"Add after", "Ajouter apres"},
	  			{"Add before", "Ajouter avant"},
	  			{"Add internal", "Ajouter interne"},
	  			{"Remove action", "Retirer l'action"},
	  			//PanelModificationSimulation.java
	  			{"Save", "Sauvegarder"},
	  			{"Make a copy of current save", "Faites une copie de sauvegarde courante"},
	  			{"Edit cognitive scheme", "Editer schema cognitive "},
	  			{"Edit environment","Editer environment"},
	  			{"Edit item","Editer item"},
	  			{"Edit civilizations","Editer civilizations"},
	  			{"Edit attributes","Editer les attribues"},
	  			{"Manage group","Gerer groupe"},
	  			{"Edit amenagement","Editer amenagement"},
	  			{"Edit configuration","Editer configuration"},
	  			{"Edit constants","Editer constantes"},
	  			{"quitter","quitter"},
	  			{"Action tree", "Action arbre"},
	  			{"Cognitons and plans", "Cognitons et plans"},
	  			{"Environment","Environement"},
	  			{"Patch type","Type de Patch"},
	  			{"Group manager","Gestionnaire de groupe"},
	  			{"Group tree","Arbre Groupe"},
	  			{"create cognitive scheme", "creer schema cognitif"},
	  			{"import cognitive scheme","importer schema cognitif"},
	  			{"delete cognitive scheme","effacer schema cognitif"},
	  			{"Save this environment", "Enregistrer cette environement"},
	  			{"Load an environment","Charger un environement"},
	  			{"Set environment bounds", "Definir bornes environement."},
	  			{"Generate an environment from an existing picture", "Creer un environnement à partir d'une photo existante."},
	  			{"Pen","Stylo"},
	  			{"Pinceau","Pinceau"},
	  			{"Paint bucket","Seau de peinture"},
	  			{"Zoom","Zoom"},
	  			{"Dezoom","Dezoom"},
	  			{"Choose environment to use for simulation","Choisissez environnement à utiliser pour la simulation"},
	  			{"Manage pheromon","Gerer les pheromones"},
	  			{"Add a new action at the start of the plan","Ajouter une nouvelle action au début du plan"},
	  			{"Create new item","Créer un nouveau point"},
	  			{"Create new amenagement","Creer un nouveau amenagement"},
	  			{"Create new civilization", "Creer nouvelle civilisation"},
	  			//PanelStructureCognitive.java
	  			{"Plan","Plan"},
	  			{"Edit Plan","Editer Plan"},
	  			{"Remove","Supprimer"},
	  			{"Cogniton","Cogniton"},
	  			{"Edit Cogniton","Editer Cogniton"},
	  			{"Edit influence links","Editer influence des liens"},
	  			{"Edit conditional links","Editer liens conditionnelles"},
	  			{"Edit triggering attributes","Editer declenchement attributs"},
	  			{"Display Custom colors", "Afficher couleurs personnalis�es"},
	  			{"Display Type colors","Afficher couleurs par type"},
	  		//civilisation.inspecteur.simulation.amenagement
	  			//PanelAmenagement.java
	  			{"Facility editor","Editeur de l'installation"},
	  			{"Add effect : ","Ajouter un effet : "},
	  			{"Add this effect","Ajouter cet effet"},
	  			{"Amenagement name : ","Nom d'amenagement :"},
	  			{"Icon :","Icone :"},
	  			{"Description :","Description :"},
	  			{"Enter facility Description here","Entrez ici Description de l'etablissement"},
	  			{"Facility color : ","La couleur de l'établissement:"},
	  			{"Change color","Changer couleur"},
	  			{"New effect","Nouvel effet"},
	  			{"save amenagement","sauver amenagement"},
	  			{"Recipe","Recette"},
	  			//PanelEffectA.java
	  			{"Effect Name : ","Effet Nom : "},
	  			{"Target type : ","Type de cible : "},
	  			{"Attributes","Attributs"},
	  			{"Objets","Objets"},
	  			{"Ressource/tick","Ressource/tick"},
	  			{"Effect Type : ","Type d'effet : "},
	  			{"Set","fixé"},
	  			{"Add","Ajouter"},
	  			{"Value : ","Valuer : "},
	  			{"Activation : ","Activation : "},
	  			{"Possession","Possession"},
	  			{"Use","Utilisation"},
	  			{"Permanent ? : ","Permanent ? : "},
	  			{"Yes","Oui"},
	  			{"No","Non"},
	  			{"Save Effect","Sauver Effet "},
	  			{"Remove Effect","Supprimer Effet"},
	  			{"Ressources/tick","Ressources/tick"},
	  			{"Target Name : ","Nom de la cible : "},
	  			//PanelListeAmenagement.java
	  			{"Amenagement list","Amenagement liste"},
	  			//PanelRecetteA.java
	  			{"Add item for craft","Ajouter un objet pour artisanat"},
	  			{"Save Recipe","Enregistrer Recette"},
	  		//civilisation.inspecteur.simulation.dialogues
	  			//DialogChangeGroupName.java
	  			{"group_name","nom_groupe"},
	  			{"Rename a group","Renommer un groupe"},
	  			//DialogChangeRoleName.java
	  			{"role_name", "nom_role"},
	  			{"Rename a role","Renommer un role"},
	  			//DialogCreatePheromon.java
	  			{"Attribute Name : ","Nom de l'attribut : "},
	  			{"Start Value : ", "Valeur de départ : " },
	  			{"Growing value (units/tick) : ","Valeur croissante (units/tick) : "},
	  			//DialogCreateRole.Java
	  			{"Create a role","Creer un rôle"},
	  			//DialogEditBounds.java
	  			{"Edit environment bounds","Modifier les limites de l'environnement"},
	  			//DialogEditPheromon.java
	  			{"Edit pheromons","Modifier pheromones"},
	  			{"Constant","Constante"},
	  			{"Edit triggering attributes","Modifier déclenchement de l'attributs"},
	  			//DialogSelectEnvironmentToLoad.java
	  			{"Load environment","Charger environement"},
	  			//DialogueAjouterAction.java
	  			{"Add new action","Ajouter nouvelle action"},
	  			//DialogueChoisirCouleurAmenagement.java
	  			{"Choisir la couleur", "Choisir la couleur"},
	  			//DialogueEditerCogniton.java
	  			{"Cogniton type :","Type Cogniton :"},
	  			{"Cogniton name :","Nom Cogniton :"},
	  			{"Starting cogniton","Lancer cogniton"},
	  			{"Give this cogniton to new agents?","Donnez ce cogniton à de nouveaux agents?"},
	  			{"Starting apparition chance :","Debuter apparition par chance :"},
	  			{"Cogniton custom color : ","Teinte personnalis�e Cogniton : "},
	  			{"Editer un cogniton","Editer un cogniton"},
	  			//DialogueEditerLiensConditionnels.java
	  			{"Conditionned plan :","Plan conditionnelle :"},
	  			//DialogueEditerPlan.java
	  			{"Auto-Plan","Auto-Plan"},
	  			{"Every agents will run this plan every tick if this box is checked. You could use this features to create automatic cognitons transmissions," ,"Tous les agents seront ex�cuter sur ce plan a chaque tick si cette case est coch�e. Vous pouvez utiliser cette fonctionnalit� pour cr�er automatiques cognitons transmissions,"},
	  			{" or change attributes (need for food...)"," ou modifier les attributs (besoin pour la nourriture ...)"},
	  			{"Birth-Plan","Plan-Naissance"},
	  			{"Every agents will run this plan at birth.","Tous les agents seront exécuter sur ce plan de la naissance."},
	  			{"Edite plan","Editer plan"},
	  			{"Plan custom color : ","Teinte personnalis�e Plan : "},
	  			//DialogueEditerTerrain.java
	  			{"Editer le terrain","Editer le terrain"},
	  			{"Passability :","Passabilitie :"},
	  			{"Impassable?","Impassable?"},
	  			{"Starting value of ","Valeur de depart de "},
	  			{"Starting value : ","Valeur depart : "},
	  			{"Growth value of ","Valeur croissance de" },
	  			{"Growth value : ","Valeur croissance : "},
	  			//DialogueEnregistrerEnvironnement.java
	  			{"nom","nom"},	  			
	  			{"Enregistrer un environnement","Enregistrer un environnement"},
	  			//DialoguePlacerCivilisation.java
	  			{"Choisissez une civilisation","Choisissez une civilisation"},
	  			//DialogueSelectionnerEnvironnementActif.java
	  			{"Enregistrer un environnement","Enregistrer un environnement"},
	  		//civilisation.inspecteur.simulation.environnement
	  			//PanelTerrains.java
	  			{"Terrain","Terrain"},
	  			{"Edit terrain", "Editer terrain " },
	  			{"Change colour","Changer colour"},
	  		//civilisation.inspecteur.simulation.groupManager
	  			//GroupToolBar.java
	  			{"Add an existing culturon","Ajoutez un culturon existante"},
	  			{"Create a new culturon","Create a new culturon"},
	  			{"Remove currently selected role (can't remove the last role)","Retirer rôle actuellement sélectionné (ne peut pas supprimer le dernier rôle)"},
	  			{"Add a new role to this group","Ajouter un nouveau rôle à ce groupe"},
	  			{"Rename the current role","Renommez le role actuel"},
	  			{"Rename the current group","Renommez le groupe actuel"},
	  		//civilisation.inspecteur.simulation.objets
	  			//PanelEffect.java
	  			{"Actions","Actions"},
	  			//PanelListeObjets.java
	  			{"Item list","Item list"},
	  			//PanelObjets.java
	  			{"Item editor","Editeur d'Item"},
	  			{"Item name : ", "Nom d'Item : "},
	  			{"Enter item Description here","Entrez Description d'item ici"},
	  			{"save object","enregistrer l'objet"},
	  		//civilisation.inspecteur.viewer
	  			//ViewerTabbed.java
	  			{"Simulation","Simulation"},
	  			{"Agent","Agent"},
	  			{"Options","Options"},
	  			{"Performances","Performances"},
	  			{"Tableau de bord","Tableau de bord"},
	  			{"Charts","Charts"},
	  			{"Legend","Legende"},
	  		//java.awt.event.KeyEvent
	  			//TKMenujava
	  			{"TurtleKit","TurtleKit"},
		  }; 
		
}
