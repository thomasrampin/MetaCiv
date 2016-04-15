package civilisation.individu.plan.action;

import civilisation.individu.Human;
import madkit.message.StringMessage;

public class I_EnvoyerMessage extends IAction {
	private StringMessage message; 
	
	@Override
	public Action effectuer(Human h) {
		if(verifParticipant(h)){
			message = new StringMessage("Attaque");
			h.sendMessage(participant2.getAgentAddressIn(participant2.getCommunity(), "membre", "membre"), message);
		}
		return nextAction;
	}
	
}
