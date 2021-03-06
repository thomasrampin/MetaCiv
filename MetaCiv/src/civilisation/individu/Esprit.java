package civilisation.individu;

import java.awt.Component;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import civilisation.Communaute;
import civilisation.Configuration;
import civilisation.group.Group;
import civilisation.group.GroupAndRole;
import civilisation.group.GroupModel;
import civilisation.individu.cognitons.TypeCogniton;
import civilisation.individu.cognitons.Cogniton;
import civilisation.individu.decisionMaking.DecisionMaker;
import civilisation.individu.plan.NPlan;
import civilisation.individu.plan.NPlanPondere;
import civilisation.individu.plan.action.Action;
import civilisation.world.World;

/** 
 * Classe de gestion des comportements des agents humain
 * @author DTEAM
 * @version 1.0 - 2/2013
*/



public class Esprit implements Serializable {
	
	/* All cognitons of the agent*/
	ArrayList<Cogniton> cognitons;
	
	/* All plans allowed for the agent*/
	ArrayList<NPlanPondere> plans;
	
	/* Hashmap to keep informations about actions*/
	HashMap<Action , Object> actionsData;
	
	/* HashMap to store the groups of the agent and his role in this group*/
	HashMap<Group , String> groups;
	
	/*Tags are used to tell other agents informations about this agent*/
	HashSet<String> tags;

	Map<Object,Object> memory;
	
	int BirthTick = 0;
	Human h;
	NPlanPondere planEnCours;
	Action actionEnCours;
	Stack<Action> actions = new Stack<Action>();
	
	int poidsTotal;
	float poidsTotalPlan;

	/*DecisionMaker used by the agent*/
	private DecisionMaker decisionMaker;

	// Utilisés pour l'augmentation d'un cogniton sur le prochain tick
	private boolean augmentedCognitonForOneTock;
	private ArrayList<Double> originalWeigthCognitonAugmentedForOneTick;
	private ArrayList<Integer> indiceCognitonAugmentedForOneTick;
	
	
	public Esprit(Human h)
	{
		cognitons = new ArrayList<Cogniton>();
		plans = new ArrayList<NPlanPondere>();
		actionsData = new HashMap<Action , Object>();
		groups = new HashMap<Group , String>();
		decisionMaker = Configuration.decisionMaker.createNewDecisionMaker(this);
		tags = new HashSet<String>();
		this.h = h;
		poidsTotalPlan = 0;
		
		this.augmentedCognitonForOneTock = false;
		this.indiceCognitonAugmentedForOneTick = new ArrayList();
		this.originalWeigthCognitonAugmentedForOneTick = new ArrayList();
		
		initialisationStandard();
		
	}
	
	/**
	 * Initialize cognitons
	 */
	private void initialisationStandard()
	{
		for (TypeCogniton cogni : h.getCiv().getStartingCognitons()) {
			if (Math.random() * 100.0 < (double)cogni.getStartChance())
			cognitons.add(new Cogniton(cogni));
		}
		for (int i = 0; i < cognitons.size(); i++) {
			cognitons.get(i).mettreEnPlace(this);
		}
	}
	
	/**
	 * Main function for the mind.
	 * Define the next plan to execute.
	 */
	public void penser()
	{
		if(this.BirthTick == 0)
		{
			this.BirthTick = 1;
			if(h.getCiv().getInitiatePlan() != null)
			{
				h.getCiv().getInitiatePlan().activer(h, h.getCiv().getInitiatePlan().getActions().get(0));
			}
			
		}
		
		/*Apply the Self-plan*/
		if (h.getCiv().getAutoPlan()!= null) {
			actions.push(null); //end of self-plan marker

			h.getCiv().getAutoPlan().activer(h, h.getCiv().getAutoPlan().getActions().get(0));
			
			Action a = null;

			while (( a = actions.pop()) != null) {
				//System.out.println("a depop : " + a + "depop" + actions);
				h.getCiv().getAutoPlan().activer(h, a);
			}
		}

		decisionMaker.selectPlan();

		planEnCours.activer(actionEnCours);
		this.actionEnCours = actions.pop();

		// Maj augementation d'un tick d'un cogniton :
		if (augmentedCognitonForOneTock)
		{
			//System.out.println(this.indiceCognitonAugmentedForOneTick.size() + " " +originalWeigthCognitonAugmentedForOneTick.size());
			for(int i = 0 ; i < this.indiceCognitonAugmentedForOneTick.size() ; i++)
				cognitons.get(indiceCognitonAugmentedForOneTick.get(i)).setWeigth(originalWeigthCognitonAugmentedForOneTick.get(i));
			this.augmentedCognitonForOneTock = false;
			this.indiceCognitonAugmentedForOneTick.clear();
			this.originalWeigthCognitonAugmentedForOneTick.clear();
		}
	}
	
	/**
	 * Modifie le poids total d'une valeur donn_______________e.
	 * @param variation de poids
	 */
	public void ajouterPoidsTotal(int poids)
	{
		poidsTotal += poids;
	}

	public Human getHumain() {
		return h;
	}
	
	public Human getH() {
		return h;
	}

	public int getPoidsTotal() {
		return poidsTotal;
	}
		
	public ArrayList<Cogniton> getCognitons() {
		return cognitons;
	}

	public void addPlan(NPlan plan)
	{
		plans.add(new NPlanPondere( 0 , plan , this.getHumain() , this));
	}
	
	public void addPlan(NPlan plan , Cogniton cogniton)
	{
		plans.add(new NPlanPondere( 0 , plan , this.getHumain() , this));
	}

	public ArrayList<NPlanPondere> getPlans() {
		return plans;
	}

	/**
	 * Change le poids actuel d'un plan
	 * @param plan : Le plan ______ modifier
	 * @param f : le poids ______ ajouter
	 */
	public void modifierPoids(NPlan plan, float f) 
	{
		int i = 0;
		//System.out.println(projets.get(i).getClass().getName());
		while ( i < plans.size() && !plans.get(i).getPlan().equals(plan))
		{
			//System.out.println(plan);
			//System.out.println(plans.get(i).getPlan());
			//System.out.println(projets.get(i).getClass().getName() + "   " + nom);
			//System.out.println("While " + i);
			i++;
		}

		if (plans.size() > 0 && i < plans.size())
		{
			plans.get(i).changerPoids(f);
		}
		else{
		}
		
	}
	
	/**
	 * Remove the specified plan from the mind of the agent
	 * @param plan
	 */
	public void removePlan(NPlan plan) {
		int i = 0;
		while ( i < plans.size() && !plans.get(i).getPlan().equals(plan))
		{
			i++;
		}
		plans.remove(i);
	}

	/**
	 * Modifie le poids total d'une valeur donn_______________e.
	 * @param variation de poids
	 */
	public void addPoidsTotal(int p)
	{
		poidsTotalPlan += p;
	}

	public void redefinirPoids() {
		poidsTotalPlan = 0;

		for (int i = 0; i < plans.size(); i++)
		{
			plans.get(i).setPoids(0);
		}
		
		for (int i = 0; i < cognitons.size(); i++)
		{
			cognitons.get(i).appliquerPoids(this);
		}
		
		for (Group g : groups.keySet()) {
			g.applyCulturons(groups.get(g), this);
		}
	}
	
	/**
	 * Save data used by an action in the mind of the agent
	 * @param act : action associated with data
	 * @param data : data to be saved
	 * 
	 */
	public void setActionData (Action act , Object data) {
		actionsData.put(act, data);
	}
	
	/**
	 * Return the data associated with an action
	 * @param act : the action associated with data
	 * @return the data of the action
	 */
	public Object getActionData (Action act) {
		return actionsData.get(act);
	}
	
	/**
	 * Remove data of an action
	 * @param act : the action which data must be cleaned
	 */
	public void cleanActionData (Action act) {
		actionsData.remove(act);
	}

	public Action getActionEnCours() {
		return actionEnCours;
	}

	public void setActionEnCours(Action actionEnCours) {
		this.actionEnCours = actionEnCours;
	}

	public NPlanPondere getPlanEnCours() {
		return planEnCours;
	}

	public void setPlanEnCours(NPlanPondere planEnCours) {
		this.planEnCours = planEnCours;
	}

	public void addCogniton(TypeCogniton cogni){
		if(!ownCogniton(cogni))
		{
			cognitons.add(new Cogniton(cogni));
			cogni.mettreEnPlace(this , 1.0); //1.0 is the standard weigth for new cogniton			
		}
	}

	
	private void addCogniton(TypeCogniton cogni, Double d) {
		//System.out.println("add + " + d);
		if(!ownCogniton(cogni))
		{
		Cogniton c = new Cogniton(cogni);
		c.setWeigth(d);
		cognitons.add(c);
		cogni.mettreEnPlace(this , d);
		}
	}
	
	public void removeCogniton(TypeCogniton c) {
		//System.out.println("remove cogniton " + c.getNom());
		for (int i = 0 ; i < this.cognitons.size(); i++) {
			if (cognitons.get(i).getCogniton() == c) {
				c.modifierPlans(false, this);
				cognitons.remove(i);
				this.redefinirPoids();
			}
		}
	}

	public float getPoidsTotalPlan() {
		return poidsTotalPlan;
	}

	public void setPoidsTotalPlan(float poidsTotalPlan) {
		this.poidsTotalPlan = poidsTotalPlan;
	}

	public Stack<Action> getActions() {
		return actions;
	}

	public void setActions(Stack<Action> actions) {
		this.actions = actions;
	}
	
	public HashMap<Group, String> getGroups() {
		return groups;
	}

	public void setGroups(HashMap<Group, String> groups) {
		this.groups = groups;
	}

	/**
	 * Return true if the agent own a specific type of cogniton
	 * @param cogniton
	 * @return
	 */
	public boolean ownCogniton(TypeCogniton cogniton) {
		
		for (int i = 0 ; i < this.cognitons.size(); i++) {
			if (cognitons.get(i).getCogniton().equals(cogniton)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Return true if the agent own a specific plan
	 * @param plan
	 * @return
	 */
	public boolean ownPlan(NPlan plan) {
		
		for (int i = 0 ; i < this.plans.size(); i++) {
			if (plans.get(i).getPlan().equals(plan)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Return the concrete plan of the type of plan in parameter
	 * @param plan
	 * @return
	 */
	public NPlanPondere getPlan(NPlan plan) {
		
		for (int i = 0 ; i < this.plans.size(); i++) {
			if (plans.get(i).getPlan().equals(plan)) {
				return plans.get(i);
			}
		}
		return null;
	}

	/**
	 * Recompute the total weigth from plans
	 */
	public void computeTotalWeight() {
		poidsTotalPlan = 0;
		for (int i = 0 ; i < plans.size() ; i++) {
			if (plans.get(i).getPoids() > 0) {
				poidsTotalPlan += plans.get(i).getPoids();
			}
		}
	}
	
	/**
	 * Return the cogniton of type t
	 * @param t
	 * @return a cogniton of type t (or null if there is any cogniton of this type)
	 */
	public Cogniton getCognitonOfType(TypeCogniton t) {
		Cogniton c = null;
		for (int i = 0 ; i < cognitons.size() ; i++) {
			if (cognitons.get(i).getCogniton().equals(t)) {
				c = cognitons.get(i);
			}
		}
		return c;
	}
	
	/**
	 * Change the weight of a cogniton of type t
	 * @param t : the type of cogniton to change
	 */
	public void changeWeightOfCognitonOfType(TypeCogniton t , Double d) {
		for (int i = 0 ; i < cognitons.size() ; i++) {
			if (cognitons.get(i).getCogniton().equals(t)) {
				cognitons.get(i).setWeigth(cognitons.get(i).getWeigth() + d);
			}
		}
		this.redefinirPoids();
	}
	
	/**
	 * Set the weight of a cogniton of type t
	 * @param t
	 * @param d
	 */
	public void setWeightOfCognitonOfType(TypeCogniton t , Double d)
	{
		for (int i = 0 ; i < cognitons.size() ; i++) {
			if (cognitons.get(i).getCogniton().equals(t)) {
				if (d <= 0) {
					t.modifierPlans(false, this);
					cognitons.remove(i);
					this.redefinirPoids();
				}
				else {
					cognitons.get(i).setWeigth(d);
				}	
			}
		}
		this.redefinirPoids();
	}
	
	
	/**
	 * Ajoute un cogniton si il n'est pas présent
	 * Si il est présent change le poids
	 * 
	 */

	public void setCogniton(TypeCogniton t, Double d)
	{
		int i = 0;
		while(i < cognitons.size() && !cognitons.get(i).getCogniton().equals(t))
		{
			i++;
		}
		if(i >= cognitons.size())
		{
			cognitons.add(new Cogniton(t));
			t.mettreEnPlace(this , d);
		}
		else
		{
			this.setWeightOfCognitonOfType(t, d);
		}
	}
	
	/**
	 * Add weight to a cogniton, or create it if he doesn't exist.
	 */
	public void addWeightToCogniton(TypeCogniton t, Double d)
	{
		int i = 0;
		while(i < cognitons.size() && !cognitons.get(i).getCogniton().equals(t))
		{
			i++;
		}
		if(i < cognitons.size())
		{
			if (cognitons.get(i).getWeigth() + d >= 0.001) {
				cognitons.get(i).setWeigth(cognitons.get(i).getWeigth() + d);
			}
			else {
				this.removeCogniton(t);
			}
			
			this.redefinirPoids();
		}
		else
		{
			this.addCogniton(t,d);
		}
	}
	
	public void addWeightToCognitonOneTick(TypeCogniton t, Double d)
	{
		int i = 0;
		while(i < cognitons.size() && !cognitons.get(i).getCogniton().equals(t))
		{
			i++;
		}
		if(i < cognitons.size())
		{
			if (cognitons.get(i).getWeigth() + d >= 0.001)
			{
				this.augmentedCognitonForOneTock = true;
				this.indiceCognitonAugmentedForOneTick.add(i);
				this.originalWeigthCognitonAugmentedForOneTick.add(cognitons.get(i).getWeigth());
				cognitons.get(i).setWeigth(cognitons.get(i).getWeigth() + d);
			}
		
			this.redefinirPoids();
		}
	}

	/**
	 * Check if the agent own a specific combination of a group and a role
	 * @param groupAndRoleToMap
	 * @return
	 */
	public boolean hasGroupAndRole(GroupAndRole groupAndRoleToMap) {
		Object[] tab = groups.keySet().toArray();

		for (int i = 0 ; i < groups.size(); i++) {
		if (((Group)tab[i]).getGroupModel().getName().equals(groupAndRoleToMap.getGroupModel().getName()) && groups.get(tab[i]).equals(groupAndRoleToMap.getRole())) {
				return true;
			}
		}	
		return false;
	}
	
	/**
	 * Check if the agent is part of a group with the specified structural organisation
	 * @param gm
	 * @return
	 */
	public boolean hasStructuralGroup(GroupModel gm) {
		Object[] tab = groups.keySet().toArray();

		for (int i = 0 ; i < groups.size(); i++) {
		if (((Group)tab[i]).getGroupModel().equals(gm)) {
				return true;
			}
		}	
		return false;
	}
	
	/**
	 * Check if the agent own a specific combination of a concrete group and a role
	 * @return true/false
	 */
	public boolean hasConcreteGroupAndRole(Group g , String r) {
		Object[] tab = groups.keySet().toArray();

		for (int i = 0 ; i < groups.size(); i++) {
		if (((Group)tab[i]).equals(g) && groups.get(tab[i]).equals(r)) {
				return true;
			}
		}	
		return false;
	}
	
	/**
	 * @param gm : the group model researched
	 * @return the concrete group instanciating GroupModel gm of this agent, null if none
	 */
	public Group getConcreteGroup(GroupModel gm) {
		Object[] tab = groups.keySet().toArray();
		for (int i = 0 ; i < groups.size(); i++) {
			if (((Group)tab[i]).getGroupModel().equals(gm)) {
				return ((Group)tab[i]);
			}
		}
		return null;
	}
	
	/**
	 * The agent join a specified restrictive group g and play the role r
	 * Join restrictive group doesn't allow an agent to be part of an other group with the same structural organization,
	 * or to play two different roles in the same group
	 */
	public void joinRestrictiveGroup(Group g , String r) {
		if (!this.hasStructuralGroup(g.getGroupModel())) {
			g.joinGroup(this, r);
		}
	}

	public void runInitiatePlan() {
		if (h.getCiv().getInitiatePlan() != null && h.getCiv().getInitiatePlan().getActions().size() > 0) {
			actions.push(null); //end of self-plan marker

			h.getCiv().getInitiatePlan().activer(h, h.getCiv().getInitiatePlan().getActions().get(0));
			
			Action a = null;

			while (( a = actions.pop()) != null) {
				//System.out.println("a depop : " + a + "depop" + actions);
				h.getCiv().getInitiatePlan().activer(h, a);
			}
		}
	}
	
	public Map<Object, Object> getMemory() {
		return memory;
	}

	public void setMemory(Map<Object, Object> memory) {
		this.memory = memory;
	}

	public HashSet<String> getTags() {
		return tags;
	}

	public void setTags(HashSet<String> tags) {
		this.tags = tags;
	}
	
	public boolean ownTag(String tag) {
		return tags.contains(tag);
	}
	
	public void addTag(String tag) {
		tags.add(tag);
	}
	
	public void removeTag(String tag) {
		tags.remove(tag);
	}

	public void leaveAllGroups() {
		synchronized(groups.keySet()) {
		Iterator<Group> it = groups.keySet().iterator();
		while(it.hasNext())
			it.next().leaveGroup(this);}
	}
	
	public Cogniton getCognitonMaxWeight(){
		Cogniton max=cognitons.get(0);
		for(Cogniton c : cognitons){
			if(c.getWeigth()>max.getWeigth()){
				max=c;
			}
		}
		return max;
	}
}
