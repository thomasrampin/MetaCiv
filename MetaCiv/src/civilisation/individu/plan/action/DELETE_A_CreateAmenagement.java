package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.Configuration;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.Amenagement_Champ;
import civilisation.amenagement.TypeAmenagement;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

/**
 *  Crée un aménagement sur le patch
 *  @param Created Amenagement ,l'amenagement a créer
 *  
 *  @return l'action suivante dans la liste d'actions de l'agent
 */

public class DELETE_A_CreateAmenagement extends Action{

	TypeAmenagement amenagement;
	public Action effectuer(Human h)
	{
		if(!h.getPatch().isMarkPresent(amenagement.getNom().toLowerCase().toString()))
		{
			Amenagement a = new Amenagement(h.getPatch(),h,amenagement);
			h.getCommunaute().construire(a);
			for(int i = 0; i < Configuration.amenagements.size();++i)
			{
				if(Configuration.amenagements.get(i).getNom().equals(a.getType().getNom()))
				{
					h.getPatch().dropMark(Configuration.amenagements.get(i).getNom().toLowerCase().toString(), a);
				}
			}
			h.getBuildings().put(amenagement.getNom(), a);
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
			String[] attrName = {"**Amenagement**" , "Modified amenagement"};

			schemaParametres.add(attrName);

		}
		return schemaParametres;	
	}
	
	public String getInfo() {
		return super.getInfo() + " Create an amenagement here.<html>";
	}
}
