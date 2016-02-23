package civilisation.stats;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WidgetPanelGeneralProperties extends JPanel{

	JTextField widgetName = new JTextField(10);
	
	public WidgetPanelGeneralProperties()
	{
		this.add(new JLabel("name"));
		this.add(widgetName);
	}
	
	public String getWidgetName()
	{
		return widgetName.getText();
	}

	public void setWidgetName(String in)
	{
		widgetName.setText(in);
	}
}
