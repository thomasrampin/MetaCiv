package civilisation;

import java.awt.Container;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import civilisation.constant.MCConstant;
import civilisation.group.GroupModel;
import civilisation.individu.cognitons.LienCogniton;
import civilisation.individu.cognitons.LienPlan;
import civilisation.individu.cognitons.TypeCogniton;
import civilisation.individu.cognitons.TypeCulturon;
import civilisation.individu.plan.NPlan;

public class SchemaCognitif implements Cloneable, Serializable {
	
	static ArrayList<SchemaCognitif> listeSchemaCognitifs = new ArrayList<SchemaCognitif>();
	
	public String name = "default_Cognitive_Scheme_name";
	
	/*Configuration des poids*/
	public double weightsBound = 10;
	public String weightInterval = DefineConstants._WeightIntervalBoth;
	
	/*Attributes*/
	public ArrayList<String> attributesNames;
	public ArrayList<Double> attributesStartingValues;
	public HashMap<String,ArrayList<Object[]>> attributesTrigerringValues;
	
	/*Constants*/
	public ArrayList<String> constantsNames;
	public ArrayList<MCConstant> constantsValues;
	
	/*All starting cognitons*/
	public ArrayList<TypeCogniton> startingCognitons;
	
	/*All cognitons*/
	public ArrayList<TypeCogniton> cognitons;
	
	/*All cloud cognitons*/
	public ArrayList<TypeCulturon> cloudCognitons;
	
	/*All plans*/
	public ArrayList<NPlan> plans;
	public NPlan initiatePlan;
	public NPlan autoPlan;

	/*Group model*/
	public ArrayList<GroupModel> groups;
	
	
	//METHODS

	//constructeur vide
	public SchemaCognitif()
	{
		// ajout automatique a la liste g�n�rale
		listeSchemaCognitifs.add(this);
		attributesNames = new ArrayList<String>();
		attributesStartingValues = new ArrayList<Double>();
		constantsNames = new ArrayList<String>();
		constantsValues = new ArrayList<MCConstant>();
		attributesTrigerringValues = new HashMap<String,ArrayList<Object[]>>();
		startingCognitons = new ArrayList<TypeCogniton>();
		cognitons = new ArrayList<TypeCogniton>();
		cloudCognitons = new ArrayList<TypeCulturon>();
		plans = new ArrayList<NPlan>();
		groups = new ArrayList<GroupModel>();
		startingCognitons = new ArrayList<TypeCogniton>();
		name = "New cognitive Scheme " + (listeSchemaCognitifs.size()+1);
	}
	
	public SchemaCognitif clone() throws CloneNotSupportedException {
	    SchemaCognitif sc = null;


    	// On r�cup�re l'instance � renvoyer par l'appel de la 
      	// m�thode super.clone()

      	sc = (SchemaCognitif) super.clone();
	    // On clone les attributs cloneable
      	if(initiatePlan != null){
      		sc.initiatePlan = initiatePlan.clone();
      	}
      	if(autoPlan != null){
      		sc.autoPlan = autoPlan.clone();
      	}
      	
      	
      	/**
      	 * Le clonage n'est pas possible liste par liste � l'arrache
      	 * Il faut cloner les �l�ments de bases (cognitons, plans et groups) puis modifier les liens pour les rediriger
      	 * vers les �l�ments clon�s et non pas cloner chaque �l�ment en parcourant les liens (r�cursion quasi infinie et
      	 * dupliquant les �l�ments plusieurs dizaines de fois)
      	 */
      //	System.out.println(this.cognitons.size());
      	ArrayList<TypeCogniton> cognitonsClones = new ArrayList<TypeCogniton>();
      	for(int i=0; i<this.cognitons.size(); i++){
      		cognitonsClones.add(this.cognitons.get(i).clone());
      	}
      	
      	
      	
      //	System.out.println(this.plans.size());
      	ArrayList<NPlan> plansClones = new ArrayList<NPlan>();
      	for(int i=0; i<this.plans.size(); i++){
      		plansClones.add(this.plans.get(i).clone());
      	}
      //	System.out.println("J'ai fini de cloner plans");
      //	System.out.println("Plans de bases : " + this.plans);
      //	System.out.println("Plans clon�s : " + plansClones);      	

      	
      	//System.out.println(this.groups.size());
      	ArrayList<GroupModel> groupsClones = new ArrayList<GroupModel>();
      	for(int i=0; i<this.groups.size(); i++){
      		groupsClones.add(this.groups.get(i).clone());
      	}
      	//System.out.println("J'ai fini de cloner groups");
      	//System.out.println("Groups de bases : " + this.groups);
      	//System.out.println("Groups clon�s : " + groupsClones);
      	
     // 	System.out.println(this.cloudCognitons.size());
      	ArrayList<TypeCulturon> cloudCognitonsClones = new ArrayList<TypeCulturon>();
      	for(int i=0; i<this.cloudCognitons.size(); i++){
      		cloudCognitonsClones.add((TypeCulturon)cloudCognitons.get(i).clone());
      	}
      	//System.out.println("J'ai fini de cloner cloudCognitons");
      	//System.out.println("Groups de bases : " + this.cloudCognitons);
      	//System.out.println("Groups clon�s : " + cloudCognitonsClones);
      	
      	
      	//And here we go for the fucking liens
      	for(TypeCogniton tc : cognitonsClones){
      		/*
      		 * ArrayList<LienCogniton> liens; 
      		 * ArrayList<LienPlan> liensPlans; 
			 * ArrayList<NPlan> plansAutorises;
      		 */
      		
      		TypeCogniton tcDeBase = getTypeCognitonEquivalentNonClone(tc, this.cognitons);
      		
      		ArrayList<LienCogniton> liensC = new ArrayList<LienCogniton>();
      		if(tcDeBase.getLiens() != null && tcDeBase.getLiens().size() > 0 ){
	      		for(LienCogniton lc : tcDeBase.getLiens()){
	      			LienCogniton lcClone = new LienCogniton(getTypeCognitonEquivalentClone(lc.getC(), cognitonsClones) ,lc.getPoids());
	      			liensC.add(lcClone);
	      		}
      		}
      		tc.setLiens(liensC);
      		
      		ArrayList<LienPlan> liensP = new ArrayList<LienPlan>();    
      		if(tcDeBase.getLiensPlans() != null && tcDeBase.getLiensPlans().size() > 0 ){
	      		for(LienPlan lp : tcDeBase.getLiensPlans()){
	      			LienPlan lpClone = new LienPlan(getNPlanEquivalentClone(lp.getP(), plansClones) ,lp.getPoids());
	      			liensP.add(lpClone);
	      		}
      		}
      		tc.setLiensPlans(liensP);
      		
      		ArrayList<NPlan> plansAu = new ArrayList<NPlan>();
      		if(tcDeBase.getPlansAutorises() != null && tcDeBase.getPlansAutorises().size() > 0 ){
	      		for(NPlan np : tcDeBase.getPlansAutorises()){
	      			plansAu.add(getNPlanEquivalentClone(np, plansClones));
	      		}
      		}
      		tc.setPlansAutorises(plansAu);
      	}
      	//System.out.println("J'ai fini de cloner cognitons");
      //	System.out.println("Cognitons de bases : " + this.cognitons);
     // 	System.out.println("Cognitons clon�s : " + cognitonsClones);
      	
    	ArrayList<TypeCogniton> startingCognitonsClones = new ArrayList<TypeCogniton>();
      	for(int i=0; i<this.startingCognitons.size(); i++){
      		startingCognitonsClones.add(getTypeCognitonEquivalentClone(this.startingCognitons.get(i), cognitonsClones));
      	}
      	
      	//System.out.println("J'ai fini de cloner startingCognitons");
      	//System.out.println("Starting : " + this.startingCognitons);
      	//System.out.println("Starting clon�s : " + startingCognitonsClones);

      	
      	
      	
      	sc.setCognitons(cognitonsClones);
      	sc.setPlans(plansClones);
      	sc.setGroups(groupsClones);
      	sc.setCloudCognitons(cloudCognitonsClones);
      	sc.setStartingCognitons(startingCognitonsClones);
	    // on renvoie le clone
	    return sc;
	}
	
	/**
	 * Renvoie le cogniton non clon� �quivalent � celui clon� afin de cr�er les nouveaux liens
	 */
	public TypeCogniton getTypeCognitonEquivalentNonClone(TypeCogniton tc, ArrayList<TypeCogniton> altc){
		for(TypeCogniton tcIter : altc){
			//System.out.println("Je teste : " + tc.getNom() + " et " + tcIter.getNom());
			if((tcIter.getNom()).equals(tc.getNom())){
				return tcIter;
			}
		}
		return null;
	}
	/**
	 * Renvoie le cogniton clon� �quivalent � celui non clon� afin de cr�er les nouveaux liens
	 */
	public TypeCogniton getTypeCognitonEquivalentClone(TypeCogniton tc, ArrayList<TypeCogniton> altc){
		for(TypeCogniton tcIter : altc){
			if(tcIter.getNom().equals(tc.getNom())){
				return tcIter;
			}
		}
		return null;
	}
	
	/**
	 * Renvoie le plan clon� �quivalent � celui non clon� afin de cr�er les nouveaux liens
	 */
	public NPlan getNPlanEquivalentClone(NPlan np, ArrayList<NPlan> alnp){
		for(NPlan npIter : alnp){
			if(npIter.getNom().equals(np.getNom())){
				return npIter;
			}
		}
		return null;
	}
	
	
	
	public ArrayList<TypeCogniton> getStartingCognitons() {
		return startingCognitons;
	}

	public void setStartingCognitons(ArrayList<TypeCogniton> s) {
		this.startingCognitons = s;
	}

	public void setAttributesTrigerringValues(HashMap<String,ArrayList<Object[]>> a){
		attributesTrigerringValues = a;
	}
	
	public HashMap<String,ArrayList<Object[]>> getAttributesTrigerringValues(){
		return attributesTrigerringValues;
	}
	
	public void setCognitons(ArrayList<TypeCogniton> c){
		cognitons = c;
	}
	
	public void setCloudCognitons(ArrayList<TypeCulturon> c){
		cloudCognitons = c;
	
	}
	
	public ArrayList<TypeCulturon> getCloudCognitons(){
		return cloudCognitons;
	}
	
	public void setGroups(ArrayList<GroupModel> algm){
		groups = algm;
	}
	
	public void addGroup(GroupModel g){
		groups.add(g);
	}
	
	public void setAutoPlan(NPlan p){
		autoPlan = p;
	}
	
	public void setInitiatePlan(NPlan p){
		initiatePlan = p;
	}
	
	public void setPlans(ArrayList<NPlan> p){
		plans = p;
	}
	
	public ArrayList<String> getAttributs(){
		return attributesNames;
	}
	
	public ArrayList<TypeCogniton> getCognitons(){
		return cognitons;
	}
	
	public ArrayList<NPlan> getPlans(){
		return plans;
	}
	
	public NPlan getInitiatePlan(){
		return initiatePlan;
	}
	
	public NPlan getAutoPlan(){
		return autoPlan;
	}

	public TypeCogniton getCognitonByName(String s){
		for (int i = 0 ; i < cognitons.size(); i++){
			if (cognitons.get(i).getNom().equals(s)){
				return(cognitons.get(i));
			}
		}
		return null;
	}
	
	public GroupModel getGroupModelByName(String s){
		for (int i = 0 ; i < groups.size(); i++){
			if (groups.get(i).getName().equals(s)){
				return(groups.get(i));
			}
		}
		return null;
	}
	
	public ArrayList<GroupModel> getGroups(){
		return  groups;
	}
	
	public void removePlan(NPlan nouveauPlan) {
		plans.remove(nouveauPlan);
	}
	
	public void removeCogniton(TypeCogniton c){
		for (int i = 0 ; i < cognitons.size(); i++){
			if (cognitons.get(i).equals(c)){
				cognitons.remove(i);
				break;
			}
		}		
	}
	
	public void removeTriggersOfCogniton(TypeCogniton refType) {
		
		attributesTrigerringValues.remove(refType);
	}
	
	public int getAttributeIndexByName(String s){
		for (int i = 0 ; i < attributesNames.size(); i++){
			if (attributesNames.get(i).equals(s)){
				return i;
			}
		}
		return -1;
	}
	
	public void addCogniton(TypeCogniton nouveauCogniton) {
		cognitons.add(nouveauCogniton);
	}
	
	public void addPlan(NPlan nouveauPlan) {
		plans.add(nouveauPlan);
	}
	
	public void addCloudCogniton(TypeCulturon newCloudCogniton) {
		cloudCognitons.add(newCloudCogniton);
	}
	
	public void addStartingCognitons(TypeCogniton nouveauCogniton) {
		startingCognitons.add(nouveauCogniton);
	}
	
	public void removeStartingCognitons(TypeCogniton c){
		for (int i = 0 ; i < startingCognitons.size(); i++){
			if (startingCognitons.get(i).equals(c)){
				startingCognitons.remove(i);
				break;
			}
		}
	}

	public String getNom() {
		return name;
	}

	public String setNom(String s) {
		name = s;
		return name;
	}
	
	@Override
	public String toString()
	{
		return getNom();
	}

	public ArrayList<String> getAttributesNames(){
		return attributesNames;
	}
	
	public ArrayList<Double> getAttributesStartingValues(){
		return attributesStartingValues;
	}
	
	public void setAttributesNames(ArrayList<String> a){
		attributesNames = a;
	}
	
	public void setAttributesStartingValues(ArrayList<Double> a){
		attributesStartingValues = a;
	}	
	
	public ArrayList<String> getConstantsNames() {
		return constantsNames;
	}

	public void setConstantsValues(ArrayList<MCConstant> a) {
		constantsValues = a;
	}

	public void setConstantsNames(ArrayList<String> a) {
		constantsNames = a;
	}

	public ArrayList<MCConstant> getConstantsValues() {
		return constantsValues;
	}

	public Double getConstantValueByName(String constant) {
		for(int i = 0; i < constantsNames.size(); i++)
		{
			if(constantsNames.get(i).equals(constant))
			{
				return constantsValues.get(i).getValue();
			}
		}
		return null;
	}

	public MCConstant getConstantByName(String constant) {
		for(int i = 0; i < constantsNames.size(); i++)
		{
			if(constantsNames.get(i).equals(constant))
			{
				return constantsValues.get(i);
			}
		}
		return null;
	}
}
