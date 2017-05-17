package civilisation.inspecteur.simulation.editeurPlan;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.TransferHandler;
import javax.swing.TransferHandler.DropLocation;
import javax.swing.event.MouseInputAdapter;

import civilisation.Configuration;
import civilisation.individu.plan.NPlan;
import civilisation.individu.plan.action.Action;
import civilisation.inspecteur.simulation.editeurPlan.AEditorBlock.ActionBlockPopupMenu;

@SuppressWarnings("serial")
public class AEditorPanelEditor extends JLayeredPane{
	
	public final static Color EDITOR_COLOR = AEditorColors.WHITE.color;
	private NPlan plan;
	private AEditorBlock root;
	
	public AEditorPanelEditor(NPlan plan) {
		super();
		this.plan = plan;
		init(plan);
	}
	
	private void init(NPlan plan) {
		initPlan(plan);
		setLayout(new AEditorDragLayout()); // pour poser où on veut dans le panel
		setTransferHandler(new PanelEditorTransferHandler(this)); // on ajoute le transfer handler entre le panel editor et la liste des actions
		setBackground(EDITOR_COLOR);
	}
	
	private void initPlan(NPlan plan) {
		root = new AEditorBlockRoot("Root"); // ajout du block root
		addBlock(root, new Point(100, 30));
		//ajout des actions du plan
		AEditorBlock parent = root;
		for(Action action : plan.getActions()) {
			parent = addAction(parent, action, AEditorBlock.APPEND);
		}
	}
	
	/**
	 * Ajoute une action dans le paneau editeur.
	 * Fonction utilisée pour initialiser le paneau editeur d'un plan qui contient déjà des actions.
	 * @param parent , le block qui va etre parent de l'action a ajouter
	 * @param action l'action a ajouter 
	 * @param mode le mode d'ajout au parent, par exemple APPEND pour ajouter apres le parent
	 * @return le block nouvelement créé 
	 */
	private AEditorBlock addAction(AEditorBlock parent , Action action, int mode){
		AEditorBlock block;
		if(action.getNumberActionSlot() == 2) { // action If_else
			block = new AEditorBlockLogique(action);
			addListenersToBlock(block);
			addBlock(parent, block, mode); // on ajoute le block crée au block parent
			addAction(block, action.getListeActions().get(0), AEditorBlockLogique.IF); // on ajoute au block crée un block representant l'action If
			addAction(block, action.getListeActions().get(1), AEditorBlockLogique.ELSE); // on ajoute au block crée un block representant l'action Else
		} else if (action.getNumberActionSlot() < 0){ // action Instant
			block = new AEditorBlockInstant(action);
			addListenersToBlock(block); // on ajoute le block crée au block parent
			addBlock(parent, block, mode);
			AEditorBlock parentAdd = block;
			for(int i = 0; i < action.getListeActions().size(); ++i) {
				if (i == 0) {
					parentAdd = addAction(parentAdd, action.getListeActions().get(i), AEditorBlockInstant.INSTANT); // on ajoute la premiere action au block Instant lui meme 
				} else
					parentAdd = addAction(parentAdd, action.getListeActions().get(i), AEditorBlockInstant.APPEND); // les autres actions seront ajoutés aux blocks de la derniere action ajoutée
			}
		} else { // action normale
			block = new AEditorBlockAction(action);
			addListenersToBlock(block);
			addBlock(parent, block, mode); // on ajoute le block crée au block parent
			AEditorBlock parentAdd = block;
			for(Action a : action.getListeActions()) {
				parentAdd = addAction(parentAdd, a, AEditorBlock.APPEND); //on ajoute l'action au block de la derniere action crée
			}
		}
		return block;
	}
	
	/**
	 * Fonction qui permet de valider le point de drop du drag and drop choisie de l'abre d'actions
	 * @param loc , la position du drop
	 * @return vrai si la position est correcte, faux sinon
	 */
	private boolean validateDropLocation(DropLocation loc) {
		for(Component c : getComponents()) {
			//if there is aleready a component at the drop location in the panel
			Point p = loc.getDropPoint();
			AEditorBlock b = (AEditorBlock) c;
			if(c.contains(p)) {
				return false;
			}
		}
		// check pour savoir si le block peut etre inserer sous un bloc ou au dessus d'un bloc sans parent
		for(Component c : getComponents()) {
			Point p = loc.getDropPoint();
			AEditorBlock b = (AEditorBlock) c;
			b.changeBorder(p); // change la bordure des blocks pour savoir si on ou le block va s'inserer
		}
		//if the is no component at the drop location
		return true;
	}
	
	/**
	 * Ajoute les listeneurs liées au panel editeur sur le block en parametre ainsi que l'option delete dans le menu popup du block
	 * @param block
	 */
	private void addListenersToBlock(AEditorBlock block) {
		//on cree un drag listener pour bouger les block apres les avoir inserer
		DragEListener dragL = new DragEListener();
		block.addMouseListener(dragL);
		block.addMouseMotionListener(dragL);
		//on ajoute l'option delete dans le menu popup du block
		final ActionBlockPopupMenu menuPopupBlock = (ActionBlockPopupMenu) block.getComponentPopupMenu();
		JMenuItem delete = new JMenuItem("Supprimer");
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				menuPopupBlock.getBlock().dissociate(menuPopupBlock.getBlock(), plan);
				AEditorPanelEditor.this.remove(menuPopupBlock.getBlock());
				AEditorPanelEditor.this.revalidate();
				//AEditorPanelEditor.this.repaint();
			}
		});
		menuPopupBlock.add(delete);
	}
	
	/**
	 * Ajoute un block si possible au point loc
	 * @param block le block a ajouté
	 * @param loc , la position où ajouter le block
	 */
	public void addBlock(AEditorBlock block, Point loc) {
		this.add(block);
		//on modifier sa taille a la main car il n'y a pas de layout manager
		block.setSize(block.getPreferredSize());
		//on le positione a l'endroit du drop
		Point p = loc;
		for(Component c : getComponents()) {
			AEditorBlock b = (AEditorBlock) c;
			if(!b.equals(block) && b.attach(block, p, plan)) { // si on peux attacher le block a un autre block
				block.updatePanelLayer(this); // on met a jour le layer du block ajouté ainsi que ses fils potentiels
				revalidate();
				//repaint();
				return;
			}
		}
		// si on ne peux pas attacher le block a un autre block, on le pose a l'emplacement de la souris
		block.setLocation(p.x - (block.getWidth()/2), p.y - (block.getHeight()/2));
		block.updatePanelLayer(this); // on met a jour le layer du block ajouté ainsi que ses fils potentiels
		revalidate();
		//repaint();
	}
	
	/**
	 * Ajoute un block dans l'editeur apres le block parent passé en parametre
	 * @param parent, block parent du block a ajouter
	 * @param block, le block a ajouter
	 */
	private void addBlock(AEditorBlock parent, AEditorBlock block, int mode) {
		this.add(block);
		//on modifier sa taille a la main car il n'y a pas de layout manager
		block.setSize(block.getPreferredSize());
		parent.attach(block, mode, null);
		block.updatePanelLayer(this);// on met a jour le layer du block ajouté ainsi que ses fils potentiels
		revalidate();
		//repaint();
	}
	
	public void applyPlanEdition() {
		//ArrayList<Action> listeAction = ;
		plan.setActions(getListeActions());
	}
	
	private ArrayList<Action> getListeActions() {
		ArrayList<Action> listeAction = new ArrayList<>();
		return listeAction;
	}
	
	/**
	 * Listener de drag permettant de deplacer les blocks dans l'editeur de plan.
	 * 
	 * @author Arnau
	 *
	 */
	private class DragEListener extends MouseInputAdapter{
		Point location;
	    MouseEvent pressed;
	    AEditorBlock block;
	    boolean drag = false;
	    
	    public void mousePressed(MouseEvent me)
	    {
	    	if(me.getButton() == MouseEvent.BUTTON1) { // si click gauche, pour pas faire conflit avec le menu click droit du block
		        pressed = me;
		        drag = true;
		        block = (AEditorBlock) me.getSource();
		        block.getLocation();
		        block.removeActionFromPlan(plan); // on enleve l'action du block du plan
		        block.dissociateFromParent(block); // on desassocie le block de son parent et de ses fils
		        block.setCanHaveChild(true); // le block peut avoir des fils a nouveau
	    	}
	    }
	 
	    public void mouseDragged(MouseEvent me)
	    {
	    	if(drag) {
		        Component component = me.getComponent();
		        location = component.getLocation(location);
		        AEditorBlock blockDrag = (AEditorBlock) component;
		        Point dropLocation = new Point(location.x + me.getX() , location.y + me.getY());
		        setCursor(DragSource.DefaultMoveDrop);
		        for(Component c : getComponents()) {
					AEditorBlock b = (AEditorBlock) c;
					if(!b.equals(blockDrag)) // ne pas changer la bordure du block que l'on drag
						b.changeBorder(dropLocation);
				}
		        int x = location.x - pressed.getX() + me.getX();
		        int y = location.y - pressed.getY() + me.getY();
		        blockDrag.setLocationWithChildren(x, y);
	    	}
	     }
	    
	    public void mouseReleased(MouseEvent me) {
	    	if(drag) {
		    	Component component = me.getComponent();
		        location = component.getLocation(location);
		        AEditorBlock blockDrag = (AEditorBlock) component;
		        Point dropLocation = new Point(location.x + me.getX() , location.y + me.getY());
				for(Component c : getComponents()) {
					AEditorBlock b = (AEditorBlock) c;
					b.resetBorder();
					if(!b.equals(blockDrag) && b.attach(blockDrag, dropLocation, plan)) {
						blockDrag.updatePanelLayer(AEditorPanelEditor.this);
						setCursor(Cursor.getDefaultCursor());
						revalidate();
						//repaint();
						return;
					}
				}
		        int x = location.x - pressed.getX() + me.getX();
		        int y = location.y - pressed.getY() + me.getY();
		        blockDrag.setLocationWithChildren(x, y);
				blockDrag.updatePanelLayer(AEditorPanelEditor.this);
		        setCursor(Cursor.getDefaultCursor());
		    }
	    }
	}
	
	/**
	 * Handler de transferts entre le jtree d'actions et le panel d'edition
	 * S'occupe de la creation des blocks ajoutés depuis la liste des actions.
	 * 
	 * @author Arnau
	 *
	 */
	private class PanelEditorTransferHandler extends TransferHandler {
		
		private AEditorPanelEditor editorPanel;
		
		public PanelEditorTransferHandler(AEditorPanelEditor panel) {
			super();
			editorPanel = panel;
		}
		
		@Override
		public boolean canImport(TransferSupport support) {
			//check for string flavor
			if(!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				return false;
			}
			//fetch the drop location
			DropLocation loc = support.getDropLocation();
			//check if there is a component at the drop location
			return editorPanel.validateDropLocation(loc);
		}
		
		@Override
		public boolean importData(TransferSupport support) {
			if(!canImport(support)) {
				return false;
			}
			//fetch transferable data
			Transferable t = support.getTransferable();
			try {
				String data = (String) t.getTransferData(DataFlavor.stringFlavor);
				// cree un block avec les données transferées
				DropLocation loc = support.getDropLocation();
				AEditorBlock block;
				String[] s = new String[1]; 
				s[0] = data; // on recupere le nom de l'action et on la met dans ce tableau utilisé pour creer l'action
				Action action = Action.actionFactory(s, Configuration.getSchemaCognitifEnCourEdition()); // l'action est crée
				if(action.getNumberActionSlot() == 2) { // action logique
					block = new AEditorBlockLogique(action);
				} else if(action.getNumberActionSlot() == 0) { // action simple
					block = new AEditorBlockAction(action);
				} else { // actions de type repeat ou instant
					block = new AEditorBlockInstant(action);
				}
				addListenersToBlock(block);
				editorPanel.addBlock(block, loc.getDropPoint());
				for(Component c : editorPanel.getComponents()) { // reset les borders d'aide au drop des blocks
					AEditorBlock b = (AEditorBlock) c;
					b.resetBorder();
				}
				editorPanel.revalidate();
			} catch (UnsupportedFlavorException e) {
				System.err.println(e.getMessage());
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
	}
}
