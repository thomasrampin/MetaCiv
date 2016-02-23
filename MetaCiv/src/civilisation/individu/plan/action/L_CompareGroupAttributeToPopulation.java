package civilisation.individu.plan.action;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.ItemPheromone;
import civilisation.amenagement.Amenagement;
import civilisation.group.Group;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

public class L_CompareGroupAttributeToPopulation extends LAction{
	
	String attribute;
	Comparator comp;
	Double d;
	
	@Override
	public Action effectuer(Human h) {
		
		if (nextAction != null) h.getEsprit().getActions().push(nextAction);
		Action a;
		
		Double attribut=0.0;
		d=0.0;
		
		if (!h.getEsprit().getGroups().isEmpty())
		{
			for (Group grp : h.getEsprit().getGroups().keySet())
			{
				if(grp.getMembers() != null)
				{
					for (Human grpH : grp.getMembers())
						attribut+=grpH.getAttr().get(attribute);
					d+=grp.getMembers().size();
				}
				
			}
		}
		
		
		//System.out.println("L'insatisfaction pour mon groupe est de "+attribut + "et la population de "+d);
		if (comp.compare(attribut , d) ) {
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
		return super.getInfo() + " Compare the sum of an attribute for all members of a group to the size of the group.<html>";
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);
		
		if (option.getParametres().get(0).getClass().equals(String.class)){
			attribute = (String) option.getParametres().get(0);
		} else
		if (option.getParametres().get(0).getClass().equals(Comparator.class)){
			comp = (Comparator) option.getParametres().get(0);
		}
	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			
			
			String[] attr = {"**Attribute**" , "attributeToCompare"};
			String[] comp = {"**Comparator**" , "comparator"};		
			
			schemaParametres.add(attr);
			schemaParametres.add(comp);

		}
		return schemaParametres;	
	}
	
	public boolean internActionsAreLinked() {
		return false;
	}
}
