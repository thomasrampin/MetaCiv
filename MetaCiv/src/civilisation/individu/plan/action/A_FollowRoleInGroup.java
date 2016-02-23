package civilisation.individu.plan.action;

import java.util.ArrayList;

import turtlekit.kernel.Patch;
import civilisation.Communaute;
import civilisation.Configuration;
import civilisation.amenagement.Amenagement_Route;
import civilisation.group.Group;
import civilisation.group.GroupAndRole;
import civilisation.group.GroupModel;
import civilisation.individu.Human;
import civilisation.pathfinder.Noeud;

public class A_FollowRoleInGroup extends Action{

	GroupModel group;
	String role;
	
	public Action effectuer(Human h) {
		
		Group gr = h.getEsprit().getConcreteGroup(group);
		if(gr != null){
			for (Human grpH : gr.getMembers())
                if (grpH != null) {
                	if(grpH.getEsprit().hasStructuralGroup(group)){
                		if(grpH.getEsprit().getGroups().get(gr).equals(role)){
                        	h.moveTowards(grpH);
                        }
                	}
                    
                }
		}
		
		
		return nextAction;
		
		
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);
		
		if (option.getParametres().get(0).getClass() == GroupAndRole.class) {
			group = ((GroupAndRole) option.getParametres().get(0)).getGroupModel();
			role = ((GroupAndRole) option.getParametres().get(0)).getRole();
		}

		

	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			String[] group = {"**GroupAndRole**" , "GroupeRoleToFollow"};
			schemaParametres.add(group);
		}
		return schemaParametres;	
	}

	
	@Override
	public String getInfo() {
		return super.getInfo() + "Sets agent to follow a certain Role in his group.";
	}

	
}
