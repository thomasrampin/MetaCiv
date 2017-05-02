package civilisation;

import java.awt.Color;
import java.awt.Image;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import civilisation.amenagement.TypeAmenagement;
import civilisation.effects.Effect;
import civilisation.group.GroupModel;
import civilisation.individu.cognitons.TypeCulturon;
import civilisation.individu.cognitons.TypeCogniton;
import civilisation.individu.decisionMaking.DecisionMaker;
import civilisation.individu.plan.NPlan;
import civilisation.individu.plan.action.Action;
import civilisation.inspecteur.viewer.ViewerTabbed;
import civilisation.inventaire.Objet;
import civilisation.world.Terrain;

/**
 * Contain all major data for running the simulation
 * @author DTEAM
 * @version 1.01 - 2/2013
 */

public class Configuration{

    public static String versionNumber = "1.90";
    public static String versionName = "Euphrates";

    public static ViewerTabbed viewer = null;


    /**
     * Paramétres de base pour la simulation
     */

	/*Decision maker*/
    public static DecisionMaker decisionMaker;
    public static ArrayList<DecisionMaker> allDecisionMakers;

    /*all effects*/
    public static ArrayList<Effect> effets;

    /*Weight limit */
    public static int weightLimit;

    /*Toutes les action disponibles*/
    public static ArrayList<Action> actions;

    /*Item pheromones*/
    public static ArrayList<ItemPheromone> itemsPheromones;

    /*Tout les amenagements*/
    public static ArrayList<TypeAmenagement> amenagements;

    /*Tous les objets*/
    public static ArrayList<Objet> objets;

    /*Terrains*/
    public static ArrayList<Terrain> terrains;

    /*Associations couleurs-terrains*/
    public static HashMap<Color, Terrain> couleurs_terrains;

    /*Environnement ___ charger*/
    public static String environnementACharger;


    //////////////////PATH///////////////////////
	/*Path to icon*/
    public static String pathToIcon = "/bin/civkraft/inspecteur/icones";
    /*Path to ressources*/
    public static String pathToRessources = null;
    public static String pathToCognitiveScheme = "schemasCognitifs";
    public static String pathToSimulation = "simulation";
    public static String pathToCivilisation = "civilisations";


    /*Passages minimaux pour une route*/
    public static Integer passagesPourCreerRoute = 20;
    public static Integer EffacementRoute = 1;
    public static int VisionRadius = 15;

    public static Integer maxAgents = 50000;
    public static boolean dispersion;

    //Civkraft v0.1
    //Ajout d'une civilisation comme civilisation en cours d'édition
    //Possibilité à terme de choisir la civilisation que l'on édite via un menu déroulant
    public static SchemaCognitif SCEnCoursEdition;
    /*Tout les schémas cognitifs*/
    public static ArrayList<SchemaCognitif> SchemasCognitifs;

    /*Toutes les civilisations*/
    public static ArrayList<Civilisation> civilisations;

    /**
     * Méthodes
     */

    public static boolean isDispersion() {
        return dispersion;
    }

    public static void setDispersion(boolean dispersion) {
        Configuration.dispersion = dispersion;
    }

    public static ItemPheromone getPheromoneByName(String s){
        for (int i = 0 ; i < itemsPheromones.size(); i++){
            if (itemsPheromones.get(i).getNom().equals(s)){
                return(itemsPheromones.get(i));
            }
        }
        return null;
    }

    public static Terrain getTerrainByName(String s){
        for (int i = 0 ; i < terrains.size(); i++){
            if (terrains.get(i).getNom().equals(s)){
                return(terrains.get(i));
            }
        }
        return null;
    }

    public static Objet getObjetByName(String s){
        for (int i = 0 ; i < objets.size(); i++){
            if (objets.get(i).getNom().equals(s)){
                return(objets.get(i));
            }
        }
        //	System.exit(-1);
        return null;
    }

    public static Action getActionByName(String s){
        for (int i = 0 ; i < actions.size(); i++){
            String[] tab = actions.get(i).getSimpleName().split("\\.");
            if (tab[tab.length - 1].equals(s)){
                return(actions.get(i));
            }
        }
        return null;
    }

    public static Civilisation getCivilisationByName(String s){
        for (int i = 0 ; i < civilisations.size(); i++){
            if (civilisations.get(i).getNom().equals(s)){
                return(civilisations.get(i));
            }
        }
        return null;
    }

    public static String getExtension() {
        return ".metaciv";
    }

    public static ImageIcon getIcon (String name) {
        try {
            return new ImageIcon(ImageIO.read(Configuration.class.getResourceAsStream("/civilisation/inspecteur/icones/" + name)));
        } catch (IOException e) {
            return null;
        }catch (IllegalArgumentException fnfe)
        {
            try {

                return new ImageIcon(ImageIO.read(Configuration.class.getResourceAsStream("/civilisation/inspecteur/icones/cross.png")));
            } catch (IOException e2) {
                return null;
            }catch (IllegalArgumentException fnfe2)
            {
                return null;
            }
        }
    }

    public static Image getImage (String name) {
        try {
            return ImageIO.read(Configuration.class.getResourceAsStream("/civilisation/inspecteur/icones/" + name));
        } catch (IOException e) {
            return null;
        }catch (IllegalArgumentException fnfe)
        {
            try {
                return ImageIO.read(Configuration.class.getResourceAsStream("/civilisation/inspecteur/icones/cross.png"));
            } catch (IOException e2) {
                return null;
            }catch (IllegalArgumentException fnfe2)
            {
                return null;
            }
        }
    }


    public static Effect getEffectByName(String name)
    {
        int i = 0;
        if(effets.size()  > 0)
        {

            while(i < effets.size() && !effets.get(i).getName().equals(name))
            {
                //		System.out.println(effets.get(i).getName() + " ==== "+name);
                i++;

            }
            if(i < effets.size())
            {
//				System.out.println(" effet : "+ effets.get(i).getName());
                return effets.get(i);
            }
            else
            {
//				System.out.println("null1");
                return null;
            }
        }
        else
        {
//			System.out.println("null2");
            return null;
        }

    }

    public static void addObjetUnique(Objet o) {
        // TODO Auto-generated method stub
        int i = 0;
        while(i < objets.size() && !objets.get(i).getNom().equals(o.getNom()))
        {
            ++i;
        }
        if(i < objets.size())
        {
            objets.remove(i);
        }
        objets.add(o);
    }

    public static void addEffectUnique(Effect temp) {
        // TODO Auto-generated method stub
        int i = 0;
        if(temp != null)
        {
            while(i < effets.size() && !effets.get(i).getName().equals(temp.getName()))
            {
                ++i;
            }
            if(i < effets.size())
            {
                effets.remove(i);
            }
            effets.add(temp);
        }

    }

    public static void addTypeAmenagementUnique(TypeAmenagement a) {
        // TODO Auto-generated method stub
        int i = 0;
        if(a != null)
        {
            while(i < amenagements.size() && !amenagements.get(i).getNom().equals(a.getNom()))
            {
                ++i;
            }
            if(i < amenagements.size())
            {
                amenagements.remove(i);
            }
            amenagements.add(a);
        }
    }

    public static TypeAmenagement getAmenagementsByName(String nom) {
        for (int i = 0 ; i < amenagements.size(); i++){
            if (amenagements.get(i).getNom().equals(nom)){
                return(amenagements.get(i));
            }
        }
        System.exit(-1);
        return null;
    }

    public static TypeAmenagement getAmenagementsByNameFor3D(String nom) {
        if(true)
            for (int i = 0 ; i < amenagements.size(); i++){
                if (amenagements.get(i).getNom().equals(nom)){
                    return(amenagements.get(i));
                }
            }

        return null;
    }

    public static ArrayList<TypeAmenagement> getAllAmenagementFor3D(){
        return amenagements;
    }

    /**
     * Ajout civkraft v0.1
     */

    public static void afficherContenuCivkraft(){
        System.out.println("Je suis Civkraft et voici mon corps");
        System.out.println("Parametres globaux");
        System.out.println("DecisionMaker : " + decisionMaker);
        System.out.println("Actions : ");
        for(Action a : actions){
            System.out.println(a.toString());
        }
        System.out.println("Amenagements :");
        for(TypeAmenagement ta : amenagements){
            System.out.println("\t" + ta.getNom());
        }
        System.out.println("Objets :");
        for(Objet o : objets){
            System.out.println("\t" + o.getNom());
        }

        System.out.println("effets : ");
        for(Effect e : effets){
            System.out.println("\t" + e.getName());
        }
        //System.out.println("actions : " + actions.toString());
        System.out.println("itemsPheromones : ");
        for(ItemPheromone ip : itemsPheromones){
            System.out.println("\t" + ip.getNom());
        }

        System.out.println("terrains : ");
        for(Terrain t : terrains){
            System.out.println("\t" + t.getNom());
        }

        for(Color c : couleurs_terrains.keySet()){
            System.out.println("\t" + couleurs_terrains.get(c).getNom() + " " + c);
        }

        System.out.println("environnementACharger : " + environnementACharger);

        System.out.println("\nCivilisations : " + civilisations.size());
        for(Civilisation c : civilisations){
            System.out.println(c.getNom());
            System.out.println("Attributs");
            for(String s :  c.getAttributs()){
                System.out.println("\t" + s);
            }

            System.out.println("Cognitons");
            for(TypeCogniton t :  c.getCognitons()){
                System.out.println("\t" + t.toString());
            }

            System.out.println("Plans");
            for(NPlan p :  c.getPlans()){
                System.out.println("\t" + p.toString());
            }
            System.out.println("Auto plan : " + c.getAutoPlan().toString());

            System.out.println("groupes");
            for(GroupModel g :  c.getGroups()){
                System.out.println("\t" + g.toString());
            }
            System.out.println("end Transubstantiation");


        }

    }


	/*//remplacé !
	public static Civilisation getCivEnCourEdition(){
		return civEnCoursEdition;
	}
	*/

    public static SchemaCognitif getSchemaCognitifEnCourEdition(){
        return SCEnCoursEdition;
    }

    public static void setSchemaCognitifEnCourEdition(SchemaCognitif sc){
        SCEnCoursEdition = sc;
    }

}

