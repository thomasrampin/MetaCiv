package civilisation.individu.plan.action;

import java.util.ArrayList;
import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

public class L_CompareAmenagementObject extends LAction
{
	Objet objet;
	Comparator comp;
	Double d;
	TypeAmenagement am;
	
	@Override
	public Action effectuer(Human h)
	{
		if (nextAction != null)
			h.getEsprit().getActions().push(nextAction);
		Action a;
		ArrayList<Amenagement> amenagementList =  h.getFacilitiesOfTypeHere(am);
		Amenagement amenagement = amenagementList.get(0);
		if (comp.compare((double) (amenagement.getInventaire().possede(objet)), d)){
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
		return super.getInfo() + " Compare if the amenagment have this object.<html>";
	}
	
	@Override
	public void parametrerOption(OptionsActions option)
	{
		super.parametrerOption(option);
		if (option.getParametres().get(0).getClass().equals(Objet.class))
			objet = (Objet) option.getParametres().get(0);
		else if (option.getParametres().get(0).getClass().equals(Comparator.class))
			comp = (Comparator) option.getParametres().get(0);
		else if (option.getParametres().get(0).getClass().equals(Double.class))
			d = (Double) option.getParametres().get(0);
		else if (option.getParametres().get(0).getClass().equals(TypeAmenagement.class))
			this.am = (TypeAmenagement) option.getParametres().get(0);
	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres()
	{
		if (schemaParametres == null)
		{
			schemaParametres = new ArrayList<String[]>();
			
			String[] attr = {"**Objet**" , "objetToCompare"};
			String[] comp = {"**Comparator**" , "comparator"};		
			String[] val = {"**Double**" , "n", "-100.0" , "100.0" , "1.0", "100"};
			String[] cog = {"**Amenagement**" , "amenagement"};
			
			schemaParametres.add(attr);
			schemaParametres.add(comp);
			schemaParametres.add(val);
			schemaParametres.add(cog);
		}
		return schemaParametres;	
	}
	
	public boolean internActionsAreLinked()
	{
		return false;
	}
}
