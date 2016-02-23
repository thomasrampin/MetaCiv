package civilisation.individu.cognitons;

import java.io.Serializable;

import civilisation.individu.plan.NPlan;

public class LienPlan implements Cloneable, Serializable{

	NPlan p;
	int poids;
	
	public LienPlan(NPlan p , int poids){
		this.poids = poids;
		this.p = p;
	}
	
	@Override
	public String toString(){
		return "Lien : " + p.getNom()+ " " + poids;
	}

	public NPlan getP() {
		return p;
	}

	public void setP(NPlan p) {
		this.p = p;
	}

	public int getPoids() {
		return poids;
	}

	public void setPoids(int poids) {
		this.poids = poids;
	}
	
	public LienPlan clone() throws CloneNotSupportedException {
	    LienPlan lplan = null;

	    lplan = (LienPlan) super.clone();
	    
	    lplan.setP(p.clone());
	    // on renvoie le clone
	    return lplan;
	}
	
	
}
