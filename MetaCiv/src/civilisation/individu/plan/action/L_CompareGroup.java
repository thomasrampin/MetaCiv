package civilisation.individu.plan.action;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.group.Group;
import civilisation.group.GroupAndRole;
import civilisation.group.GroupModel;
import civilisation.individu.Human;

public class L_CompareGroup extends LAction{
	
	GroupModel group;
	Comparator comp;
	Double d;
	String role;
	
	@Override
	public Action effectuer(Human h) {
		
		if (nextAction != null) h.getEsprit().getActions().push(nextAction);
		Action a;
		
		int nombrePersonneDansLeGroupe = 0;
		if(h.getEsprit().getConcreteGroup(group) != null)
		{
			nombrePersonneDansLeGroupe = h.getEsprit().getConcreteGroup(group).getMembers().size();
		}
		
		/*
		int nombrePersonneDansLeGroupe =0;
		if (!h.getEsprit().getGroups().isEmpty())
		{
			for (Group grp : h.getEsprit().getGroups().keySet())
			{
				if(grp.getMembers() != null)
					nombrePersonneDansLeGroupe= grp.getMembers().size();
			}
		}*/
			
		//System.out.println("Il reste "+ nombrePersonneDansLeGroupe + " personnes dans le groupe");
		if (comp.compare(new Double(nombrePersonneDansLeGroupe),d)){
			a = listeActions.get(0).effectuer(h);
		} else {
			a = listeActions.get(1).effectuer(h);
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
		return super.getInfo() + " Compare the numbers of members in the group (the role doesn't count).<html>";
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);
		
		if (option.getParametres().get(0).getClass() == GroupAndRole.class) {
			group = ((GroupAndRole) option.getParametres().get(0)).getGroupModel();
			role = ((GroupAndRole) option.getParametres().get(0)).getRole();
		} else
		if (option.getParametres().get(0).getClass().equals(Comparator.class)){
			comp = (Comparator) option.getParametres().get(0);
		} else
		if (option.getParametres().get(0).getClass().equals(Double.class)){
			d = (Double) option.getParametres().get(0);
		}
	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			
			
			String[] group = {"**GroupAndRole**" , "GroupToCreate"};
			String[] comp  = {"**Comparator**" , "comparator"};		
			String[] val   = {"**Double**" , "n", "-100.0" , "100.0" , "1.0", "100"};
			
			schemaParametres.add(group);
			schemaParametres.add(comp);
			schemaParametres.add(val);

		}
		return schemaParametres;	
	}
	
	public boolean internActionsAreLinked() {
		return false;
	}
}
