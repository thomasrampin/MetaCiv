package civilisation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

public class Convertisseur {
	
	
	static String pathToSchemas = "/schemasCognitifs";
	static String attributes = "attributes";
	static String cognitons = "cognitons";
	static String plans = "plans";
	static String groups = "groups";
	static String cloudCognitons = "cloudCognitons";
	
	static String pathToAttributes = "/" + attributes;
	static String pathToCognitons = "/" + cognitons;
	static String pathToPlans = "/" + plans;
	static String pathToCloudCognitons = "/" + cloudCognitons;
	static String pathToGroups = "/" + groups;
	
	static String simulation = "simulation";
	static String actions = "actions/civilisation/individu/plan/action";
	static String amenagements = "amenagements";
	static String civilisations = "civilisations";
	static String effects = "effects";
	static String environnements = "environnements";
	static String itemPheromones = "itemPheromones";
	static String objets = "objets";
	static String terrains = "terrains";
	
	static String pathToSimulation = "/" + simulation;
	static String pathToActions = "/" + actions;
	static String pathToSousActions = "/" + "civilisation/individu/plan/action";
	static String pathToAmenagements = "/" + amenagements;
	static String pathToCivilisations = "/" + civilisations;
	static String pathToEffects = "/" + effects;
	static String pathToEnvironnements = "/" + environnements;
	static String pathToItemPheromones = "/" + itemPheromones;
	static String pathToObjets = "/" + objets;
	static String pathToTerrains = "/" + terrains;
	
	
	
	
	
	/** Convertisseur d'ancienne version metaciv (pre TER M1 2015 ) vers la nouvelle version (schemaCognitif)
	 * @param fichierIn 	-> parametres.metaciv de l'ancienne version
	 * @param fichierOut	-> parametres.metaciv de la nouvelle version (existant ou non)
	 */
	
	public static void convertir(String fichierIn, String fichierOut){
		if(fichierIn == null || fichierOut == null){
			System.err.println("Conversion ratée - Paix et amour sur Loïc");
			System.exit(0);
		}
		
		System.out.println("Je convertis de " + fichierIn + " vers " + fichierOut);
		
		
		
		String pathIn = (String)fichierIn.subSequence(0, fichierIn.length() - 18);
		String pathOut = (String)fichierOut.subSequence(0, fichierOut.length() - 18);
		
		File testOut = new File(pathOut);
		if(!testOut.exists()){
			testOut.mkdirs();
		}
		
		
		File[] fichiersIn = new File(pathIn).listFiles();
		File[] fichiersOut = new File(pathOut).listFiles();
		
		
		//Traitement du nouveau parametres.metaciv
		/*
		 *	Carte
		 * 	DecisionMaker
		 * 	MaxWeight
		 * 	RoadCreation
		 * 	RoadEvaporation
		 * 	VisionRadius
		 * 	Dispersion
		 * 
		 */
		File parametresMetacivOld = new File(fichierIn);
		File parametresMetaciv = new File(fichierOut);
		if(!parametresMetaciv.exists()){
			FileInputStream sourceFile = null;
			FileOutputStream destinationFile = null;
			try {
				//System.out.println(parametresMetaciv.getAbsolutePath());
				parametresMetaciv.createNewFile();
				sourceFile = new FileInputStream(parametresMetacivOld);
				destinationFile = new FileOutputStream(parametresMetaciv);
				
				byte buffer[] = new byte[1024];
				int nbLecture;
				while( (nbLecture = sourceFile.read(buffer)) != -1 ) {
					destinationFile.write(buffer, 0, nbLecture);
				}
				
				sourceFile.close();
				destinationFile.close();

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
		}
			
		//Traitement des dossiers de schémas cognitifs
		/*
		 * 	Nom		Check
		 * 	attributes	Check
		 * 	cognitons	Check
		 * 	plans		Check
		 * 	groups		Check
		 * 	cloudCognitons	Check
		 * 	
		 */
		
		//Création du dossier schemasCognitifs si il n'existe pas
		if(!contiens(fichiersIn, pathToSchemas)){
			//System.out.println("Je crée " + pathToSchemas);
			File schema = new File(pathOut + pathToSchemas);
			schema.mkdirs();
		}
		File[] schemas = new File(pathOut + pathToSchemas).listFiles();
		String[] stringTab = fichierIn.split(Pattern.quote(System.getProperty("file.separator")));
		String nomSchemaCognitif = stringTab[stringTab.length - 2];
		//System.out.println(nomSchemaCognitif);
		
		//Création du sous dossier si il n'existe pas
		if(!contiens(schemas, nomSchemaCognitif)){
			File schema = new File(pathOut + pathToSchemas + "/" + nomSchemaCognitif);
			schema.mkdirs();
			
			//Création du nom.metaciv
			File nomSchemaCognitifMetaciv = new File(schema + "/" + nomSchemaCognitif + ".metaciv");
			try {
				nomSchemaCognitifMetaciv.createNewFile();
				FileWriter output = new FileWriter(nomSchemaCognitifMetaciv);				
				output.write("Nom : " + nomSchemaCognitif);		
				output.flush();
				output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Récupération des attributes
			int indice = indiceDansFichiers(fichiersIn, attributes);
			File attributesOld = new File(fichiersIn[indice].getAbsolutePath());
			File attributesNew = new File(schema + "/" + pathToAttributes);
			copierDossiersVers(attributesOld, attributesNew);
			
			//Récupération des cognitons
			indice = indiceDansFichiers(fichiersIn, cognitons);
			File cognitonsOld = new File(fichiersIn[indice].getAbsolutePath());
			File cognitonsNew = new File(schema + "/" + pathToCognitons);
			copierDossiersVers(cognitonsOld, cognitonsNew);
			
			//Récupération des plans
			indice = indiceDansFichiers(fichiersIn, plans);
			File plansOld = new File(fichiersIn[indice].getAbsolutePath());
			File plansNew = new File(schema + "/" + pathToPlans);
			copierDossiersVers(plansOld, plansNew);
			
			//Récupération des groups
			indice = indiceDansFichiers(fichiersIn, groups);
			File groupsOld = new File(fichiersIn[indice].getAbsolutePath());
			File groupsNew = new File(schema + "/" + pathToGroups);
			copierDossiersVers(groupsOld, groupsNew);
			
			//Récupération des cloudCognitons
			indice = indiceDansFichiers(fichiersIn, cloudCognitons);
			File cloudCognitonsOld = new File(fichiersIn[indice].getAbsolutePath());
			File cloudCognitonsNew = new File(schema + "/" + pathToCloudCognitons);
			copierDossiersVers(cloudCognitonsOld, cloudCognitonsNew);
		
		}
		
		//Traitement des dossiers de simulation
		/*
		 * 	actions/civilisation/individu/plan/action	Check	
		 * 	amenagements								Check
		 * 	civilisations 								Check
		 * 	effects										Check
		 * 	environnements 								Check				
		 * 	itemPheromones								Check		
		 * 	objets										Check
		 * 	terrains									Check
		 */
		//Création du dossier simulation si il n'existe pas
		if(!contiens(fichiersOut, pathToSimulation)){
			//System.out.println("Je crée " + pathToSimulation);
			File simu = new File(pathOut + pathToSimulation);
			simu.mkdirs();
		}		
		String simu = pathOut + simulation;
		File fichiersOutSimu[] = new File(simu).listFiles();
		
		//Dossier action
		creationActions(simu);
		
		//Récupération des actions
		int indice = indiceDansFichiers(fichiersIn, "actions");
		File actionsOld = new File(fichiersIn[indice].getAbsolutePath() + "/" + pathToSousActions);
		File actionsNew = new File(simu + "/" + pathToActions);
		copierDossiersVers(actionsOld, actionsNew);
		
		//Récupération des amenagements
		indice = indiceDansFichiers(fichiersIn, amenagements);
		File amenagementsOld = new File(fichiersIn[indice].getAbsolutePath());
		File amenagementsNew = new File(simu + "/" + pathToAmenagements);
		copierDossiersVers(amenagementsOld, amenagementsNew);
		
		
		//Récupération des civilisations
		File civs = new File(simu + "/" + civilisations);
		civs.mkdirs();
		File civ = new File(simu + "/" + civilisations + "/" + nomSchemaCognitif + ".metaciv");
	
		try {
			civ.createNewFile();
			FileWriter output = new FileWriter(civ);
			File civsALire[] = new File(pathIn + civilisations).listFiles();
			if(civsALire != null && civsALire.length >0){
				BufferedReader sourceFile = new BufferedReader(new FileReader(civsALire[0]));
				String line = sourceFile.readLine();
				while (line != null) {
					output.write(line);
					output.write(System.lineSeparator());
		            line = sourceFile.readLine();
		        }
				output.write("SchemaCognitif : " + nomSchemaCognitif);	
				sourceFile.close();
				output.flush();			
				output.close();
			}else{
				output.write("Nom : Civ1");	
				output.write(System.lineSeparator());
				output.write("Agents : 100");	
				output.write(System.lineSeparator());
				output.write("Scattered : 0");
				output.write(System.lineSeparator());
				output.write("Couleur : 0.0,1.0,1");
				output.write(System.lineSeparator());
				output.write("SchemaCognitif : " + nomSchemaCognitif);
				output.flush();			
				output.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Récupération des effects	
		indice = indiceDansFichiers(fichiersIn, effects);
		File effectsOld = new File(fichiersIn[indice].getAbsolutePath());
		File effectsNew = new File(simu + "/" + pathToEffects);
		copierDossiersVers(effectsOld, effectsNew);
		
		//Récupération des environnements	
		indice = indiceDansFichiers(fichiersIn, environnements);
		File environnementsOld = new File(fichiersIn[indice].getAbsolutePath());
		File environnementsNew = new File(simu + "/" + pathToEnvironnements);
		copierDossiersVers(environnementsOld, environnementsNew);
		
		//Récupération des itemPheromones
		indice = indiceDansFichiers(fichiersIn, itemPheromones);
		File itemPheromonesOld = new File(fichiersIn[indice].getAbsolutePath());
		File itemPheromonesNew = new File(simu + "/" + pathToItemPheromones);
		copierDossiersVers(itemPheromonesOld, itemPheromonesNew);
		
		//Récupération des itemPheromones
		indice = indiceDansFichiers(fichiersIn, objets);
		File objetsOld = new File(fichiersIn[indice].getAbsolutePath());
		File objetsNew = new File(simu + "/" + pathToObjets);
		copierDossiersVers(objetsOld, objetsNew);
		
		
		//Récupération des terrains
		indice = indiceDansFichiers(fichiersIn, terrains);
		File terrainsOld = new File(fichiersIn[indice].getAbsolutePath());
		File terrainsNew = new File(simu + "/" + pathToTerrains);
		copierDossiersVers(terrainsOld, terrainsNew);
		
	}
	
	/**
	 * 
	 * @param fichiers
	 * @param nom
	 * @return vrai si nom est dans fichiers, faux sinon
	 */
	public static boolean contiens(File[] fichiers, String nom){
		boolean res = false;
		if(fichiers != null && fichiers.length > 0){
			for(File f : fichiers){
				if(f.getName().equals(nom)){
					res = true;
				}
			}
		}
		
		return res;
	}

	/**
	 * 
	 * @param fichiers
	 * @param nom
	 * @return l'indice auquel correspond nom dans fichiers
	 */
	public static int indiceDansFichiers(File[] fichiers, String nom){
		int cpt=0,indice=0;
		for(File f : fichiers){
			if(f.getName().equals(nom)){
				indice = cpt;
			}
			cpt++;
		}
		return indice;
	}
	
	/**
	 * Easy
	 * @param dossierOrigine
	 * @param dossierSortie
	 */
	public static void copierDossiersVers(File dossierOrigine, File dossierSortie){
		//System.out.println("Entrée : " + dossierOrigine);
		//System.out.println("Sortie : " + dossierSortie);
		
		dossierSortie.mkdirs();
		
		if(dossierOrigine.exists()){
			File[] filesOrigine = new File(dossierOrigine.getAbsolutePath()).listFiles();
			if(filesOrigine != null && filesOrigine.length > 0){
				for(File f : filesOrigine){
					if(f.isDirectory()){
						copierDossiersVers(f, new File(dossierSortie + "/" + f.getName()));
					}else{
						File fSortie = new File(dossierSortie + "/" + f.getName());
						FileInputStream sourceFile = null;
						FileOutputStream destinationFile = null;
						try {
							fSortie.createNewFile();
							sourceFile = new FileInputStream(f);
							destinationFile = new FileOutputStream(fSortie);
							
							byte buffer[] = new byte[1024];
							int nbLecture;
							while( (nbLecture = sourceFile.read(buffer)) != -1 ) {
								destinationFile.write(buffer, 0, nbLecture);
							}
								
							sourceFile.close();
							destinationFile.close();
	
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}		
	}
	/**
	 * Crée le dossier contenant les actions dans simulation
	 * @param pathSimu
	 */
	public static void creationActions(String pathSimu){
		//System.out.println("Je travaille dans " + pathSimu);
		String path = pathSimu;
		File fileSimu[] = new File(path).listFiles();
		
		if(fileSimu != null && fileSimu.length > 0){
			path += "/" + "actions";
			if(!contiens(fileSimu, "actions")){				
				File actions = new File(path);
				actions.mkdirs();
			}
			
			File filesActions[] = new File(path).listFiles();
			path += "/" + "civilisation";
			if(!contiens(filesActions, "civilisation")){
				File civilisation = new File(path);
				civilisation.mkdirs();
			}
			
			File filesCiv[] = new File(path).listFiles();
			path += "/" + "individu";
			if(!contiens(filesCiv, "individu")){
				File individu = new File(path);
				individu.mkdirs();
			}
			
			File filesInd[] = new File(path).listFiles();
			path += "/" + "plan";
			if(!contiens(filesInd, "plan")){
				File plan = new File(path);
				plan.mkdirs();
			}
			
			File filesPla[] = new File(path).listFiles();
			path += "/" + "action";
			if(!contiens(filesPla, "action")){
				File action = new File(path);
				action.mkdirs();
			}
			
		}else{
			File actionsF = new File(pathSimu + "/" + actions);
			actionsF.mkdirs();			
		}		
	}

	public static boolean checkVersion(String parent) {
		boolean schemas = false;
		boolean simu = false;
		File test[] = new File(parent).listFiles();		
		
		for(File f : test){
			if(f.getName().equals(DefinePath.schemasCognitifs)){
				schemas = true;
			}
			if(f.getName().equals(DefinePath.simulation)){
				simu = true;
			}	
		}
		return schemas && simu;
	}
}