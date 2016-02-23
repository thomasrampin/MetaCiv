package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.ItemPheromone;
import civilisation.group.Group;
import civilisation.group.GroupAndRole;
import civilisation.group.GroupModel;
import civilisation.individu.Human;
import turtlekit.kernel.Turtle;

public class A_HireForRoleRandomGroupMenber extends Action{

	GroupModel group;
	String role;

	@Override
	public Action effectuer(Human h) {
		boolean doAction = false;
		Group gr = h.getEsprit().getConcreteGroup(group);
		if (gr != null) {
           for (Group grp : h.getEsprit().getGroups().keySet())
            {
                for (Human grpH : grp.getMembers())
                    if (grpH != null) {
                    	gr.joinGroup(grpH.getEsprit(), role);
                        //grpH.getEsprit().joinRestrictiveGroup(gr, role);
                        doAction = true;
                        break;
                    }
                if (doAction)
                    break;
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
			String[] group = {"**GroupAndRole**" , "GroupToCreate"};
			schemaParametres.add(group);
		}
		return schemaParametres;	
	}
	
	
	@Override
	public String getInfo() {
		return super.getInfo() + " Pick another random agent in the group and give it a role.<html>";
	}


	
}
