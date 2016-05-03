package civilisation.amenagement;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import civilisation.Configuration;
import civilisation.DefineConstants;
import civilisation.effects.Effect;
import civilisation.individu.Human;
import civilisation.individu.Human.HumainListener;
import civilisation.inventaire.NAInventaire;
import civilisation.inventaire.NInventaire;
import civilisation.inventaire.Objet;
import turtlekit.kernel.Patch;
import turtlekit.kernel.Turtle;

public class Amenagement extends Turtle implements Serializable {

	Patch position; //Le patch contenant l'amenagement
	Human possesseur; //Possesseur de l'amenagement
	ArrayList<Effect> effets;
	ArrayList<Objet> recette;
	ArrayList<Integer> nombre;
	TypeAmenagement type;
	NAInventaire inventaire;

	public Amenagement(Patch p, Human h,TypeAmenagement type)
	{
		super("idle");
		position = p;
		possesseur = h;
		this.type = type;
		//p.dropMark(type.getNom(), this);
		this.inventaire = new NAInventaire(this);
	}
	
	public Amenagement(Patch p,Human h,String string)
	{
		super("idle");
		position = p;
		possesseur = h;
	}

	public String idle()
	{
		return "idle";
	}
	
	@Override
	public void activate()
	{
		super.activate();
		this.moveTo(position.x, position.y);
		setColor(type.getColor());
		this.playRole(DefineConstants.Role_Facility);
	} 

	
	public void dessiner(Graphics g,int x,int y,int cellS)
	{
		g.setColor(type.getColor());
		
		for (int i = 0; i < cellS; i++)
		{
			if (i%2 == 1)
			{
				g.drawLine(x, y+i, x+cellS-1, y+i);
				
			}

		}
	}
	
	public void Utiliser()
	{
	//	System.out.println("Utiliser Amenagement size effet "+type.effets.size());
		for(int i = 0; i < type.effets.size();++i)
		{
			if(type.effets.get(i).getActivation() == 1)
			{
			//	System.out.println(type.effets.get(i).getName());
				type.effets.get(i).Activer(possesseur);
			}
		}
	}
	
	public String getNom()
	{
		return "---Amenagement---";
	}

	public Human getPossesseur() {
		return possesseur;
	}

	public void setPossesseur(Human possesseur) {
		this.possesseur = possesseur;
	}

	public Patch getPosition() {
		return position;
	}
	

	/**
	 * Les cognitons declench___s par cet amenagement
	 * @return tableau de string repr___sentant les cognitons
	 */
	public String[] cognitonsLies()
	{
		return null;
	}

	public TypeAmenagement getType() {
		// TODO Auto-generated method stub
		return type;

	}
	
	public NAInventaire getInventaire()
	{
		return this.inventaire;
	}
}
