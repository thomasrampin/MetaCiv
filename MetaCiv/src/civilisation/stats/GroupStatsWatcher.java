package civilisation.stats;

import java.util.HashMap;

import madkit.kernel.Probe;
import civilisation.Configuration;
import civilisation.DefineConstants;
import civilisation.group.Group;
import civilisation.group.GroupProbeHuman;
import civilisation.individu.Human;

public class GroupStatsWatcher extends StatsWatcher{

	String grp = "";
	boolean total = false;
	boolean separate = false;
	
	public GroupStatsWatcher(AbstractProbeWidget in) {
		super(in);
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
					s.setPlotName(civ + " " + grp +" inst :" + g.getId());
					s.setciv(((String)civ));
					Configuration.viewer.launchAgent(s);
					for(String role : g.getRolesAndCulturons().keySet())
					{
						s.addProbe(new Probe<Human>(civ, g.getId(), role));
					}
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
					for(String role : g.getRolesAndCulturons().keySet())
				 	{
				 	 	s.addProbe(new Probe<Human>(civ, g.getId(), role));
				 	}
				}
			}
			target.updateFromProbe(s);
			Configuration.viewer.killAgent(s);
		}
	}

	public void activate() {
	 	super.activate();
	 	this.addProbe(new Probe<Group>(DefineConstants.Comunity_MetacivSoftware, DefineConstants.Group_MetacivSocialStructure, DefineConstants.Role_ConcreteGroup));
	}
	
	public void setGrp(String in)
	{
		grp = in;
	}

	public void setTotal(Boolean in)
	{
		total = in;
	}

	public void setSeparate(Boolean in)
	{
		separate = in;
	}
}
