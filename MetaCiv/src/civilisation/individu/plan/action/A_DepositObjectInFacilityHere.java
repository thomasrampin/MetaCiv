package civilisation.individu.plan.action;

import java.util.ArrayList;
import java.util.Iterator;

import civilisation.Configuration;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
import civilisation.constant.MCIntegerParameter;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

/**
 *  Ajoute un objet a l'inventaire de l'amenagement
 *  @author Bruno VISSE
 */

public class A_DepositObjectInFacilityHere extends Action
{
	Objet objet;
	MCIntegerParameter variation;
	TypeAmenagement a;
	int val;
	
	public Action effectuer(Human h)
	{
	
		if(h.getFacilitiesOfTypeHere(a).size()>0)
		{
			val = Math.min(h.getInventaire().possede(objet), variation.getValue());
			if(val > 0)
			{
				h.getFacilitiesOfTypeHere(a).get(0).getInventaire().addObjets(objet, val);
				h.getInventaire().deleteObjets(objet, val);
			}
		}
		return nextAction;
	}
	
	@Override
	public void parametrerOption(OptionsActions option)
	{
		super.parametrerOption(option);
		if (option.getParametres().get(0).getClass().equals(Objet.class))
			this.objet = (Objet) option.getParametres().get(0);
		else if (option.getParametres().get(0).getClass() == Integer.class)
			variation = loadIntegerParam(option);
		else if (option.getParametres().get(0).getClass() == TypeAmenagement.class)
			this.a = (TypeAmenagement) option.getParametres().get(0);
	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			String[] attrName = {"**Objet**" , "Modified object"};
			String[] n = {"**Integer**" , "n", "-10" , "10" , "1"};
			String[] ameName = {"**Amenagement**" , "Amenagement"};

			schemaParametres.add(attrName);
			schemaParametres.add(n);
			schemaParametres.add(ameName);

		}
		return schemaParametres;	
	}
	
	
	@Override
	public String getInfo()
	{
		return super.getInfo() + "Deposit a maximum of n Objects ,or as much as the agent can give, in the facility located on the same patch.<html>";
	}

	public boolean isDeprecated()
	{
		return false;
	}
}
