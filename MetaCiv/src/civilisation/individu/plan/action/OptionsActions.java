package civilisation.individu.plan.action;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import civilisation.ItemPheromone;
import civilisation.amenagement.Amenagement;
import civilisation.amenagement.TypeAmenagement;
import civilisation.group.GroupAndRole;
import civilisation.group.GroupModel;
import civilisation.individu.cognitons.TypeCogniton;
import civilisation.inventaire.Objet;
import civilisation.world.Terrain;


public class OptionsActions implements Cloneable, Serializable{

	String name;
	ArrayList<Object> parametres = new ArrayList<Object>();
    HashMap<Object,String> specialInfo = new HashMap<Object,String>();
	
    public OptionsActions(String name){
    	this.name = name;
    }
    
    public OptionsActions clone() throws CloneNotSupportedException {
    	OptionsActions act = null;

    	// On r�cup�re l'instance � renvoyer par l'appel de la 
      	// m�thode super.clone()
      	act = (OptionsActions) super.clone();
	    
	    // on renvoie le clone
	    return act;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
	@Override
	public String toString(){
		return name + " : " + parametres.size();
	}
    
	public void addParametre(Object o){
		parametres.add(o);
	}
	
	public void addParametre(Object o, String info){
		addParametre(o);
		specialInfo.put(o, info);
	}

	public ArrayList<Object> getParametres() {
		return parametres;
	}
	
	/**
	 * 
	 * @return String au format des fichiers textes .metaciv
	 */
	public String toFormatedString(){
		final String separator = " ";
		if (parametres.size() == 0){
			return name;
		}
		else{
			String s = "(";
			for (int i = 0; i < parametres.size(); i++){
				if (parametres.get(i).getClass().equals(Objet.class)){
					s += "Objet "; /*Note the space before the name*/
					s += ((Objet) parametres.get(i)).getNom();
					if (i < parametres.size() - 1){
						s += separator; /*Separate multiple parameters*/
					}
				}
				else if (parametres.get(i).getClass().equals(TypeAmenagement.class)){
					s += "Amenagement "; /*Note the space before the name*/
					s += ((TypeAmenagement) parametres.get(i)).getNom();
					if (i < parametres.size() - 1){
						s += separator; /*Separate multiple parameters*/
					}
				}
				else if (parametres.get(i).getClass().equals(GroupModel.class)){
					s += "Group "; /*Note the space before the name*/
					s += ((GroupModel) parametres.get(i)).getName();
					if (i < parametres.size() - 1){
						s += separator; /*Separate multiple parameters*/
					}
				}
				else if (parametres.get(i).getClass().equals(GroupAndRole.class)){
					s += "GroupAndRole "; /*Note the space before the name*/
					s += ((GroupAndRole) parametres.get(i)).getGroupModel().getName() +":"+((GroupAndRole) parametres.get(i)).getRole();
					if (i < parametres.size() - 1){
						s += separator; /*Separate multiple parameters*/
					}
				}
				else if (parametres.get(i).getClass().equals(Integer.class)){
					s += "Integer ";
					s += parametres.get(i).toString();
					if (i < parametres.size() - 1){
						s += separator;
					}
				}
				else if (parametres.get(i).getClass().equals(Double.class)){
					s += "Double ";
					s += parametres.get(i).toString();
					if (i < parametres.size() - 1){
						s += separator;
					}
				}
				else if (parametres.get(i).getClass().equals(String.class) && this.specialInfo.get(parametres.get(i)) != null && this.specialInfo.get(parametres.get(i)).equals("String")){
					s += "String ";
					s += parametres.get(i);
					if (i < parametres.size() - 1){
						s += separator;
					}
				}
				else if (parametres.get(i).getClass().equals(String.class)&& this.specialInfo.get(parametres.get(i)) != null && this.specialInfo.get(parametres.get(i)).equals("Constant")){
					s += "Constant ";
					s += parametres.get(i);
					if (i < parametres.size() - 1){
						s += separator;
					}
				}
				else if (parametres.get(i).getClass().equals(String.class)){
					s += "Attribute ";
					s += parametres.get(i);
					if (i < parametres.size() - 1){
						s += separator;
					}
				}

				else if (parametres.get(i).getClass().equals(ItemPheromone.class)){
					s += "Pheromone ";
					s += ((ItemPheromone) parametres.get(i)).getNom();
					if (i < parametres.size() - 1){
						s += separator;
					}
				}
				else if (parametres.get(i).getClass().equals(TypeCogniton.class)){
					s += "Cogniton ";
					s += ((TypeCogniton) parametres.get(i)).getNom();
					if (i < parametres.size() - 1){
						s += separator;
					}
				}
				else if (parametres.get(i).getClass().equals(Comparator.class)){
					s += "Comparator ";
					s += ((Comparator) parametres.get(i)).toSymbol();
					if (i < parametres.size() - 1){
						s += separator;
					}
				}
				else if (parametres.get(i).getClass().equals(Amenagement.class)){
					s += "Amenagement ";
					s += ((TypeAmenagement) parametres.get(i)).getNom();
					if (i < parametres.size() - 1){
						s += separator;
					}
				}
				else if (parametres.get(i).getClass().equals(Terrain.class)){
					s += "Terrain ";
					s += ((Terrain) parametres.get(i)).getNom();
					if (i < parametres.size() - 1){
						s += separator;
					}
				}
			}
			s+=")";
			return name + s;
		}
	}
	public boolean testStringSubType(String in, int pos)
	{
		if(this.specialInfo != null && this.specialInfo.size() >0)
		{
			return 	this.specialInfo.get(parametres.get(pos)).equals(in);			
		}
		else
			return false;
		
	}
}
