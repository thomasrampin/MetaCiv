package civilisation.inspecteur.simulation.editeurPlan;

import civilisation.individu.plan.action.Action;

@SuppressWarnings("serial")
/**
 * Block action simple
 * @author Arnau
 *
 */
public class AEditorBlockAction extends AEditorBlock {

	public AEditorBlockAction(String name) {
		super(name);
	}
	
	public AEditorBlockAction(Action action) {
		super(action);
	}
}
