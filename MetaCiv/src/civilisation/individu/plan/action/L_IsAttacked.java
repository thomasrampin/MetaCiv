package civilisation.individu.plan.action;

import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.individu.Human;
import civilisation.message.StringMessage;

public class L_IsAttacked extends LAction{

	@Override
	public Action effectuer(Human h) {
		if (nextAction != null) h.getEsprit().getActions().push(nextAction);
		Action a;
		StringMessage message = h.gotStringMessage("Attaque");
		h.flushMail();
		if (message != null) {
			h.setInitiateur(message.getSender());
			if(listeActions.size() > 0){
				a = listeActions.get(0).effectuer(h);
			}else{
				a = new A_DoNothing().effectuer(h);
			}
		} else {
			if(listeActions.size() > 1){
				a = listeActions.get(1).effectuer(h);
			}else{
				a = new A_DoNothing().effectuer(h);
			}
		}
		return a;
	}
	
	@Override
	public ImageIcon getIcon(){
		return Configuration.getIcon("processor.png");
	}
	
	@Override
	public int getNumberActionSlot(){
		return 2;
	}
	
}
