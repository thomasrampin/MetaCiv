package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.Configuration;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
import civilisation.constant.MCIntegerParameter;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

public class A_WithdrawObjectFromFacilityHere extends Action
{
	Objet obj;
	MCIntegerParameter val;
	TypeAmenagement a;
	Amenagement facility;
	int realQuantity;
	
	public Action effectuer(Human h)
	{
		if(h.getFacilitiesOfTypeHere(a).size()>0)
		{
			realQuantity = Math.min(h.getFacilitiesOfTypeHere(a).get(0).getInventaire().possede(obj), val.getValue());
			if(realQuantity > 0)
			{
				h.getFacilitiesOfTypeHere(a).get(0).getInventaire().deleteObjets(obj, realQuantity);
				h.getInventaire().addObjets(obj, realQuantity);
			}
		}
		return nextAction;
		}
	
	@Override
	public void parametrerOption(OptionsActions option)
	{
		super.parametrerOption(option);
		if (option.getParametres().get(0).getClass().equals(Objet.class))
			this.obj = ((Objet) option.getParametres().get(0));
		else if (option.getParametres().get(0).getClass() == Integer.class)
			val = loadIntegerParam(option);
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
		return super.getInfo() + "Withdraw a maximum of n Objects ,or as much as the facility can give, in the facility located on the same patch";
	}
	
	public boolean isDeprecated()
	{
		return false;
	}
}
