package civilisation.amenagement;

import java.util.HashMap;

import civilisation.individu.Human;

public class NPossessions {

	HashMap<Amenagement, Integer> possessions;
	Human h;
	
	public NPossessions(Human h)
	{
		this.h = h;
		this.possessions = new HashMap<Amenagement,Integer>();
	}
	
	public void addAmenagement(Amenagement a)
	{
		
	}
}
