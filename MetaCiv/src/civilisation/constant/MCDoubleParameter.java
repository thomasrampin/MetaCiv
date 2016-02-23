package civilisation.constant;

import java.util.Observable;
import java.util.Observer;

import java.io.Serializable;

public class MCDoubleParameter implements Observer, Serializable{
	private double value;
	private MCConstant link = null;
	
	public MCDoubleParameter(double val , MCConstant constant) {
		setValue(val);
		if(constant != null)
		{
			link = constant;
			constant.addObserver(this);
		}		
		
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		this.setValue((double)((MCConstant)arg0).getValue());
		}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public void changeConstantLink(MCConstant newCst)
	{
		if(link != null)
			link.deleteObserver(this);
		link = newCst;
		if(link !=null)
			link.addObserver(this);
	}
}
