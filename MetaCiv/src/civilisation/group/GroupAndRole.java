package civilisation.group;

import java.io.Serializable;

import civilisation.Civilisation;
import civilisation.Configuration;
import civilisation.SchemaCognitif;

/**
 * Just a convenient class to load, save and use a combination of a groupModel and a role
 */

public class GroupAndRole implements Serializable{
	
	GroupModel groupModel;
	String role;
	
	public GroupAndRole (String s, SchemaCognitif c) {//Test Civkraftv0.1
		String g = s.split(":")[0];
		role = s.split(":")[1];
		groupModel = c.getGroupModelByName(g);
	}
	
	public GroupAndRole (Group g , String r) {
		groupModel = g.getGroupModel();
		role = r;
	}

	public GroupModel getGroupModel() {
		return groupModel;
	}

	public void setGroupModel(GroupModel groupModel) {
		this.groupModel = groupModel;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
	
}
