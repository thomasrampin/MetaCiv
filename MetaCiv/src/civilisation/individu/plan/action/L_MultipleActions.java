package civilisation.individu.plan.action;

import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.individu.Human;

public class L_MultipleActions extends LAction{
	
	public Action effectuer(Human h) {
		
		if (nextAction != null) h.getEsprit().getActions().push(nextAction);
		h.getEsprit().getActions().push(listeActions.get(0));
		h.getEsprit().getActions().push(listeActions.get(1));
		
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
	

}
