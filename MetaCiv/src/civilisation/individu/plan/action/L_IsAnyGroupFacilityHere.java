package civilisation.individu.plan.action;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import turtlekit.kernel.Turtle;
import civilisation.Configuration;
import civilisation.DefineConstants;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
import civilisation.group.Group;
import civilisation.individu.Human;

public class L_IsAnyGroupFacilityHere extends LAction
{
	TypeAmenagement objet;
	
	@Override
	public Action effectuer(Human h)
	{
		if (nextAction != null)
			h.getEsprit().getActions().push(nextAction);
		Action a;
		boolean doAction = false;
		
		for(Turtle t : h.getPatch().getTurtlesWithRole(DefineConstants.Role_Facility))
		{
			if(((Amenagement)t).getType().equals(objet))
			{
				for(String group : h.getMyGroups(h.getCiv().getNom()))
				{
					if(((Amenagement)t).getMyGroups(h.getCiv().getNom()).contains(group))
					{
						doAction = true;break;
					}
				}
			}
		}
		
		/*
		if (!h.getPatch().isMarkPresent(objet.getNom().toLowerCase().toString()))
			if (!h.getEsprit().getGroups().isEmpty())
				for (Group grp : h.getEsprit().getGroups().keySet())
				{
					for (Human grpH : grp.getMembers())
						if (grpH.getBuildings().containsKey(objet.getNom()))
						{
							doAction = true;
							break;
						}
					if (doAction)
						break;
				}
*/
		if (doAction){
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
	public ImageIcon getIcon()
	{
		return Configuration.getIcon("processor.png");
	}
	
	@Override
	public int getNumberActionSlot()
	{
		return 2;
	}
	
	@Override
	public String getInfo()
	{
		return super.getInfo() + " Play first action if there is an amenagement which belongs to the agent's group,<br> the second otherwise.<html>";
	}
	
	@Override
	public void parametrerOption(OptionsActions option)
	{
		super.parametrerOption(option);
		
		if (option.getParametres().get(0).getClass() == TypeAmenagement.class)
			objet = (TypeAmenagement) option.getParametres().get(0);
	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres()
	{
		if (schemaParametres == null)
		{
			schemaParametres = new ArrayList<String[]>();
				
			String[] cog = {"**Amenagement**" , "amenagement"};
			schemaParametres.add(cog);
		}
		return schemaParametres;	
	}

	public boolean internActionsAreLinked()
	{
		return false;
	}
}
