package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.individu.Human;
import madkit.kernel.AgentAddress;
import madkit.message.StringMessage;

public class A_AccepterCommerce extends Action{
	
	public Action effectuer(Human h) {
		StringMessage ok = new StringMessage("commerceOK");
		Human i = h.getInitiateur();
		if(i != null){
			if(i.getCibleInteraction() == null)
				i.setCibleInteraction(h);
			h.sendMessage(i.getAgentAddressIn(i.getCommunity(), "membre", "membre"), ok);
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