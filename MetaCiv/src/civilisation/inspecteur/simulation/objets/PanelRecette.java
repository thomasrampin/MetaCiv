package civilisation.inspecteur.simulation.objets;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ScrollPaneLayout;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import I18n.I18nList;

import civilisation.Configuration;
import civilisation.effects.Effect;

public class PanelRecette extends JPanel implements ActionListener{
	
	ArrayList<String> recettes;
	ArrayList<Integer> necessaires;
	JComboBox ItemCraft;
	JSpinner NumberItem;
	JButton addItem;
	JButton SaveEffect;
	DefaultTableModel model = new DefaultTableModel();
	JTable table = new JTable(model);
	JScrollPane scroll = new JScrollPane(table);
	PanelObjets sup;
	String tempo = "bla";
	public PanelRecette(PanelObjets objet)
	{
		super();
		
		Box b1 = Box.createVerticalBox();
		Box b2 = Box.createHorizontalBox();
		Box b3 = Box.createHorizontalBox();
		Box b4 = Box.createHorizontalBox();
		sup = objet;
		recettes = new ArrayList<String>();
		necessaires = new ArrayList<Integer>();
		this.ItemCraft = new JComboBox();
		SpinnerModel modele =
		        new SpinnerNumberModel(0, //initial value
		                               -10, //min
		                              10, //max
		                               1);    
		this.NumberItem = new JSpinner(modele);
		model.addColumn("Object");
	     model.addColumn("Number");
	     for(int i = 0; i < this.recettes.size();++i)
	     {
	    	 this.model.addRow(new Object[]{this.recettes.get(i),this.necessaires.get(i)});
	     }
	     scroll.setVisible(true);
	     for(int i = 0; i < Configuration.objets.size();++i)
	     {
	    	 if(Configuration.objets.get(i).getNom() != null)
	    	 {
	    		 ItemCraft.addItem(Configuration.objets.get(i).getNom());
	    	 }
	    	
	     }
	     ItemCraft.addActionListener(this);
	     
	     
	     this.addItem = new JButton(I18nList.CheckLang("Add item for craft"));
			this.addItem.addActionListener(this);
			this.addItem.setActionCommand("item");
			
			SaveEffect = new JButton(I18nList.CheckLang("Save Recipe"));

			SaveEffect.addActionListener(this);
			SaveEffect.setActionCommand("SaveEffect");
	    
		ItemCraft.setVisible(true);
		NumberItem.setVisible(true);
		addItem.setVisible(true);
		SaveEffect.setVisible(true);
		b2.add(scroll);
		b3.add(ItemCraft);
		b3.add(NumberItem);
		b3.add(addItem);
		b4.add(SaveEffect);
		b1.add(b2);
		b1.add(b3);
		b1.add(b4);
		this.add(b1);
		this.setVisible(true);
	}
	
	public void update(PanelObjets objet)
	{
		if(!tempo.equals(objet.nameField.getText()))
		{
		//	System.out.println("Bla");
			tempo = objet.nameField.getText();
			sup = objet;
			String tem = sup.nameField.getText();
			for(int i = 0; i < this.model.getRowCount();++i)
			{
				this.model.removeRow(i);
			}
			int j = 0;
			while(j < Configuration.objets.size() && !Configuration.objets.get(j).getNom().equals(tem))
			{
				++j;
			}
			if(j < Configuration.objets.size())
			{
				this.recettes = Configuration.objets.get(j).getRecetteString();
				this.necessaires =  Configuration.objets.get(j).getNombre();
			}
			else
			{
				this.recettes = new ArrayList<String>();
				this.necessaires = new ArrayList<Integer>();
			}
			for(int i = 0; i < this.model.getRowCount();++i)
			{
				this.model.removeRow(i);
			}
			for(int i = 0; i < this.recettes.size();++i)
			{
			    this.model.addRow(new Object[]{this.recettes.get(i),this.necessaires.get(i)});
			}
		}
		else
		{
			for(int i = 0; i < this.model.getRowCount();++i)
			{
				this.model.removeRow(i);
			}
			for(int i = 0; i < this.recettes.size();++i)
			{
			    this.model.addRow(new Object[]{this.recettes.get(i),this.necessaires.get(i)});
			}
		}
//		System.out.println(sup.nameField.getText());
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand() == "item")
		{
			int i = 0;
			while( i < this.recettes.size() && !this.recettes.get(i).equals(this.ItemCraft.getSelectedItem()))
			{
				++i;
			}
			if(i < this.recettes.size())
			{
				this.recettes.remove(i);
				int temp = this.necessaires.get(i);
				this.necessaires.remove(i);
				this.recettes.add((String) this.ItemCraft.getSelectedItem());
				this.necessaires.add((Integer) this.NumberItem.getValue());
			}
			else
			{
				this.recettes.add((String) this.ItemCraft.getSelectedItem());
				this.necessaires.add((Integer) this.NumberItem.getValue());
			}
			sup.recettes = this.recettes;
			this.update(sup);
		}
		if(e.getActionCommand() == "SaveEffect")
		{
			sup.recettes = this.recettes;
			sup.necessaires = this.necessaires;
			this.update(sup);
		}
	}
	public void resizeColumnWidth(JTable table) {
	    final TableColumnModel columnModel = table.getColumnModel();
	    for (int column = 0; column < table.getColumnCount(); column++) {
	        int width = 50; // Min width
	        for (int row = 0; row < table.getRowCount(); row++) {
	            TableCellRenderer renderer = table.getCellRenderer(row, column);
	            Component comp = table.prepareRenderer(renderer, row, column);
	            width = Math.max(comp.getPreferredSize().width +1 , width);
	        }
	        columnModel.getColumn(column).setPreferredWidth(width);
	    }
	}


}
