package civilisation.inspecteur;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import I18n.I18nList;
import turtlekit.kernel.Turtle;
import civilisation.Configuration;
import civilisation.DefineConstants;
import civilisation.group.GroupAndRole;
import civilisation.individu.Human;
import civilisation.world.World;
import civilisation.world.WorldViewer;

/** 
 * Present options for vizualisation
*/


public class PanelOptions extends JPanel{

	JToolBar toolBar = new JToolBar();
	Box boite;
	JComboBox planAffiche;
	JComboBox frontiereAffichee;
	JComboBox pheroMap;
	JComboBox groupesAndRoles;
	ArrayList<String> listePlan;
	
	public PanelOptions()
	{
		listePlan =new ArrayList<String>();
		listePlan.add("--NONE--");
		
		for (int i = 0; i < Configuration.getSchemaCognitifEnCourEdition().getPlans().size(); i++) {
			listePlan.add(Configuration.getSchemaCognitifEnCourEdition().getPlans().get(i).getNom());  
		}

		
        boite = new Box(BoxLayout.PAGE_AXIS);

        //boite.add(Box.createVerticalStrut(600));
        
        planAffiche = new JComboBox(listePlan.toArray());
        planAffiche.addActionListener(new ActionOptionsListener(this, 0));

        JLabel labelAffichage = new JLabel(I18nList.CheckLang("Affichage des plans : "));

        frontiereAffichee = new JComboBox();
        frontiereAffichee.addItem("Show limes (NYI)");
        frontiereAffichee.addItem("Hide limes (NYI)");
        frontiereAffichee.addActionListener(new ActionOptionsListener(this, 1));
        
        /* Allow to select a phero-map*/

        JLabel labelPheroMap = new JLabel(I18nList.CheckLang("Phero map : "));

        pheroMap = new JComboBox();
        
        
        pheroMap.addItem("--NONE--");
		for (int i = 0; i < Configuration.itemsPheromones.size(); i++) {
			pheroMap.addItem(Configuration.itemsPheromones.get(i).getNom());  
		}
        pheroMap.addActionListener(new ActionOptionsListener(this, 2));
        
        groupesAndRoles = new JComboBox();
        groupesAndRoles.addItem("--NONE--");
		for (int j = 0; j < Configuration.getSchemaCognitifEnCourEdition().getGroups().size(); j++){
			Object[] keys = (Object[]) Configuration.getSchemaCognitifEnCourEdition().getGroups().get(j).getCulturons().keySet().toArray();
			for (int k = 0 ; k < Configuration.getSchemaCognitifEnCourEdition().getGroups().get(j).getCulturons().size() ; k++) {
				groupesAndRoles.addItem(Configuration.getSchemaCognitifEnCourEdition().getGroups().get(j).getName()+":"+(String)keys[k]);	
			}
		}
		groupesAndRoles.addActionListener(new ActionOptionsListener(this, 3));

        boite.add(labelAffichage);
		boite.add(planAffiche);
		boite.add(frontiereAffichee);
		boite.add(labelPheroMap);
		boite.add(pheroMap);

		boite.add(new JLabel(I18nList.CheckLang("Show specific group and role :")));
		boite.add(groupesAndRoles);

		this.add(boite);
		
		this.setVisible(true);
	}
	
	
	public void modifierAffichagePlans()
	{
		if (planAffiche.getSelectedIndex() > 0) {
			WorldViewer.getInstance().setPlanVisible(Configuration.getSchemaCognitifEnCourEdition().getPlans().get(planAffiche.getSelectedIndex() - 1));
		} else {
			WorldViewer.getInstance().setPlanVisible(null);
		}
	}
	
	public void modifierAffichageFrontieres()
	{
		if (frontiereAffichee.getSelectedItem().equals("Show limes :"))
		{
			WorldViewer.getInstance().setFrontieresVisibles(true);
		}
		else
		{
			WorldViewer.getInstance().setFrontieresVisibles(false);
		}

	}
	
	public void showPheroMap()
	{
		if (pheroMap.getSelectedIndex() > 0) {
			WorldViewer.getInstance().setPheroToMap(Configuration.itemsPheromones.get(pheroMap.getSelectedIndex() - 1));
		} else {
			WorldViewer.getInstance().setPheroToMap(null);
		}
	}


	public void showGroupAndRole() {
		
		int nHumans = World.getInstance().getTurtlesWithRoles(DefineConstants.Role_Human).size();
		if (!groupesAndRoles.getSelectedItem().equals("--NONE--")) {
		for (int i = 0 ; i < nHumans ; i++) {
			if ((select(i) != null && select(i).getClass() == Human.class))
			{
				System.out.println(i);
				if ( ((Human)select(i)).getEsprit().hasGroupAndRole(new GroupAndRole((String)groupesAndRoles.getSelectedItem(), Configuration.getSchemaCognitifEnCourEdition()) ))
				{
					System.out.println(true);
					((Human) select(i)).isShowGroup = true;
				} else {
					((Human) select(i)).isShowGroup = false;			
				}
			}
		}
		}
		else {
			for (int i = 0 ; i < nHumans ; i++) {
				if ((select(i) != null && select(i).getClass() == Human.class))
				{		
					((Human) select(i)).isShowGroup = false;			
				}
			}	
		}
	}


	private Turtle select(int id) {
		return (Human) World.getInstance().getTurtlesWithRoles(DefineConstants.Role_Human).get(id);
	}
	


	
}




