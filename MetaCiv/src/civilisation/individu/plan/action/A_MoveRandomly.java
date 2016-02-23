package civilisation.individu.plan.action;

import civilisation.individu.Human;

public class A_MoveRandomly extends Action{

	@Override
	public Action effectuer(Human h) {
		h.setHeading(Math.random()*360.);
		h.fd(1);
		
		return nextAction;
	}

	
	
	
}
