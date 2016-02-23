package civilisation.inspecteur.simulation.groupManager;

import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JToolBar;

import I18n.I18nList;
import civilisation.Configuration;
import civilisation.group.GroupAndRole;
import civilisation.group.GroupModel;
import civilisation.inspecteur.simulation.ActionStructureCognitive;

public class GroupToolBar extends JToolBar{

	PanelGroupManager panelGroupManager; //the associated panel for viewing group and role
	JComboBox comboRole;
	JButton buttonNewCulturon;
	JButton buttonAddExistingCulturon;
	JButton buttonRemoveRole;
	JButton buttonAddRole;
	JButton buttonRenameRole;
	JButton buttonGroupOptions;
	public GroupModel groupModel;

	public GroupToolBar(PanelGroupManager panelGroupManager) {
		super();
		
		buttonNewCulturon = new JButton(Configuration.getIcon("prohibition.png"));
		buttonNewCulturon.addActionListener(new ActionsToolBarGroupManager(this,1));


		buttonNewCulturon.setToolTipText(I18nList.CheckLang("Add an existing culturon"));

		this.add(buttonNewCulturon);
		
		buttonAddExistingCulturon = new JButton(Configuration.getIcon("weather-cloud.png"));
		buttonAddExistingCulturon.addActionListener(new ActionsToolBarGroupManager(this,2));

		buttonAddExistingCulturon.setToolTipText(I18nList.CheckLang("Create a new culturon"));
		this.add(buttonAddExistingCulturon);
		
		addSeparator();
		
		buttonRemoveRole = new JButton(Configuration.getIcon("user--minus.png"));
		buttonRemoveRole.addActionListener(new ActionsToolBarGroupManager(this,3));

		buttonRemoveRole.setToolTipText(I18nList.CheckLang("Remove currently selected role (can't remove the last role)"));

		this.add(buttonRemoveRole);
		
		buttonAddRole = new JButton(Configuration.getIcon("user--plus.png"));
		buttonAddRole.addActionListener(new ActionsToolBarGroupManager(this,4));

		buttonAddRole.setToolTipText(I18nList.CheckLang("Add a new role to this group"));

		this.add(buttonAddRole);
		
		buttonRenameRole = new JButton(Configuration.getIcon("user--pencil.png"));
		buttonRenameRole.addActionListener(new ActionsToolBarGroupManager(this,5));

		buttonRenameRole.setToolTipText(I18nList.CheckLang("Rename the current role"));
		this.add(buttonRenameRole);
		
		addSeparator();
		
		buttonGroupOptions = new JButton(Configuration.getIcon("user--pencil.png"));
		buttonGroupOptions.addActionListener(new ActionsToolBarGroupManager(this,6));

		buttonGroupOptions.setToolTipText("Edit the current group options");

		this.add(buttonGroupOptions);
		
		comboRole = new JComboBox();
		this.add(comboRole);
		comboRole.addActionListener(new ActionsToolBarGroupManager(this,0));
		this.panelGroupManager = panelGroupManager;
		

		
	}
	
	public void groupSelectionChanged() {
		panelGroupManager.changeSelection();
	}

	public void changeSelection(GroupModel gm) {
		groupModel = gm;
		if(comboRole.getActionListeners().length > 0)
			comboRole.removeActionListener(comboRole.getActionListeners()[0]);
		comboRole.removeAllItems();
		if(gm != null)
		{
			Set<String> roles = gm.getCulturons().keySet();
			Object[] rolesArray = roles.toArray();
			for (int i = 0 ; i < roles.size() ; i++) {
				comboRole.addItem(rolesArray[i]);
			}
			comboRole.addActionListener(new ActionsToolBarGroupManager(this,0));
		}	
	}
	public String getSelectedRole() {
		return (String) comboRole.getSelectedItem();
	}

	public void createRole(String text) {
		groupModel.setRole(text);
		comboRole.addItem(text);
		if (groupModel.getCulturons().size() > 1) {
			buttonRemoveRole.setEnabled(true);
		}
	}

	public void changeRoleName(String text) {
		groupModel.changeRoleName((String) comboRole.getSelectedItem(), text);
		comboRole.removeItemAt(comboRole.getSelectedIndex());
		comboRole.addItem(text);
	}

	public void removeRole() {
		groupModel.removeRole((String) comboRole.getSelectedItem());
		comboRole.removeItemAt(comboRole.getSelectedIndex());
		if (groupModel.getCulturons().size() <= 1) {
			buttonRemoveRole.setEnabled(false);
		}
	}

	public void changeGroupName(String text) {
		groupModel.setName(text);
		
	}
	
	public Object[] getRoleArray()
	{
		Set<String> roles = groupModel.getCulturons().keySet();
		return roles.toArray();
	}
	
	public void setAutoJoin(boolean join,String role)
	{
		groupModel.setAutoJoin(join, role);
	}
	
}
