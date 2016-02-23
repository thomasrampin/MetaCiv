package civilisation.individu.plan.action;

import civilisation.Configuration;
import civilisation.individu.Human;

public class A_ConvertToFieldAH extends Action{

	@Override
	public Action effectuer(Human h) {
		h.getPatch().setColor(Configuration.getTerrainByName("Terres agricoles").getCouleur());
		return nextAction;
	}

	
	@Override
	public String getInfo() {
		return super.getInfo() + " An ad hoc action to create new terre agricole.<html>";
	}

	
	
}
