package civilisation.inspecteur.simulation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import civilisation.Civilisation;
import civilisation.DefineConstants;
import civilisation.DefinePath;
import civilisation.SchemaCognitif;
import civilisation.Configuration;
import civilisation.FabriqueCiv;
import civilisation.FabriqueScheme;

/** 
 * G_re l'interaction avec la fen_tre de des cognitons graphiques
 * @author DTEAM
 * @version 1.0 - 2/2013
*/

public class ActionPanelCognitonsGraphiques implements ActionListener{

	/// sauve tout les fichiers 
	PanelModificationSimulation p;
	int index;
	
	public ActionPanelCognitonsGraphiques(PanelModificationSimulation p, int i)
	{
		this.p = p;
		index = i;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (index == 0) /*Remplacer par la version actuellement visible dans l'_diteur*/
		{
	
			System.out.println("---Enregistrement des param_tres de la simulation---");
			File cible = new File(Configuration.pathToRessources + "");
			//File test = cible;
			
			/* Chemin paramètres globaux */
			File simul = new File(Configuration.pathToRessources + "/simulation");
			
			/* Chemin paramètres civilisation */
			File civ = new File(Configuration.pathToRessources + "/civilisations");

			/*
			 * Save the old environment and actions
			 */
			File environnements = new File(simul.getPath() + "/environnements");
			if (environnements.isDirectory())
				System.out.println("--_ Sauvegarde des environnements de simulation");
				try {
					copierDossier(environnements , new File(System.getProperty("user.dir")+"/TMPenvironnements"));
				} catch (IOException e2) {
					e2.printStackTrace();
				}
	
			File actions = new File(simul.getPath() + "/actions");
			if (actions.isDirectory())
				try {
					copierDossier(actions , new File(System.getProperty("user.dir")+"/TMPactions"));
				} catch (IOException e2) {
					e2.printStackTrace();
				}

			
			
			sauvegardeIncrementale(cible);
			System.out.println("--_ Suppression de l'ancienne version");
			supprimerDossier(new File(DefinePath.pathToSchemas));
			supprimerDossier(new File(DefinePath.pathToSimulation));
			
			if(cible != null)
			{
				cible.mkdirs();
			}
			/*else if(test != null)
			{
				test.mkdirs();
			}
			else
			{
				System.out.println("null");
			}*/
			PrintWriter out = null;
			try {
				while(out == null)
				{
					out = new PrintWriter(cible.getPath()+"/"+"parametres"+Configuration.getExtension());
				}
				
				if (Configuration.environnementACharger == null){
					out.println("Carte : " + "AUCUNE");
				}
				else{
					out.println("Carte : " + Configuration.environnementACharger + Configuration.getExtension());
				}
				out.println("DecisionMaker : " + p.getSelectedDecisionMaker());
				out.println("MaxWeight : " + Configuration.weightLimit);
				out.println("RoadCreation : "+ Configuration.passagesPourCreerRoute);
				out.println("RoadEvaporation : "+Configuration.EffacementRoute);
				out.println("VisionRadius : "+Configuration.VisionRadius);
				if(Configuration.dispersion)
				{
					out.println("Dispersion : "+1);
				}
				else
				{
					out.println("Dispersion : "+0);
				}
				out.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				out.close();
			}

			/* Nouvelle sauvegarde par civilisation */
			for(Civilisation c : FabriqueCiv.listeCiv){	
				FabriqueCiv.sauverCivilisation(c);
			}
			
			for(SchemaCognitif sc : FabriqueScheme.listeSchemaCognitifs){
				FabriqueScheme.sauverSchemaCognitif(sc);
			}
		
			/*
			// Attributs 
			System.out.println("--_ Enregistrement des attributs de " + c.getNom());
			File attributes = new File(civ.getPath()+ "/" + c.getNom() + "/attributes");
			attributes.mkdirs();
			for (int i = 0; i < c.getAttributesNames().size();i++){
				try {
					out = new PrintWriter(new FileWriter(attributes.getPath()+"/"+c.getAttributesNames().get(i)+Configuration.getExtension()));
					out.println("Name : " + c.getAttributesNames().get(i));
					out.println("Starting value : " + c.getAttributesStartingValues().get(i));	
				} catch (IOException e2) {
					e2.printStackTrace();
				} finally{
					out.close();
				}
			}
			
			// Cognitons 
			System.out.println("--_ Enregistrement des cognitons de " + c.getNom());
			File cognitons = new File(civ.getPath() + "/" + c.getNom() + "/cognitons");
			cognitons.mkdirs();
			for (int i = 0; i < c.getCognitons().size();i++){
				System.out.println("Save : " + c.getCognitons().get(i).getNom());
				c.getCognitons().get(i).enregistrer(cognitons);
			}
			
			// Cloud Cognitons 
			System.out.println("--_ Save cloud cognitons de " + c.getNom());
			File cloudCognitons = new File(civ.getPath() + "/" + c.getNom() + "/cloudCognitons");
			cloudCognitons.mkdirs();
			for (int i = 0; i < c.getCloudCognitons().size();i++){
				c.getCloudCognitons().get(i).enregistrer(cloudCognitons);
			}
			
			// Plans 
			System.out.println("--_ Enregistrement des plans de " + c.getNom());
			File plans = new File(civ.getPath() + "/" + c.getNom() + "/plans");
			plans.mkdirs();
			for (int i = 0; i < c.getPlans().size();i++){
				c.getPlans().get(i).enregistrer(plans);
			}
			
			// Groupes 
			System.out.println("--_ Save groups de " + c.getNom());
			File groups = new File(civ.getPath() + "/" + c.getNom() + "/groups");
			groups.mkdirs();
			for (int i = 0; i < c.getGroups().size();i++){
				c.getGroups().get(i).enregistrer(groups);
			}
			
			// Génerer le "nomCivilisation.metaciv" 
			System.out.println("--_ Creation fichier civilisation de " + c.getNom());
			File civilisations = new File(civ.getPath() + "/" + c.getNom());
			civilisations.mkdirs();
			c.enregistrer(civilisations);*/
			
			
			System.out.println("--_ Enregistrement des Effets");
			File effects = new File(simul.getPath() + "/effects");
			effects.mkdirs();
			for (int i = 0; i < Configuration.effets.size();i++){
				Configuration.effets.get(i).enregistrer(effects);
			}

			System.out.println("--_ Enregistrement des objets");
			File objets = new File(simul.getPath() + "/objets");
			objets.mkdirs();
			for (int i = 0; i < Configuration.objets.size();i++){
				Configuration.objets.get(i).enregistrer(objets);
			}
			
			System.out.println("--_ Enregistrement des amenagements");
			File amenagement = new File(simul.getPath() + "/amenagements");
			amenagement.mkdirs();
			for (int i = 0; i < Configuration.amenagements.size();i++){
				Configuration.amenagements.get(i).enregistrer(amenagement);
			}

			System.out.println("--_ Enregistrement des items pheromones");
			File phero = new File(simul.getPath() + "/itemPheromones");
			phero.mkdirs();
			for (int i = 0; i < Configuration.itemsPheromones.size();i++){
				System.out.println("Configuration.itemsPheromones");
				Configuration.itemsPheromones.get(i).enregistrer(phero);
			}			
			
				System.out.println("--_ Enregistrement des terrains");
			File terrains = new File(simul.getPath() + "/terrains");
			terrains.mkdirs();
			for (int i = 0; i < Configuration.terrains.size();i++){
				Configuration.terrains.get(i).enregistrer(terrains);
			}

			/*On remet les environnements en place*/
			environnements = new File(System.getProperty("user.dir")+"/TMPenvironnements");
			System.out.println(environnements.getAbsolutePath() + " "+environnements.isDirectory());
			if (environnements.isDirectory()){
				try {
					copierDossier(environnements , new File(simul.getPath() + "/environnements"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				supprimerDossier(environnements);
			}
			
			actions = new File(System.getProperty("user.dir")+"/TMPactions");
			System.out.println(actions.getAbsolutePath() + " "+actions.isDirectory());
			if (actions.isDirectory())
			{
				try {
					copierDossier(actions , new File(simul.getPath() + "/actions"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				supprimerDossier(actions);
			}
			
			
			
		}
		else if (index == 1) /*Archiver l'ancienne version des ressources*/
		{
			DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
			Date date = new Date();
			
	    	File versionActuelle = new File(Configuration.pathToRessources + "");
	    	File archive = new File(Configuration.pathToRessources + ""+dateFormat.format(date));
	    	System.out.println(Configuration.pathToRessources + ""+" "+dateFormat.format(date));

	    	try {
	    		copierDossier(versionActuelle, archive);
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println("Echec lors de la copie des fichiers");
			}
	    	
		}
		else if (index == 2) 
		{
			p.afficherStructureCognitive();
		}
		else if (index == 3) 
		{
			p.afficherEnvironnement();
		}
		else if (index == 4) 
		{
			p.afficherObjets();
		}
		else if (index == 5) 
		{
			p.afficherCivilisations();
		}
		else if (index == 6) 
		{
			p.afficherAttributes();
		}
		else if (index == 7) 
		{
			p.showGroupManager();
		}
		else if (index == 8) 
		{
			p.afficherAmenagement();
		}
		else if (index == 9) 
		{
			p.afficherConfiguration();
		}
		else if (index == 10) 
		{
			p.afficherConstantes();
		}
		else if (index == 11) 
		{
			System.exit(0);
		}
	}
	
	
    private void sauvegardeIncrementale(File cible) {
    	DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date date = new Date();
		
    	File versionActuelle = new File(Configuration.pathToRessources + "");
    	File archiveRoot = new File(Configuration.pathToRessources + "_backup");
    	if(!archiveRoot.exists()){
    		archiveRoot.mkdirs();
 		}
    	String[] tab = archiveRoot.list();
    	Arrays.sort(tab);
    	for(String s: tab)
    		System.out.println(s);	
    	if(tab.length > 9)
    	{
    		supprimerDossier(new File(Configuration.pathToRessources + "_backup/"+tab[0]));
    	}
    
    	File archive = new File(Configuration.pathToRessources + "_backup"+File.separator+"svg_" +dateFormat.format(date));
    	System.out.println(Configuration.pathToRessources + ""+" "+dateFormat.format(date));
    	
    	if(!archive.exists()){
    		archive.mkdirs();
 		}
    	try {
    		copierDossier(versionActuelle, archive);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Echec lors de la copie des fichiers");
		}
	}


	public static void copierDossier(File src, File dest) throws IOException{
     
    	if(src.isDirectory()){
    	     
    		if(!dest.exists()){
    		   dest.mkdirs();
    		}
 
    		String files[] = src.list();
 
    		for (String file : files) {
    		   File srcFile = new File(src, file);
    		   File destFile = new File(dest, file);
    		   copierDossier(srcFile,destFile);
    		}
 
    	}
    	else{
    		
    		InputStream in = new FileInputStream(src);
    		File destDir = new File(dest.getParent());
    		
    		
    		if(!destDir.exists()){
     		   destDir.mkdirs();
     		}
  
    	    OutputStream out = new FileOutputStream(dest); 
 
	        byte[] buffer = new byte[1024];
	        
	        int length;
	        //copy the file content in bytes 
	        while ((length = in.read(buffer)) > 0){
	    	   out.write(buffer, 0, length);
	        }

	        in.close();
	        out.close();
    	}
    }


    public void supprimerDossier(File cible){
    	if (cible.isDirectory()){
            File [] fileList = cible.listFiles();
            for(int i = 0;i<fileList.length;i++){
            	  supprimerDossier(fileList[i]);
            }
    	}
    	cible.delete();
    }
}
