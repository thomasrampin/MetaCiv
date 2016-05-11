package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.Configuration;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.Amenagement_Efficacite;
import civilisation.constant.MCIntegerParameter;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

public class A_CollectFromFacilityEfficacite extends Action{

	String ObjectName;
	Integer variation;
	MCIntegerParameter val;
	
	public Action effectuer(Human h) {
		for(Amenagement a : h.getFacilitiesHere())
		{
			if(a instanceof Amenagement_Efficacite && ((Amenagement_Efficacite) a).getCompteur() <= 0){
				((Amenagement_Efficacite) a).resetCompteur();
				if (val.getValue() > 0)
					h.getInventaire().addObjets(Configuration.getObjetByName(ObjectName),val.getValue());
			}
		}
		
		return nextAction;
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);

		if (option.getParametres().get(0).getClass().equals(Objet.class)) {
			ObjectName = ((Objet) option.getParametres().get(0)).getNom();

		} 
		else if (option.getParametres().get(0).getClass() == Integer.class) {
			variation = (Integer) option.getParametres().get(0);
			val= loadIntegerParam(option);
		}

	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			String[] attrName = {"**Objet**" , "Modified object"};
			String[] n = {"**Integer**" , "n", "-100" , "100" , "1"};

			schemaParametres.add(attrName);
			schemaParametres.add(n);

		}
		return schemaParametres;	
	}
	
	
	@Override
	public String getInfo() {
		return super.getInfo() + "Reset the facility's counter and add items to the agent.<html>";
	}

}
