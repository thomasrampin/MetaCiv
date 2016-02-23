package civilisation.individu.plan.action;

import java.util.ArrayList;

import civilisation.constant.MCIntegerParameter;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;

public class A_SetPoissonLaw extends Action{

	String attributeName;
	long  fact(int n)
	{
	    if(n==0)
	        return 1;
	    else
	        return (long)n* fact(n-1);
	}
	
	@Override
	public Action effectuer(Human h) {
		
		float lambda = 10;
	    float[] cumul = new float[20];
	    for(int i=0; i<20; i++)
	        cumul[i] = 0;
	    
	    cumul[0] = 0;
	    for(int i=1; i<20; i++)
	    {
	        cumul[i] = (float) (cumul[i-1] + Math.pow(lambda,i)* Math.exp(-lambda) / fact(i));
	    }
	    cumul[19] = 1;
	    
	    double r =  Math.random();
	    int j=0;
	    while(cumul[j] < r)
	    	j++;
	        
	    double a = cumul[j] - cumul[j-1];
	       
	    h.putAttr(attributeName, (r/a - cumul[j]/a + j ) * 5);
		return nextAction;
	}

	@Override
	public void parametrerOption(OptionsActions option){
		super.parametrerOption(option);

		if (option.getParametres().get(0).getClass() == String.class) {
			attributeName = (String) option.getParametres().get(0);
		}
	}
	
	@Override
	public ArrayList<String[]> getSchemaParametres(){
		
		if (schemaParametres == null){
			schemaParametres = new ArrayList<String[]>();
			String[] attrName = {"**Attribute**" , "Modified attribute"};
			schemaParametres.add(attrName);

		}
		return schemaParametres;	
	}
	
	
	@Override
	public String getInfo() {
		return super.getInfo() + " Change the current value of an attribute to a number following the Poisson Law with lambda = 10 and multiplied by 5.<html>";
	}


	
}
