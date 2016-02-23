package civilisation.individu.plan.action;

import civilisation.individu.Human;
import civilisation.inventaire.Objet;

public class A_GiveObjectToChildren extends Action
{
	public Action effectuer(Human h)
	{
		for(Human gamin : h.getEnfants())
			for (Objet truc : h.getInventaire().getAll())
				gamin.getInventaire().addObjets(truc, h.getInventaire().possede(truc)/h.getEnfants().size());
		return nextAction;
		
		// Old code :
		/*if(h.getEnfants().size() > 0)
		{
			for(int i = 0; i < h.getInventaire().getAll().size();++i)
			{
				for(int j = 0; j < h.getEnfants().size();++j)
				{
					h.getEnfants().get(j).getInventaire().addObjets(h.getInventaire().getAll().get(i), h.getInventaire().possede(h.getInventaire().getAll().get(i))/h.getEnfants().size());
				}
				h.getInventaire().deleteObjets(h.getInventaire().getAll().get(i),  h.getInventaire().possede(h.getInventaire().getAll().get(i)));
			}
		}
		return nextAction;*/
	}
}
