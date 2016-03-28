package civilisation.individu.plan.action;

import civilisation.individu.Human;

public class A_SetCibleInteractionInitiateur extends Action {

	public Action effectuer(Human h) {
		if(h.getInitiateur() != null && h.getInitiateur().isAlive())
			h.setCibleInteraction(h.getInitiateur());
		return nextAction;
	}
}
