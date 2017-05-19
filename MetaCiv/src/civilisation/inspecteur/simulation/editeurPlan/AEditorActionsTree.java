package civilisation.inspecteur.simulation.editeurPlan;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

@SuppressWarnings("serial")
/**
 * JTree contenant le nom des actions
 * 
 * @author Arnau
 *
 */
public class AEditorActionsTree extends JTree {

	public AEditorActionsTree(TreeNode root) {
		super(root);
		init();
	}

	private void init() {
		setDragEnabled(true);
		setTransferHandler(new ActionsJTreeTransferHandler());
	}

	/**
	 * Gestionnaire du transfert de données
	 * 
	 * @author Arnau
	 *
	 */
	private class ActionsJTreeTransferHandler extends TransferHandler {

		@Override
		public int getSourceActions(JComponent c) {
			return TransferHandler.COPY; // copy seulement
		}

		@Override
		protected Transferable createTransferable(JComponent c) {
			JTree tree = (JTree) c;
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			if (node.isLeaf()) { // seulement les feuilles sont transferables
				return new StringSelection(node.getUserObject().toString());
			} else {
				return null;
			}
		}

	}
}
