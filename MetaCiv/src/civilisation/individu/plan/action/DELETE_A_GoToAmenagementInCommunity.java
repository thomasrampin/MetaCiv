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

public class DELETE_A_GoToAmenagementInCommunity extends Action{

	TypeAmenagement amenagement;
	public Action effectuer(Human h)
	{	
		Patch pos;
		if(h.getCommunaute().getBatiments().containsKey(amenagement.getNom()))
		{
			ArrayList<Amenagement> allAmenagements = h.getCommunaute().getBatiments().get(amenagement.getNom());
			if(allAmenagements.size()>0){
				Amenagement clossestAmenagement = allAmenagements.get(0);
				for (int i = 0; i < allAmenagements.size(); i++) {
					if(h.distance(allAmenagements.get(i).getPosition().x, allAmenagements.get(i).getPosition().y) < h.distance(clossestAmenagement.getPosition().x, clossestAmenagement.getPosition().y)){
						clossestAmenagement = allAmenagements.get(i);
					}
				}
				pos = clossestAmenagement.getPosition();
			 	if(pos.x == h.xcor() && pos.y == h.ycor())
			 	{
			 		return nextAction;
			 	}
			 	else
			 	{
			 		h.face(clossestAmenagement.getPosition());
					h.fd(1);
					return this;
			 	}
				
			}
			else{
				return nextAction;
			}
			
			
		}
		else
		{
			return nextAction;
		}
		
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
