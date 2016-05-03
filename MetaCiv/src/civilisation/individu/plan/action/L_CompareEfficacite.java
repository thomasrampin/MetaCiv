package civilisation.individu.plan.action;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.Amenagement_Efficacite;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

public class L_CompareEfficacite extends LAction{
	
	Comparator comp;
	Double d;
	
	@Override
	public Action effectuer(Human h) {
		if (nextAction != null) h.getEsprit().getActions().push(nextAction);
		Action a;
		Amenagement_Efficacite af = null;
		ArrayList<Amenagement> am = h.getFacilitiesHere();
		if(am.size() > 0){
			af = (Amenagement_Efficacite) am.get(0);
		}
		if (comp.compare((double) af.getEfficacite(), d)) {
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
		return super.getInfo() + " Compare the max counter of the facility on this patch.<html>";
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);
		if (option.getParametres().get(0).getClass().equals(Comparator.class)){
			comp = (Comparator) option.getParametres().get(0);
		} else
		if (option.getParametres().get(0).getClass().equals(Double.class)){
			d = (Double) option.getParametres().get(0);
		}
	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			
			
			String[] comp = {"**Comparator**" , "comparator"};		
			String[] val = {"**Double**" , "n", "-100.0" , "100.0" , "1.0", "100"};
			
			schemaParametres.add(comp);
			schemaParametres.add(val);

		}
		return schemaParametres;	
	}
	
	public boolean internActionsAreLinked() {
		return false;
	}
}