package civilisation.individu.plan.action;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.group.Group;
import civilisation.group.GroupAndRole;
import civilisation.group.GroupModel;
import civilisation.individu.Human;

public class L_IsInThatGroupInThatRole extends LAction{
	
	GroupModel group;
	String role;
	
	@Override
	public Action effectuer(Human h) {
		
		if (nextAction != null) h.getEsprit().getActions().push(nextAction);
		Action a;
		
		Group gr = h.getEsprit().getConcreteGroup(group);
		boolean DoAction=false;
		if (gr != null)
		{
			if(h.getEsprit().getGroups().get(gr).equals(role))
				DoAction=true;
		}
			
		
		
		if (DoAction){
			if(listeActions.size() > 0){
				a = listeActions.get(0).effectuer(h);
			}else{
				a = new A_DoNothing().effectuer(h);
			}
		} else {
			if(listeActions.size() > 1){
				a = listeActions.get(1).effectuer(h);
			}else{
				a = new A_DoNothing().effectuer(h);
			}
		}
		return a;
		
	}

	@Override
	public ImageIcon getIcon(){
		return Configuration.getIcon("processor.png");
	}
	
	@Override
	public int getNumberActionSlot(){
		return 2;
	}
	
	@Override
	public String getInfo() {
		return super.getInfo() + " Do the first action if he has the role in the group else otherwise.<html>";
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
	
	public boolean internActionsAreLinked() {
		return false;
	}
}
