package civilisation.inspecteur.simulation.amenagement;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import civilisation.amenagement.TypeAmenagement;
import civilisation.inventaire.Objet;

public class ListeAmenagementRenderer  extends DefaultListCellRenderer{


	    @Override
		public Component getListCellRendererComponent(JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus)
	    {

	        super.getListCellRendererComponent( list,
	                 value,
	                 index,
	                 isSelected,
	                 cellHasFocus);
	        
	       TypeAmenagement o = (TypeAmenagement) value;

	       this.setText(o.getNom());
	      // this.setBackground(((Terrain) value).getCouleur());

	       
	        return this;
	    }
}
