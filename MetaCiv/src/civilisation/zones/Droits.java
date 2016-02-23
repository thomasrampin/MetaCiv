package civilisation.zones;

import java.util.ArrayList;

import civilisation.effects.Effect;
import civilisation.individu.Human;

public class Droits {

	private String name;
	private ArrayList<Effect> effets;
	
	public Droits()
	{
		
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void activer(Human h)
	{
		for(int i = 0; i < this.effets.size();++i)
		{
			this.effets.get(i).Activer(h);
		}
	}
}
