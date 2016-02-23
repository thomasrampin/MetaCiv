package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.Configuration;
import civilisation.constant.MCIntegerParameter;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

/**
 *  Ajoute un objet a l'inventaire de l'agent 
 *  @param Changed Object , l'objet a ajouter
 *  @param Integer , le nombre d'objets a ajouter
 *  
 *  @return l'action suivante dans la liste d'actions de l'agent
 */

public class A_AddItem extends Action{

	String ObjectName;
	Integer variation;
	MCIntegerParameter val;
	
	public Action effectuer(Human h) {
		
		if (val.getValue() > 0)
			h.getInventaire().addObjets(Configuration.getObjetByName(ObjectName),val.getValue());
		return nextAction;
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);

		if (option.getParametres().get(0).getClass().equals(Objet.class)) {
			ObjectName = ((Objet) option.getParametres().get(0)).getNom();

		} 
		else if (option.getParametres().get(0).getClass() == Integer.class) {
			variation = (Integer) option.getParametres().get(0);
			val= loadIntegerParam(option);
		}

	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			String[] attrName = {"**Objet**" , "Modified object"};
			String[] n = {"**Integer**" , "n", "-10" , "10" , "1"};

			schemaParametres.add(attrName);
			schemaParametres.add(n);

		}
		return schemaParametres;	
	}
	
	
	@Override
	public String getInfo() {
		return super.getInfo() + " Add an object to the inventory.<html>";
	}

}
