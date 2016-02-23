package civilisation.individu.plan.action;

import civilisation.individu.Human;

public class A_GetAnotherCityPatch extends Action {
	
	public Action effectuer(Human h)
	{
		int rand = (int)Math.random()* h.TurtlesWithRole("Communaute").size();
		h.setCible(h.TurtlesWithRole("Communaute").get(rand).getPatch());
		return nextAction;
	}
	
	@Override
	public String getInfo() {
		return super.getInfo() + " Met la cible de Allervers sur une ville au hasard.<html>";
	}

}
