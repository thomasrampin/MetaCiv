package civilisation.individu.plan.action;

import civilisation.individu.Human;

public class A_GiveBirth extends Action{

	@Override
	public Action effectuer(Human h) {
		Human child = new Human(h.getCiv(), h.getCommunaute(),h,null);
		h.createTurtle(child);
		child.moveTo(h.getX(), h.getY());
		h.getEnfants().add(child);
		return nextAction;
	}

	
	@Override
	public String getInfo() {
		return super.getInfo() + "Create a new agent.<html>";
	}

	
	
}
