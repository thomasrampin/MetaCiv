package civilisation.individu.plan.action;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.individu.Human;

public class L_EveryXTick extends LAction{
	
	Integer n;
	
	@Override
	public Action effectuer(Human h) {
		
		if (nextAction != null) h.getEsprit().getActions().push(nextAction);
		Action a;
		if (h.compteurTick == n) {
			h.compteurTick = 0;
			if(listeActions.size() > 0){
				a = listeActions.get(0).effectuer(h);
			}else{
				a = new A_DoNothing().effectuer(h);
			}
		} else {
			h.compteurTick++;
			a = new A_DoNothing().effectuer(h);
		}
		return a;
	}

	@Override
	public ImageIcon getIcon(){
		return Configuration.getIcon("processor.png");
	}
	
	@Override
	public int getNumberActionSlot(){
		return 1;
	}
	
	@Override
	public String getInfo() {
		return super.getInfo() + "Do the internal action once every n ticks.<html>";
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);
		
		if (option.getParametres().get(0).getClass().equals(Integer.class)){
			n = (Integer) option.getParametres().get(0);
		}
		else
		{
			System.out.println("Mauvaise initialisation d'une action!");
		}

	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			String[] nRepeat = {"**Integer**" , "n", "0" , "10000" , "1"};
			schemaParametres.add(nRepeat);
		}
		return schemaParametres;	
	}
	
}