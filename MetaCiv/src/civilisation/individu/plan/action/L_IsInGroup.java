package civilisation.individu.plan.action;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.group.Group;
import civilisation.group.GroupAndRole;
import civilisation.group.GroupModel;
import civilisation.individu.Human;

public class L_IsInGroup extends LAction{
	
	GroupModel group;
	
	@Override
	public Action effectuer(Human h) {
		
		if (nextAction != null) h.getEsprit().getActions().push(nextAction);
		Action a;
		
		if ( h.getEsprit().getConcreteGroup(group) != null){
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
		return super.getInfo() + " Do the first action if the agent is in a group of the given type ,do the other action otherwise.<html>";
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);
		
		if (option.getParametres().get(0).getClass() == GroupModel.class) {
			group = ((GroupModel) option.getParametres().get(0));
		}
	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			
			
			String[] group = {"**Group**" , "Group type to check"};
			
			schemaParametres.add(group);

		}
		return schemaParametres;	
	}
	
	public boolean internActionsAreLinked() {
		return false;
	}
}