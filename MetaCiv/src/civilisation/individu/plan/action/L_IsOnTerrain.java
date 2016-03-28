package civilisation.individu.plan.action;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import civilisation.Configuration;
import civilisation.amenagement.TypeAmenagement;
import civilisation.group.Group;
import civilisation.individu.Human;
import civilisation.world.Terrain;

public class L_IsOnTerrain extends LAction {

Terrain t;
	
	@Override
	public Action effectuer(Human h)
	{
		if (nextAction != null)
			h.getEsprit().getActions().push(nextAction);
		Action a;
		
		if (h.getPatch().getColor().equals(t.getCouleur())){
			if(listeActions.size() > 0){
				a = listeActions.get(0).effectuer(h);
			}else{
				a = new A_DoNothing().effectuer(h);
			}
		} else {
			if(listeActions.size() > 1){
				a = listeActions.get(1).effectuer(h);
			}else{
				a = new A_DoNothing().effectuer(h);
			}
		}
		return a;
	}

	public ImageIcon getIcon()
	{
		return Configuration.getIcon("processor.png");
	}
	
	@Override
	public int getNumberActionSlot()
	{
		return 2;
	}
	
	@Override
	public String getInfo()
	{
		return super.getInfo() + " Play first action if the agent's current patch matches the argument,<br> the second otherwise.<br>for Metaciv 1.89. the determination of terrain type is based on color(same color = same terrain)<html>";
	}
	
	@Override
	public void parametrerOption(OptionsActions option)
	{
		super.parametrerOption(option);
		
		if (option.getParametres().size() > 0 && option.getParametres().get(0).getClass() == Terrain.class)
			t = (Terrain) option.getParametres().get(0);
	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres()
	{
		if (schemaParametres == null)
		{
			schemaParametres = new ArrayList<String[]>();
				
			String[] cog = {"**Terrain**" , "terrain"};
			schemaParametres.add(cog);
		}
		return schemaParametres;	
	}

	public boolean internActionsAreLinked()
	{
		return false;
	}
}
