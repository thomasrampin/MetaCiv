package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.ItemPheromone;
import civilisation.group.Group;
import civilisation.group.GroupAndRole;
import civilisation.group.GroupModel;
import civilisation.individu.Human;
import turtlekit.kernel.Turtle;

public class A_BirthGroupAndRole extends Action{

	GroupModel group;
	String role;

	@Override
	public Action effectuer(Human h) {
		Human child = new Human(h.getCiv(), h.getCommunaute(),h,h);
		h.createTurtle(child);
		h.getEnfants().add(child);
		child.moveTo(h.getX(), h.getY());

		Group gr = h.getEsprit().getConcreteGroup(group);
		if (gr != null) {
			if (child != null) {
				gr.joinGroup(child.getEsprit(), role);
				//child.getEsprit().joinRestrictiveGroup(gr, role);
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
		return super.getInfo() + " Give Birth to another agent with the selected role in the group.<html>";
	}


	
}
