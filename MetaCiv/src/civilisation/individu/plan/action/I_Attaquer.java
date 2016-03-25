package civilisation.individu.plan.action;

import civilisation.individu.Human;

public class I_Attaquer extends IAction{
	int degats = 5;
	@Override
	public Action effectuer(Human h){
		if(verifParticipant(h) && participant1.isHere(participant2)){
			participant2.putAttr("Vie", participant2.getAttr().get("Vie") - degats);
		}else{
			if(participant2!=null){
				participant1.setHeadingTowards(participant2);
				participant1.fd(1);
			}
		}
		return nextAction;
	}
	
	public String getInfo() {
		return super.getInfo() + "Type of action where the agents deals 5 damage to his target. <html>";
	}
	
}
