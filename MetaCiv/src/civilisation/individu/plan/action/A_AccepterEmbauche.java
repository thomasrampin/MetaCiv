package civilisation.individu.plan.action;

import civilisation.individu.Human;
import madkit.message.StringMessage;

public class A_AccepterEmbauche extends Action{
	
	public Action effectuer(Human h) {
		Human i = h.getInitiateur();
		h.setInitiateur(null);
		if(i != null){
			if(i.chef == null && i.getCibleInteraction() == null){
				i.setCibleInteraction(h);
			}
		}
		return nextAction;
	}
	
	@Override
	public String getInfo() {
		return super.getInfo() + "Accpet a pending trade.<html>";
	}

	public boolean isDeprecated()
	{
		return false;
	}
}