package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.constant.MCDoubleParameter;
import civilisation.group.Group;
import civilisation.individu.Human;

public class A_GetCibleInteraction_AutreCiv_Aleatoire extends Action{

	Double d;
	MCDoubleParameter val;
	
	@Override
	public Action effectuer(Human h) {
		ArrayList<Human> target = h.HumaininRadius((int) val.getValue());
		for(Human cible : target){
			if (cible.getCiv() != h.getCiv()){
				h.setCibleInteraction(cible);
				break;
			}
		}
		return nextAction;
	}

	@Override
	public String getInfo() {
		return super.getInfo() + " Choisi une cible d'interaction au hasard n'appartenant pas a sa civilisation.<html>";
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);

		if (option.getParametres().get(0).getClass().equals(Double.class)){
			d = (Double) option.getParametres().get(0);
			val = loadDoubleParam(option);
		}
	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			
			

			String[] val = {"**Double**" , "n", "-100.0" , "100.0" , "1.0", "100"};
		
			schemaParametres.add(val);

		}
		return schemaParametres;	
	}
}
