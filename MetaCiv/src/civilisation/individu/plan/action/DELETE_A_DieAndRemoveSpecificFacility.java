package civilisation.individu.plan.action;



import java.awt.Color;
import java.util.ArrayList;

import turtlekit.kernel.Patch;
import civilisation.Configuration;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
import civilisation.group.Group;
import civilisation.individu.Human;

public class DELETE_A_DieAndRemoveSpecificFacility extends Action{
	TypeAmenagement amenagement;
	
	@Override
	public Action effectuer(Human h) {
		
		if (h.getBuildings().containsKey(amenagement.getNom()))
		{
			//Permet d'enlever la marque
			Amenagement atemp = (Amenagement)h.getBuildings().get(amenagement.getNom()).getPosition().getMark(amenagement.getNom().toLowerCase().toString());
			
			//Enleve de la communauté
			h.getCommunaute().detruire(h.getBuildings().get(amenagement.getNom()), h);
		}
		
		h.die();
		
		return nextAction;
	}

	public void parametrerOption(OptionsActions option)
	{
		super.parametrerOption(option);
		if (option.getParametres().get(0).getClass() == TypeAmenagement.class)
			amenagement = (TypeAmenagement) option.getParametres().get(0);
	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null)
		{
			schemaParametres = new ArrayList<String[]>();
			String[] attrName = {"**Amenagement**" , "Amenagement"};
			schemaParametres.add(attrName);
		}
		return schemaParametres;	
	}
	
	@Override
	public String getInfo() {
		return super.getInfo() + " Kill the agent. Remove his facilities<html>";
	}

	
	
}
