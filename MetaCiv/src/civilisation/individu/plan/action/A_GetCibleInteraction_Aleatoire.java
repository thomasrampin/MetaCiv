package civilisation.individu.plan.action;

import civilisation.group.Group;
import civilisation.individu.Human;

public class A_GetCibleInteraction_Aleatoire extends Action{
	@Override
	public Action effectuer(Human h) {
		Human target = h.oneOfHumanInRadius(5);
		h.setCibleInteraction(target);
		return nextAction;
	}

	@Override
	public String getInfo() {
		return super.getInfo() + " Choisi une cible d'interaction au hasard.<html>";
	}
}
