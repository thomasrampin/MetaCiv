package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.constant.MCDoubleParameter;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

public class A_ChangeAttributeDouble extends Action{

	String attributeName;
	Double variation;
	MCDoubleParameter val;
	
	
	
	@Override
	public Action effectuer(Human h) {
		h.putAttr(attributeName, h.getAttr().get(attributeName) + val.getValue() );
		return nextAction;
	}

	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);

		if (option.getParametres().get(0).getClass() == String.class) {
			attributeName = (String) option.getParametres().get(0);
		} else if (option.getParametres().get(0).getClass() == Double.class) {
			variation = (Double) option.getParametres().get(0);
			val = loadDoubleParam(option);
		}

	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			String[] attrName = {"**Attribute**" , "Modified attribute"};
			String[] n = {"**Double**" , "n", "-10.0" , "10.0" , "0.1","100"};

			schemaParametres.add(attrName);
			schemaParametres.add(n);

		}
		return schemaParametres;	
	}
	
	
	@Override
	public String getInfo() {
		return super.getInfo() + " Change the current value of an attribute.<html>";
	}


	
}
