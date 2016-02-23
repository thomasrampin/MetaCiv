package civilisation.individu.plan.action;

import civilisation.individu.Human;

public class A_DoNothing extends Action{

	@Override
	public Action effectuer(Human h) {
		return nextAction;
	}

	
	@Override
	public String getInfo() {
		return super.getInfo() + " Do nothing.<html>";
	}

	
	
}
