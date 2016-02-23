package civilisation.constant;

import java.util.Observable;
import java.util.Observer;
import java.io.Serializable;

public class MCIntegerParameter implements Observer, Serializable{
	private int value;
	private MCConstant link = null;
	
	public MCIntegerParameter(int val , MCConstant constant) {
		setValue(val);
		if(constant != null)
		{
			link = constant;
			constant.addObserver(this);
		}		
		
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		this.setValue((int)((MCConstant)arg0).getValue());
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
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
