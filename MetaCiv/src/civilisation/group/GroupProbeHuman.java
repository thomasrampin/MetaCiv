package civilisation.group;

import civilisation.DefineConstants;
import civilisation.TurtleGenerator;
import civilisation.individu.Human;
import civilisation.world.World;
import madkit.kernel.Probe;
import madkit.message.SenderRoleFilter;

public class GroupProbeHuman extends Probe<Human>{
	
	Group myGroup;

	
	public GroupProbeHuman(String communityName, String groupName,String roleName, Group myGroupIn) {
		super(communityName, groupName, roleName);
		myGroup = myGroupIn;
	}

	@Override
	public void adding(Human H)
	{
		//System.out.println(H.getName() + " is joining group " + myGroup.groupId + " of type "+myGroup.groupModel.getName());

		myGroup.hasMembersInRole.put(getRole(), true);
	}
	@Override
	public void removing(Human H)
	{
		//System.out.println(H.getName() + " is leaving group " + myGroup.groupId + " of type "+myGroup.groupModel.getName());
		if(getCurrentAgentsList().size() <= 0)
		{		
			myGroup.hasMembersInRole.put(getRole(), false);
			if(myGroup.checkIfEmpty())
			{
				myGroup.sendMessage(myGroup.getAgentAddressIn(DefineConstants.Comunity_MetacivSoftware, DefineConstants.Group_MetacivSocialStructure, DefineConstants.Role_ConcreteGroup),new MessageString(DefineConstants.Msg_KillYourself));
			}
		}
	}
	
}
