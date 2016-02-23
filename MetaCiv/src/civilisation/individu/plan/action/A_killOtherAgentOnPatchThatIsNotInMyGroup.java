package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.ItemPheromone;
import civilisation.group.Group;
import civilisation.group.GroupAndRole;
import civilisation.group.GroupModel;
import civilisation.individu.Human;
import turtlekit.kernel.Turtle;

public class A_killOtherAgentOnPatchThatIsNotInMyGroup extends Action{
	@Override
	public Action effectuer(Human h) {
		Human target = h.oneOfHumanHere();
		if (target != null && target != h) {

			boolean InMyGroup = false;
			if (!h.getEsprit().getGroups().isEmpty())
				for (Group grp : h.getEsprit().getGroups().keySet())
				{
					for (Human grpH : grp.getMembers())
						if(target==grpH)	
							InMyGroup=true;
				}

			if(!InMyGroup)
				target.die();

		}

		return nextAction;
	}

	@Override
	public String getInfo() {
		return super.getInfo() + " Pick another random agent in the patch and give it a role in a group.<html>";
	}



}
