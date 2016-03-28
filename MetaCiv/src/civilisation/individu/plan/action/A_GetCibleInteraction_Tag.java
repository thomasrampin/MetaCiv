package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.constant.MCIntegerParameter;
import civilisation.individu.Human;

public class A_GetCibleInteraction_Tag extends Action{
	String attributeName;
	Integer variation;
	MCIntegerParameter val;
	
	
	@Override
	public Action effectuer(Human h) {
		Human target = h.oneOfHumanInRadiusWithAttribute(100, attributeName);
		h.setCibleInteraction(target);
		return nextAction;
	}

	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);

		if (option.getParametres().get(0).getClass() == String.class) {
			attributeName = (String) option.getParametres().get(0);
		} else if (option.getParametres().get(0).getClass() == Integer.class) {
			variation = (Integer) option.getParametres().get(0);
			val = loadIntegerParam(option);
		}

	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			String[] attrName = {"**Attribute**" , "Modified attribute"};
			String[] n = {"**Integer**" , "n", "-100" , "100" , "100"};

			schemaParametres.add(attrName);
			schemaParametres.add(n);

		}
		return schemaParametres;	
	}
	
	@Override
	public String getInfo() {
		return super.getInfo() + " Choisi une cible d'interaction ayant le tag spécifié.<html>";
	}
}
