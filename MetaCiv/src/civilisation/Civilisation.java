package civilisation;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import madkit.kernel.AbstractAgent;
import civilisation.group.Group;
import civilisation.group.GroupModel;
import civilisation.individu.cognitons.TypeCogniton;
import civilisation.individu.cognitons.TypeCulturon;
import civilisation.individu.plan.NPlan;
import civilisation.world.World;

public class Civilisation extends AbstractAgent implements Cloneable{

    static ArrayList<Civilisation> listeCiv = new ArrayList<Civilisation>();
    ArrayList<Group> autoGroups = new ArrayList<Group>();

    public ArrayList<Group> getAutoGroups() {
        return autoGroups;
    }

    public void setAutoGroups(ArrayList<Group> autoGroups) {
        this.autoGroups = autoGroups;
    }

    /**
     * Dossiers de civ
     */

    SchemaCognitif cerveau;

    /**
     * Paramétre.metaciv de civ
     */
    String nom;
    Color couleur;
    int agentsInitiaux;
    int scatteredModifier;
    boolean mustBeSaved = true;
    int startX,startY;

    public Civilisation(String path)
    {
        // todo
    }

    public Civilisation(String nom , Color couleur, int nbAgents, int scatter, SchemaCognitif sc)
    {
        this.nom = nom;
        this.couleur = couleur;
        this.agentsInitiaux = nbAgents;
        this.scatteredModifier = scatter;
        this.cerveau = sc;
    }

    public Civilisation ()
    {
        couleur = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
        cerveau = null;
    }

    public Civilisation createDaugtherCivilization() {
        Civilisation civ = new Civilisation();

        civ.setAgentsInitiaux(agentsInitiaux);

        civ.setCognitiveScheme(cerveau);
		/*
		civ.setAttributesNames(attributesNames);
		civ.setAttributesStartingValues(attributesStartingValues);
		civ.setAttributesTrigerringValues(attributesTrigerringValues);
		civ.setStartingCognitons(startingCognitons);
		civ.setCognitons(cognitons);
		civ.setCloudCognitons(cloudCognitons);
		civ.setPlans(plans);
		*/
        //civ.setGroups(groups); faut il ajouter les groupes?

        mustBeSaved = false;
        Configuration.civilisations.add(civ);
        return civ;
    }


    public void setCognitiveScheme(SchemaCognitif cerveau2) {
        cerveau = cerveau2;
    }

    public SchemaCognitif getCognitiveScheme() {
        return cerveau;
    }

    public Civilisation (int nb){
        agentsInitiaux = nb;
        cerveau = null;

		/*
		attributesNames = new ArrayList<String>();
		attributesStartingValues = new ArrayList<Double>();
		attributesTrigerringValues = new HashMap<String,ArrayList<Object[]>>();
		startingCognitons = new ArrayList<TypeCogniton>();
		cognitons = new ArrayList<TypeCogniton>();
		cloudCognitons = new ArrayList<TypeCulturon>();
		plans = new ArrayList<NPlan>();
		groups = new ArrayList<GroupModel>();
		*/
    }

    public Civilisation clone() throws CloneNotSupportedException {
        Civilisation civ = null;

        // On récupère l'instance à renvoyer par l'appel de la
        // méthode super.clone()
        civ = (Civilisation) super.clone();

        // On clone les attributs cloneable
        //Soit on clone le cerveau
        //civ.cerveau = cerveau.clone();
        //Soit on pointe le même cerveau
        civ.cerveau = cerveau;

        // on renvoie le clone
        return civ;
    }

    /**
     * Méthodes
     */

    public void enregistrer(File cible) {
        if (mustBeSaved) {
            PrintWriter out;
            try {
                out = new PrintWriter(new FileWriter(cible.getPath()+"/"+getNom()+Configuration.getExtension()));
                out.println("Nom : " + getNom());
                out.println("Agents : " + agentsInitiaux);
                out.println("Scattered : " + this.scatteredModifier);

                float hsb[] = Color.RGBtoHSB( this.getCouleur().getRed(),this.getCouleur().getGreen(),this.getCouleur().getBlue(), null );
                out.println("Couleur : "+hsb[0]+","+hsb[1]+","+hsb[2]);

                out.println("Cognitive scheme : "+cerveau.getNom());

			/*
			for (int i = 0 ; i < startingCognitons.size() ; i++) {
				out.println("Cogniton : "+startingCognitons.get(i).getNom());
			}
			*/

                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }
    }

    //-------------GETTERS-------------

    public Color getCouleur() {
        return couleur;
    }

    public int getIndexCiv()
    {
        //Civkraft v0.1
        int i=0;
        while(!Configuration.civilisations.get(i).getNom().equals(nom) && i < Configuration.civilisations.size()){
            i++;
        }
        if(i<Configuration.civilisations.size())
        {
            return i;
        }else
        {
            return 0;
        }
        //return indexCiv; original
    }
	/*
	public static int getNombreCiv()
	{
		return nombreCiv;
	}*/

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public static ArrayList<Civilisation> getListeCiv() {
        return listeCiv;
    }

    public static void setListeCiv(ArrayList<Civilisation> listeCiv) {
        Civilisation.listeCiv = listeCiv;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }

    public int getAgentsInitiaux() {
        return agentsInitiaux;
    }

    public void setAgentsInitiaux(int agentsInitiaux) {
        this.agentsInitiaux = agentsInitiaux;
    }

    public void postWorldSetup() {
        World.getInstance().addFlavor("civ"+nom);
    }

    public ArrayList<TypeCogniton> getStartingCognitons() {
        return cerveau.getStartingCognitons();
    }

    public void setStartingCognitons(ArrayList<TypeCogniton> s) {
        cerveau.setStartingCognitons(s);
    }

    public int getScatteredModifier() {
        return scatteredModifier;
    }

    public void setScatteredModifier(int scatteredModifier) {
        this.scatteredModifier = scatteredModifier;
    }

    /**
     * Ajouts Civkraft v0.1
     */
    @Override
    public String toString()
    {
        return getNom();
    }

    public void setAttributesNames(ArrayList<String> a){
        cerveau.setAttributesNames(a);
    }

    public void setAttributesStartingValues(ArrayList<Double> a){
        cerveau.setAttributesStartingValues(a);
    }


    public void setAttributesTrigerringValues(HashMap<String,ArrayList<Object[]>> a){
        cerveau.setAttributesTrigerringValues(a);
    }

    public HashMap<String,ArrayList<Object[]>> getAttributesTrigerringValues(){
        return cerveau.getAttributesTrigerringValues();
    }

    public void setCognitons(ArrayList<TypeCogniton> c){
        cerveau.setCognitons(c);
    }

    public void setCloudCognitons(ArrayList<TypeCulturon> c){
        cerveau.setCloudCognitons(c);
    }

    public ArrayList<TypeCulturon> getCloudCognitons(){
        return cerveau.getCloudCognitons();
    }


    public void addGroup(GroupModel g){
        cerveau.addGroup(g);
    }

    public void setAutoPlan(NPlan p){
        cerveau.setAutoPlan(p);
    }

    public void setInitiatePlan(NPlan p){
        cerveau.setInitiatePlan(p);
    }

    public void setPlans(ArrayList<NPlan> p){
        cerveau.setPlans(p);
    }

    public ArrayList<String> getAttributs(){
        return cerveau.getAttributs();
    }

    public ArrayList<TypeCogniton> getCognitons(){
        return cerveau.getCognitons();
    }

    public ArrayList<NPlan> getPlans(){
        return cerveau.getPlans();
    }

    public NPlan getInitiatePlan(){
        return cerveau.getInitiatePlan();
    }

    public NPlan getAutoPlan(){
        return cerveau.getAutoPlan();
    }

    public ArrayList<String> getAttributesNames(){
        return cerveau.getAttributesNames();
    }

    public ArrayList<Double> getAttributesStartingValues(){
        return cerveau.getAttributesStartingValues();
    }

    public TypeCogniton getCognitonByName(String s){
        return cerveau.getCognitonByName(s);
    }

    public GroupModel getGroupModelByName(String s){
        return cerveau.getGroupModelByName(s);
    }

    public ArrayList<GroupModel> getGroups(){
        return cerveau.getGroups();
    }

    public void removePlan(NPlan nouveauPlan) {
        cerveau.removePlan(nouveauPlan);
    }

    public void removeCogniton(TypeCogniton c){
        cerveau.removeCogniton(c);
    }

    public void removeTriggersOfCogniton(TypeCogniton refType) {
        cerveau.removeTriggersOfCogniton(refType);
    }

    public int getAttributeIndexByName(String s){
        return cerveau.getAttributeIndexByName(s);
    }

	/*// WHY SO STATIC ???
	public static int getAttributeIndexByName(String s){
		for (int i = 0 ; i < attributesNames.size(); i++){
			if (attributesNames.get(i).equals(s)){
				return i;
			}
		}
		return -1;
	}*/

    public void addCogniton(TypeCogniton nouveauCogniton) {
        cerveau.addCogniton(nouveauCogniton);
    }

    public void addPlan(NPlan nouveauPlan) {
        cerveau.addPlan(nouveauPlan);
    }

    public void addCloudCogniton(TypeCulturon newCloudCogniton) {
        cerveau.addCloudCogniton(newCloudCogniton);
    }

    public void addStartingCognitons(TypeCogniton nouveauCogniton) {
        cerveau.addStartingCognitons(nouveauCogniton);
    }

    public void removeStartingCognitons(TypeCogniton c){
        cerveau.removeStartingCognitons(c);
    }

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

    

}
