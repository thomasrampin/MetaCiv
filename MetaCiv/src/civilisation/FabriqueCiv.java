package civilisation;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FabriqueCiv {

	public static ArrayList<Civilisation> listeCiv = new ArrayList<Civilisation>();
	
	public static Civilisation creerCivilisation(){
		
		Civilisation civ = new Civilisation();
		civ.setNom("Default_Civ_" + FabriqueCiv.listeCiv.size());
		
		/* Couleur random */
		Color couleur = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
		civ.setCouleur(couleur);
		
		/* Schema vide */
		civ.setCognitiveScheme(null);
		
		listeCiv.add(civ);
		
		return civ;
	}
	
	public static Civilisation creerCivilisation(String nom , Color couleur, int nbAgents, SchemaCognitif sc){
		Civilisation civ = new Civilisation();
		civ.setNom(nom);
		civ.setCouleur(couleur);
		civ.setAgentsInitiaux(nbAgents);
		civ.setCognitiveScheme(sc);
		
		listeCiv.add(civ);
		
		return civ;
	}
	
	/* Par copie */
	public static Civilisation creerCivilisation(String nom, Civilisation c) throws CloneNotSupportedException{

		// A tester
		Civilisation civ = c.clone();
		civ.setNom(nom);
		
		listeCiv.add(civ);
		
		return civ;
	}
	
	/* Avec un path, "Parce que c'est plus cool" - François (~5 min) */
	public static Civilisation creerCivilisation(String path)
	{
		Civilisation civ = new Civilisation();
		System.out.println("civ at :"+ path);
		File res = new File(path);
		
		if (!res.isHidden() && res.getName().endsWith(Configuration.getExtension()))
		{
		    if (res.isFile()){
				/* Le nom */
				String nom = Initialiseur.getChamp("Nom", res)[0];
				civ.setNom(nom);
				
				/* La couleur */
				String[] HSB = Initialiseur.getChamp("Couleur" , res);
		    	civ.setCouleur(Color.getHSBColor((float)Double.parseDouble(HSB[0]), (float)Double.parseDouble(HSB[1]), (float)Double.parseDouble(HSB[2])));
		    	
		    	/* Agents */
		    	String agents = Initialiseur.getChamp("Agents", res)[0];
		    	civ.setAgentsInitiaux(Integer.parseInt(agents));
		    	
		    	/*Scattered*/
		    	String scat = Initialiseur.getChamp("Scattered", res)[0];
		    	civ.setScatteredModifier(Integer.parseInt(scat));
		    	
		    	/* Cerveau */
		    	String schema = Initialiseur.getChamp("SchemaCognitif", res)[0];
		    	for(SchemaCognitif sc : FabriqueScheme.listeSchemaCognitifs){
		    		if(sc.getNom().equals(schema)){
		    			civ.setCognitiveScheme(sc);break;
		    		}
		    	}
		    	System.out.println(civ.getCognitiveScheme());
		    	
		    	/*Route pour la 3d*/
		    	String road = Initialiseur.getChamp("RouteTexture", res)[0];
		    	civ.setRoadTexture(road);
		    }
		}
    	
    	listeCiv.add(civ);
    	
    	return civ;
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
	
	 public static void sauverCivilisation(Civilisation civ){
		 
			File dossierCivPath = new File(DefinePath.pathToCivilisations);
			File civilisations = new File(dossierCivPath.getPath()+"/"+civ.getNom()+Configuration.getExtension());
			
			if(civilisations.isFile()){
				supprimerDossier(civilisations);
			}
			
			if(dossierCivPath != null){
				dossierCivPath.mkdirs();
			}
			
			System.out.println("--_ Creation fichier civilisation de " + civ.getNom());

			PrintWriter out;
			try {
				out = new PrintWriter(new FileWriter(civilisations.getPath()));
				out.println("Nom : " + civ.getNom());
				out.println("Agents : " + civ.getAgentsInitiaux());
				out.println("Scattered : " + civ.getScatteredModifier());

			    float hsb[] = Color.RGBtoHSB( civ.getCouleur().getRed(),civ.getCouleur().getGreen(),civ.getCouleur().getBlue(), null );
				out.println("Couleur : "+hsb[0]+","+hsb[1]+","+hsb[2]);	
				out.println("SchemaCognitif : "+civ.getCognitiveScheme().getNom());
						
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			} 
	 }
	 
	 /* Sauvegarder avec un path */
	 public static void sauverCivilisation(Civilisation civ, String path){
		 	
			File dossierCivPath = new File(path+"/civilisations");
			File civilisations = new File(dossierCivPath.getPath()+"/"+civ.getNom()+Configuration.getExtension());
			
			if(civilisations.isFile()){
				supprimerDossier(civilisations);
			}
			
			if(dossierCivPath != null){
				dossierCivPath.mkdirs();
			}
			
			System.out.println("--_ Creation fichier civilisation de " + civ.getNom()+ ". Emplacement : " + path);

			PrintWriter out;
			try {
				out = new PrintWriter(new FileWriter(civilisations.getPath()));
				out.println("Nom : " + civ.getNom());
				out.println("Agents : " + civ.getAgentsInitiaux());
				out.println("Scattered : " + civ.getScatteredModifier());

			    float hsb[] = Color.RGBtoHSB( civ.getCouleur().getRed(),civ.getCouleur().getGreen(),civ.getCouleur().getBlue(), null );
				out.println("Couleur : "+hsb[0]+","+hsb[1]+","+hsb[2]);	
				out.println("SchemaCognitif : "+civ.getCognitiveScheme().getNom());
						
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			} 
	 }
}
