package civilisation.individu.cognitons;

import java.io.Serializable;

public class LienCogniton implements Cloneable, Serializable {

	TypeCogniton c;
	int poids;
	
	public LienCogniton(TypeCogniton c , int poids){
		this.poids = poids;
		this.c = c;
	}
	
	@Override
	public String toString(){
		return "Lien : " + c.getNom() + " " + poids;
	}

	public TypeCogniton getC() {
		return c;
	}

	public void setC(TypeCogniton c) {
		this.c = c;
	}

	public int getPoids() {
		return poids;
	}

	public void setPoids(int poids) {
		this.poids = poids;
	}
	
	public LienCogniton clone() throws CloneNotSupportedException {
	    LienCogniton lcog = null;

	    lcog = (LienCogniton) super.clone();
	    
	    lcog.c = c.clone();
	    
	    // on renvoie le clone
	    return lcog;
	}
	
}
