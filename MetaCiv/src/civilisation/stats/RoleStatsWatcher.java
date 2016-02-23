package civilisation.stats;

import madkit.kernel.Probe;
import civilisation.Configuration;
import civilisation.group.Group;
import civilisation.individu.Human;

public class RoleStatsWatcher extends GroupStatsWatcher{

	String role;
	
	public RoleStatsWatcher(AbstractProbeWidget in) {
		super(in);
	}

	public void setRole(String in)
	{
		role = in;
	}
	
	public void updateChart()
	{
		if(separate)
		{
			Probe<Group> p = (Probe<Group>)this.getProbes().toArray()[0];
			for(Group g : p.getCurrentAgentsList())
			{
				if(g.getGroupModel().getName().equals(grp) && g.getCiv().equals(civ))
				{
					StatsWatcher s = new StatsWatcher(target);
					s.setPlotName(civ + " " + grp +" inst :" + g.getId() + " role :" + role);
					s.setciv(((String)civ));
					Configuration.viewer.launchAgent(s);
					
					s.addProbe(new Probe<Human>(civ, g.getId(), role));
					
					target.updateFromProbe(s);
					Configuration.viewer.killAgent(s);
				}
			}
		}
		if(total)
		{
			StatsWatcher s = new StatsWatcher(target);
			s.setPlotName(plotName);
			s.setciv(((String)civ));
			Configuration.viewer.launchAgent(s);
			Probe<Group> p = (Probe<Group>)this.getProbes().toArray()[0];
			for(Group g : p.getCurrentAgentsList())
			{
				if(g.getGroupModel().getName().equals(grp) && g.getCiv().equals(civ))
				{
					s.addProbe(new Probe<Human>(civ, g.getId(), role));
				}
			}
			target.updateFromProbe(s);
			Configuration.viewer.killAgent(s);
		}
	}
}
