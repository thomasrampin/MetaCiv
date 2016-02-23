package civilisation.group;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Agent;
import madkit.kernel.Message;
import madkit.kernel.Probe;
import madkit.kernel.Watcher;
import civilisation.Civilisation;
import civilisation.DefineConstants;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
import civilisation.individu.Esprit;
import civilisation.individu.Human;
import civilisation.individu.cognitons.TypeCulturon;
import civilisation.individu.cognitons.Cogniton;
import civilisation.individu.cognitons.TypeCogniton;
import turtlekit.kernel.Turtle;
import turtlekit.kernel.Patch;

public class Group extends Watcher implements Serializable
{

	String groupId = "defaultGroup";
	Group parent;
	GroupModel groupModel;
	HashMap<String,ArrayList<Cogniton>> rolesAndCulturons = new HashMap<String,ArrayList<Cogniton>>();
	HashMap<String, Boolean> hasMembersInRole;
	static long groupInstanceCounter = 0;
	String civName = "no civ";
	GroupProbeFacility facilities;
	
	public String getCiv()
	{
		return civName;
	}
	
	public String getId()
	{
		return groupId;
	}
	
	public Group() {
	}
	
	public void activate() {
		GroupProbeHuman p;
		
	 	createGroupIfAbsent(DefineConstants.Comunity_MetacivSoftware, DefineConstants.Group_MetacivSocialStructure);
	 	requestRole(DefineConstants.Comunity_MetacivSoftware, DefineConstants.Group_MetacivSocialStructure, DefineConstants.Role_ConcreteGroup);
	 	createGroupIfAbsent(civName, groupId);
	 	hasMembersInRole = new HashMap<String, Boolean>();
	 	for(String role : getRolesAndCulturons().keySet())
	 	{
	 		p = new GroupProbeHuman(civName, groupId, role,this);
	 		hasMembersInRole.put(role, false);
	    	this.addProbe(p);
	 	}
	 	facilities = new GroupProbeFacility(civName, groupId, DefineConstants.Role_Facility);
	 	this.addProbe(facilities);
	}
	
	
	public Group(Group parent , GroupModel groupModel, Civilisation inCiv){
		
		
		this.parent = parent;
		this.groupModel = groupModel;
		groupInstanceCounter++;
		this.groupId = groupModel.getName() + "_" + groupInstanceCounter;
		civName = inCiv.getNom();
	    Iterator<String> iterator = groupModel.getCulturons().keySet().iterator();
	    while(iterator.hasNext()) {
	    	String role = iterator.next();
	    	ArrayList<Cogniton> culturons = new ArrayList<Cogniton>();
	    	ArrayList<TypeCogniton> modelCulturons = groupModel.getCulturons().get(role);
	    	for(int i = 0 ; i < modelCulturons.size() ; i++) {
	    		culturons.add(new Cogniton(modelCulturons.get(i)));
	    	}
	    	this.setRole(role, culturons);	
	    }		
	}
	
	public void applyCulturons(String role , Esprit e){
		ArrayList<Cogniton> c = rolesAndCulturons.get(role);
		for (int i = 0 ; i < c.size() ; i++){
			c.get(i).appliquerPoids(e);
		}
		if (parent != null) parent.applyCulturons(role, e);
	}
	
	public ArrayList<Amenagement> getFacilities()
	{
		ArrayList<Amenagement> result;
		result = new ArrayList<Amenagement>(facilities.getCurrentAgentsList());
		return result;
	}
	
	public ArrayList<Amenagement> getFacilitiesOfType(TypeAmenagement type)
	{
		return facilities.getFacilitiesOfType(type);
	}

	public Amenagement getClosestFacilityOfType(TypeAmenagement type, int x, int y)
	{
		return facilities.getClosestFacilityOfType(type, x, y);
	}

	public Amenagement createGroupFacility(TypeAmenagement type, Patch p, Human h)
	{
		Amenagement result =null;
		if(isAlive())
		{
			result = new Amenagement(p, null, type);
			h.launchAgent(result);
			result.requestRole(civName, groupId, DefineConstants.Role_Facility);
			//System.out.println("grpFac created " + result.getMyGroups(civName));
		}
		return result;
	}

	public void setRole(String role){
		rolesAndCulturons.put(role , null);
	}
	
	public void setRole(String role , ArrayList<Cogniton> newCulturons){
		rolesAndCulturons.put(role , newCulturons);
	}
	
	public void addCulturonToRole(String role , Cogniton culturon){
		if (!rolesAndCulturons.containsKey(role)) {
			ArrayList<Cogniton> lc = new ArrayList<Cogniton>();
			lc.add(culturon);
			rolesAndCulturons.put(role , lc);
		} else {
			rolesAndCulturons.get(role).add(culturon);
		}
	}
	
	public void joinGroup(Esprit e, String role) {
		if(this.isAlive() && e.getConcreteGroup(groupModel) == null && e.getHumain().requestRole(e.getHumain().getCiv().getNom(), this.groupId, role).equals(AbstractAgent.ReturnCode.SUCCESS))
		{
			e.getGroups().put(this,role);
			setupCulturons(role, e);
		}
	}
	
	public void leaveGroup(Esprit e) {
		if(e.getHumain().leaveGroup(e.getHumain().getCiv().getNom(), this.groupId).equals(AbstractAgent.ReturnCode.SUCCESS))
		{
			e.getGroups().remove(this);
		}
		}
	
	public void changeRole(Esprit e, String role) {
		String previousRole = e.getGroups().get(this);
		if(this.isAlive())
		{
		/*if(previousRole!=null)
			removeCulturons(previousRole, e);
		*/
		leaveGroup(e);
		joinGroup(e, role);
		//System.out.println("changed role to " + role + " from " + previousRole + " : " + e.getHumain().getMyRoles(e.getHumain().getCiv().getNom(), this.groupId));
		
		/*
		if(previousRole != null)
			e.getHumain().leaveRole(e.getHumain().getCiv().getNom(), this.groupId,previousRole);
			*/
		}
	}
	
	public void setupCulturons(String role , Esprit e){
		ArrayList<Cogniton> c = rolesAndCulturons.get(role);
		//System.out.println(c.size() + " " + c.get(0).getCogniton().getNom());
		for (int i = 0 ; i < c.size() ; i++){
			c.get(i).mettreEnPlace(e);
		}
		if (parent != null) parent.setupCulturons(role, e);
	}

	public void removeCulturons(String role , Esprit e)
	{
		for(Cogniton c : rolesAndCulturons.get(role))
			e.removeCogniton(c.getCogniton());
	}

	public boolean checkIfEmpty()
	{
		for(Boolean b : hasMembersInRole.values())
			if(b.booleanValue())
				return false;
		return true;
	}
	
	public void disbandGroup()
	{
		if(this.isAlive())
		{
		//System.out.println("disbanding group : "+groupId);
		//System.out.println(getMembers().size() + " members left");

		// temporaire : a la dissolution du groupe , tout les amenagements de groupe sont detruits
		//facilities.killAgents();
		/*
		for(Probe p : this.allProbes())
			if(p.getRole().equals(DefineConstants.Role_Facility))
				p.killAgents();*/
		/////////////////
		
		//this.removeAllProbes();
		//System.out.println(facilities.getCurrentAgentsList().size());
		facilities.killAgents();
		//System.out.println(facilities.getCurrentAgentsList().size());
		this.removeAllProbes();
		//this.end();
		this.killAgent(this);
		//System.out.println("done : "+groupId);
		}
	}
	
	@Override
	public void end()
	{
		super.end();
		
	}
	
	public Group getParent() {
		return parent;
	}

	public void setParent(Group parent) {
		this.parent = parent;
	}

	public GroupModel getGroupModel() {
		return groupModel;
	}

	public void setGroupModel(GroupModel groupModel) {
		this.groupModel = groupModel;
	}

	public ArrayList<Human> getMembers() {
		ArrayList<Human> result = new ArrayList<Human>();
		Set<Probe<? extends AbstractAgent>> probes = this.getProbes();
		for(Probe<? extends AbstractAgent> p : probes)
		{
			if(!p.getRole().equals(DefineConstants.Role_Facility))
				result.addAll((Collection<? extends Human>) p.getCurrentAgentsList());
		}
		return result;
	}

	public HashMap<String, ArrayList<Cogniton>> getRolesAndCulturons() {
		return rolesAndCulturons;
	}

	public void setRolesAndCulturons(
			HashMap<String, ArrayList<Cogniton>> rolesAndCulturons) {
		this.rolesAndCulturons = rolesAndCulturons;
	}
	
	public ArrayList<TypeCogniton> getArrayListOfCognitonType(String r) {
		ArrayList<TypeCogniton> array = new ArrayList<TypeCogniton>();
		for (Cogniton cog : rolesAndCulturons.get(r)) {
			array.add(cog.getCogniton());
		}
		return array;
		
	}
	
	//TODO
	public boolean roleContainsCulturon(TypeCogniton cogniton, String r) {
		for (Cogniton cog : rolesAndCulturons.get(r)) {
			if (cog.getCogniton() == cogniton) {
				return true;
			}
		}
		return false;
		
	}

	public ArrayList<Human> getMembersWithRole(String role) {
		ArrayList<Human> result = new ArrayList<Human>();
		
		Set<Probe<? extends AbstractAgent>> probes = this.getProbes();
		for(Probe<? extends AbstractAgent> p : probes)
		{
			if(p.getRole().equals(role))
				result.addAll((Collection<? extends Human>) p.getCurrentAgentsList());break;
		}
		return result;
	}

	public void processMessages()
	{
		while(!this.isMessageBoxEmpty())
			if(((MessageString)this.nextMessage()).content.equals(DefineConstants.Msg_KillYourself))
				disbandGroup();this.purgeMailbox();
	}
	
	public ArrayList<GroupProbeHuman> getHumanProbes()
	{
		ArrayList<GroupProbeHuman> result = new ArrayList<GroupProbeHuman>();
	
	Set<Probe<? extends AbstractAgent>> probes = this.getProbes();
	for(Probe<? extends AbstractAgent> p : probes)
	{
		if(!p.getRole().equals(DefineConstants.Role_Facility))
			result.add((GroupProbeHuman)p);
	}
	return result;
	}
}
