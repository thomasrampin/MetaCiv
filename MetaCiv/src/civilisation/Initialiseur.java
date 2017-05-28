package civilisation;

import java.awt.Color;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.swing.filechooser.FileNameExtensionFilter;

import civilisation.effects.Effect;
import civilisation.group.GroupModel;
import civilisation.individu.cognitons.*;
import civilisation.individu.decisionMaking.DecisionMaker;
import civilisation.individu.decisionMaking.MaxWeightDecisionMaker;
import civilisation.individu.decisionMaking.MaxWeightOx2_DecisionMaker;
import civilisation.individu.decisionMaking.WeightedStochasticDecisionMaker;
import civilisation.individu.plan.NPlan;
import civilisation.individu.plan.action.Action;
import civilisation.inventaire.Objet;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
import civilisation.world.Terrain;

/////////////// directory explorer 0.00001
class DirectoryExplorer{public static String[] listFiles(String path){return new File(path).list();}
						public static String[] listMetacivFiles(String path){return new File(path).list(new FilenameFilter() {							
							@Override
							public boolean accept(File dir, String name) {
								return name.endsWith(Configuration.getExtension());}
						});}
						public static String[] listDirectories(String path){return new File(path).list(new FilenameFilter() {							
							@Override
							public boolean accept(File dir, String name) {
								return new File(dir.getAbsolutePath()+"/"+name).isDirectory();}
						});
							}}


public class Initialiseur {
	/* Configuration n'en a pas besoin, tout ça se stocke dans les civs
	HashMap<String, TypeCogniton> listeCognitons;
	HashMap<String, TypeCulturon> listCloudCognitons;
	HashMap<String, NPlan> listePlans;*/
	
	HashMap<Color, Terrain> couleurs_terrains; //TODO : Gerer le cas ou la meme couleur est utilisee pour deux terrains
	final int passabiliteParDefaut = 30;
	static int agentsInitiaux = 100;
	
	//Configuration v0.1
	String pathToRessources;


	public Initialiseur(){
		/**
		 * Configuration
		 * Initialiseur doit récupérer trois choses principales :
		 * 		-parametres.metaciv qui contient les parametres globaux
		 * 											Carte 
		 *											DecisionMaker 
		 *											MaxWeight
         *											RoadCreation
		 *											RoadEvaporation
		 *	 										VisionRadius
         *											Dispersion
		 *		-le dossier simulation qui contient les dossiers de parametres globaux
		 *											actions
		 *											amenagements
		 *											effets
		 *											environnements  -> ne doit pas être vide car on charge le premier dedans si celui décrit dans param.metaciv n'y est pas présent
		 *											itemPheromones
		 *											objets
		 *											terrains
		 *		-le dossier civilisations contenant les dossiers de chaque civilisations,
		 *				dont chacun sera traité individuellement
		 */
		
		/* Configuration n'en a pas besoin, tout ça se stocke dans les civs
		listeCognitons = new HashMap<String, TypeCogniton>();
		listCloudCognitons = new HashMap<String, TypeCulturon>();
		listePlans = new HashMap<String, NPlan>();
		ArrayList<TypeCogniton> cognitonsDeBase = new ArrayList<TypeCogniton>();
		ArrayList<TypeCogniton> tousLesCognitons = new ArrayList<TypeCogniton>();
		ArrayList<TypeCulturon> allCloudCogniton = new ArrayList<TypeCulturon>();
		ArrayList<NPlan> tousLesPlans = new ArrayList<NPlan>();
		*/

		couleurs_terrains = new HashMap<Color, Terrain>();

		String nom;	
		
		//Configuration v0.1
		pathToRessources = Configuration.pathToRessources + "/simulation";
		
		//Actions		
		loadActions();
		
		//Effets
		Configuration.effets = new ArrayList<Effect>();
		File[] files = new File(pathToRessources + "/effects").listFiles();
		if(files != null)
		{			
			for(File file : files)
			{
				if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension()))
				{
				    if (file.isFile()) 
				    {
				    	nom = Initialiseur.getChamp("Nom" , file)[0];
				    	Effect e = new Effect();
				    	e.setName(nom);
				    	e.setDescription(getChamp("Description" , file)[0]);
				    	e.setTarget(getChamp("Cible" , file)[0]);
				    	e.setType(Integer.parseInt(getChamp("Type" , file)[0]));
				    	e.setVarget(getChamp("NomCible" , file)[0]);
				    	e.setValue(Double.parseDouble(getChamp("Valeur" , file)[0]));
				    	if(getChamp("Permanence" , file)[0].equals("true"))
				    	{
				    		e.setPermanent(true);
				    	}
				    	else
				    	{
				    		e.setPermanent(false);
				    	}
				    	e.setActivation(Integer.parseInt(getChamp("Activation" , file)[0]));
		//		    	System.out.println("Chargement de l'effet "+e.getName());
				    	Configuration.effets.add(e);
				    }
				}	
			}
		}	
		
		
		//Amenagements
		
    	ArrayList<TypeAmenagement> amenagements = new ArrayList<TypeAmenagement>();
		File[] filesAmenagements = new File(pathToRessources + "/amenagements").listFiles();
		if(filesAmenagements != null)
		{
			for (File file : filesAmenagements) {
				if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension())){
			    if (file.isFile()) {
			    	nom = Initialiseur.getChamp("Nom" , file)[0];
			    	TypeAmenagement a = new TypeAmenagement();
			    	a.setNom(nom);
			    	
			    	if(getChamp("Couleur" , file) != null)
			    	{
			    		String[] HSB = getChamp("Couleur" , file);
				    	a.setColor(Color.getHSBColor((float)Double.parseDouble(HSB[0]), (float)Double.parseDouble(HSB[1]), (float)Double.parseDouble(HSB[2])));
			    	}
			    	else
			    	{
			    		Random rand = new Random();
				    	float r = rand.nextFloat();
				    	float g = rand.nextFloat();
				    	float b = rand.nextFloat();
				    	a.setColor(new Color(r,g,b));
			    	}
			    	
			    	ArrayList<String[]> eff = Initialiseur.getListeChamp("Effects", file);
					
					int i = 0;
					if(eff.size() > 0)
					{
						while(i < eff.get(0).length && eff.get(0)[i] != null)
						{
							Effect ef = Configuration.getEffectByName(eff.get(0)[i]);
				    		if(ef != null)
				    		{
				    			a.addEffect(ef);
				    		}
				    		++i;
						}
					}						
			    	amenagements.add(a);
			    }
				}
			}
		}
		Configuration.amenagements = amenagements;
		
		for(TypeAmenagement a : Configuration.amenagements)
		{
			System.out.println(a.getNom());
		}
		
		/*Chargement des recettes amenagement
		 * 
		 */
		
		filesAmenagements = new File(pathToRessources + "/amenagements").listFiles();
		if(filesAmenagements != null)
		{
			for (File file : filesAmenagements) {
				if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension())){
			    if (file.isFile()) {
			    	nom = getChamp("Nom" , file)[0];
			    	
			    	ArrayList<String[]> rec = getListeChamp("Objets", file);
			    	ArrayList<String[]> nbre = getListeChamp("Nombre", file);
			    	
					int i = 0;
					if(rec.size() > 0)
					{
						int nb; 
						while(i < rec.get(0).length && rec.get(0)[i] != null)
						{
							
							nb = Integer.parseInt(nbre.get(0)[i]);
							Configuration.getAmenagementsByName(nom).addItemRecipe(rec.get(0)[i], nb);
				    		++i;
						}
					}				
			    }
				}
			}
		}
		
		
		//Objets
		ArrayList<Objet> objets = new ArrayList<Objet>();
		files = new File(pathToRessources + "/objets").listFiles();
		System.out.println(files);
		for (File file : files) {
			if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension())){
		    if (file.isFile()) {
		    	nom = getChamp("Nom" , file)[0];
		    	Objet o = new Objet();
		    	o.setNom(nom);
		    	o.setDescription(getChamp("Description" , file)[0]);
		    	o.setIconeFromString(getChamp("Icone", file)[0]);
		    	ArrayList<String[]> eff = getListeChamp("Effects", file);
			//	System.out.println("Enregistrement de l'objet "+nom);
				int i = 0;
				if(eff.size() > 0)
				{
					while(i < eff.get(0).length && eff.get(0)[i] != null)
					{
						Effect ef = Configuration.getEffectByName(eff.get(0)[i]);
			    		if(ef != null)
			    		{
			 //   			System.out.println("    Ajout de l'effet "+ef.getName());
			    			o.addEffect(ef);
			    		}
			    		++i;
					}
				}				
		    	objets.add(o);
		    }
			}
		}	
		Configuration.objets = objets;
		
		/*Chargement des recettes objet
		 * 
		 */
		files = new File(pathToRessources + "/objets").listFiles();
		for (File file : files) {
			if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension())){
		    if (file.isFile()) {
		    	nom = getChamp("Nom" , file)[0];
		    	
		    	ArrayList<String[]> rec = getListeChamp("Objets", file);
		    	ArrayList<String[]> nbre = getListeChamp("Nombre", file);
		    	
				int i = 0;
				if(rec.size() > 0)
				{
					int nb; 
					while(i < rec.get(0).length && rec.get(0)[i] != null)
					{	
						nb = Integer.parseInt(nbre.get(0)[i]);
						Configuration.getObjetByName(nom).addItemRecipe(rec.get(0)[i], nb);
			    		++i;
					}
				}				
		    }
			}
		}		
		
		
		
		//Pheromones
		File[] filesPhero = new File(pathToRessources + "/itemPheromones").listFiles();
		ArrayList<ItemPheromone> phero = new ArrayList<ItemPheromone>();
		for (File file : filesPhero) {
			if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension())){
			    if (file.isFile()) {
			    	nom = getChamp("Nom" , file)[0];
			    	phero.add(new ItemPheromone(nom));
			    }
			}
		}	
		Configuration.itemsPheromones = phero;
		
		
		//Terrains
		File[] filesTerrains = new File(pathToRessources + "/terrains").listFiles();
		ArrayList<Terrain> terrains = new ArrayList<Terrain>();
		for (File file : filesTerrains) {
			if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension())){
		    if (file.isFile()) {
		    	nom = Initialiseur.getChamp("Nom" , file)[0];
		    	Terrain t = new Terrain(nom);
		    	terrains.add(t);
		    	
		    	String[] HSB = getChamp("Couleur" , file);
		    	t.setCouleur(Color.getHSBColor((float)Double.parseDouble(HSB[0]), (float)Double.parseDouble(HSB[1]), (float)Double.parseDouble(HSB[2])));
		    	
		       	ArrayList<String[]> pheromonesLiees = Initialiseur.getListeChamp("Pheromone", file);
		       	for (int i = 0; i < pheromonesLiees.size(); i++){
		       		t.addPheromoneLiee(Configuration.getPheromoneByName(pheromonesLiees.get(i)[0]), Double.parseDouble(pheromonesLiees.get(i)[1]), Double.parseDouble(pheromonesLiees.get(i)[2]));
		       	}
		       	
		       	String [] passabilite = getChamp("Passabilite",file);
		       	if (passabilite != null){
		       		t.setPassabilite(Integer.parseInt(passabilite[0]));
		       	}
		       	else{
		       		t.setPassabilite(passabiliteParDefaut);
		       	}
		       	
		       	String [] infranchissable = getChamp("Infranchissable",file);
		       	if (infranchissable != null){
		       		t.setInfranchissable(Boolean.parseBoolean(infranchissable[0]));
		       	}
		       	else{
		       		t.setInfranchissable(false);
		       	}
		       	
		       	String[] height = getChamp("Hauteur",file);
		       	if(!height[0].equals("null")){
		       		t.setHeight((double)Float.parseFloat(height[0]));
		       	}else{
		       		t.setHeight(1.0);
		       	}

		       	String[] Erosion = getChamp("Erosion",file);
		       	if(!Erosion[0].equals("null")){
		       		t.setErosion(Integer.parseInt(Erosion[0]));
		       	}else{
		       		t.setErosion(1);
		       	}
		       	
		       	String[] BlurMethod = getChamp("BlurMethod",file);
		       	if(!BlurMethod[0].equals("null")){
		       		t.setBlur(Integer.parseInt(BlurMethod[0]));
		       	}else{
		       		t.setBlur(0);
		       	}
		       	
		       	String[] Merge = getChamp("Merge",file);
		       	if(!Merge[0].equals("null")){
		       		t.setMerge(Integer.parseInt(Merge[0]));
		       	}else{
		       		t.setMerge(1);
		       	}
		       	
		       	String[] IH = getChamp("intensityHeightMap",file);
		       	if(!IH[0].equals("null")){
		       		t.setIntensityHeightMap((double)Float.parseFloat(IH[0]));
		       	}else{
		       		t.setIntensityHeightMap(0);
		       	}
		       	
		       	String[] Texture = getChamp("Texture",file);
		       	if(!Erosion[0].equals("null")){
		       		t.setTexture(Texture[0]);
		       	}else{
		       		t.setTexture(nom);
		       	}
		       		
		       	String[] Tiling = getChamp("Tiling",file);
		       	if(!Tiling[0].equals("null")){
		       		t.setTiling((double)Float.parseFloat(Tiling[0]));
		       	}else{
		       		t.setTiling(1);
		       	}
		       	
		       	
		       	couleurs_terrains.put(t.getCouleur(), t);
		    }
		    }
		}	
		
		if (terrains.size() == 0){
			/*Si il n'y a aucun terrain disponible, un terrain pas defaut est cree*/
			Terrain tDefault = new Terrain("Default");
			terrains.add(tDefault);
			tDefault.setCouleur(Color.GREEN);
			tDefault.setPassabilite(passabiliteParDefaut);
       		tDefault.setInfranchissable(false);
	       	couleurs_terrains.put(tDefault.getCouleur(), tDefault);

		}
		
		Configuration.terrains = terrains;
		Configuration.couleurs_terrains = couleurs_terrains;		


		
		///////////////////schema cognitifs
		Configuration.SchemasCognitifs = FabriqueScheme.listeSchemaCognitifs;		
		Configuration.SchemasCognitifs.clear();
		for(String s:DirectoryExplorer.listDirectories(DefinePath.pathToSchemas))
			FabriqueScheme.creerSchemaCognitif(DefinePath.pathToSchemas+"/"+s);			
		Configuration.SCEnCoursEdition = FabriqueScheme.listeSchemaCognitifs.get(0);
		///////////// end schema cognitifs (painless)
		
		//////////////// Civ
		Configuration.civilisations = FabriqueCiv.listeCiv;
		Configuration.civilisations.clear();
		for(String s:DirectoryExplorer.listMetacivFiles(DefinePath.pathToCivilisations))

			FabriqueCiv.creerCivilisation(DefinePath.pathToCivilisations+"/"+s);			
		////////// end Civ 
		/*
		try {
			FabriqueScheme.creerSchemaCognitif("TestSchemeClone", Configuration.SchemasCognitifs.get(0));
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		///////////////////////////////////////////////////////////////////////////////////////////
	////System.out.println("Verification");	
		
			//printAllCognitons();
			//TODO
			//Chargement des civilisations		
			/*Lecture des civilisations*/
			//System.out.println("Loading civilizations...");
			//Configuration.civilisations = new ArrayList<Civilisation>();
			/*
			Configuration.civilisations = FabriqueCiv.listeCiv;
			pathToRessources = Configuration.pathToRessources + "/civilisations";
			files = new File(pathToRessources).listFiles();
			System.out.println("path general : "+pathToRessources);
			
			for(File f : files){
				//System.out.println("Je traite le sous dossier civilisation : " + f.getAbsolutePath());
				Civilisation c = new Civilisation(agentsInitiaux);
				
				File params = new File(pathToRessources + "/" + f.getName() + "/" + f.getName() + Configuration.getExtension());			
			   // System.out.println("FILE : " + params);   
				//Civ.metaciv
				//Nom
				String s = getChamp("Nom", params)[0];
				//System.out.println("Nom : " + s);
				c.setNom(s);
				//Couleurs
				String[] HSB = getChamp("Couleur" , params);
		    	c.setCouleur(Color.getHSBColor((float)Double.parseDouble(HSB[0]), (float)Double.parseDouble(HSB[1]), (float)Double.parseDouble(HSB[2])));
		    	c.setAgentsInitiaux(agentsInitiaux);
				
		    	String pathToRessourcesB = pathToRessources + "/" + f.getName();
		    	System.out.println("path civ : "+pathToRessourcesB);
				//SousDossiers			
				//Attributs
				
				File[] filesAttributes = new File(pathToRessourcesB + "/attributes").listFiles();
				ArrayList<String> attributesNames = new ArrayList<String>();
				ArrayList<Double> attributesStartingValues = new ArrayList<Double>();
				for (File file : filesAttributes) {
					if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension())){
					    if (file.isFile()) {
					    	String name = getChamp("Name" , file)[0];
					    	attributesNames.add(name);
					    	Double startingValue = Double.parseDouble(getChamp("Starting value" , file)[0]);
					    	attributesStartingValues.add(startingValue);
					    }
					}
				}	
				c.setAttributesNames(attributesNames);
				c.setAttributesStartingValues(attributesStartingValues);
				
				//System.out.println("Attributs faits");
				//System.out.println(c.getAttributs());

				
				//Cognitons
				HashMap<String , ArrayList<Object[]>> attributesTrigerringValues = new HashMap<String , ArrayList<Object[]>>();
				HashMap<String, TypeCogniton> listeCognitons = new HashMap<String, TypeCogniton>();
				ArrayList<TypeCogniton> cognitonsDeBase = new ArrayList<TypeCogniton>();
				ArrayList<TypeCogniton> tousLesCognitons = new ArrayList<TypeCogniton>();
				files = new File(pathToRessourcesB + "/cognitons").listFiles();
				for (File file : files) {
					if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension())){
				    if (file.isFile()) {
				    	nom = getChamp("Nom" , file)[0];
				    	listeCognitons.put(nom , new TypeCogniton());
				    	TypeCogniton cogni = listeCognitons.get(nom);
				    	cogni.setNom(nom);
				    	cogni.setDescription(getChamp("Description" , file)[0]);
				    	cogni.setStartChance(Integer.parseInt(getChamp("StartChance" , file)[0]));
				    	cogni.setType(TypeDeCogniton.toType( getChamp("Type" , file)[0]));
				    	if (getChamp("Initial" , file)[0].equals("1")){
				    		cognitonsDeBase.add(cogni);
					    	cogni.setRecuAuDemarrage(true);
				    	}
				    	for (int i = 0; i < TypeCogniton.nHues; i++) {
				    		if (getChamp("Hue" + i , file) != null) {
				    			cogni.getHues()[i] = Integer.parseInt(getChamp("Hue" + i , file)[0]);
				    		}
				    	}
				    	//Load triggering attributes
				       	ArrayList<String[]> triggers = Initialiseur.getListeChamp("Trigger", file);
				       	for(int i = 0 ; i < triggers.size(); i++) {
				       		Object[] trig = new Object[3];
				       		trig[0] = cogni;
				       		trig[1] = Double.parseDouble(triggers.get(i)[1]);
				       		trig[2] = Integer.parseInt(triggers.get(i)[2]);
				       		if(attributesTrigerringValues.get(triggers.get(i)[0]) == null) { 
				       			attributesTrigerringValues.put(triggers.get(i)[0] , new ArrayList<Object[]>());
				       			}
				       		attributesTrigerringValues.get(triggers.get(i)[0]).add(trig);
				       		
				       		//Now we add trigger to cognitons to keep the model easy to understand
				       		trig = new Object[3];
				       		trig[0] = triggers.get(i)[0];
				       		trig[1] = Double.parseDouble(triggers.get(i)[1]);
				       		trig[2] = Integer.parseInt(triggers.get(i)[2]);
				       		cogni.getTriggeringAttributes().add(trig);
				       		
			       		
				       	}
				    	tousLesCognitons.add(cogni);
				    }
					}
				}
				c.setStartingCognitons(cognitonsDeBase);
				c.setAttributesTrigerringValues(attributesTrigerringValues);
				c.setCognitons(tousLesCognitons);
				
				//System.out.println("Cognitons faits");

				
				//CloudCognitons
				HashMap<String, TypeCulturon> listCloudCognitons = new HashMap<String, TypeCulturon>();
				ArrayList<TypeCulturon> allCloudCogniton = new ArrayList<TypeCulturon>();
				files = new File(pathToRessourcesB + "/cloudCognitons").listFiles();
				for (File file : files) {
					if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension())){
					//System.out.println("Load culturon : " + file.getName());
				    if (file.isFile()) {
				    	nom = getChamp("Nom" , file)[0];
				    	listCloudCognitons.put(nom , new TypeCulturon());
				    	TypeCulturon cogni = listCloudCognitons.get(nom);
				    	cogni.setNom(nom);
				    	cogni.setDescription(getChamp("Description" , file)[0]);
				    	cogni.setType(TypeDeCogniton.toType( getChamp("Type" , file)[0]));
				    	if (getChamp("Initial" , file)[0].equals("1")){
				    		cognitonsDeBase.add(cogni);
					    	cogni.setRecuAuDemarrage(true);
				    	}
				    	for (int i = 0; i < TypeCogniton.nHues; i++) {
				    		if (getChamp("Hue" + i , file) != null) {
				    			cogni.getHues()[i] = Integer.parseInt(getChamp("Hue" + i , file)[0]);
				    		}
				    	}
				    	allCloudCogniton.add(cogni);
				    }
					}
				}	
				c.setCloudCognitons(allCloudCogniton);
							
				//System.out.println("CloudCognitons faits");

				
				//Groups
				files = new File(pathToRessourcesB + "/groups").listFiles();
				for (File file : files) {
					if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension())){
					//System.out.println("\tLoad group : " + file.getName());
				    if (file.isFile()) {
				    	nom = getChamp("Name" , file)[0];
				    	GroupModel g = new GroupModel(nom);
				    	ArrayList<String[]> culturons = getListeChamp("Culturon", file);
				    	for (int i = 0 ; i < culturons.size() ; i++) {
				    		g.addCulturonToRole(culturons.get(i)[0], c.getCognitonByName(culturons.get(i)[1]));
				    	}

				    	c.addGroup(g);
				    }
					}
				}	
				
				//System.out.println("Groups faits");

				
				

				
				//Plans
				ArrayList<NPlan> tousLesPlans = new ArrayList<NPlan>();
				HashMap<String, NPlan> listePlans = new HashMap<String, NPlan>();
				File[] filesPlans = new File(pathToRessourcesB + "/plans").listFiles();
				for (File file : filesPlans) {
					System.out.println(file.getName());
					if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension())){
					//System.out.println("Creation du plan : " + file.getName());
				    if (file.isFile()) {
				    	nom = Initialiseur.getChamp("Nom" , file)[0];
				    	listePlans.put(nom , new NPlan());
				    	listePlans.get(nom).setNom(nom);
				       	ArrayList<String[]> actions = Initialiseur.getListeChamp("Action", file);				       	
				       	setupPlans(listePlans.get(nom), file, 0, 0, null);
				       	listePlans.get(nom).setIsBirthPlan(Boolean.parseBoolean(Initialiseur.getChamp("Birth", file)[0]));
				       	listePlans.get(nom).setIsSelfPlan(Boolean.parseBoolean(Initialiseur.getChamp("Self", file)[0]));
*/
				       /*	for (int i = 0; i < actions.size(); i++){
				       		listePlans.get(nom).addAction(actions.get(i));
				       		if (i > 0){
				       			listePlans.get(nom).getActions().get(i-1).setNextAction(listePlans.get(nom).getActions().get(i));
				       		}
				       	}*/
	/*			    	
				    	if (listePlans.get(nom).getIsSelfPlan()) {
				    		c.setAutoPlan(listePlans.get(nom));
				    	}
				    	else if (listePlans.get(nom).getIsBirthPlan()) {
				    		//System.out.println("j'ajoute ca : "+listePlans.get(nom));
				    		c.setInitiatePlan(listePlans.get(nom));
				    	}
					    tousLesPlans.add(listePlans.get(nom));
				    }
					}
				}	
				
				//System.out.println("Plans faits");

				
				//System.out.println("Creating links...");
				files = new File(pathToRessourcesB + "/cognitons").listFiles();
				for (File file : files) {
					if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension())){
					//System.out.println("Create links of : " + file.getName());
				    if (file.isFile()) {
				    	nom = getChamp("Nom" , file)[0];
				    	
				    	/*Les liens inter-cognitons (liens d'apprentissage)*/
				     /*  	ArrayList<String[]> liste = getListeChamp("Chaine", file);
				       	ArrayList<LienCogniton> liens = new ArrayList<LienCogniton>();		      
				       	for (int i = 0 ; i < liste.size(); i++){
				       		liens.add(new LienCogniton(listeCognitons.get(liste.get(i)[0]), Integer.parseInt(liste.get(i)[1])));
				       	}
			       		listeCognitons.get(nom).setLiens(liens);

				    	/*Les liens cognitons-plans (liens d'influence)*/
				       /*	liste = getListeChamp("Influence", file);
				       	ArrayList<LienPlan> liensP = new ArrayList<LienPlan>();		      
				       	for (int i = 0 ; i < liste.size(); i++){
				       		liensP.add(new LienPlan(listePlans.get(liste.get(i)[0]), Integer.parseInt(liste.get(i)[1])));
				       	}
			       		listeCognitons.get(nom).setLiensPlans(liensP);
				       	
				    	/*Les liens cognitons-plans debloques (liens conditionnels)*/
				       /*	liste = getListeChamp("Permet", file);
				       	ArrayList<NPlan> plans = new ArrayList<NPlan>();		      
				       	for (int i = 0 ; i < liste.size(); i++){
					       	////System.out.println("Le nom qu'on trouve : " + liste.get(i)[0]);
					       	////System.out.println("Hach assoccie : " + listePlans.get(liste.get(i)[0]));
				       		plans.add(listePlans.get(liste.get(i)[0]));
				       	}
				       	////System.out.println("plans autorises : "+ nom + "  : "+plans.toString());
				       //	//System.out.println("array : "+ plans);

			       		listeCognitons.get(nom).setPlansAutorises(plans);
				    }
				}	
				}
				
				c.setCognitons(tousLesCognitons);
				
				System.out.println("adding "+ tousLesPlans.size()+" plans to civ : "+c.nom);
				c.setPlans(tousLesPlans);
				
				//System.out.println(f.getName() + " finie");
				Configuration.civilisations.add(c);
				*/

		
				
		//Configuration.civEnCoursEdition = Configuration.civilisations.get(0);		
	}
	
	/**
	 * Return the value of the first parameter field encountered
	 * @param field : the field to read
	 * @param f : the file where the research must be done
	 * @return An array containing all field value for the first field encountered (with the name "field")
	 */
	static public String[] getChamp(String field ,  File f){
		
		 Scanner scanner = null;
		 String[] test = {"null"};

		try {
			scanner = new Scanner(new FileReader(f));
			 String str = null;
			 while (scanner.hasNextLine()) {
			     str = scanner.nextLine();
			     if(str.split(" : ")[0].equals(field)){
			    	 if(str.split(" : ").length > 1){
			    		 return str.split(" : ")[1].split(",");
			    	 }else{
			    		 return test;
			    	 }
			     }
			 }			 
			 return test;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally{
		scanner.close();
		}
		return null;
	}
	
	//Les paramètres de parametre.metaciv sont récupérés ici
	//Modifié pour Configuration v0.1
		public static void readParameters() {
			
			String pathToRessources = Configuration.pathToRessources;
			//Initialize decisions makers
			ArrayList<DecisionMaker> decisionMakers = new ArrayList<DecisionMaker>();
			decisionMakers.add(new WeightedStochasticDecisionMaker(null));
			decisionMakers.add(new MaxWeightDecisionMaker(null));
			decisionMakers.add(new MaxWeightOx2_DecisionMaker(null));
			Configuration.allDecisionMakers = decisionMakers;
			
			//System.out.println("Reading parameters...");
			File params = new File(pathToRessources + "/parametres"+Configuration.getExtension());
			if (params.exists()){
		       	String s = getChamp("Carte", params)[0];
		       	//System.out.println(s);
		       	if (!s.equals("AUCUNE")){
		    		File carte = new File(pathToRessources + "/environnements/"+s);
		    		if (carte.isFile()){
		//    			System.out.println("Loading map : "+s);
		    			Configuration.environnementACharger = s.split("\\.")[0];
		    		}
		       	}
		       	Configuration.environnementACharger = s.split("\\.")[0];
		       	
		       	String[] str = getChamp("DecisionMaker", params);
		       	if (str == null) {
		       		Configuration.decisionMaker = new WeightedStochasticDecisionMaker(null);
		       	} else {
		       		for (int i = 0 ; i < Configuration.allDecisionMakers.size() ; i++) {
		       			if (Configuration.allDecisionMakers.get(i).toString().equals(str[0])) {
		       				Configuration.decisionMaker = Configuration.allDecisionMakers.get(i);
		       			}
		       		}
		       	}
		       	
		       	if(getChamp("MaxWeight",params) != null && getChamp("MaxWeight",params).length > 0 )
		       	{
		       		String sweight = getChamp("MaxWeight",params)[0];
		       		int temmWeight = Integer.parseInt(sweight);
		       		if(temmWeight > 0)
		       		{
		       			Configuration.weightLimit = temmWeight;
		       		}
		       		else
		       		{
		       			Configuration.weightLimit = Integer.MAX_VALUE;
		       		}
		       	}
		       	else
		       	{
		       		Configuration.weightLimit = Integer.MAX_VALUE;
		       	}
		       	
		       	if(getChamp("RoadCreation",params) != null && getChamp("RoadCreation",params).length > 0 )
		       	{
		       		String sRoad = getChamp("RoadCreation",params)[0];
		       		int temproad = Integer.parseInt(sRoad);
		       		if(temproad > 0)
		       		{
		       			Configuration.passagesPourCreerRoute = temproad;
		       		}
		       		else
		       		{
		       			Configuration.passagesPourCreerRoute = Integer.MAX_VALUE;
		       		}
		       	}
		       	else
		       	{
		       		Configuration.passagesPourCreerRoute = 30;
		       	}
		       	
		       	if(getChamp("Dispersion",params) != null && getChamp("Dispersion",params).length > 0 )
		       	{
		       		String sRoad = getChamp("Dispersion",params)[0];
		       		int temproad = Integer.parseInt(sRoad);
		       		if(temproad == 1)
		       		{
		       			Configuration.dispersion = true;
		       		}
		       		else
		       		{
		       			Configuration.dispersion = false;
		       		}
		       	}
		       	else
		       	{
		       		Configuration.dispersion = false;
		       	}
		       	
		       	if(getChamp("RoadEvaporation",params) != null && getChamp("RoadEvaporation",params).length > 0 )
		       	{
		       		String sRoade = getChamp("RoadEvaporation",params)[0];
		       		int temproade = Integer.parseInt(sRoade);
		       		if(temproade > 0)
		       		{
		       			Configuration.EffacementRoute = temproade;
		       		}
		       		else
		       		{
		       			Configuration.EffacementRoute = Integer.MAX_VALUE;
		       		}
		       	}
		       	else
		       	{
		       		Configuration.EffacementRoute = 1;
		       	}
		       	
		       	if(getChamp("VisionRadius",params) != null && getChamp("VisionRadius",params).length > 0 )
		       	{
		       		String sVision = getChamp("VisionRadius",params)[0];
		       		int tempVision = Integer.parseInt(sVision);
		       		if(tempVision > 0)
		       		{
		       			Configuration.VisionRadius = tempVision;
		       		}
		       		else
		       		{
		       			Configuration.VisionRadius = Integer.MAX_VALUE;
		       		}
		       	}
		       	else
		       	{
		       		Configuration.VisionRadius = 10;
		       	}
			}  
			
			//TODO
			//Configuration.decisionMaker = new WeightedStochasticDecisionMaker(null);
			//Configuration.decisionMaker = new MaxWeightDecisionMaker(null);

		}
	
	
	public static ArrayList<String[]> getListeChamp(String champ ,  File f){
		
		 Scanner scanner;
		 ArrayList<String[]> liste = new ArrayList<String[]>();
		 
		try {
			scanner = new Scanner(new FileReader(f));
			 String str = null;
			 while (scanner.hasNextLine()) {
			     str = scanner.nextLine();			     
			     if(str.split(" : ")[0].equals(champ)){
			    	 if(str.split(" : ").length > 1)
			    	 {
			    		 String list = str.split(" : ")[1];
				    	 liste.add(list.split(","));
			    	 }
			    	
			     }

			   
			 }
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
		return liste;
	}
	
	/**
	 * Load every actions in the parameter file
	 */
	private void loadActions()
	{
		File folder = new File(Configuration.pathToRessources + "/simulation"+"/actions");
	//	System.out.println(Configuration.pathToRessources+"/actions");
		Configuration.actions = new ArrayList<Action>();
		try
		{
			URL fileURL = folder.toURI().toURL();
			URL urls [] = { fileURL};  
			URLClassLoader ucl = new URLClassLoader(urls);
				
			loadActionsRecursif(ucl, folder, "");
		}
		catch (MalformedURLException e1)
		{
			e1.printStackTrace();
		}
	}
	
	/**
	 * Recursive call to load all actions
	 */
	private void loadActionsRecursif(URLClassLoader loader,File folder,String path)
	{
		File[] files = folder.listFiles();
		if(files != null && files.length > 0)
		{
			for (File f : files)
			{
				if (f.isDirectory())
				{
		//		System.out.println("File : " + f.getName());
				loadActionsRecursif(loader,f, path+f.getName()+".");
				}
				else
				{
					try
					{
						
						Action a = null;
					//	System.out.println(path);
					//	System.out.println("Recherche du .DS "+f.getName().substring( f.getName().length() - 6, f.getName().length()));
						if(f.getName().substring( f.getName().length() - 6, f.getName().length()).equals(".class"))
						{
						//	System.out.println(path+f.getName().substring(0, f.getName().length()-6));
							Class<?> c = loader.loadClass(path+f.getName().substring(0, f.getName().length()-6)); // TODO peut être a modifier le   "substring(0, f.getName().length()-6)"  qui correspond au .class 
							if (Action.class.isAssignableFrom(c) && !c.getSimpleName().equals("Action"))
							{
								Action action = (Action) c.newInstance();
								Configuration.actions.add(action);
							}
						}
						
					}
					catch (ClassNotFoundException e)
					{
						e.printStackTrace();
					}
					catch (InstantiationException e)
					{
						e.printStackTrace();
					}
					catch (IllegalAccessException e)
					{
						e.printStackTrace();
					}
//=======

				}
			}
		}
		
	}
	

	


}
