package civilisation.inspecteur.simulation.editeurPlan;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import civilisation.Configuration;

@SuppressWarnings("serial")
/**
 * Panel qui contient l'abre des actions disponibles a ajouter dans l'editeur
 * @author Arnau
 *
 */
public class AEditorPanelActions extends JPanel {
	private JTree actionsTree;

	public AEditorPanelActions() {
		super();
		init();
	}
	
	private void init() {
		setLayout(new BorderLayout());
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Listes des actions de l'editeur de plan");
		DefaultMutableTreeNode actions = new DefaultMutableTreeNode("Actions");
		DefaultMutableTreeNode actionsL = new DefaultMutableTreeNode("Actions logiques");
		for (int i = 0; i < Configuration.actions.size(); i++){
			if(!Configuration.actions.get(i).isDeprecated()){
				String actionName = Configuration.actions.get(i).getName().split("\\.")[Configuration.actions.get(i).getName().split("\\.").length -1];
				DefaultMutableTreeNode action = new DefaultMutableTreeNode(actionName);	//on cree un nouveau noeud contantn le nom de l'action
				if(actionName.startsWith("L_")){ // si action logique, on ajoute dans l'arbre d'actions logiques
					actionsL.add(action);
				} else if(actionName.startsWith("A_")) { // si action normale, on ajoute dans l'arbre d'actions normales
					actions.add(action);
				}
			}
		}
		root.add(actions);
		root.add(actionsL);
		actionsTree = new AEditorActionsTree(root);
		add(actionsTree, BorderLayout.CENTER);
	}
}
