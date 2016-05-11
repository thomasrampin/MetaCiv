package civilisation.amenagement;

import java.util.Random;

import civilisation.individu.Human;
import turtlekit.kernel.Patch;

public class Amenagement_Efficacite extends Amenagement {

	private int efficacite;
	private int compteur;
	
	
	public Amenagement_Efficacite(Patch p, Human h, String string) {
		super(p, h, string);
		efficacite = new Random().nextInt(100);
		compteur = efficacite;
	}

	public Amenagement_Efficacite(Patch p, Human h, TypeAmenagement type) {
		super(p, h, type);
		efficacite = new Random().nextInt(100);
		compteur = efficacite;
	}

	public int getEfficacite() {
		return efficacite;
	}

	public void setEfficacite(int efficacite) {
		this.efficacite = efficacite;
	}

	public int getCompteur() {
		return compteur;
	}

	public void setCompteur(int compteur) {
		this.compteur = compteur;
	}
	
	public void decrementer(){
		compteur --;
	}
	
	public void decrementer(int n){
		compteur = compteur - n;
	}
	
	public void resetCompteur(){
		compteur = efficacite;
	}

}
