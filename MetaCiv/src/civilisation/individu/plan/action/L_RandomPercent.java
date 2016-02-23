package civilisation.individu.plan.action;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.ItemPheromone;
import civilisation.constant.MCDoubleParameter;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

public class L_RandomPercent extends LAction{
	
	Double d;
	MCDoubleParameter val;
	
	@Override
	public Action effectuer(Human h) {
		
		if (nextAction != null) h.getEsprit().getActions().push(nextAction);
		Action a;
		if (val.getValue() > Math.random() * 100.0) {
			a = listeActions.get(0).effectuer(h);
		} else {
			a = listeActions.get(1).effectuer(h);
		}
		return a;
		
	}

	@Override
	public ImageIcon getIcon(){
		return Configuration.getIcon("processor.png");
	}
	
	@Override
	public int getNumberActionSlot(){
		return 2;
	}
	
	@Override
	public String getInfo() {
		return super.getInfo() + " Chance to do the first sub-action (in %).<html>";
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
	
	public boolean internActionsAreLinked() {
		return false;
	}
}
