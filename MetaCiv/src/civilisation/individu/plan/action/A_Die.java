package civilisation.individu.plan.action;



import java.awt.Color;

import turtlekit.kernel.Patch;
import civilisation.Configuration;
import civilisation.individu.Human;

public class A_Die extends Action{

	@Override
	public Action effectuer(Human h) {
	/*		for(int j = 0; j < Configuration.amenagements.size();++j)
			{
				if(h.getBuildings().containsKey(Configuration.amenagements.get(j).getNom()))
				{
					Patch pos = h.getBuildings().get(Configuration.amenagements.get(j).getNom()).getPosition();
					h.getPatchAt(pos.x - h.xcor(), pos.y - h.ycor()).getMark(Configuration.amenagements.get(j).getNom().toLowerCase().toString());
				}
				
			}*/
		h.die();
		//return null;
		return nextAction;
	}

	
	@Override
	public String getInfo() {
		return super.getInfo() + " Kill the agent.<html>";
	}

	
	
}
