package civilisation.individu.plan.action;

import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.individu.Human;

public class L_Instant extends LAction{
	
	@Override
	public Action effectuer(Human h) {	
	//TODO a verifier
		
		
		int i = 0;
		Action next = listeActions.get(0);
		while (i < listeActions.size()) {
			next = listeActions.get(i);
			if (next != null) {
				next.effectuer(h);
			}
			i++;
			
		}

		return nextAction;
	}

	@Override
	public ImageIcon getIcon(){
		return Configuration.getIcon("processor.png");
	}
	
	@Override
	public int getNumberActionSlot(){
		return -1;
	}
	
	@Override
	public String getInfo() {
		return super.getInfo() + " All actions includes in this logical controller will be played in one tick.<html>";
	}
	
}
