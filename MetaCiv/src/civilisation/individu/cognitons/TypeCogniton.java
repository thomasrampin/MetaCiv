package civilisation.individu.cognitons;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import civilisation.Civilisation;
import civilisation.Configuration;
import civilisation.SchemaCognitif;
import civilisation.individu.Esprit;
import civilisation.individu.plan.NPlan;

public class TypeCogniton implements Cloneable, Serializable{

	TypeDeCogniton typeDeCogniton;
	String nom;
	String description;
	boolean recuAuDemarrage = false;
	int startChance = 0;
	ArrayList<LienCogniton> liens; /*Lien avec les cognitons debloques par ce cogniton*/
	ArrayList<LienPlan> liensPlans; /*Lien avec les plans qu'influence ce cogniton*/
	ArrayList<NPlan> plansAutorises;
	ArrayList<Object[]> triggeringAttributes;
	private Color CustomColor;
	private Boolean DisplayCustomColor;
	
	/*public final static int nHues = 7;
	Integer[] hues = new Integer[nHues];
	public final static Color[] hueColors = {Color.gray , Color.pink , Color.red , Color.blue, Color.orange , Color.green , Color.yellow};
		/* Color from developement spiral (hue):
		 * 0 = brown
		 * 1 = purple
		 * 2 = red
		 * 3 = blue
		 * 4 = orange
		 * 5 = green
		 * 6 = yellow
		 */
	

	
	public TypeCogniton(){
		liens = new ArrayList<LienCogniton>();
		liensPlans = new ArrayList<LienPlan>();
		plansAutorises = new ArrayList<NPlan>();
		triggeringAttributes = new ArrayList<Object[]>();
		CustomColor = Color.WHITE;
		DisplayCustomColor = false;

		/*for (int i = 0 ; i < hues.length; i++){
			hues[i] = 0;
		}*/
	}
	
	

	public TypeCogniton clone() throws CloneNotSupportedException {
	    TypeCogniton tcog = null;

    	// On r�cup�re l'instance � renvoyer par l'appel de la 
      	// m�thode super.clone()
      	tcog = (TypeCogniton) super.clone();
	    
	    // On clone les attributs cloneable
      /*	for(int i=0; i<liens.size(); i++){
      		tcog.liens.add(liens.get(i).clone());
      	}
      	System.out.println("J'ai fini de cloner liens");
      	for(int i=0; i<liensPlans.size(); i++){
      		tcog.liensPlans.add(liensPlans.get(i).clone());
      	}
      	System.out.println("J'ai fini de cloner liensPlans");
      	for(int i=0; i<plansAutorises.size(); i++){
      		tcog.plansAutorises.add(plansAutorises.get(i).clone());
      	}
      	System.out.println("J'ai fini de cloner plansAutorises");*/
	    // on renvoie le clone
	    return tcog;
	}
	
	@Override
	public String toString(){
		return (" Nom : " + nom
				+ "\n Description : " + description
				+ " \n Type : " + typeDeCogniton
				+ " \n Liens : " + liens
				+ " \n Liens avec les plans : " + liensPlans
				+ " \n Plans autoris_s : " + plansAutorises
				+"\n\n"
				);
	}

	public void mettreEnPlace(Esprit e , double weigth){
		modifierPlans(true , e);
		if (plansAutorises.isEmpty()){ 
			appliquerPoids(e , weigth);
		}
		else{
			e.redefinirPoids();
		}
	}
	
	
	
	
	public void appliquerPoids(Esprit e , double weigth)
	{
		for (int i = 0 ; i < liensPlans.size() ; i++)
		{
			e.modifierPoids(liensPlans.get(i).getP(), (float)(liensPlans.get(i).getPoids()*weigth));
		}
	}
	
	
	/**
	 * Ajoute ou retire des projets a l'esprit associ_
	 * @param nouveauxPlans : liste des plans _ ajouter ou enlever
	 * @param add : true indique qu'il faut les ajouter, false les enlever
	 * @param e : l'esprit associ_
	 */
	public void modifierPlans(Boolean add, Esprit e)
	{
		//.out.println("Modifier projet:" + getNom());
		//Plan nPlans[] = getTabNouveauxPlans(e);
		if (!plansAutorises.isEmpty())
		{
			//System.out.println("Modifier projet != null:" + getNom());
			int taille = plansAutorises.size();
			if (add)
			{
				for (int i = 0 ; i < taille ; i++)
				{
					NPlan p = plansAutorises.get(i);
					if (!e.ownPlan(p))
					e.addPlan(p);
				}
				e.redefinirPoids();
			}
			else
			{
				for (int i = 0 ; i < taille ; i++)
				{
					e.removePlan(plansAutorises.get(i));
				}
			}
		}
	}
	
	
	
	public TypeDeCogniton getType() {
		return typeDeCogniton;
	}

	public void setType(TypeDeCogniton typeDeCogniton) {
		this.typeDeCogniton = typeDeCogniton;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayList<LienCogniton> getLiens() {
		return liens;
	}

	public void setLiens(ArrayList<LienCogniton> liens) {
		this.liens = liens;
	}

	public ArrayList<LienPlan> getLiensPlans() {
		return liensPlans;
	}

	public void setLiensPlans(ArrayList<LienPlan> liensP) {
		this.liensPlans = liensP;
	}

	public ArrayList<NPlan> getPlansAutorises() {
		return plansAutorises;
	}

	public void setPlansAutorises(ArrayList<NPlan> plansAutorises) {
		this.plansAutorises = plansAutorises;
	}
	
	public void creerCognitonLambda(SchemaCognitif c){
		nom = "nouveauCogniton"; /*TODO : faire un nom unique*/
		description = "Un nouveau cogniton";
		typeDeCogniton = TypeDeCogniton.BELIEF;
	}

	public boolean isRecuAuDemarrage() {
		return recuAuDemarrage;
	}

	public void setRecuAuDemarrage(boolean recuAuDemarrage) {
		this.recuAuDemarrage = recuAuDemarrage;
	}

	/*public Integer[] getHues() {
		return hues;
	}

	public void setHues(Integer[] spiralColors) {
		this.hues = spiralColors;
	}*/
	
	public int getStartChance() {
		return startChance;
	}

	public void setStartChance(int startChance) {
		this.startChance = startChance;
	}

	public void enregistrer(File cible) {
		PrintWriter out;
		try {
			out = new PrintWriter(new FileWriter(cible.getPath()+"/"+getNom()+Configuration.getExtension()));
			out.println("Nom : " + getNom());
			out.println("Description : " + getDescription());
			out.println("Type : " + getType());
			out.println("StartChance : " + this.startChance);
			out.println("Couleur : "+this.CustomColor);
			if (recuAuDemarrage){
				out.println("Initial : 1");
			}
			else{
				out.println("Initial : 0");
			}
			
			for (int i = 0; i < this.plansAutorises.size() ;i++){
				out.println("Permet : "+plansAutorises.get(i).getNom());
			} 
			
			for (int i = 0; i < this.liens.size() ;i++){
				out.println("Chaine : "+ this.liens.get(i).getC().getNom() + "," + this.liens.get(i).getPoids());
			}
			
			for (int i = 0; i < this.liensPlans.size() ;i++){
				out.println("Influence : "+liensPlans.get(i).getP().getNom()+","+liensPlans.get(i).getPoids());
			}
			
	    	/*for (int i = 0; i < nHues; i++) {
				out.println("Hue" + i + " : " + hues[i]);
	    	}*/
	    	
	    	for (int i = 0; i < triggeringAttributes.size(); i++) {
				out.println("Trigger : " + triggeringAttributes.get(i)[0] + "," + triggeringAttributes.get(i)[1] + "," + triggeringAttributes.get(i)[2]);
	    	}
			
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}

	public ArrayList<Object[]> getTriggeringAttributes() {
		return triggeringAttributes;
	}

	public void setTriggeringAttributes(ArrayList<Object[]> triggeringAttributes) {
		this.triggeringAttributes = triggeringAttributes;
	}

	public void removePlan(NPlan refP) {
		for(Iterator<LienPlan> it = liensPlans.iterator(); it.hasNext();)
		{
			LienPlan link = it.next();
			if(link.p.equals(refP))
				it.remove();
		}
		for(Iterator<NPlan> it2 = plansAutorises.iterator(); it2.hasNext();)
		{
			NPlan plan = it2.next();
			if(plan.equals(refP))
				it2.remove();
		}
	}

	@Override
	public boolean equals(Object t)
	{
		if(((TypeCogniton)t).getNom().equals(nom))
			return true;
		else 
			return false;
				
	}
	
	public Color getCustomColor() {
		return CustomColor;
	}

	public void setCustomColor(Color customColor) {
		CustomColor = customColor;
	}
	
	public Boolean getDisplayCustomColor() {
		return DisplayCustomColor;
	}

	public void setDisplayCustomColor(Boolean displayCustomColor) {
		DisplayCustomColor = displayCustomColor;
	}
}
