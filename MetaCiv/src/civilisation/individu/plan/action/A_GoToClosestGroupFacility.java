package civilisation.individu.plan.action;

import java.util.ArrayList;

import turtlekit.kernel.Patch;
import turtlekit.kernel.Turtle;
import civilisation.Configuration;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.Amenagement_Route;
import civilisation.amenagement.TypeAmenagement;
import civilisation.group.Group;
import civilisation.group.GroupModel;
import civilisation.individu.Human;

public class A_GoToClosestGroupFacility extends Action
{
	/**
	 * Va dans l'aménagement du groupe de l'individu
	 * @author brunoyun , bob
	 */
	
	TypeAmenagement amenagement;
	GroupModel grp;
	
	public Action effectuer(Human h)
	{	
		Group concreteGroup = null;
		Amenagement cible = null;
		
		
		if(cible == null &&(concreteGroup = h.getEsprit().getConcreteGroup(grp)) != null)
			cible = concreteGroup.getClosestFacilityOfType(amenagement, h.xcor(), h.ycor());

		/*		
		if (!h.getEsprit().getGroups().isEmpty())
		{
			for (Group grp : h.getEsprit().getGroups().keySet())
			{
				for (Human grpH : grp.getMembers())
					if (grpH.getBuildings().containsKey(amenagement.getNom()))
					{
						pos = grpH.getBuildings().get(amenagement.getNom()).getPosition();
						doAction = true;
						break;
					}
				if (doAction)
					break;
			}
		}
		*/

		if (cible != null)
		{
			//On est sur l'aménagement
			if(cible.xcor() == h.xcor() && cible.ycor() == h.ycor())
		 		return nextAction;

			h.moveTowards(cible);
			return this; 
		}
		else
			return nextAction;
	}
	
	/**
	 * Donne des infos sur l'action
	 */
	@Override
	public String getInfo() {
		return super.getInfo() + "Go to one of a group member facility.<html>";
	}
	
	@Override
	public void parametrerOption(OptionsActions option)
	{
		super.parametrerOption(option);
		if (option.getParametres().get(0).getClass() == TypeAmenagement.class)
			amenagement = (TypeAmenagement) option.getParametres().get(0);
		else if (option.getParametres().get(0).getClass() == GroupModel.class)
			grp = (GroupModel) option.getParametres().get(0);
	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null)
		{
			schemaParametres = new ArrayList<String[]>();
			String[] attrName = {"**Amenagement**" , "Facility"};
			String[] attrGrp = {"**Group**" , "Group type"};
			
			schemaParametres.add(attrName);
			schemaParametres.add(attrGrp);
		}
		return schemaParametres;	
	}
}
