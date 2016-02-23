package civilisation.stats;

import java.util.List;

import civilisation.DefineConstants;
import civilisation.world.World;
import madkit.kernel.AbstractAgent;
import madkit.simulation.activator.*;

public class StatsWatcherBehaviorActivator extends GenericBehaviorActivator<StatsWatcher>{

	public StatsWatcherBehaviorActivator(String community, String group,
			String role, String theBehaviorToActivate) {
		super(community, group, role, theBehaviorToActivate);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void execute(List<StatsWatcher> arg0, Object... arg1) {
		if(World.getInstance().getTick() % DefineConstants.statRefreshInterval == 0)
			super.execute(arg0, arg1);
	}

}
