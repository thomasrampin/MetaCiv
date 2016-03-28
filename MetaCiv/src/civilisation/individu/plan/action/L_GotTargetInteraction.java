package civilisation.individu.plan.action;

import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.individu.Human;

public class L_GotTargetInteraction extends LAction{

	@Override
	public Action effectuer(Human h) {
		if (nextAction != null) h.getEsprit().getActions().push(nextAction);
		Action a;
		Human cible = h.getCibleInteraction();
		if (cible != null && cible.isAlive() && cible != h) {
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
	
	public boolean internActionsAreLinked() {
		return false;
	}
}
