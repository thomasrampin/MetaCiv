package civilisation.individu.plan.action;

import civilisation.SchemaCognitif;

public class LAction extends Action{

	public LAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LAction(SchemaCognitif inSc) {
		super(inSc);
		// TODO Auto-generated constructor stub
	}
	/**
	 * Logical actions are designed to manage loop, test, etc...
	 */
	
	
	public boolean isLogical() {
		return true;
	}
	
	public boolean internActionsAreLinked() {
		return false;
	}
}
