package civilisation.inspecteur.simulation.editeurPlan;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import civilisation.individu.plan.NPlan;
import civilisation.individu.plan.action.Action;

@SuppressWarnings("serial")
public class AEditorBlockLogique extends AEditorBlock{
	
	public final static int IF = 2;
	public final static int ELSE = 3;
	
	private Rectangle2D ifBlock, elseBlock;
	private final static int INDENT_RATIO_INSIDE = 4;
	private AEditorBlock filsIf, filsElse;
	private Color ifColor, elseColor;
	private final static float DEFAULT_INNER_SIZE = 40; // taille des rectangles If et Else quand le block a pas de fils
	private final static float DEFAULT_SEPARATOR_SIZE = 20; // taille des séparateurs, comme celui entre le block If et Else
	private Dimension prefSize;

	public AEditorBlockLogique(String name) {
		super(name);
		init();
	}
	
	public AEditorBlockLogique(Action action) {
		super(action);
		init();
	}
	
	private void init() {
		filsIf = null;
		filsElse = null;
		ifColor = AEditorPanelEditor.EDITOR_COLOR;
		elseColor = AEditorPanelEditor.EDITOR_COLOR;
		prefSize = getPreferredSize();
		ifBlock = new Rectangle2D.Float((float) (getX() + prefSize.getWidth() / INDENT_RATIO_INSIDE), (float) (getY() + content.getPreferredSize().getHeight() + DEFAULT_SEPARATOR_SIZE), (float) ((getX() + prefSize.getWidth() / INDENT_RATIO_INSIDE + 5) * (INDENT_RATIO_INSIDE - 1)), DEFAULT_INNER_SIZE);
		elseBlock = new Rectangle2D.Float((float) (getX() + prefSize.getWidth() / INDENT_RATIO_INSIDE), (float) (ifBlock.getY() + ifBlock.getHeight() + DEFAULT_SEPARATOR_SIZE), (float) ((getX() + prefSize.getWidth() / INDENT_RATIO_INSIDE + 5) * (INDENT_RATIO_INSIDE - 1)), DEFAULT_INNER_SIZE);
		setPreferredSize(new Dimension(prefSize.width, (int) (prefSize.height + ifBlock.getHeight() + elseBlock.getHeight() + (DEFAULT_SEPARATOR_SIZE * 2))));
		setBackground(AEditorColors.ORANGE.color);
		content.setBackground(AEditorColors.ORANGE.color);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		updateBlockSize();
		g.setColor(ifColor);
		g.fillRect((int) ifBlock.getX(),(int) ifBlock.getY(),(int) ifBlock.getWidth(),(int) ifBlock.getHeight()); // affiche le rectangle utilisé pour faire le trou If du block
		g.setColor(elseColor);
		g.fillRect((int) elseBlock.getX(),(int) elseBlock.getY(),(int) elseBlock.getWidth(),(int) elseBlock.getHeight()); // affiche le rectangle utilisé pour faire le trou Else du block
	}
	
	/**
	 * Verifie que le point en parametre se situe dans l'espace magnetique If du block logique
	 * @param p
	 * @return vrai si le point est dans l'effect magnetique du If, faux sinon
	 */
	public boolean isInsideMagnetEffectIf(Point p) {
		Point pBlock = new Point(p.x - getX(), p.y - getY());
		return ifBlock.contains(pBlock);
	}
	
	/**
	 * Verifie que le point en parametre se situe dans l'espace magnetique Else du block logique
	 * @param p
	 * @return vrai si le point est dans l'effect magnetique du Else, faux sinon
	 */
	public boolean isInsideMagnetEffectElse(Point p) {
		Point pBlock = new Point(p.x - getX(), p.y - getY());
		return elseBlock.contains(pBlock);
	}
	
	@Override
	public boolean contains(Point p) {
		if(isInsideMagnetEffectIf(p) || isInsideMagnetEffectElse(p)) {
			return false;
		}
		return getX()< p.x && getY() < p.y && (getX() + getWidth()) > p.x && (getY() + getHeight()) > p.y;
	}
	
	/**
	 * Ajoute le block en paremetre à ce block comme filsIf
	 * @param bl
	 */
	public void addIfBlock(AEditorBlock bl, NPlan plan) {
		if(!bl.hasChild()) { // le block if ne peut contenir que un seul block normal ou un seul block logique et ses sous blocks
			associateIf(bl);
			bl.setLocationWithChildren((int) (this.getX() + ifBlock.getX() + 5),(int) (this.getY() + ifBlock.getY() + SPACE_BETWEEN_BLOCK + 2)); 
			bl.setCanHaveChild(false);
			/**
			 * Modificitations du plan des actions
			 */
			if(plan != null && action != null) { // si ce block contient une action et que l'on ajoute au plan
				if(filsElse != null) { // si le block contient un fils dans la partie Else
					action.addActionBefore(bl.getAction(), action.getListeActions().get(0));
					//bl.addChildrenActions(action);
				} else {
					action.addSousAction(bl.getAction());
					//bl.addChildrenActions(action);
				}
			}
		}
	}
	
	/**
	 * Ajoute le block en paremetre à ce block comme filsElse
	 * @param bl
	 */
	public void addElseBlock(AEditorBlock bl, NPlan plan) {
		if(!bl.hasChild()) {
			associateElse(bl);
			bl.setLocationWithChildren((int) (this.getX() + elseBlock.getX() + 5),(int) (this.getY() + elseBlock.getY() + SPACE_BETWEEN_BLOCK + 2)); 
			bl.setCanHaveChild(false);
			/**
			 * Modificitations du plan des actions
			 */
			if(plan != null && action != null) { // si ce block contient une action et que l'on ajoute au plan
				action.addSousAction(bl.getAction());
				bl.addChildrenActions(action);
			}
		}
	}
	
	/**
	 * Mets a jour la taille du block suivant le nombre de fils 
	 */
	private void updateBlockSize() {
		int nbif = 0, nbelse = 0;
		int spaceBetweenBlockPlusBorder = SPACE_BETWEEN_BLOCK + 2; // la bordure verte ajouté sous les blocks pour dire indiquer apres quels blocks insérés fait que les blocks Actions finnissent par depasser du cadre du block if du block logique au bout d'un certain nombre ajoutés.
		if(filsIf != null) {
			AEditorBlock child = filsIf;
			double nHeight = spaceBetweenBlockPlusBorder; // space before block
			++nbif;
			while(child != null) {
				nHeight += child.getPreferredSize().getHeight() + spaceBetweenBlockPlusBorder; // height of block + space after it
				++nbif;
				child = child.getChild();
			}
			ifBlock.setRect(ifBlock.getX(), ifBlock.getY(), ifBlock.getWidth(), nHeight);
		} else {
			ifBlock.setRect(ifBlock.getX(), ifBlock.getY(), ifBlock.getWidth(), DEFAULT_INNER_SIZE);
		}
		if(filsElse != null) {
			AEditorBlock child = filsElse;
			double nHeight = spaceBetweenBlockPlusBorder;
			++nbelse;
			while(child != null) {
				nHeight += child.getPreferredSize().getHeight() + spaceBetweenBlockPlusBorder;
				++nbelse;
				child = child.getChild();
			}
			elseBlock.setRect(elseBlock.getX(), (float) (ifBlock.getY() + ifBlock.getHeight() + (nbif * spaceBetweenBlockPlusBorder) + DEFAULT_SEPARATOR_SIZE), elseBlock.getWidth(), nHeight);
		} else {
			elseBlock.setRect(elseBlock.getX(), (float) (ifBlock.getY() + ifBlock.getHeight() + (nbif * spaceBetweenBlockPlusBorder) + DEFAULT_SEPARATOR_SIZE), elseBlock.getWidth(), DEFAULT_INNER_SIZE);
		}
		setSize(new Dimension(prefSize.width, (int) (prefSize.height + ifBlock.getHeight() + elseBlock.getHeight() + (nbif * spaceBetweenBlockPlusBorder) + (nbelse * spaceBetweenBlockPlusBorder) + (DEFAULT_SEPARATOR_SIZE * 2))));
		setPreferredSize(new Dimension(prefSize.width, (int) (prefSize.height + ifBlock.getHeight() + elseBlock.getHeight() + (nbif * spaceBetweenBlockPlusBorder) + (nbelse * spaceBetweenBlockPlusBorder) + (DEFAULT_SEPARATOR_SIZE * 2))));
		updateChildrenLocation();
		if(parent != null)
			parent.repaint();
	}
	
	/**
	 * Mets a jour la position des fils du blocks, Le block est enlévé puis rajouté comme ça on na pas besoin de recalculer la position à la main
	 */
	private void updateChildrenLocation() {
		if(filsIf != null) {
			AEditorBlock fi = filsIf;
			filsIf.dissociateFromParent(filsIf);
			attach(fi, IF, null);
		}
		if(filsElse != null) {
			AEditorBlock fe = filsElse;
			filsElse.dissociateFromParent(filsElse);
			attach(fe, ELSE, null);
		}
		if(fils != null) {
			AEditorBlock ch = fils;
			fils.dissociateFromParent(fils);
			attach(ch, APPEND, null);
		}
	}

	@Override
	public Action getParentAction(Action thisAction) {
		if((filsIf != null && filsIf.getAction().equals(thisAction)) || (filsElse != null && filsElse.getAction().equals(thisAction))) {
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
		if(isInsideMagnetEffectIf(p)) {
			if(filsIf == null) {
				addIfBlock(block, plan);
			}
			return true;
		} else if(isInsideMagnetEffectElse(p)) {
			if(filsElse == null) {
				addElseBlock(block, plan);
			}
			return true;
		}
		return super.attach(block, p, plan);
	}
	

	@Override
	public boolean attach(AEditorBlock block, int mode, NPlan plan) {
		switch(mode) {
			case IF:
				if(filsIf == null) {
					addIfBlock(block, plan);
					return true;
				}
				break;
			case ELSE:
				if(filsElse == null) {
					addElseBlock(block, plan);
				}
				return true;
			default:
				break;
		}
		return super.attach(block, mode, plan);
	}
	
	/**
	 * Associe ce block logique au block en parametre, le block en parametre deviendra le block fils de la partie If du block logique
	 * et le block logique deviendra le parent du block en parametre.
	 * @param ifBloc, le block qui doit etre considerer comme le premier block de la partie If du block logique
	 */
	public void associateIf(AEditorBlock ifBlock) {
		filsIf = ifBlock;
		ifBlock.setParentBlock(this);
	}
	
	/**
	 * Associe ce block logique au block en parametre, le block en parametre deviendra le block fils de la partie Else du block logique
	 * et le block logique deviendra le parent du block en parametre.
	 * @param ifBloc, le block qui doit etre considerer comme le premier block de la partie Else du block logique
	 */
	public void associateElse(AEditorBlock elseBlock) {
		filsElse = elseBlock;
		elseBlock.setParentBlock(this);
	}
	
	/**
	 * Dessassocie le block logique de son block fils de la partie If
	 */
	public void dissociateIf() {
		if(filsIf != null) {
			filsIf.setParentBlock(null);
			filsIf = null;
		}
	}
	
	/**
	 * Dessassocie le block logique de son block fils de la partie Else
	 */
	public void dissociateElse() {
		if(filsElse != null) {
			filsElse.setParentBlock(null);
			filsElse = null;
		}
	}
	
	@Override
	protected void dissociateFromChild(AEditorBlock child) {
		if(filsIf != null && child.equals(filsIf)) {
			/*filsIf.setParentBlock(null);
			filsIf = null;*/
			dissociateIf();
		} else if(filsElse != null && child.equals(filsElse)) {
			/*filsElse.setParentBlock(null);
			filsElse = null;*/
			dissociateElse();
		} else {
			super.dissociateFromChild(child);
		}
	}
	
	@Override
	protected void setLocationDiffToChildren(int diffX, int diffY) {
		if(filsIf != null) {
			filsIf.setLocation(filsIf.getX() - diffX, filsIf.getY() - diffY);
			filsIf.setLocationDiffToChildren(diffX, diffY);
		}
		if(filsElse != null) {
			filsElse.setLocation(filsElse.getX() - diffX, filsElse.getY() - diffY);
			filsElse.setLocationDiffToChildren(diffX, diffY);
		}
		super.setLocationDiffToChildren(diffX, diffY);
	}

	@Override
	public void changeBorder(Point p) {
		resetBorder();
		if(filsIf == null && isInsideMagnetEffectIf(p)) {
			ifColor = Color.GREEN;
			repaint(); // sinon le block ne s'affiche pas correctement
		} else if(filsElse == null && isInsideMagnetEffectElse(p)){
			elseColor = Color.GREEN;
			repaint();// sinon le block ne s'affiche pas correctement
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
		ifColor = AEditorPanelEditor.EDITOR_COLOR;
		elseColor = AEditorPanelEditor.EDITOR_COLOR;
	}
	
	@Override
	public void updatePanelLayer(AEditorPanelEditor panel) {
		if(filsIf != null) {
			filsIf.updatePanelLayer(panel);
		}
		if(filsElse != null) {
			filsElse.updatePanelLayer(panel);
		}
		super.updatePanelLayer(panel);
	}
	
	@Override
	public String toString() {
		String str =  super.toString();
		if(filsIf != null) {
			str += "\nPremier fils block if : " + filsIf.name;
		}
		if(filsElse != null) {
			str += "\nPremier fils block else : " + filsElse.name;
		}
		return str;
	}
}
