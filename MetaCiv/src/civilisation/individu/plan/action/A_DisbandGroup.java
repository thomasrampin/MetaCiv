package civilisation.individu.plan.action;

import java.util.ArrayList;
import java.util.HashMap;

import civilisation.ItemPheromone;
import civilisation.group.Group;
import civilisation.group.GroupAndRole;
import civilisation.group.GroupModel;
import civilisation.individu.Human;
import turtlekit.kernel.Turtle;

public class A_DisbandGroup extends Action{

	GroupModel group;
	String role;

	@Override
	public Action effectuer(Human h) {
		Group gr = h.getEsprit().getConcreteGroup(group);
		if(gr != null){
			for (Human grpH : gr.getMembers())
                if (grpH != null) {
                    gr.leaveGroup(grpH.getEsprit());
                }
		}
		gr.leaveGroup(h.getEsprit());
		
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
			String[] group = {"**GroupAndRole**" , "GroupToDisband"};
			schemaParametres.add(group);
		}
		return schemaParametres;	
	}
	
	
	@Override
	public String getInfo() {
		return super.getInfo() + " Disbands a group that the agent is part of.<html>";
	}


	
}
