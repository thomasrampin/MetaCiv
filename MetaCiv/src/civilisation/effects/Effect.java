package civilisation.effects;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import civilisation.Configuration;
import civilisation.amenagement.Amenagement;
import civilisation.individu.Esprit;
import civilisation.individu.Human;

public class Effect {

	private String target; // la cible est un attribut ou un cogniton ?
	private String Varget; // nom de l'attribut ou du cogniton cibl�
	private Double value; // valeur de la modification
	private String name;
	private boolean permanent;
	private int type; // 0 = add or replace , 1 = modify , 2 = remove
	private int activation; //0 = posses , 1 = use
	private String Description;
	
	public Effect()
	{
		
	}
	
	public Effect(String name,String target,String varget, Double value,boolean permanent,int type)
	{
		this.Varget = varget;
		this.target = target;
		this.name = name;
		this.permanent = permanent;
		this.value = value;
		this.setType(type);
	}
	
	/**
	 * TODO faire la partie modification de cognitons
	 * @param h
	 * @param e
	 */

	public void Activer(Human h)
	{
	//	System.out.println(this.target);
		if(this.target.equals("attribut"))
		{
			switch(type){
			case 0 :	//add or replace
				h.getAttr().put(Varget, value);
				break;
				
			case 1 :	//modify
				double attr = h.getAttr().get(Varget);
				h.getAttr().put(Varget, attr + value.intValue());
				break;
			
			case 2 :	//remove
				h.getAttr().remove(Varget);
				break;
			default :
				break;
			}
		}
		else if(this.target.equals("cogniton"))
		{
			switch(type){
			case 0 :	//add or replace
				h.getEsprit().setCogniton(h.getCiv().getCognitonByName(Varget), value);
				break;
				
			case 1 :	//modify
				h.getEsprit().addWeightToCogniton(h.getCiv().getCognitonByName(Varget), value);
				break;
			
			case 2 :	//remove
				h.getEsprit().removeCogniton(h.getCiv().getCognitonByName(Varget));
				break;
			default :
				break;
			}
		}
		else if(this.target.equals("objet"))
		{
			switch(type){
			case 0 :	//add or replace
				h.getInventaire().deleteObjets(Configuration.getObjetByName(Varget),h.getInventaire().possede(Configuration.getObjetByName(Varget)));
				h.getInventaire().addObjets(Configuration.getObjetByName(Varget), value.intValue());
				break;
				
			case 1 :	//modify
				if(value > 0)
				{
					h.getInventaire().addObjets(Configuration.getObjetByName(Varget), value.intValue());
				}
				else
				{
					h.getInventaire().deleteObjets(Configuration.getObjetByName(Varget), value.intValue());
				}
				break;
			
			case 2 :	//remove
				h.getInventaire().deleteObjets(Configuration.getObjetByName(Varget),h.getInventaire().possede(Configuration.getObjetByName(Varget)));
				break;
			default :
				break;
			}
		}
		else if(this.target.equals("ressource"))
		{
			
		}
		else
		{
			
		}
	}
	
	public void Desactiver(Human h)
	{

		if(this.target == "attribut")
		{
			if(type == 1)
			{
				double attr = h.getAttr().get(Varget);
				h.getAttr().put(Varget, attr - value.intValue());
			}
		}
		else if(this.target.equals("cogniton"))
		{
			if(type == 1)
			{
				h.getEsprit().addWeightToCogniton(h.getCiv().getCognitonByName(Varget), -value);
			}
		}
		else if(this.target.equals("objet"))
		{
			if(type == 1)
			{
				if(value > 0)
				{
					h.getInventaire().deleteObjets(Configuration.getObjetByName(Varget), value.intValue());
				}
				else
				{
					h.getInventaire().addObjets(Configuration.getObjetByName(Varget), value.intValue());
				}
			}
		}
		else if(this.target.equals("ressource"))
		{
			
		}
		else
		{
			
		}
	}
	
	
	public void enregistrer(File cible) {
		PrintWriter out;
		System.out.println("Enregistrement de l'effet "+getName());
		try {
			out = new PrintWriter(new FileWriter(cible.getPath()+"/"+getName()+Configuration.getExtension()));
			out.println("Nom : " + getName());
			out.println("Description : " + getDescription());
			out.println("Cible : " + getTarget());
			out.println("NomCible : " + getVarget());
			out.println("Valeur : " + getValue());
			out.println("Permanence : " + isPermanent());
			out.println("Type : " + getType());
			out.println("Activation : "+ getActivation());
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	

	public void Activer(Amenagement a)
	{
		// NOTE : cette méthode doit exister pour l'inventaire des aménagements
	}
	
	public void Desactiver(Amenagement a)
	{
		// NOTE : cette méthode doit exister pour l'inventaire des aménagements
	}
	
	public boolean isPermanent()
	{
		return this.permanent;
	}
	
	public void setPermanent(boolean perm)
	{
		this.permanent = perm;
	}
	
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getVarget() {
		return Varget;
	}
	public void setVarget(String varget) {
		this.Varget = varget;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		this.Description = description;
	}

	public int getActivation() {
		return activation;
	}

	public void setActivation(int activation) {
		this.activation = activation;
	}
}
