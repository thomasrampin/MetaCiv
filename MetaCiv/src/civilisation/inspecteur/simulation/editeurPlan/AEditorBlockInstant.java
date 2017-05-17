package civilisation.inspecteur.simulation.editeurPlan;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import civilisation.individu.plan.NPlan;
import civilisation.individu.plan.action.Action;

@SuppressWarnings("serial")
public class AEditorBlockInstant extends AEditorBlock{
	
	public final static int INSTANT = 3;
	
	private Rectangle2D instantBlock;
	private final static int INDENT_RATIO_INSIDE = 4;
	private AEditorBlock filsInstant;
	private Color instantColor;
	private final static float DEFAULT_INNER_SIZE = 40;
	private final static float DEFAULT_SEPARATOR_SIZE = 20;
	private Dimension prefSize;

	public AEditorBlockInstant(String name) {
		super(name);
		init();
	}
	
	public AEditorBlockInstant(Action action) {
		super(action);
		init();
	}
	
	private void init() {
		filsInstant = null;
		instantColor = AEditorPanelEditor.EDITOR_COLOR;
		prefSize = getPreferredSize();
		instantBlock = new Rectangle2D.Float((float) (getX() + prefSize.getWidth() / INDENT_RATIO_INSIDE), (float) (getY() + content.getPreferredSize().getHeight() + DEFAULT_SEPARATOR_SIZE), (float) ((getX() + prefSize.getWidth() / INDENT_RATIO_INSIDE + 5) * (INDENT_RATIO_INSIDE - 1)), DEFAULT_INNER_SIZE);
		setPreferredSize(new Dimension(prefSize.width, (int) (prefSize.height + instantBlock.getHeight() + DEFAULT_SEPARATOR_SIZE )));
		setBackground(AEditorColors.GREEN.color);
		content.setBackground(AEditorColors.GREEN.color);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		updateBlockSize();
		g.setColor(instantColor);
		g.fillRect((int) instantBlock.getX(),(int) instantBlock.getY(),(int) instantBlock.getWidth(),(int) instantBlock.getHeight());
	}
	
	
	/**
	 * Verifie que le point en parametre se situe dans l'espace magnetique If du block logique
	 * @param p
	 * @return vrai si le point est dans l'effect magnetique du If, faux sinon
	 */
	public boolean isInsideMagnetEffectInstant(Point p) {
		Point pBlock = new Point(p.x - getX(), p.y - getY());
		return instantBlock.contains(pBlock);
	}
	
	@Override
	public boolean contains(Point p) {
		if(isInsideMagnetEffectInstant(p)) {
			return false;
		}
		return getX()< p.x && getY() < p.y && (getX() + getWidth()) > p.x && (getY() + getHeight()) > p.y;
	}
	
	public void addInstantBlock(AEditorBlock bl, NPlan plan) {
		associateInstant(bl);
		bl.setLocationWithChildren((int) (this.getX() + instantBlock.getX() + 5),(int) (this.getY() + instantBlock.getY() + SPACE_BETWEEN_BLOCK + 2)); 
		/**
		 * Modificitations du plan des actions
		 */
		if(plan != null && action != null) { // si ce block contient une action et que l'on ajoute au plan
			action.addSousAction(bl.getAction());
			bl.addChildrenActions(action);
		}
	}
	
	private void updateBlockSize() {
		int nbif = 0, nbelse = 0;
		int spaceBetweenBlockPlusBorder = SPACE_BETWEEN_BLOCK + 2; // la bordure verte ajouté sous les blocks pour dire indiquer apres quels blocks insérés fait que les blocks Actions finnissent par depasser du cadre du block if du block logique au bout d'un certain nombre ajoutés.
		if(filsInstant != null) {
			AEditorBlock child = filsInstant;
			double nHeight = spaceBetweenBlockPlusBorder; // space before block
			++nbif;
			while(child != null) {
				nHeight += child.getPreferredSize().getHeight() + spaceBetweenBlockPlusBorder; // height of block + space after it
				++nbif;
				child = child.getChild();
			}
			instantBlock.setRect(instantBlock.getX(), instantBlock.getY(), instantBlock.getWidth(), nHeight);
		} else {
			instantBlock.setRect(instantBlock.getX(), instantBlock.getY(), instantBlock.getWidth(), DEFAULT_INNER_SIZE);
		}
		setSize(new Dimension(prefSize.width, (int) (prefSize.height + instantBlock.getHeight() + (nbif * spaceBetweenBlockPlusBorder) + (nbelse * spaceBetweenBlockPlusBorder) + DEFAULT_SEPARATOR_SIZE)));
		setPreferredSize(new Dimension(prefSize.width, (int) (prefSize.height + instantBlock.getHeight() + (nbif * spaceBetweenBlockPlusBorder) + (nbelse * spaceBetweenBlockPlusBorder) + DEFAULT_SEPARATOR_SIZE)));
		updateChildrenLocation();
		if(parent != null)
			parent.repaint();
	}
	
	private void updateChildrenLocation() {
		if(filsInstant != null) {
			AEditorBlock fi = filsInstant;
			filsInstant.dissociateFromParent(filsInstant);
			attach(fi, INSTANT, null);
		}
		if(fils != null) {
			AEditorBlock ch = fils;
			fils.dissociateFromParent(fils);
			attach(ch, APPEND, null);
		}
	}
	
	@Override
	public Action getParentAction(Action thisAction) {
		if(filsInstant != null && filsInstant.getAction().equals(thisAction)) {
			return this.action;
		} else {
			return parent.getParentAction(this.action);
		}
	}

	@Override
	protected void addChildrenActionsPlan(NPlan plan) {
		//addChildrenActions(action);
		super.addChildrenActionsPlan(plan);
	}
	
	@Override
	public boolean attach(AEditorBlock block, Point p, NPlan plan) {
		if(isInsideMagnetEffectInstant(p)) {
			if(filsInstant == null) {
				addInstantBlock(block, plan);
			}
			return true;
		}
		return super.attach(block, p, plan);
	}
	

	@Override
	public boolean attach(AEditorBlock block, int mode, NPlan plan) {
		switch(mode) {
			case INSTANT:
				if(filsInstant == null) {
					addInstantBlock(block, plan);
					return true;
				}
				break;
			default:
				break;
		}
		return super.attach(block, mode, plan);
	}
	
	/**
	 * Associe ce block logique au block en parametre, le block en parametre deviendra le block fils de la partie Instant du block logique
	 * et le block logique deviendra le parent du block en parametre.
	 * @param instantBloc, le block qui doit etre considerer comme le premier block de la partie Instant du block logique
	 */
	public void associateInstant(AEditorBlock instantBlock) {
		filsInstant = instantBlock;
		instantBlock.setParentBlock(this);
	}
	
	/**
	 * Dessassocie le block logique de son block fils de la partie Instant
	 */
	public void dissociateInstant() {
		if(filsInstant != null) {
			filsInstant.setParentBlock(null);
			filsInstant = null;
		}
	}
	
	@Override
	protected void dissociateFromChild(AEditorBlock child) {
		if(filsInstant != null && child.equals(filsInstant)) {
			/*filsInstant.setParentBlock(null);
			filsInstant = null;*/
			dissociateInstant();
		} else {
			super.dissociateFromChild(child);
		}
	}
	
	@Override
	protected void setLocationDiffToChildren(int diffX, int diffY) {
		if(filsInstant != null) {
			filsInstant.setLocation(filsInstant.getX() - diffX, filsInstant.getY() - diffY);
			filsInstant.setLocationDiffToChildren(diffX, diffY);
		}
		super.setLocationDiffToChildren(diffX, diffY);
	}

	@Override
	public void changeBorder(Point p) {
		resetBorder();
		if(filsInstant == null && isInsideMagnetEffectInstant(p)) {
			instantColor = Color.GREEN;
			repaint(); // sinon le block ne s'affiche pas correctement
		} else
			super.changeBorder(p);
	}
	
	@Override
	public void resetBorder() {
		resetIfElseColor();
		super.resetBorder();
		repaint();// sinon le block ne s'affiche pas correctement
	}
	
	public void resetIfElseColor() {
		instantColor = AEditorPanelEditor.EDITOR_COLOR;
	}
	
	@Override
	public void updatePanelLayer(AEditorPanelEditor panel) {
		if(filsInstant != null) // on met a jour le layer du fils dans le blck
			filsInstant.updatePanelLayer(panel);
		super.updatePanelLayer(panel);
	}
	
	@Override
	public String toString() {
		String str =  super.toString();
		if(filsInstant != null) {
			str += "\nPremier fils block instant : " + filsInstant.name;
		}
		return str;
	}
}
 
