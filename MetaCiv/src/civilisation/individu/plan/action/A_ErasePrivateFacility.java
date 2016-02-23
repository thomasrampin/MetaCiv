package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.amenagement.Amenagement;
import civilisation.amenagement.Amenagement_Champ;
import civilisation.amenagement.TypeAmenagement;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

/**
 *  Détruit l'aménagement
 *  
 *  @param Created Amenagement ,l'amenagement a détruire
 *  
 *  @return l'action suivante dans la liste d'actions de l'agent
 */

public class A_ErasePrivateFacility extends Action{

	TypeAmenagement amenagement;
	public Action effectuer(Human h)
	{	
		if(h.getPrivateFacilities().get(amenagement.getNom()) != null)
			h.killAgent(h.getPrivateFacilities().get(amenagement.getNom()));
		return nextAction;
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);

		if (option.getParametres().get(0).getClass() == TypeAmenagement.class) {
			amenagement = (TypeAmenagement) option.getParametres().get(0);
		//	System.out.println(objet);
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
			String[] attrName = {"**Amenagement**" , "Facility"};

			schemaParametres.add(attrName);

		}
		return schemaParametres;	
	}
}
