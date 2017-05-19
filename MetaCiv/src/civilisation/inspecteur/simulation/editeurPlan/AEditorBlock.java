package civilisation.inspecteur.simulation.editeurPlan;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import civilisation.individu.plan.NPlan;
import civilisation.individu.plan.action.Action;
import civilisation.inspecteur.simulation.dialogues.DialogueEditerAction;

@SuppressWarnings("serial")
public abstract class AEditorBlock extends JPanel implements AEditorMagnetComponent {
	
	protected boolean canHaveChild;
	protected Action action;
	protected AEditorPanelParametres content; // panel qui contient le nom de l'action et les parametres
	protected String name;
	protected JLabel lname;
	protected AEditorBlock parent; // block parent
	protected AEditorBlock fils; // block fils
	protected int INDENT_RATIO = 0; // val utilisé pour determiner le ratio d'indentation d'un block fils
	protected final static int SPACE_BETWEEN_BLOCK = 7; // espace entre chaque block quand ils sont connectés
	public static final int OK_MODE = 0; // mode utilisé pour changer les borders
	public static final int FORBIDDEN_MODE = 1; //mode utilisé pour changer les borders
	private final Border PADDING = BorderFactory.createEmptyBorder(5, 10, 5, 10);
	private final Border OK_BORDER_TOP = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.GREEN), PADDING);
	private final Border FORBIDDEN_BORDER_TOP = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.RED), PADDING);
	private final Border OK_BORDER_BOTTOM = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GREEN), PADDING);
	private final Border FORBIDDEN_BORDER_BOTTOM = BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.RED), PADDING);
	public static final int PREPEND = 0; // mode utilisé pour ajouter un block 
	public static final int APPEND = 1; // mode utilisé pour ajouter un block 
	
	/**
	 * Cree un block sans action mais avec le nom en parametre
	 * @param name
	 */
	public AEditorBlock(String name) {
		super();		
		init(name);
	}
	
	/**
	 * Cree un block en utilisant representant l'action en parametre
	 * @param action
	 */
	public AEditorBlock(Action action) {
		super();
		this.action = action;
		init(action.getSimpleName());
	}
	
	private void init(String name) {
		if(action != null) {
			this.content = new AEditorPanelParametres(action);
		} else {
			this.content = new AEditorPanelParametres(name);
		}
		this.name = name;
		this.parent = null;
		this.fils = null;
		this.canHaveChild = true;
		setBackground(AEditorColors.PURPLE.color);
		lname = content.getLName();
		lname.setForeground(Color.RED);
		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
		content.setBackground(AEditorColors.PURPLE.color);
		this.add(content);
		if(action != null) {
			this.setToolTipText(action.getInfo());
			this.setComponentPopupMenu(new ActionBlockPopupMenu(this));
		}
		setBorder(PADDING);
	}
	
	/**
	 * 
	 * @return l'action du block
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * bl devient le parent de ce block
	 * @param bl lenouveau parent
	 */
	public void setParentBlock(AEditorBlock bl) {
		this.parent = bl;
		if(bl != null)
			lname.setForeground(Color.WHITE);
		else
			lname.setForeground(Color.RED);
	}
	
	/**
	 * 
	 * @return block parent
	 */
	public AEditorBlock getParentBlock() {
		return this.parent;
	}
	
	/**
	 * 
	 * @param child le fils du block, càd le block en dessous de celui ci
	 */
	public void setChild(AEditorBlock child) {
		this.fils = child;
	}
	
	/**
	 * 
	 * @return le fils du block, càd le block en dessous
	 */
	public AEditorBlock getChild() {
		return this.fils;
	}
	
	public boolean hasChild() {
		return fils != null;
	}
	
	public boolean hasParent() {
		return parent != null;
	}
	
	/**
	 * Renvoie le Z-Index du block dans le panel editor
	 * @return 
	 */
	public int getLevel() {
		return parent != null ? parent.getLevel() + 1 : 0;
	}
	
	public boolean canHaveChild() {
		return canHaveChild;
	}
	
	public void setCanHaveChild(boolean b) {
		this.canHaveChild = b;
	}
	
	/**
	 * Associe les deux blocks au blocken parametre qui deviendra son parent
	 * @param parent le nouveau parent pour ce block
	 */
	public void associate(AEditorBlock parent) {
		if(this.parent == null && !parent.hasChild()) {
			parent.setChild(this);
			this.setParentBlock(parent);
		}
	}
	
	/**
	 * Dessasocie le block de son parent
	 * @param child doit etre le block faisant appel a la fonction
	 * Exemple : block.dissociateFromParent(block)
	 */
	public void dissociateFromParent(AEditorBlock child) {
		if(parent != null) {
			parent.dissociateFromChild(child);
		}
	}
	
	/**
	 * Dessasocie le block de son fils
	 */
	public void dissociateFromChild() {
		if(fils != null) {
			fils.setParentBlock(null);
			fils = null;
		}
	}
	
	/**
	 * 
	 * @param child
	 */
	protected void dissociateFromChild(AEditorBlock child) {
		dissociateFromChild();
	}
	
	/**
	 * Dessasocie le block de son parent et de ses fils
	 * @param bl le block faisant appel a la fonction 
	 * @param plan le plan a modifié
	 */
	public void dissociate(AEditorBlock bl, NPlan plan) {
		removeActionFromPlan(plan);
		dissociateFromParent(bl);
		dissociateFromChild();
	}
	
	/**
	 * Supprime l'action de ce block du plan
	 * @param plan
	 */
	public void removeActionFromPlan(NPlan plan) {
		for(Action a : plan.getActions()) {
			if(a.equals(action)) { // si l'action de ce block est directement accesible depuis le plan
				plan.removeAction(action);
				AEditorBlock f = fils;
				while(f != null) { // tant que ya des fils
					plan.removeAction(f.getAction());
					f = f.getChild();
				}
				break;
			} else { // sinon on cherche l'action parent de l'action du block auquel on append
				Action act = a.getParentAction(action); // on cherche l'action parent de l'action de ce block
				if(act != null) { // si on on a trouvé cette action
					act.removeAction(action);
					AEditorBlock f = fils;
					while(f != null) { // tant que ya des fils
						act.removeAction(f.getAction());
						f = f.getChild();
					}
					break;
				}
			}
		}
	}
	
	/**
	 * Retourne le dernier fils (soit le block sans fils) à partir de ce block
	 * @return
	 */
	public AEditorBlock getLastChild() {
		if(fils == null) {
			return this;
		}
		return fils.getLastChild();
	}
	
	/**
	 * Donne le ration d'indentation du block
	 * @return 
	 */
	public int getIndentRatio() {
		return INDENT_RATIO;
	}
	
	/**
	 * retourne l'action parente de l'action du block dans le plan
	 * Par exemple, pour trouver l'action Repeat dans laquelle est contenue une action simple, on remonte les blocks parents recursivement jusqua que le block instant revoit son action
	 * @param thisAction
	 * @return
	 */
	public Action getParentAction(Action thisAction) {
		if(parent != null) {
			return parent.getParentAction(action);
		}
		return null;
	}
	
	/**
	 * Deplace le block aux coordonnées passés en parametre ainsi qu'a la bonne position pour ces fils
	 * @param x
	 * @param y
	 */
	public void setLocationWithChildren(int x, int y) {
		int oldx, oldy;
		oldx = getX();
		oldy = getY();
		int diffX, diffY;
		diffX = oldx - x;
		diffY = oldy - y;
		this.setLocation(x, y);
		setLocationDiffToChildren(diffX, diffY); // update la position des fils par rapport à la nouvelle posistion du block
	}
	
	/**
	 * Modifier la position des fils
	 * @param diffX
	 * @param diffY
	 */
	protected void setLocationDiffToChildren(int diffX, int diffY) {
		if(fils != null) {
			fils.setLocation(fils.getX() - diffX, fils.getY() - diffY);
			fils.setLocationDiffToChildren(diffX, diffY);
		}
	}
	
	/**
	 * Ajoute le block en parametre aprés ce block
	 * @param bl
	 */
	public void append(AEditorBlock bl, NPlan plan) {
		bl.associate(this); // on associe bl a ce block
		if(getIndentRatio() == 0) { // pour indenter les blocks dans l'editeur
			bl.setLocationWithChildren(getX(), getY() + getHeight() + SPACE_BETWEEN_BLOCK);
		} else {
			bl.setLocationWithChildren(getX() + getWidth() / getIndentRatio(), getY() + getHeight() + SPACE_BETWEEN_BLOCK);
		}
		/**
		 * Modificitations du plan des actions
		 */
		if(plan != null && action != null) { // si ce block contient une action et que l'on ajoute au plan
			for(Action a : plan.getActions()) {
				if(a.equals(action)) { // si l'action de ce block est directement accesible depuis le plan
					plan.addActionAfter(bl.getAction(), a); // on ajoute l'action de notre nouveau block directement apres
					bl.addChildrenActionsPlan(plan);
					return;
				}
			}
			Action act = getParentAction(this.action);
			if(act != null) { // si on on a trouvé cette action
				act.addActionAfter(bl.getAction(), action); // on ajoute l'action de notre nouveau block aprés l'action de ce block dans l'action parente qu'on vient de cherché
				bl.addChildrenActions(act);
			}
		}
	}

	/**
	 * Ajoute le block en parametre avant ce block
	 * N'est jamais utilisé puisque il n'y a plus de blocks permettant le PREPEND
	 * Ne marche pas je crois
	 * @param bl
	 */
	@Deprecated
	public void prepend(AEditorBlock bl, NPlan plan) {
		this.associate(bl);
		if(getIndentRatio() == 0) {
			bl.setLocation(getX(), getY() - getHeight() - SPACE_BETWEEN_BLOCK);
		} else {
			bl.setLocation(getX() - getWidth() / getIndentRatio(), getY() - getHeight() - SPACE_BETWEEN_BLOCK);
		}
		/**
		 * Modificitations du plan des actions
		 */
		if(plan != null && action != null) { // si ce block contient une action et que l'on ajoute au plan
			for(Action a : plan.getActions()) {
				if(a.equals(action)) { // si l'action de ce block est directement accesible depuis le plan
					a.addActionBefore(bl.getAction(), a); // on ajoute l'action de notre nouveau block directement avant
					return;
				} else { // sinon on cherche l'action parent de l'action du block auquel on append
					Action act = a.getParentAction(action); // on cherche l'action parent de l'action de ce block
					if(act != null) { // si on on a trouvé cette action
						act.addActionBefore(bl.getAction(), action); // on ajoute l'action de notre nouveau block avant l'action de ce block dans l'action parente qu'on vient de cherché
						return;
					}
				}
			}
		}
	}
	
	/**
	 * Ajoute les actions des fils de ce block a l'action en parametre apres l'action de ce block
	 * @param aParent, l'action parent de l'action de ce block
	 */
	protected void addChildrenActions(Action aParent) {
		if(hasChild()) {
			aParent.addActionAfter(fils.getAction(), action);
			fils.addChildrenActions(aParent);
		}
	}
	
	/**
	 * Ajoute les actions des fils de ce block au plan directement apres l'action de ce block
	 * @param plan, le plan des actions
	 */
	protected void addChildrenActionsPlan(NPlan plan) {
		if(hasChild()) {
			plan.addActionAfter(fils.getAction(), action);
			fils.addChildrenActionsPlan(plan);
		}
	}
	
	/**
	 * Determine si l'endroit au point p est dans une zone magnetique
	 * @param p
	 * @return 
	 */
	public boolean isInMagnetEffect(Point p) {
		if(isInsideMagnetEffectBottom(p) || isInsideMagnetEffectTop(p)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * Attache le block en parametre a ce block si le point p se situe dans les zones magnetiques
	 * @param block
	 * @param p
	 * @return vrai si l'operation a reussie, faux sinon
	 */
	public boolean attach(AEditorBlock block, Point p, NPlan plan) {
		if(isInsideMagnetEffectBottom(p) && canHaveChild()) {
			if(!hasChild()) { // si pas de fils, on append direct
				append(block, plan);
			} else { // sinon on recupere le fils actuel, on append le nouveau block puis on append l'ancien fils au block qu'on a ajouté
				AEditorBlock currentChild = getChild();
				dissociateFromChild(currentChild);
				append(block, plan);
				block.getLastChild().append(currentChild, null); // plan a null puisque le plan estdeja modifie dans append du nouveau block
			}
			return true;
		} else if(isInsideMagnetEffectTop(p)) {
			if(!hasParent()) {
				prepend(block, plan);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Attahce le block en parametre a ce block selon le mode passé en parametre
	 * @param block a attacher
	 * @param mode, ProtoBlock.PREPEND, ProtoBlock.APPEND, ProtoBlockLogique.IF ou ProtoBlockLogique.ELSE
	 * @return vrai si opération reussie, faux sinon
	 */
	public boolean attach(AEditorBlock block, int mode, NPlan plan) {
		switch(mode) {
			case PREPEND:
				if(!hasParent()) {
					prepend(block, plan);
					return true;
				}
				break;
			case APPEND:
				if(canHaveChild()) {
					if(!hasChild()) {
						append(block, plan);
					} else {
						AEditorBlock currentChild = getChild();
						dissociateFromChild(null);
						append(block, plan);
						block.append(currentChild, null);
					}
					return true;
				}
				break;
			default:
				break;
		}
		return false;
	}
	
	/**
	 * Colorie la bordure bottom du block selon le mode passé en param
	 * @param mode OK_MODE ou FORBIDDEN_MODE
	 */
	public void setBottomBorder(int mode) {
		if(mode == OK_MODE) {
			this.setBorder(OK_BORDER_BOTTOM);
		} else if(mode == FORBIDDEN_MODE) {
			this.setBorder(FORBIDDEN_BORDER_BOTTOM);
		}
	}
	
	/**
	 * Colorie la bordure top du block selon le mode passé en param
	 * @param mode OK_MODE ou FORBIDDEN_MODE
	 */
	public void setTopBorder(int mode) {
		if(mode == OK_MODE) {
			this.setBorder(OK_BORDER_TOP);
		} else if(mode == FORBIDDEN_MODE) {
			this.setBorder(FORBIDDEN_BORDER_TOP);
		}
	}
	
	/**
	 * Enleve les bordures ajoutées par les fonctions setTopBorder et setBottomBorder
	 */
	public void resetBorder() {
		this.setBorder(PADDING);
	}
	
	/**
	 * Change la bordure du block selon la position du point p en parametre, si il est dans les zones magnetiques du block, la bordure changera de couleur
	 * ou se reinitialisera si le point p n'est pas dans les zones magnetiques du block
	 * @param p
	 */
	public void changeBorder(Point p) {
		resetBorder();
		if(isInsideMagnetEffectBottom(p)) {
			if(canHaveChild()) {
				setBottomBorder(AEditorBlock.OK_MODE);
			}
		} else if(isInsideMagnetEffectTop(p)) {
			if(!hasParent() && !hasParent()) {
				setTopBorder(AEditorBlock.OK_MODE);
			}
		}
	}
	
	/**
	 * Met a jour le layer du block dans le panel editor ainsi que les fils du block
	 * @param panel, le panel editor
	 */
	public void updatePanelLayer(AEditorPanelEditor panel) {
		panel.setLayer(this, this.getLevel());
		if(hasChild())
			this.fils.updatePanelLayer(panel);
	}
	
	@Override
	public boolean isInsideMagnetEffectTop(Point loc) {
		Point p = loc;
		if(p.x > getX() && p.x < (getX() + getWidth())) {
			if(p.y < getY() && p.y > (getY() - MAGNET_EFFECT)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean isInsideMagnetEffectBottom(Point loc) {
		Point p = loc;
		if(p.x > getX() && p.x < (getX() + getWidth())) {
			if(p.y > (getY() + getHeight()) && p.y < (getY() + getHeight() + MAGNET_EFFECT)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean contains(Point p) {
		return getX()< p.x && getY() < p.y && (getX() + getWidth()) > p.x && (getY() + getHeight()) > p.y;
	}
	
	@Override
	public String toString() {
		String str = "Nom : " + this.name;
		str += "\nLevel : " + getLevel();
		if(this.parent != null) {
			str += "\nParent : " + this.parent.name;
		}
		if(this.fils != null) {
			str += "\nFils :" + this.fils.name;
		}
		return str;
	}
	
	/**
	 * Menu Popup d'un block, contient par defaut l'option editer
	 * @author Arnau
	 *
	 */
	public class ActionBlockPopupMenu extends JPopupMenu{
		private JMenuItem edit;
		//private JMenuItem displayInfo; // pour debug
		private AEditorBlock block;
		
		public ActionBlockPopupMenu(AEditorBlock bl) {
			this.block = bl;
			edit = new JMenuItem("Editer");
			edit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						DialogueEditerAction d = new DialogueEditerAction((Frame) getTopLevelAncestor() , true, block.getAction());
						d.setVisible(true);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			add(edit);
			/*
			displayInfo = new JMenuItem("Infos Debug");
			displayInfo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println(block);
				}
			});
			add(displayInfo);
			*/
		}
		
		public AEditorBlock getBlock() {
			return block;
		}
	}
	
}
