package civilisation.inspecteur.simulation.editeurPlan;

import java.awt.Color;
import java.awt.Point;

import civilisation.individu.plan.action.Action;

@SuppressWarnings("serial")
public class AEditorBlockRoot extends AEditorBlock {

	public AEditorBlockRoot(String name) {
		super(name);
		init();
	}
	
	public AEditorBlockRoot(Action action) {
		super(action);
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
		return false; // can't insert above root block
	}
	
	@Override
	public int getLevel() {
		return 0;
	}
	
}
