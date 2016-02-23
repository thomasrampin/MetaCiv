package civilisation.individu.plan.action;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

public class DELETE_L_IsAnyAmenagementHere extends LAction{
	
Objet objet;

	
	@Override
	public Action effectuer(Human h) {
		
		if (nextAction != null) h.getEsprit().getActions().push(nextAction);
		Action a;
		boolean test = false;
		for(int i = 0; i < Configuration.amenagements.size();++i)
		{
			if(h.getPatch().isMarkPresent( Configuration.amenagements.get(i).getNom().toLowerCase().toString()))
			{
				test = true;
			}
		}
		if (test) {
			a = listeActions.get(0).effectuer(h);
		} else {
			a = listeActions.get(1).effectuer(h);
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
		return super.getInfo() + " Play first action if the patch own any amenagement,<br> the second otherwise.<html>";
	}
	
	
	public boolean internActionsAreLinked() {
		return false;
	}

}
