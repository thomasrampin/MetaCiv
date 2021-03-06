package civilisation.individu.plan.action;

import civilisation.individu.Human;

public class I_RejoindreGroupe extends IAction{

	public Action effectuer(Human h) {
		Human i = h.getCibleInteraction();
		if(i != null){
			if(i.createGroupIfAbsent(i.getCommunity(), "" + i.getID()))
				i.requestRole(i.getCommunity(), "" + i.getID(), "chef");
			h.requestRole(i.getCommunity(), "" + i.getID(), "ouvrier");
			h.chef = i;
			i.employes.add(h);
			h.setCibleInteraction(null);
		}
		return nextAction;
	}
	
	@Override
	public String getInfo() {
		return super.getInfo() + "Join the the employees of the targeted agent.<html>";
	}

	public boolean isDeprecated()
	{
		return false;
	}
}