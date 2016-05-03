package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.amenagement.TypeAmenagement;
import civilisation.individu.Human;

public class A_CreatePrivateFacility_Efficacite extends Action {
	TypeAmenagement amenagement;
	public Action effectuer(Human h)
	{
		//System.out.println("begin  A_CreatePrivateFacility : " + amenagement.getNom());
		if(h.getFacilitiesHere().size() == 0)
		{
			if(amenagement.getRecette().size() > 0)
			{
				boolean test = true;
				for(int i = 0; i < amenagement.getRecette().size(); ++i)
				{
					if(h.getInventaire().possede(amenagement.getRecette().get(i)) < amenagement.getNombre().get(i) )
					{
						test = false;
					}
				}
				if(test)
				{
					h.createPrivateFacilityEfficacite(amenagement, h.getPatch());
				}
			}
			else
			{
				h.createPrivateFacilityEfficacite(amenagement, h.getPatch());
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
	
	public String getInfo() {
		return super.getInfo() + " Create a facility with a random efficacity on the patch if the agent own objects from recipe.<html>";
	}
}

