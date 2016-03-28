package civilisation.individu.plan.action;

import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.amenagement.Amenagement_Champ;
import civilisation.individu.Human;

public class L_HaveParents extends LAction{

	public Action effectuer(Human h)
	{
		if (nextAction != null) h.getEsprit().getActions().push(nextAction);
		Action a;
		if (h.getPere() != null || h.getMere() != null) {
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
	
	@Override
	public String getInfo() {
		return super.getInfo() + " Play first action if the agent have parents,<br> the second otherwise.<html>";
	}
	
	
	public boolean internActionsAreLinked() {
		return false;
	}
}
