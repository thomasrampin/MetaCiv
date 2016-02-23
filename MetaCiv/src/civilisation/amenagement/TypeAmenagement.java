package civilisation.amenagement;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;

import civilisation.Configuration;
import civilisation.effects.Effect;
import civilisation.inventaire.Objet;

public class TypeAmenagement implements Serializable {
	
	Color color;
	String nom;
	ArrayList<Effect> effets;
	ArrayList<Objet> recette;
	ArrayList<Integer> nombre;
	
	
	public TypeAmenagement()
	{
		this.color = Color.GRAY;
		this.effets = new ArrayList<Effect>();
		this.recette = new ArrayList<Objet>();
		this.nombre = new ArrayList<Integer>();
	}

	public TypeAmenagement(String name,ArrayList<Effect> effets)
	{
		this.color = Color.GRAY;
		nom = name;
		this.effets = effets;
		this.recette = new ArrayList<Objet>();
		this.nombre = new ArrayList<Integer>();

	}
	
	public TypeAmenagement(String name, Effect effet)
	{
		this.color = Color.GRAY;
		this.effets = new ArrayList<Effect>();
		this.effets.add(effet);
		this.recette = new ArrayList<Objet>();
		this.nombre = new ArrayList<Integer>();

	}
	
	public TypeAmenagement(String string) {
		this.color = Color.GRAY;
		nom = string;
		this.effets = new ArrayList<Effect>();
		this.recette = new ArrayList<Objet>();
		this.nombre = new ArrayList<Integer>();
	}

	public void enregistrer(File cible) {
		PrintWriter out;
		try {
		    float hsb[] = Color.RGBtoHSB( this.getColor().getRed(),this.getColor().getGreen(),this.getColor().getBlue(), null );
			out = new PrintWriter(new FileWriter(cible.getPath()+"/"+getNom()+Configuration.getExtension()));
			out.println("Nom : " + getNom());
			out.println("Couleur : "+hsb[0]+","+hsb[1]+","+hsb[2]);
			out.print("Effects : ");
	//		System.out.println("Nombre d'effets nregistres "+this.effets.size());
			if(this.effets.size() > 0)
			{
				for(int i = 0; i < this.effets.size();++i)
				{
					out.print(this.effets.get(i).getName() + ",");
				}
			}
			out.println();
			out.print("Objets : ");
			if(this.recette.size() > 0)
			{
				for(int i = 0; i < this.recette.size();++i)
				{
					out.print(this.recette.get(i).getNom() + ",");
				}
			}
			out.println();
			out.print("Nombre : ");
			if(this.nombre.size() > 0)
			{
				for(int i = 0; i < this.nombre.size();++i)
				{
					out.print(this.nombre.get(i) + ",");
				}
			}
				
			
			out.println();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void addItemRecipe(String objet, int nombre)
	{
		int i = 0;
		while(i < this.recette.size() && !this.recette.get(i).getNom().equals(objet))
		{
			++i;
		}
		if(i <  this.recette.size())
		{
			this.recette.remove(i);
			int temp = this.nombre.get(i);
			this.nombre.remove(i);
			this.recette.add(Configuration.getObjetByName(objet));
			this.nombre.add(nombre + temp);
		}
		else
		{
			this.recette.add(Configuration.getObjetByName(objet));
			this.nombre.add(nombre);
		}
	}
	
	public void addEffect(Effect e)
	{
		int i = 0;
		if(e != null)
		{
			while(i < this.effets.size() && this.effets.get(i).getName().equals(e.getName()))
			{
				i++;
			}
			if(i < this.effets.size())
			{
				Configuration.effets.remove(i);
			}
			this.effets.add(e);
		}
		
		
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public ArrayList<Effect> getEffets() {
		return effets;
	}

	public void setEffets(ArrayList<Effect> effets) {
		this.effets = effets;
	}

	public ArrayList<Objet> getRecette() {
		return recette;
	}

	public void setRecette(ArrayList<Objet> recette) {
		this.recette = recette;
	}

	public ArrayList<Integer> getNombre() {
		return nombre;
	}

	public void setNombre(ArrayList<Integer> nombre) {
		this.nombre = nombre;
	}
	
}
