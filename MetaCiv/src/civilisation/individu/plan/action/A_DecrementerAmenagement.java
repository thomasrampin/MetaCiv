package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.amenagement.Amenagement;
import civilisation.amenagement.Amenagement_Efficacite;
import civilisation.amenagement.TypeAmenagement;
import civilisation.constant.MCIntegerParameter;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

public class A_DecrementerAmenagement extends Action{
	
	public Action effectuer(Human h)
	{
	
		for(Amenagement a : h.getFacilitiesHere())
		{
			if(a instanceof Amenagement_Efficacite){
				((Amenagement_Efficacite) a).decrementer();
			}
		}
		return nextAction;
	}
	
	
	@Override
	public String getInfo()
	{
		return super.getInfo() + "Reduce by 1 the counter of the facility on the patch.<html>";
	}

}
