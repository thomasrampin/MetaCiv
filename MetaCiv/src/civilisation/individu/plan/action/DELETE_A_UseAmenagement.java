package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
import civilisation.individu.Human;


/**
 * 
 * @author DTEAM
 * 
 * L'agent utilise l'aménagement
 * 
 * @param Created amenagement : l'aménagement a utiliser
 *
 */

public class DELETE_A_UseAmenagement extends Action {

	TypeAmenagement amenagement;
	public Action effectuer(Human h)
	{	
	//	System.out.println("Use");
		if(h.getPatch().isMarkPresent(amenagement.getNom().toLowerCase().toString()))
		{
			h.getBuildings().get(amenagement.getNom()).Utiliser();
		}

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
			String[] attrName = {"**Amenagement**" , "Amenagement"};

			schemaParametres.add(attrName);

		}
		return schemaParametres;	
	}
}
