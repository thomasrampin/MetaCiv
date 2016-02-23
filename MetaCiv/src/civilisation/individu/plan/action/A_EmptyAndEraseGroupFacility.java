package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
import civilisation.group.GroupModel;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

public class A_EmptyAndEraseGroupFacility  extends Action{

	TypeAmenagement amenagement;
	GroupModel grp;
	public Action effectuer(Human h)
	{	
		for(String s: h.getEsprit().getConcreteGroup(grp).getFacilitiesOfType(amenagement).get(0).getInventaire().getListeObjets().keySet())
		{
			int num = 0;
			for(Integer i : h.getEsprit().getConcreteGroup(grp).getFacilitiesOfType(amenagement).get(0).getInventaire().getListeObjets().get(s).values())
			{
				num+=i;
			}
			h.getInventaire().addObjets(new Objet(s), num);
 		}
		h.killAgent(h.getEsprit().getConcreteGroup(grp).getFacilitiesOfType(amenagement).get(0));
		
		return nextAction;
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);

		if (option.getParametres().get(0).getClass() == TypeAmenagement.class) {
			amenagement = (TypeAmenagement) option.getParametres().get(0);
		//	System.out.println(objet);
		}else if (option.getParametres().get(0).getClass() == GroupModel.class)
			grp = (GroupModel) option.getParametres().get(0);

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
			String[] attrGrp = {"**Group**" , "Group type"};
			
			schemaParametres.add(attrName);	
			schemaParametres.add(attrGrp);

		}
		return schemaParametres;	
	}
}
