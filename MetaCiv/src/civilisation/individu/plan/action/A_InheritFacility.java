package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.Configuration;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
import civilisation.individu.Human;

public class A_InheritFacility extends Action{

	TypeAmenagement amenagement;
	public Action effectuer(Human h)
	{
		if(h.getPere() != null)
		{
			if(h.getPere().getBuildings().containsKey(amenagement.getNom()))
			{
			//	System.out.println(h.getID()+ ": J'ai herite de un "+ amenagement.getNom());
				h.getBuildings().put(amenagement.getNom(), h.getPere().getBuildings().get(amenagement.getNom()));
			}
			
		}
		else
		{
			if(h.getMere() != null)
			{
				//System.out.println("J'ai herite de un "+ amenagement.getNom());
				if(h.getMere().getBuildings().containsKey(amenagement.getNom()))
				{
					h.getBuildings().put(amenagement.getNom(), h.getMere().getBuildings().get(amenagement.getNom()));
				}
				
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
			String[] attrName = {"**Amenagement**" , "Inherit facility"};

			schemaParametres.add(attrName);

		}
		return schemaParametres;	
	}
	
	public String getInfo() {
		return super.getInfo() + "The agent inherit an amenagement from his parents if he have parents.<html>";
	}
}
