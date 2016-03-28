package civilisation.inspecteur.simulation;

import java.util.ArrayList;

import javax.swing.tree.*;

import civilisation.individu.plan.NPlan;
import civilisation.individu.plan.action.Action;
import civilisation.pathfinder.Noeud;

public class ModeleArbreActions extends DefaultTreeModel
{
	NodeArbreActions root;
	
	public ModeleArbreActions(NPlan plan)
	{
		super(new NodeArbreActions("ROOT"));
		root = (NodeArbreActions) getRoot();
		

		for (int j = 0; j < plan.getActions().size(); j++)
		{
			addAction(root , plan.getActions().get(j));
		}
	}
	
	public void addAction(NodeArbreActions parent , Action action){		
		NodeArbreActions node = new NodeArbreActions(action);
		if (!action.getListeActions().isEmpty()){
			for (int i = 0; i < action.getListeActions().size(); i++){
				addAction(node , action.getListeActions().get(i));
			}
		}
		parent.add(node);
	}
	public NPlan setPlan(NodeArbreActions rt){
		Action a;
		NPlan np = new NPlan();
		for(int i=0;i<rt.getChildCount();i++){
			a = ((NodeArbreActions) rt.getChildAt(i)).getAction();
			System.out.println("on ajoute "+a+" au plan");
			np.addAction(a);
		}
		for(int i=0;i<rt.getChildCount();i++){
			a = ((NodeArbreActions) rt.getChildAt(i)).getAction();
			if(a.isLogical()){
				System.out.println("////////////");
				parcours((NodeArbreActions)rt.getChildAt(i));
			}
		}
		return np;
	}
	
	public void parcours(NodeArbreActions nd){
		Action a = nd.getAction();
		a.getListeActions().clear();
		Action fils;
		for (int i = 0; i < nd.getChildCount(); i++){
			fils = ((NodeArbreActions) nd.getChildAt(i)).getAction();
			a.addSousAction(fils);
			System.out.println("on ajoute "+fils+" au plan");
			System.out.println("logical "+fils+" ? "+fils.isLogical());
			System.out.println("a pour père "+a);
		}
		for (int i = 0; i < nd.getChildCount(); i++){
			fils = ((NodeArbreActions) nd.getChildAt(i)).getAction();
			if(fils.isLogical()){
				System.out.println("////////////");
				parcours((NodeArbreActions)nd.getChildAt(i));
			}
		}
	}
}
