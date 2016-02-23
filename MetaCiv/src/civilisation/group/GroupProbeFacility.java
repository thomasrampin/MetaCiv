package civilisation.group;

import java.util.ArrayList;

import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
import madkit.kernel.Probe;

public class GroupProbeFacility extends Probe<Amenagement>{

	public GroupProbeFacility(String communityName, String groupName,String roleName) {
		super(communityName, groupName, roleName);
	}
	
	public ArrayList<Amenagement> getFacilitiesOfType(TypeAmenagement type)
	{
		ArrayList<Amenagement> result;
		result = new ArrayList<Amenagement>();

		for(Amenagement a : this.getCurrentAgentsList())
			if(a.getType().getNom().equals(type.getNom()))
				result.add(a);
		//System.out.println("facilities of type " + type.getNom() + " = "+result.size());
		return result;
	}

	public Amenagement getClosestFacilityOfType(TypeAmenagement type, int x, int y) {
		Amenagement result = null;
		
		for(Amenagement a : this.getCurrentAgentsList())
			if(a.getType().equals(type) && ((result == null) || (a.distance(x,y) < result.distance(x,y))))
				result = a;

		return result;
	}
	
	@Override
	public void killAgents()
	{
		for(Amenagement a : this.getCurrentAgentsList())
			a.killAgent(a);
	}


}
