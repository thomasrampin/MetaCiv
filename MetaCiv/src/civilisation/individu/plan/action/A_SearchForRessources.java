package civilisation.individu.plan.action;


import java.util.ArrayList;
import java.util.List;

import turtlekit.kernel.Patch;
import civilisation.ItemPheromone;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

public class A_SearchForRessources extends Action{

	ItemPheromone item;

	public Action effectuer(Human h)
	{
		if(h.getCible() == null)
		{
			int distance = 10000;
			Patch cible = null;
			List<Patch> liste = h.getPatch().getNeighbors(20, true);
			for(int i = 0; i < liste.size();++i)
			{
				if(h.smellAt(item.getNom(), liste.get(i).x - h.xcor(), liste.get(i).y - h.ycor()) >= 1)
				{
					int temp = Math.abs(liste.get(i).x - h.xcor()) + Math.abs(liste.get(i).y - h.ycor());
					if(temp < distance)
					{
						distance = temp;
						cible = liste.get(i);
					}
				}
			}
			if(distance < 10000)
			{
				h.setCible(cible);
				return this;
			}
			else
			{
				h.setHeading(Math.random()*360.);
				h.fd(1);
				return nextAction;
			}
		}
		else
		{
			if(h.getCible().x == h.xcor() && h.getCible().y == h.ycor())
			{
				h.setCible(null);
				return nextAction;
			}
			else
			{
				h.moveTowards(h.getCible());
				return this;
			}
		}
		
	}
	
	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);

		if (option.getParametres().get(0).getClass() == ItemPheromone.class) {
			item = (ItemPheromone) option.getParametres().get(0);
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
			String[] attrName = {"**Pheromone**" , "Resources to collect"};

			schemaParametres.add(attrName);

		}
		return schemaParametres;	
	}
}
