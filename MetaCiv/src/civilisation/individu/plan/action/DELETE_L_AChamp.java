package civilisation.individu.plan.action;

import civilisation.amenagement.Amenagement_Champ;
import civilisation.individu.Human;

public class DELETE_L_AChamp extends LAction {

	public Action effectuer(Human h)
	{
		if (nextAction != null) h.getEsprit().getActions().push(nextAction);
		Action a;
		if (h.getBuildings().containsKey(Amenagement_Champ.class.getName())) {
			a = listeActions.get(0).effectuer(h);
		} else {
			a = listeActions.get(1).effectuer(h);
		}
		return a;
	}
}
