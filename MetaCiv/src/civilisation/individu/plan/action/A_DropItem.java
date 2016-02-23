package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.Configuration;
import civilisation.constant.MCIntegerParameter;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

/**
 *  Retire un objet a l'inventaire de l'agent 
 *  @param Changed object , l'objet a d�truire
 *  @param Integer , le nombre d'objets a d�truire
 *  
 *  @return l'action suivante dans la liste d'actions de l'agent
 */

public class A_DropItem extends Action{

	Objet objet;
	int variation;
	MCIntegerParameter val;
	
	@Override
	public Action effectuer(Human h) {
		h.getInventaire().deleteObjets(objet, val.getValue());
		return nextAction;
	}

	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);

		if (option.getParametres().get(0).getClass() == Objet.class) {
			objet = (Objet) option.getParametres().get(0);
			
		} else if (option.getParametres().get(0).getClass() == Integer.class) {
			variation = (Integer) option.getParametres().get(0);
			val= loadIntegerParam(option);
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
			String[] attrName = {"**Objet**" , "Dropped object"};
			String[] n = {"**Integer**" , "n", "-10" , "10" , "1"};

			schemaParametres.add(attrName);
			schemaParametres.add(n);

		}
		return schemaParametres;	
	}
	
	
}
