package civilisation.inspecteur.simulation.editeurPlan;

import java.awt.Color;
import java.awt.Point;

import civilisation.individu.plan.NPlan;
import civilisation.individu.plan.action.Action;

@SuppressWarnings("serial")
/**
 * Block root Ce block marque le depart d'un plan, les blocks insérés aprés
 * seront ajoutés au plan
 * 
 * @author Arnau
 *
 */
public class AEditorBlockRoot extends AEditorBlock {

	public AEditorBlockRoot(String name) {
		super(name);
		init();
	}

	private void init() {
		INDENT_RATIO = 4;
		setBackground(AEditorColors.GRAY.color);
		content.setBackground(AEditorColors.GRAY.color);
		lname.setForeground(Color.WHITE);
	}

	@Override
	public boolean isInsideMagnetEffectTop(Point loc) {
		return false; // on ne peut pas inserer avant le block root
	}

	@Override
	public void append(AEditorBlock bl, NPlan plan) {
		bl.associate(this);
		if (getIndentRatio() == 0) {
			bl.setLocationWithChildren(getX(), getY() + getHeight() + SPACE_BETWEEN_BLOCK);
		} else {
			bl.setLocationWithChildren(getX() + getWidth() / getIndentRatio(),
					getY() + getHeight() + SPACE_BETWEEN_BLOCK);
		}
		/**
		 * Modificitations du plan des actions
		 */
		if (plan != null) { // si on modif le plan
			plan.addFirstAction(bl.getAction()); // add action au debut du plan
			bl.addChildrenActionsPlan(plan);
		}
	}

	@Override
	public int getLevel() {
		return 0;
	}

}
