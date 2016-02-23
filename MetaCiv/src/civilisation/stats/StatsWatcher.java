package civilisation.stats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import turtlekit.kernel.Patch;
import civilisation.Civilisation;
import civilisation.DefineConstants;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
import civilisation.group.Group;
import civilisation.group.GroupModel;
import civilisation.group.GroupProbeFacility;
import civilisation.group.GroupProbeHuman;
import civilisation.group.MessageString;
import civilisation.individu.Esprit;
import civilisation.individu.Human;
import civilisation.individu.cognitons.Cogniton;
import civilisation.individu.cognitons.TypeCogniton;
import madkit.kernel.AbstractAgent;
import madkit.kernel.Probe;
import madkit.kernel.Watcher;

public class StatsWatcher extends Watcher{
	
	String plotName;
	AbstractProbeWidget target;
	String civ = "";
	
	public StatsWatcher(AbstractProbeWidget in) {
		target = in;
	}
	
	public void activate() {
	 	createGroupIfAbsent(DefineConstants.Comunity_MetacivSoftware, DefineConstants.Group_StatWatcher);
	 	requestRole(DefineConstants.Comunity_MetacivSoftware,  DefineConstants.Group_StatWatcher, DefineConstants.Role_StatWatcher);
	}

	public void updateChart()
	{
		target.updateFromProbe(this);
	}

	public void setPlotName(String in) {
		plotName = in;
	}

	public void setciv(String in) {
		civ = in;
	}

	public int getAgentCount() {
		int result = 0;
		
		for(Probe p : getProbes())
		{
			result+= p.getCurrentAgentsList().size();
		}
		return result;
	}
	
	public ArrayList<Human> getAllHumans() {
		ArrayList<Human> result = new ArrayList<Human>();
		Set<Probe<? extends AbstractAgent>> probes = this.getProbes();
		for(Probe<? extends AbstractAgent> p : probes)
		{
//			if(p.getRole().equals(DefineConstants.Role_Human))
			result.addAll((Collection<? extends Human>) p.getCurrentAgentsList());
		}
		return result;
	}

}
