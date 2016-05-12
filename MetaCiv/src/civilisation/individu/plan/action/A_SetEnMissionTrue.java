package civilisation.individu.plan.action;

import civilisation.individu.Human;

public class A_SetEnMissionTrue extends Action{
	
	@Override
	public Action effectuer(Human h) {
		h.enMission = true;
		
		return nextAction;
	}

}
