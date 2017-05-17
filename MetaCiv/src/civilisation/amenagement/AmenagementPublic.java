package civilisation.amenagement;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.Serializable;

import turtlekit.kernel.Patch;

public class AmenagementPublic extends Amenagement implements Serializable {

	Patch position; //Le patch contenant l'am___nagement

	
	
	public AmenagementPublic(Patch p)
	{
		super(p,null,"");
	}
	
	@Override
	public void dessiner(Graphics g,Graphics2D g2d,int x,int y,int cellS)
	{
		
	}
	
	@Override
	public String getNom()
	{
		return "---Amenagement Public---";
	}


}
