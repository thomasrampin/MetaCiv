package civilisation.individu.plan.action;

import java.util.ArrayList;
import java.util.Random;

import civilisation.individu.Human;

public class A_Move_Cardinals_Random extends Action{

	double angle;
	
	@Override
	public Action effectuer(Human h) {
		Random random=new Random();
		int randomNumber=(random.nextInt(180)-90);
		h.setHeading(angle + randomNumber);
		h.fd(1);
		
		return nextAction;
	}

	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);
		
		
		if (option.getName().equals("SOUTH")){
			angle = 270.;
		}
		else if (option.getName().equals("WEST")){
			angle = 180.;
		}
		else if (option.getName().equals("NORTH")){
			angle = 90.;
		}
		else if (option.getName().equals("EAST")){
			angle = 0.;
		}
	}
	

	/**
	 * Retourne la structure des param_tres.
	 * Permet de d_terminer la pr_sentation de la fen_tre de r_glages.
	 */
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			String[] directions = {"NORTH","SOUTH","WEST","EAST"};
			schemaParametres.add(directions);
		}
		return schemaParametres;	
	}
	
	
	@Override
	public String getInfo() {
		return super.getInfo() + " The agent move in one direction.<html>";
	}

}