package civilisation.amenagement;

import civilisation.individu.Human;
import turtlekit.kernel.Patch;

public class Mark {

	Patch patch;
	Human owner;
	
	
	
	
	
	public Mark(Patch patch, Human owner) {
		super();
		this.patch = patch;
		this.owner = owner;
	}
	
	
	
	public Patch getPatch() {
		return patch;
	}
	public void setPatch(Patch patch) {
		this.patch = patch;
	}
	public Human getOwner() {
		return owner;
	}
	public void setOwner(Human owner) {
		this.owner = owner;
	}
	
}
