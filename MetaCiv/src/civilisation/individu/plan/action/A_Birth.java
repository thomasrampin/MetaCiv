package civilisation.individu.plan.action;

import civilisation.individu.Human;

public class A_Birth extends Action{

	@Override
	public Action effectuer(Human h) {
		Human child = new Human(h.getCiv(), h.getCommunaute(),h,h);
		h.createTurtle(child);
		h.getEnfants().add(child);
		child.moveTo(h.getX(), h.getY());
		return nextAction;
	}

	
	@Override
	public String getInfo() {
		return super.getInfo() + "Create a new agent.<html>";
	}

	
	
}
