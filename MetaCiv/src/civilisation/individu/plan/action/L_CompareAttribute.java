package civilisation.individu.plan.action;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.ItemPheromone;
import civilisation.constant.MCDoubleParameter;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

public class L_CompareAttribute extends LAction{
	
	String attribute;
	Comparator comp;
	Double d;
	MCDoubleParameter val;
	
	@Override
	public Action effectuer(Human h) {
		
		if (nextAction != null) h.getEsprit().getActions().push(nextAction);
		Action a;
		if (comp.compare((h.getAttr().get(attribute)) , val.getValue()) ) {
			if(listeActions.size() > 0){
				a = listeActions.get(0).effectuer(h);
			}else{
				a = new A_DoNothing().effectuer(h);
			}
		} else {
			if(listeActions.size() > 1){
				a = listeActions.get(1).effectuer(h);
			}else{
				a = new A_DoNothing().effectuer(h);
			}
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
		return super.getInfo() + " Compare with the value of a specified attribute.<html>";
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);
		
		if (option.getParametres().get(0).getClass().equals(String.class)){
			attribute = (String) option.getParametres().get(0);
		} else
		if (option.getParametres().get(0).getClass().equals(Comparator.class)){
			comp = (Comparator) option.getParametres().get(0);
		} else
		if (option.getParametres().get(0).getClass().equals(Double.class)){
			d = (Double) option.getParametres().get(0);
			val = loadDoubleParam(option);
		}
	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			
			
			String[] attr = {"**Attribute**" , "attributeToCompare"};
			String[] comp = {"**Comparator**" , "comparator"};		
			String[] val = {"**Double**" , "n", "-100.0" , "700.0" , "1.0", "100"};
			
			schemaParametres.add(attr);
			schemaParametres.add(comp);	
			schemaParametres.add(val);
		}
		return schemaParametres;	
	}
	
	public boolean internActionsAreLinked() {
		return false;
	}
}
