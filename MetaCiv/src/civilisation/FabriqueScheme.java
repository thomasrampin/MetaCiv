package civilisation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import civilisation.constant.MCConstant;
import civilisation.group.GroupModel;
import civilisation.individu.cognitons.LienCogniton;
import civilisation.individu.cognitons.LienPlan;
import civilisation.individu.cognitons.TypeCogniton;
import civilisation.individu.cognitons.TypeDeCogniton;
import civilisation.individu.cognitons.TypeCulturon;
import civilisation.individu.plan.NPlan;
import civilisation.individu.plan.action.Action;


public class FabriqueScheme {

	public static ArrayList<SchemaCognitif> listeSchemaCognitifs = new ArrayList<SchemaCognitif>();
	
	
	public static SchemaCognitif creerSchemaCognitif(){
		
		SchemaCognitif sc = new SchemaCognitif();
		sc.setNom("New_cognitive_Scheme " + (listeSchemaCognitifs.size()+1));
		
		listeSchemaCognitifs.add(sc);
		
		return sc;
	}
	
	/* Charger a partir d'un fichier */
	public static SchemaCognitif creerSchemaCognitif(String path)
	{
		SchemaCognitif sc = new SchemaCognitif();
		System.out.println("cogScheme at :"+ path);
		File res = new File(path);
		
		if (!res.isHidden())
		{
			for(File f:res.listFiles())
			{
				if(f.getName().endsWith(Configuration.getExtension()))
				{
				    if (f.isFile()){
				    	/* Le nom */
						sc.setNom(Initialiseur.getChamp("Nom", f)[0]);
						Configuration.setSchemaCognitifEnCourEdition(sc);
						// les poids d'influence
						String read = Initialiseur.getChamp("WeightInterval", f)[0];
						if(!read.equals("null"))
							sc.weightInterval = read; 
						read = Initialiseur.getChamp("WeightBound", f)[0];
						if(!read.equals("null"))
							sc.weightsBound = Double.parseDouble(read);
						
						filesToSc(sc,path);
				    }
				}
			}			
		}
		
		listeSchemaCognitifs.add(sc);
		
		return sc;
	}
	
	/* Par copie */
	public static SchemaCognitif creerSchemaCognitif(String nom, SchemaCognitif copy) throws CloneNotSupportedException
	{
		// A tester
		SchemaCognitif sc = copy.clone();
		sc.setNom(nom);
		
		listeSchemaCognitifs.add(sc);
		
		return sc;
	}
	
	public static void supprimerDossier(File cible){
    	if (cible.isDirectory()){
            File [] fileList = cible.listFiles();
            for(int i = 0;i<fileList.length;i++){
            	  supprimerDossier(fileList[i]);
            }
    	}
    	cible.delete();
    }
	
	public static void sauverSchemaCognitif(SchemaCognitif sc){
		sauverSchemaCognitif(sc, DefinePath.pathToSchemas);
	}

	
	/* Sauvegarder avec un path */
	public static void sauverSchemaCognitif(SchemaCognitif sc, String path){
		/* Supprimer le dossier déjà existant */
		File dossierSchema = new File(path);
		File supp = new File(path + "/" + sc.getNom());

		if(dossierSchema.exists()){
			supprimerDossier(supp);
		}
		
		/* Enregistrer tous les attributs */
		System.out.println("--_ Enregistrement des attributs de " + sc.getNom());
			
		File dossierAttributes = new File(dossierSchema.getPath() + "/" + sc.getNom() + DefinePath.pathToAttributes);
		dossierAttributes.mkdirs();
		
		PrintWriter out;	
		for (int i = 0; i < sc.getAttributesNames().size();i++){
			
			File attributes=null;
			try {
				attributes = new File(dossierAttributes.getPath()+"/"+URLEncoder.encode(sc.getAttributesNames().get(i),"UTF-8")+Configuration.getExtension());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				out = new PrintWriter(new FileWriter(attributes.getPath()));
				out.println("Name : " + sc.getAttributesNames().get(i));
				out.println("Starting value : " + sc.getAttributesStartingValues().get(i));	
				
				out.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}

		/* Enregistrer toutes les constantes */
		System.out.println("--_ Enregistrement des constantes de " + sc.getNom());
			
		File dossierConstants = new File(dossierSchema.getPath() + "/" + sc.getNom() + DefinePath.pathToConstants);
		dossierConstants.mkdirs();
		
		for (int i = 0; i < sc.getConstantsNames().size();i++){
			
			File constants= null;
			try {
				constants = new File(dossierConstants.getPath()+"/"+URLEncoder.encode(sc.getConstantsNames().get(i),"UTF-8")+Configuration.getExtension());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				out = new PrintWriter(new FileWriter(constants.getPath()));
				out.println("Name : " + sc.getConstantsNames().get(i));
				out.println("Value : " + sc.getConstantsValues().get(i).getValue());
				out.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		
		/* Enregistrer les cognitons */
		System.out.println("--_ Enregistrement des cognitons de " + sc.getNom());
		File dossierCognitons = new File(dossierSchema.getPath() + "/" + sc.getNom() + DefinePath.pathToCognitons);
		dossierCognitons.mkdirs();

		for (int i = 0; i < sc.getCognitons().size();i++){

			File cognitons = null;
			try {
				cognitons = new File(dossierCognitons.getPath()+"/"+URLEncoder.encode(sc.getCognitons().get(i).getNom(),"UTF-8")+Configuration.getExtension());
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				out = new PrintWriter(new FileWriter(cognitons.getPath()));
				out.println("Nom : " + sc.getCognitons().get(i).getNom());
				out.println("Description : " + sc.getCognitons().get(i).getDescription());
				out.println("Type : " + sc.getCognitons().get(i).getType());
				out.println("StartChance : " + sc.getCognitons().get(i).getStartChance());
				if (sc.getCognitons().get(i).isRecuAuDemarrage()){
					out.println("Initial : 1");
				}
				else{
					out.println("Initial : 0");
				}
				
				for (int j = 0; j < sc.getCognitons().get(i).getPlansAutorises().size() ;j++){
					out.println("Permet : "+sc.getCognitons().get(i).getPlansAutorises().get(j).getNom());
				} 
				
				for (int j = 0; j < sc.getCognitons().get(i).getLiens().size() ;j++){
					out.println("Chaine : "+ sc.getCognitons().get(i).getLiens().get(j).getC().getNom() + "," + sc.getCognitons().get(i).getLiens().get(j).getPoids());
				}
				
				for (int j = 0; j < sc.getCognitons().get(i).getLiensPlans().size() ;j++){
					out.println("Influence : "+sc.getCognitons().get(i).getLiensPlans().get(j).getP().getNom()+","+sc.getCognitons().get(i).getLiensPlans().get(j).getPoids());
				}
		    	
		    	for (int j = 0; j < sc.getCognitons().get(i).getTriggeringAttributes().size(); j++) {
					out.println("Trigger : " + sc.getCognitons().get(i).getTriggeringAttributes().get(j)[0] + "," + sc.getCognitons().get(i).getTriggeringAttributes().get(j)[1] + "," + sc.getCognitons().get(i).getTriggeringAttributes().get(j)[2]+ "," + sc.getCognitons().get(i).getTriggeringAttributes().get(j)[3]);
		    	}
				
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		/* Enregistrer les CloudCognitons */
		File dossierCloudCognitons = new File(dossierSchema.getPath() + "/" + sc.getNom() + DefinePath.pathToCloudCognitons);
		dossierCloudCognitons.mkdirs();
		System.out.println("--_ Save cloud cognitons de " + sc.getNom());
		
		for (int i = 0; i < sc.getCloudCognitons().size();i++){
			File cloudCognitons = null;
			try {
				cloudCognitons = new File(dossierCloudCognitons.getPath()+"/"+URLEncoder.encode(sc.getCloudCognitons().get(i).getNom(),"UTF-8")+Configuration.getExtension());
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
			try {
				out = new PrintWriter(new FileWriter(cloudCognitons.getPath()));
				out.println("Nom : " + sc.getCloudCognitons().get(i).getNom());
				out.println("Description : " + sc.getCloudCognitons().get(i).getDescription());
				out.println("Type : " + sc.getCloudCognitons().get(i).getType());
				out.println("StartChance : " + sc.getCloudCognitons().get(i).getStartChance());
				if (sc.getCloudCognitons().get(i).isRecuAuDemarrage()){
					out.println("Initial : 1");
				}
				else{
					out.println("Initial : 0");
				}
				
				for (int j = 0; j < sc.getCloudCognitons().get(i).getPlansAutorises().size() ;j++){
					out.println("Permet : "+sc.getCloudCognitons().get(i).getPlansAutorises().get(j).getNom());
				} 
				
				for (int j = 0; j < sc.getCloudCognitons().get(i).getLiens().size() ;j++){
					out.println("Chaine : "+ sc.getCloudCognitons().get(i).getLiens().get(j).getC().getNom() + "," + sc.getCloudCognitons().get(i).getLiens().get(j).getPoids());
				}
				
				for (int j = 0; j < sc.getCloudCognitons().get(i).getLiensPlans().size() ;j++){
					out.println("Influence : "+sc.getCloudCognitons().get(i).getLiensPlans().get(j).getP().getNom()+","+sc.getCloudCognitons().get(i).getLiensPlans().get(j).getPoids());
				}
		    	
		    	for (int j = 0; j < sc.getCloudCognitons().get(i).getTriggeringAttributes().size(); j++) {
					out.println("Trigger : " + sc.getCloudCognitons().get(i).getTriggeringAttributes().get(j)[0] + "," + sc.getCloudCognitons().get(i).getTriggeringAttributes().get(j)[1] + "," + sc.getCloudCognitons().get(i).getTriggeringAttributes().get(j)[2]);
		    	}
				
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		
		/* Enregistrer les groupes */
		File dossierGroups = new File(dossierSchema.getPath() + "/" + sc.getNom() + DefinePath.pathToGroups);
		System.out.println("--_ Save groups de " + sc.getNom());
		dossierGroups.mkdirs();
		
		for (int i = 0; i < sc.getGroups().size();i++){
			File groups = null;
			try {
				groups = new File(dossierGroups.getPath()+"/"+URLEncoder.encode(sc.getGroups().get(i).getName(),"UTF-8")+Configuration.getExtension());
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
			try {
				out = new PrintWriter(groups.getPath());
				out.println("Name : " + sc.getGroups().get(i).getName());
				
				Object[] keysRoles = (Object[]) sc.getGroups().get(i).getCulturons().keySet().toArray();
				for (int j = 0; j < keysRoles.length ;j++){
					ArrayList<TypeCogniton> arrayList = sc.getGroups().get(i).getCulturons().get((String)keysRoles[j]);
					
					for (int k = 0 ; k < arrayList.size() ; k++) {
						out.println("Culturon : " + (String)keysRoles[j] +"," + arrayList.get(k).getNom());
					}
				} 
				out.println("AutoJoin : " + sc.getGroups().get(i).isAutoJoin() +"," + sc.getGroups().get(i).getAutoJoinRole());
				
				
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
		}
		
		/* Enregistrer les plans */
		File dossierPlans = new File(dossierSchema.getPath() + "/" + sc.getNom() + DefinePath.pathToPlans);
		System.out.println("--_ Enregistrement des plans de " + sc.getNom());
		dossierPlans.mkdirs();
		
		for (int i = 0; i < sc.getPlans().size();i++){
			
			File plans = null;
			try {
				plans = new File(dossierPlans.getPath()+"/"+URLEncoder.encode(sc.getPlans().get(i).getNom(),"UTF-8")+Configuration.getExtension());
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				out = new PrintWriter(new FileWriter(plans.getPath()));
				out.println("Nom : " + sc.getPlans().get(i).getNom());
				out.println("Birth : " + sc.getPlans().get(i).getIsBirthPlan());
				out.println("Self : " + sc.getPlans().get(i).getIsSelfPlan());

				if (!sc.getPlans().get(i).getActions().isEmpty()){
					for(Action a : sc.getPlans().get(i).getActions())
					{
						sc.getPlans().get(i).ecrireAction(out,0,a);
					}
			
				}	
				out.close();
			} catch (IOException e) {
				// TODO Self-generated catch block
				e.printStackTrace();
			} 
		}
		
		/* Generer le fichier nomSchemaCognitif.metaciv */
		System.out.println("--_ Creation fichier schemaCognitif de " + sc.getNom());
		try {
			out = new PrintWriter(new FileWriter(supp.getPath() + "/" + URLEncoder.encode(sc.getNom(),"UTF-8")+Configuration.getExtension()));
			out.println("Nom : " + sc.getNom());
			out.println("WeightInterval : " + sc.weightInterval);
			out.println("WeightBound : " + sc.weightsBound);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} 
	}
	
	private static void filesToSc(SchemaCognitif sc, String path)
	{
			
			
			//SousDossiers			
		//Attributs
		
		File[] files = new File(path +"/"+ DefinePath.attributes).listFiles();
		ArrayList<String> attributesNames = new ArrayList<String>();
		ArrayList<Double> attributesStartingValues = new ArrayList<Double>();
		if(files != null && files.length >0){
			for (File file : files) {
				if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension())){
				    if (file.isFile()) {
				    	String name = Initialiseur.getChamp("Name" , file)[0];
				    	attributesNames.add(name);
				    	Double startingValue = Double.parseDouble(Initialiseur.getChamp("Starting value" , file)[0]);
				    	attributesStartingValues.add(startingValue);
				    }
				}
			}	
		}
		sc.setAttributesNames(attributesNames);
		sc.setAttributesStartingValues(attributesStartingValues);
		
		//Constants
		
		files = new File(path +"/"+ DefinePath.constants).listFiles();
		ArrayList<String> constantsNames = new ArrayList<String>();
		ArrayList<MCConstant> constantsValues = new ArrayList<MCConstant>();
		if(files != null && files.length >0){
			for (File file : files) {
				if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension())){
				    if (file.isFile()) {
				    	String name = Initialiseur.getChamp("Name" , file)[0];
				    	constantsNames.add(name);
				    	MCConstant values = new MCConstant(Double.parseDouble(Initialiseur.getChamp("Value" , file)[0]));
				    	constantsValues.add(values);
				    }
				}
			}	
		}
		sc.setConstantsNames(constantsNames);
		sc.setConstantsValues(constantsValues);
			
			//Cognitons
			HashMap<String , ArrayList<Object[]>> attributesTrigerringValues = new HashMap<String , ArrayList<Object[]>>();
			HashMap<String, TypeCogniton> listeCognitons = new HashMap<String, TypeCogniton>();
			ArrayList<TypeCogniton> cognitonsDeBase = new ArrayList<TypeCogniton>();
			ArrayList<TypeCogniton> tousLesCognitons = new ArrayList<TypeCogniton>();
			files = new File(path + "/cognitons").listFiles();
			if(files != null && files.length >0){
				for (File file : files) {
					if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension())){
				    if (file.isFile()) {
				    	String nom = Initialiseur.getChamp("Nom" , file)[0];
				    	listeCognitons.put(nom , new TypeCogniton());
				    	TypeCogniton cogni = listeCognitons.get(nom);
				    	cogni.setNom(nom);
				    	cogni.setDescription(Initialiseur.getChamp("Description" , file)[0]);
				    	cogni.setStartChance(Integer.parseInt(Initialiseur.getChamp("StartChance" , file)[0]));
				    	cogni.setType(TypeDeCogniton.toType(Initialiseur.getChamp("Type" , file)[0]));
				    	if (Initialiseur.getChamp("Initial" , file)[0].equals("1")){
					   		cognitonsDeBase.add(cogni);
					    	cogni.setRecuAuDemarrage(true);
				    	}
				    	
				    	//Load triggering attributes
				       	ArrayList<String[]> triggers = Initialiseur.getListeChamp("Trigger", file);
				       	for(int i = 0 ; i < triggers.size(); i++) {
				       		Object[] trig = new Object[5];
				       		/* OH GOD WHY ????
				       		trig[0] = cogni;
				       		trig[1] = Double.parseDouble(triggers.get(i)[1]);
				       		trig[2] = Integer.parseInt(triggers.get(i)[2]);
				       		if(triggers.get(i).length >= 4)
				       		{
				       			trig[3] =triggers.get(i)[3];
				       		}
				       		else
				       		{
				       			trig[3] = DefineConstants.__MC_NULL_CONSTANT;
				       		}
				       		
				       		//Now we add trigger to cognitons to keep the model easy to understand <-- i.e. duplicating data and making a mess
				       		trig = new Object[4];*/
				       		trig[0] = triggers.get(i)[0];
				       		trig[1] = Double.parseDouble(triggers.get(i)[1]);
				       		trig[2] = Integer.parseInt(triggers.get(i)[2]);
				       		if(triggers.get(i).length >= 4)
				       		{
				       			trig[3] = triggers.get(i)[3];
				       		}
				       		else
				       		{
				       			trig[3] = DefineConstants.__MC_NULL_CONSTANT;
				       		}
				       		trig[4] = cogni;
				       		cogni.getTriggeringAttributes().add(trig);
				       		if(attributesTrigerringValues.get(triggers.get(i)[0]) == null) { 
				       			attributesTrigerringValues.put(triggers.get(i)[0] , new ArrayList<Object[]>());
				       			}
				       		attributesTrigerringValues.get(triggers.get(i)[0]).add(trig);
				       	}
				    	tousLesCognitons.add(cogni);
				    }
					}
				}
			}
			sc.setStartingCognitons(cognitonsDeBase);
			sc.setAttributesTrigerringValues(attributesTrigerringValues);
			sc.setCognitons(tousLesCognitons);
			
			//System.out.println("Cognitons faits");

			
			//CloudCognitons
			HashMap<String, TypeCulturon> listCloudCognitons = new HashMap<String, TypeCulturon>();
			ArrayList<TypeCulturon> allCloudCogniton = new ArrayList<TypeCulturon>();
			files = new File(path + "/cloudCognitons").listFiles();
			if(files != null && files.length >0){
				for (File file : files) {
					if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension())){
					//System.out.println("Load culturon : " + file.getName());
				    if (file.isFile()) {
				    	String nom = Initialiseur.getChamp("Nom" , file)[0];
				    	listCloudCognitons.put(nom , new TypeCulturon());
				    	TypeCulturon cogni = listCloudCognitons.get(nom);
				    	cogni.setNom(nom);
				    	cogni.setDescription(Initialiseur.getChamp("Description" , file)[0]);
				    	cogni.setType(TypeDeCogniton.toType( Initialiseur.getChamp("Type" , file)[0]));
				    	if (Initialiseur.getChamp("Initial" , file)[0].equals("1")){
				    		cognitonsDeBase.add(cogni);
					    	cogni.setRecuAuDemarrage(true);
				    	}
				    	
				    	allCloudCogniton.add(cogni);
				    }
					}
				}	
			}
			sc.setCloudCognitons(allCloudCogniton);
						
			//System.out.println("CloudCognitons faits");

			
			//Groups
			files = new File(path + "/groups").listFiles();
			if(files != null && files.length >0){
				for (File file : files) {
					if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension())){
					    if (file.isFile()) {
					    	String nom = Initialiseur.getChamp("Name" , file)[0];
					    	GroupModel g = new GroupModel(nom);
					    	ArrayList<String[]> culturons = getListeChamp("Culturon", file);
					    	for (int i = 0 ; i < culturons.size() ; i++) {
					    		g.addCulturonToRole(culturons.get(i)[0], sc.getCognitonByName(culturons.get(i)[1]));
					    	}
					    	if(Initialiseur.getChamp("AutoJoin" , file).length ==2)
						    	g.setAutoJoin(Boolean.parseBoolean(Initialiseur.getChamp("AutoJoin" , file)[0]), Initialiseur.getChamp("AutoJoin" , file)[1]);
					    	sc.addGroup(g);
					    }
					}
				}	
			}
				
			//Plans
			ArrayList<NPlan> tousLesPlans = new ArrayList<NPlan>();
			HashMap<String, NPlan> listePlans = new HashMap<String, NPlan>();
			File[] filesPlans = new File(path + "/plans").listFiles();
			if(filesPlans != null && filesPlans.length >0){
				for (File file : filesPlans) {
					System.out.println(file.getName());
					if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension())){
					//System.out.println("Creation du plan : " + file.getName());
				    if (file.isFile()) {
				    	String nom = Initialiseur.getChamp("Nom" , file)[0];
				    	listePlans.put(nom , new NPlan());
				    	listePlans.get(nom).setNom(nom);
				       	//ArrayList<String[]> actions = Initialiseur.getListeChamp("Action", file);				       	
				       	setupPlans(listePlans.get(nom), file, 0, 0, null, sc);
				       	listePlans.get(nom).setIsBirthPlan(Boolean.parseBoolean(Initialiseur.getChamp("Birth", file)[0]));
				       	listePlans.get(nom).setIsSelfPlan(Boolean.parseBoolean(Initialiseur.getChamp("Self", file)[0]));
	
				       /*	for (int i = 0; i < actions.size(); i++){
				       		listePlans.get(nom).addAction(actions.get(i));
				       		if (i > 0){
				       			listePlans.get(nom).getActions().get(i-1).setNextAction(listePlans.get(nom).getActions().get(i));
				       		}
				       	}*/
				    	if (listePlans.get(nom).getIsSelfPlan()) {
				    		sc.setAutoPlan(listePlans.get(nom));
				    	}
				    	else if (listePlans.get(nom).getIsBirthPlan()) {
				    		//System.out.println("j'ajoute ca : "+listePlans.get(nom));
				    		sc.setInitiatePlan(listePlans.get(nom));
				    	}
					    tousLesPlans.add(listePlans.get(nom));
				    }
					}
				}	
			}
			
			//System.out.println("Plans faits");

			
			//System.out.println("Creating links...");
			files = new File(path + "/cognitons").listFiles();
			if(files != null && files.length >0){
				for (File file : files) {
					if (!file.isHidden() && file.getName().endsWith(Configuration.getExtension())){
					//System.out.println("Create links of : " + file.getName());
				    if (file.isFile()) {
				    	String nom = Initialiseur.getChamp("Nom" , file)[0];
				    	
				    	/*Les liens inter-cognitons (liens d'apprentissage)*/
				       	ArrayList<String[]> liste = getListeChamp("Chaine", file);
				       	ArrayList<LienCogniton> liens = new ArrayList<LienCogniton>();		      
				       	for (int i = 0 ; i < liste.size(); i++){
				       		liens.add(new LienCogniton(listeCognitons.get(liste.get(i)[0]), Integer.parseInt(liste.get(i)[1])));
				       	}
			       		listeCognitons.get(nom).setLiens(liens);
	
				    	/*Les liens cognitons-plans (liens d'influence)*/
				       	liste = getListeChamp("Influence", file);
				       	ArrayList<LienPlan> liensP = new ArrayList<LienPlan>();		      
				       	for (int i = 0 ; i < liste.size(); i++){
				       		liensP.add(new LienPlan(listePlans.get(liste.get(i)[0]), Integer.parseInt(liste.get(i)[1])));
				       	}
			       		listeCognitons.get(nom).setLiensPlans(liensP);
				       	
				    	/*Les liens cognitons-plans debloques (liens conditionnels)*/
				       	liste = getListeChamp("Permet", file);
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
			}
			
			sc.setCognitons(tousLesCognitons);

			//System.out.println("adding "+ tousLesPlans.size()+" plans to cogScheme : "+sc.name);
			sc.setPlans(tousLesPlans);
	}

	private static ArrayList<String[]> getListeChamp(String champ ,  File f){
		
		 Scanner scanner=null;
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
		}finally{
			scanner.close();
		}
		return liste;
	}
	
	private static void setupPlans(NPlan p , File f, int iteration, int ligne , Action a, SchemaCognitif sc){
		
		 int ligneActuelle = 0;
	     Action nouvelleAction = null;
	     Action ancienneAction = null;
		 Scanner scanner;
		 String champ = "";
		 for (int i = 0; i < iteration; i++){
			 champ += "\t";
		 }
		 champ += "Action";

		 ArrayList<String[]> liste = new ArrayList<String[]>();
		 
		try {
			scanner = new Scanner(new FileReader(f));
			 String str = null;
			 boolean recursionLancee = false;
		//	 System.out.println("Setup Plans");
			 while (scanner.hasNextLine()) {
				 int nTab = -1;
			     str = scanner.nextLine();
			    // System.out.println(str);
			     if (str.split("Action").length > 1){
				     nTab = str.split("Action")[0].length();
			     }

			     ligneActuelle++;
			     
			     if (ligneActuelle+1 > ligne){
				     if(str.split(" : ")[0].equals(champ) && nTab == iteration){
					     ancienneAction = nouvelleAction;
					     nouvelleAction = Action.actionFactory(str.split(" : ")[1].split(","),sc);
					     if (ancienneAction != null && (a == null || a.internActionsAreLinked())){
					    	 ancienneAction.setNextAction(nouvelleAction);
					     }
					     if (a == null){
					    	 p.addAction(nouvelleAction);
					     }
					     else {
					    	 a.addSousAction(nouvelleAction);
					     }
				    	 recursionLancee = false;
				     }
				     else if(nTab > iteration && !recursionLancee){
				    	 recursionLancee = true;
				    	 setupPlans(p,f,iteration+1,ligneActuelle, nouvelleAction,sc);
				     }
				     else if (nTab < iteration && nTab != -1){
				    	 break;
				     }
			     }

			     
			 }
			 
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}


