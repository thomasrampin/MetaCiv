package civilisation.zones;
import java.io.Serializable;
import turtlekit.kernel.Patch;

public class Zone implements Serializable {

	Patch centroide;
	int width;
	int height;

	public Zone(Patch centroide)
	{
		this.centroide = centroide;
	}
	
	public Zone()
	{
		this.centroide = null;
	}
	
	public Patch getCentroide()
	{
		return centroide;
	}
	
	
}
