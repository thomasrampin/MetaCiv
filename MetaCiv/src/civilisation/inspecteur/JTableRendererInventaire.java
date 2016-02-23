package civilisation.inspecteur;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import civilisation.Configuration;
import civilisation.inventaire.Objet;

public class JTableRendererInventaire extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

			ImageIcon icone;
			if(value != null && value.getClass().equals(String.class) && Configuration.getObjetByName((String) value) != null  /*(value.getClass().equals(Objet.class))*/)
			{
		//		Objet o = (Objet) value;
				Objet o = Configuration.getObjetByName((String) value);
				
				if (o != null)
				{
					setToolTipText((o.getNom()));
					this.setValue(o.getNom());

					if (o.getIcone() != null)
				    {
			    		setIcon(o.getIcone());
					}
					else
					{
						setIcon(null);
					}


				}
				else
				{
					setIcon(null);
					setValue(null);
					setToolTipText(null);
				}
			}
			else
			{
				Integer o = (Integer) value;
				
				if (o != null)
				{
					this.setValue(o);

				}
				else
				{
					
					setValue(null);
					
				}
				setIcon(null);
				setToolTipText(null);
			}
		    	
         return this;
	}
}