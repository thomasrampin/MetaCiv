package civilisation.individu.plan.action;

import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.individu.Human;

public class L_AEmployeeEnMission extends LAction{

	@Override
	public Action effectuer(Human h) {
		
		if (nextAction != null) h.getEsprit().getActions().push(nextAction);
		Action a;
		boolean rep = false;
		for(Human i : h.employes){
			if (i.enMission)
				rep = true;
		}
		if (rep) {
			if(listeActions.size() > 0){
				a = listeActions.get(0).effectuer(h);
			}else{
				a = new A_DoNothing().effectuer(h);
			}
		} else if(listeActions.size() > 1){
			a = listeActions.get(1).effectuer(h);
			} else {
			a = new A_DoNothing().effectuer(h);
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
	
	@Override
	public String getInfo() {
		return super.getInfo() + "Do the first action if the target as a employee in a mission, the second otherwise.<html>";
	}
}
