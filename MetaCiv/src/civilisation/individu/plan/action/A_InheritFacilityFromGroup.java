package civilisation.individu.plan.action;

import java.util.ArrayList;

import turtlekit.kernel.Patch;
import civilisation.Configuration;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
import civilisation.group.Group;
import civilisation.individu.Human;

public class A_InheritFacilityFromGroup extends Action{

	TypeAmenagement amenagement;
	public Action effectuer(Human h)
	{
		//Si l'individu n'as pas deja la facility
		if(!h.getBuildings().containsKey(amenagement.getNom()))
		{
			//On va faire une recherche pour voir si un des individu du groupe possède l'aménagement concerné
			boolean doAction = false;
			Amenagement facility=null;
			
			if (!h.getEsprit().getGroups().isEmpty())
			{
				for (Group grp : h.getEsprit().getGroups().keySet())
				{
					for (Human grpH : grp.getMembers())
						if (grpH.getBuildings().containsKey(amenagement.getNom()))
						{
							facility= grpH.getBuildings().get(amenagement.getNom());
							doAction = true;
							break;
						}
					if (doAction)
						break;
				}
			}
			
			//Si on a trouvé l'aménagement
			if(facility != null)
			{
				h.getBuildings().put(amenagement.getNom(),facility);
			}
			
		}
		
	
		return nextAction;
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);

		if (option.getParametres().get(0).getClass() == TypeAmenagement.class) {
			amenagement = (TypeAmenagement) option.getParametres().get(0);
		//	System.out.println(objet);
		}

	}
	

	/**
	 * Retourne la structure des param_tres.
	 * Permet de d_terminer la pr_sentation de la fen_tre de r_glages.
	 */
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			String[] attrName = {"**Amenagement**" , "Inherit facility"};

			schemaParametres.add(attrName);

		}
		return schemaParametres;	
	}
	
	public String getInfo() {
		return super.getInfo() + "The agent inherit amenagement from his groups if he has a group (and the group has the amenagement).<html>";
	}
}
