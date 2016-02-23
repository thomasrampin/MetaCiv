package civilisation.individu.plan.action;

import java.util.ArrayList;
import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.individu.Human;
import civilisation.individu.cognitons.TypeCogniton;

public class L_CompareCogniton extends LAction
{
	TypeCogniton cogniton;
	Comparator comp;
	Double d;
	
	@Override
	public Action effectuer(Human h)
	{	
		if (nextAction != null)
			h.getEsprit().getActions().push(nextAction);
		Action a;
		if (comp.compare(h.getEsprit().getCognitonOfType(cogniton).getWeigth(), d))
			a = listeActions.get(0).effectuer(h);
		else
			a = listeActions.get(1).effectuer(h);
		return a;
		
	}

	@Override
	public ImageIcon getIcon()
	{
		return Configuration.getIcon("processor.png");
	}
	
	@Override
	public int getNumberActionSlot()
	{
		return 2;
	}
	
	@Override
	public String getInfo()
	{
		return super.getInfo() + " Compare with the value of a specified cogniton.<html>";
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);
		
		if (option.getParametres().get(0).getClass().equals(TypeCogniton.class))
			this.cogniton = (TypeCogniton) option.getParametres().get(0);
		else if (option.getParametres().get(0).getClass().equals(Comparator.class))
			comp = (Comparator) option.getParametres().get(0);
		else if (option.getParametres().get(0).getClass().equals(Double.class))
			d = (Double) option.getParametres().get(0);
	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			
			
			String[] attr = {"**Cogniton**" , "Cogniton"};
			String[] comp = {"**Comparator**" , "comparator"};		
			String[] val = {"**Double**" , "n", "-100.0" , "700.0" , "1.0", "100"};
			
			schemaParametres.add(attr);
			schemaParametres.add(comp);
			schemaParametres.add(val);

		}
		return schemaParametres;	
	}
	
	public boolean internActionsAreLinked()
	{
		return false;
	}
}
