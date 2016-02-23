package civilisation.constant;

import java.util.Observable;
import java.io.Serializable;

public class MCConstant extends Observable implements Serializable{
	private double value;
	
	public MCConstant(double val)
	{
		value = val;
	}

	public MCConstant(int val)
	{
		value = val;
	}
	
	public double getValue()
	{
		return value;
	}

	public void setValue(double val)
	{
			value = val;
			setChanged();
			notifyObservers();
			clearChanged();
	}

	public void setValue(int val)
	{
		value = val;
	}
}
