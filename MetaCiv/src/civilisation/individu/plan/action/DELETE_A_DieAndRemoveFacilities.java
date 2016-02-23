package civilisation.individu.plan.action;



import java.awt.Color;

import turtlekit.kernel.Patch;
import civilisation.Configuration;
import civilisation.amenagement.Amenagement;
import civilisation.individu.Human;

public class DELETE_A_DieAndRemoveFacilities extends Action{

	@Override
	public Action effectuer(Human h) {
		
		for(Amenagement a : h.getBuildings().values())
		{
			//Permet d'enlever de la map
			Amenagement atemp = (Amenagement)a.getPosition().getMark(a.getType().getNom().toLowerCase().toString());
			
			//Enleve de la communauté
			h.getCommunaute().detruire(a, h);
		}
		
		h.die();
		
		return nextAction;
	}

	
	@Override
	public String getInfo() {
		return super.getInfo() + " Kill the agent. Remove his facilities<html>";
	}

	
	
}
