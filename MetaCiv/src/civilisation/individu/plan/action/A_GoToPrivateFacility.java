package civilisation.individu.plan.action;

import java.util.ArrayList;

import turtlekit.kernel.Patch;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
import civilisation.individu.Human;

/**
 * 
 * @author DTEAM
 * 
 * L'agent se dirige vers l'aménagement qu'il possède
 * 
 * @param Created Amenagement l'aménagement cible
 *
 */

public class A_GoToPrivateFacility extends Action{

	TypeAmenagement amenagement;
	Amenagement cible = null;
	public Action effectuer(Human h)
	{	
		if(cible == null)
			cible = h.getPrivateFacilities().get(amenagement.getNom());
		if(cible != null)
		{
		 	if(cible.xcor() == h.xcor() && cible.ycor() == h.ycor())
		 	{
		 		return nextAction;
		 	}
		 	else
		 	{
		 		h.moveTowards(cible);
		 		return this;
		 	}
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
			String[] attrName = {"**Amenagement**" , "Facility"};

			schemaParametres.add(attrName);

		}
		return schemaParametres;	
	}
}
