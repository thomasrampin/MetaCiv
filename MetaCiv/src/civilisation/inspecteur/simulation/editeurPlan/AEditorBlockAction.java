package civilisation.inspecteur.simulation.editeurPlan;

import civilisation.individu.plan.action.Action;

@SuppressWarnings("serial")
public class AEditorBlockAction extends AEditorBlock {

	public AEditorBlockAction(String name) {
		super(name);
		init();
	}
	
	public AEditorBlockAction(Action action) {
		super(action);
		init();
	}
	
	private void init() {
		//this.addParam(name);
	}
}
