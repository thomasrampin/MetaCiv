package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.constant.MCIntegerParameter;
import civilisation.individu.Human;

public class A_SetStrikeValue extends Action{
	Integer variation;
	MCIntegerParameter val;

	@Override
	public Action effectuer(Human h) {
		h.strike = val.getValue();
		return nextAction;
	}

	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);
		if (option.getParametres().get(0).getClass() == Integer.class) {
			variation = (Integer) option.getParametres().get(0);
			val = loadIntegerParam(option);
		}

	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			String[] n = {"**Integer**" , "n", "0" , "100" , "100"};

			schemaParametres.add(n);

		}
		return schemaParametres;	
	}
	
	
	@Override
	public String getInfo() {
		return super.getInfo() + " Set the agent's strike value.<html>";
	}


	
}



