package civilisation.individu.plan.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;

import turtlekit.kernel.Patch;
import turtlekit.kernel.Turtle;
import civilisation.Configuration;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
import civilisation.constant.MCIntegerParameter;
import civilisation.group.Group;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

/**
 *  Prend des objets dans l'aménagement du groupe 
 *  (il faut que celui qui possède l'aménagement soit en vie, on ne travaille pas avec la qualité)
 *  @author Bruno YUN
 */

public class DELETE_A_WithdrawObjectInAmenagementHereLargeQuantity extends Action
{

	Objet objet;
	Integer variation;
	TypeAmenagement a;
	MCIntegerParameter val;
	
	public Action effectuer(Human h)
	{
		if (val.getValue() != 0)
		{
				
			//Trouvons l'aménagement sur lequel nous sommes
			boolean doAction = false;
			//Patch pos = new Patch(0, 0);
			Patch pos = new Patch();
			pos.setCoordinates(0, 0);
			Amenagement cible=null;
			if (h.getPatch().isMarkPresent(objet.getNom().toLowerCase().toString()))
			{
				if(h.getCommunaute().getBatiments().containsKey(objet.getNom()))
				{
					ArrayList<Amenagement> allAmenagements = h.getCommunaute().getBatiments().get(objet.getNom());
					if(allAmenagements.size()>0){
						
						for (int i = 0; i < allAmenagements.size(); i++) {
							if(allAmenagements.get(i).getPosition().equals(h.getPatch())){
								cible = allAmenagements.get(i);
							}
						}
					}
					else{
						return nextAction;
					}
					
					
				}
			}
			
			
			//Il faut s'assurer que l'objet existe dans l'inventaire des deux
			if(cible != null)
			{
				cible.getInventaire().addObjets(objet, 0);
				h.getInventaire().addObjets(objet, 0);
			}
			
			
			if(h.getInventaire().getListeObjets() != null && h.getInventaire().getListeObjets().get(objet.getNom()) != null && cible !=null && cible.getInventaire().getListeObjets() != null && cible.getInventaire().getListeObjets().get(objet.getNom())!= null && cible.getInventaire().getListeObjets().get(objet.getNom()).get(1)!= 0 )
			{
				int nombreObjet = cible.getInventaire().getListeObjets().get(objet.getNom()).get(1);
				
				
				//System.out.println("Je suis "+ h.getID() + " et j'ai "+ h.getInventaire().getListeObjets().get(objet.getNom()).get(1));
				
				if (nombreObjet < val.getValue())
					{
						h.getInventaire().addObjets(objet, nombreObjet);
						
						//Il faut enlever les objets de l'inventaire de l'agent
						HashMap<String, HashMap<Integer, Integer>> Bag = cible.getInventaire().getListeObjets();
						HashMap<Integer, Integer> temp= new HashMap<Integer, Integer>();
						temp.put(1,0);
						Bag.put(objet.getNom(), temp);
						cible.getInventaire().setListeObjets(Bag);
					}
					else
					{
						h.getInventaire().addObjets(objet, val.getValue());
						
						//Il faut enlever les objets de l'inventaire de l'agent
						HashMap<String, HashMap<Integer, Integer>> Bag = cible.getInventaire().getListeObjets();
						HashMap<Integer, Integer> temp= new HashMap<Integer, Integer>();
						temp.put(1,nombreObjet-val.getValue());
						Bag.put(objet.getNom(), temp);
						cible.getInventaire().setListeObjets(Bag);
					}
				
				
		
				} 
			
			
		}
		return nextAction;
	}
	
	@Override
	public void parametrerOption(OptionsActions option)
	{
		super.parametrerOption(option);
		if (option.getParametres().get(0).getClass().equals(Objet.class)){
			objet = (Objet) option.getParametres().get(0);
		}else if (option.getParametres().get(0).getClass() == Integer.class)
		{
			this.variation = (Integer) option.getParametres().get(0);
			val = loadIntegerParam(option);
		}
		else if (option.getParametres().get(0).getClass() == TypeAmenagement.class)
			this.a = (TypeAmenagement) option.getParametres().get(0);
		
	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			String[] attr = {"**Objet**" , "objetToCompare"};
			String[] n = {"**Integer**" , "n", "-10" , "100" , "1"};
			String[] ameName = {"**Amenagement**" , "Amenagement"};

			schemaParametres.add(attr);
			schemaParametres.add(n);
			schemaParametres.add(ameName);

		}
		return schemaParametres;	
	}
	
	
	@Override
	public String getInfo()
	{
		return super.getInfo() + " Withdraw n objects (large quantity and quality 1 (default)) from amenagement on current patch. If there's not enough, retrieves all it can. <html>";
	}

	public boolean isDeprecated()
	{
		return false;
	}
}


