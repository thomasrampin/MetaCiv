package civilisation.inspecteur.simulation.objets;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import I18n.I18nList;

import civilisation.Configuration;
import civilisation.effects.Effect;
import civilisation.inventaire.Objet;

public class PanelEffect extends JPanel implements ActionListener{
	
	JLabel nomEffet;
	JTextField nomEffetText;
	JLabel target;
	ButtonGroup choixTarget;
	JRadioButton choix1T;
	JRadioButton choix2T;
	JRadioButton choix3T;
	JLabel nomCible;
	JComboBox listeCibles;
	JLabel modifValue;
	JTextField modifValueText;
	JLabel permanent;
	ButtonGroup choixPermanent;
	JRadioButton choix1P;
	JRadioButton choix2P;
	JLabel nomType;
	ButtonGroup choixType;
	JRadioButton choix1Ty;
	JRadioButton choix2Ty;
	JRadioButton choix3Ty;
	JLabel nomActive;
	ButtonGroup choixActive;
	JRadioButton choix1A;
	JRadioButton choix2A;
	JRadioButton choix3A;
	JButton SaveEffect;
	Effect effet;
	JButton RemoveEffect;
	Box b8 = Box.createVerticalBox();
	
	public PanelEffect(PanelObjets panelParent)
	{
		super();

		nomEffet = new JLabel(I18nList.CheckLang("Effect Name : "));
		nomEffetText = new JTextField(20);
		target = new JLabel(I18nList.CheckLang("Target type : "));
		choixTarget = new ButtonGroup();
		choix1T = new JRadioButton(I18nList.CheckLang("Attributes"));
		choix2T = new JRadioButton(I18nList.CheckLang("Cognitons"));
		choix3T = new JRadioButton(I18nList.CheckLang("Actions"));

		choix1T.addActionListener(this);
		choix1T.setActionCommand("Attributes");
		choix1T.setSelected(true);
		choix2T.addActionListener(this);
		choix2T.setActionCommand("Cognitons");
		choix3T.addActionListener(this);
		choix3T.setActionCommand("Actions");
		choixTarget.add(choix1T);
		choixTarget.add(choix2T);
		choixTarget.add(choix3T);

		nomCible = new JLabel(I18nList.CheckLang("Target Name : "));

		listeCibles = new JComboBox();
		if(choix1T.isSelected())
		{
			for(int i = 0; i < Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().size();++i)
			{
				listeCibles.addItem(Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().get(i));
			}
		}
		else
		{
			for(int i = 0; i < Configuration.getSchemaCognitifEnCourEdition().getCognitons().size();++i)
			{
				listeCibles.addItem(Configuration.getSchemaCognitifEnCourEdition().getCognitons().get(i).getNom());
			}
		}

		nomType = new JLabel(I18nList.CheckLang("Effect Type : "));
		choixType = new ButtonGroup();
		choix1Ty = new JRadioButton(I18nList.CheckLang("Set"));
		choix2Ty = new JRadioButton(I18nList.CheckLang("Add"));
		choix3Ty = new JRadioButton(I18nList.CheckLang("Remove"));
		choix1Ty.addActionListener(this);
		choix2Ty.addActionListener(this);
		choix3Ty.addActionListener(this);
		choix3Ty.setActionCommand("Remove");
		choix1Ty.setSelected(true);
		choixType.add(choix1Ty);
		choixType.add(choix2Ty);
		choixType.add(choix3Ty);

		modifValue = new JLabel(I18nList.CheckLang("Value : "));
		modifValueText = new JTextField(5);
		modifValueText.setText("0");
		nomActive = new JLabel(I18nList.CheckLang("Activation : "));
		choixActive = new ButtonGroup();
		choix1A = new JRadioButton(I18nList.CheckLang("Possession"));
		choix2A = new JRadioButton(I18nList.CheckLang("Use"));
		choix3A = new JRadioButton(I18nList.CheckLang("Remove"));
		choixActive.add(choix1A);
		choixActive.add(choix2A);
		choixActive.add(choix3A);
		choix1A.setSelected(true);


		permanent = new JLabel(I18nList.CheckLang("Permanent ? : "));
		choixPermanent = new ButtonGroup();
		choix1P = new JRadioButton(I18nList.CheckLang("Yes"));
		choix2P = new JRadioButton(I18nList.CheckLang("No"));
		choixPermanent.add(choix1P);
		choixPermanent.add(choix2P);
		choix1P.setSelected(true);
		SaveEffect = new JButton(I18nList.CheckLang("Save Effect"));
		SaveEffect.addActionListener(this);
		SaveEffect.setActionCommand("SaveEffect");
		
		RemoveEffect = new JButton(I18nList.CheckLang("Remove Effect"));

		RemoveEffect.addActionListener(this);
		RemoveEffect.setActionCommand("delete");
		
		Box b1 =  Box.createHorizontalBox();
		b1.setAlignmentX(LEFT_ALIGNMENT);
		b1.setAlignmentY(LEFT_ALIGNMENT);
		
		b1.add(nomEffet);
		b1.add(nomEffetText);
		
		Box b2 = Box.createHorizontalBox();
		b2.setAlignmentX(LEFT_ALIGNMENT);
		b2.setAlignmentY(LEFT_ALIGNMENT);
		
		b2.add(target);
		b2.add(choix1T);
		b2.add(choix2T);
		b2.add(choix3T);
		
		Box b3 = Box.createHorizontalBox();
		b3.setAlignmentX(LEFT_ALIGNMENT);
		b3.setAlignmentY(LEFT_ALIGNMENT);
		
		b3.add(nomCible);
		b3.add(listeCibles);
		
		Box b4 = Box.createHorizontalBox();
		b4.setAlignmentX(LEFT_ALIGNMENT);
		b4.setAlignmentY(LEFT_ALIGNMENT);
		
		b4.add(modifValue);
		b4.add(modifValueText);
		
		Box b5 = Box.createHorizontalBox();
		b5.setAlignmentX(LEFT_ALIGNMENT);
		b5.setAlignmentY(LEFT_ALIGNMENT);
		
		b5.add(permanent);
		b5.add(choix1P);
		b5.add(choix2P);
		
		Box b6 = Box.createHorizontalBox();
		b6.setAlignmentX(LEFT_ALIGNMENT);
		b6.setAlignmentY(LEFT_ALIGNMENT);
		
		b6.add(nomType);
		b6.add(choix1Ty);
		b6.add(choix2Ty);
		b6.add(choix3Ty);
		
		Box b7 = Box.createHorizontalBox();
		b7.setAlignmentX(LEFT_ALIGNMENT);
		b7.setAlignmentY(LEFT_ALIGNMENT);
		
		b7.add(nomActive);
		b7.add(choix1A);
		b7.add(choix2A);
		b7.add(choix3A);
		
		
		b8.add(b1);
		b8.add(b2);
		b8.add(b3);
		b8.add(b5);
		b8.add(b6);
		b8.add(b4);
		b8.add(b7);
		b8.add(SaveEffect);
		b8.add(RemoveEffect);
		this.add(b8);
	}
	
	public PanelEffect(PanelObjets p,Effect e)
	{
		super();

		nomEffet = new JLabel(I18nList.CheckLang("Effect Name : "));
		nomEffetText = new JTextField(20);
		nomEffetText.setText(e.getName());
		target = new JLabel(I18nList.CheckLang("Target type : "));
		choixTarget = new ButtonGroup();
		choix1T = new JRadioButton(I18nList.CheckLang("Attributes"));
		choix2T = new JRadioButton(I18nList.CheckLang("Cognitons"));
		choix3T = new JRadioButton(I18nList.CheckLang("Actions"));

		
		choix1T.addActionListener(this);
		choix2T.addActionListener(this);
		choix3T.addActionListener(this);
		choixTarget.add(choix1T);
		choixTarget.add(choix2T);
		choixTarget.add(choix3T);
		if(e.getTarget().equals("attribut"))
		{
			choix1T.setSelected(true);
		}
		else if(e.getTarget().equals("cogniton"))
		{
			choix2T.setSelected(true);
		}
		else
		{
			choix3T.setSelected(true);
		}

		nomCible = new JLabel(I18nList.CheckLang("Target Name : "));
		listeCibles = new JComboBox();
		if(choix1T.isSelected())
		{
			for(int i = 0; i < Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().size();++i)
			{
				listeCibles.addItem(Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().get(i));
			}
		}
		else if(e.getTarget().equals("cogniton"))
		{
			for(int i = 0; i < Configuration.getSchemaCognitifEnCourEdition().getCognitons().size();++i)
			{
				listeCibles.addItem(Configuration.getSchemaCognitifEnCourEdition().getCognitons().get(i).getNom());
			}
		}
		else
		{
			for(int i = 0; i < Configuration.actions.size();++i)
			{
				listeCibles.addItem(Configuration.actions.get(i).getSimpleName());
			}
		}
		int index = 0;
		for(int j = 0; j < listeCibles.getItemCount();++j)
		{
			if(listeCibles.getItemAt(j).equals(e.getVarget()))
			{
				index = j;
			}
		}
		listeCibles.setSelectedIndex(index);

		nomType = new JLabel(I18nList.CheckLang("Effect Type : "));
		choixType = new ButtonGroup();
		choix1Ty = new JRadioButton(I18nList.CheckLang("Set"));
		choix2Ty = new JRadioButton(I18nList.CheckLang("Add"));
		choix3Ty = new JRadioButton(I18nList.CheckLang("Remove"));

		choix1Ty.addActionListener(this);
		choix2Ty.addActionListener(this);
		choix3Ty.addActionListener(this);
		choix3Ty.setActionCommand("Remove");
		choixType.add(choix1Ty);
		choixType.add(choix2Ty);
		choixType.add(choix3Ty);
		if(e.getType() == 0)
		{
			choix1Ty.setSelected(true);
		}
		else
		{
			if(e.getType() == 1)
			{
				choix2Ty.setSelected(true);
			}
			else
			{
				choix3Ty.setSelected(true);
			}
		}

		modifValue = new JLabel(I18nList.CheckLang("Value : "));
		modifValueText = new JTextField(5);
		Double val = e.getValue();
		modifValueText.setText(val.toString());
		nomActive = new JLabel(I18nList.CheckLang("Activation : "));
		choixActive = new ButtonGroup();
		choix1A = new JRadioButton(I18nList.CheckLang("Possession"));
		choix2A = new JRadioButton(I18nList.CheckLang("Use"));
		choix3A = new JRadioButton(I18nList.CheckLang("Remove"));
		choixActive.add(choix1A);
		choixActive.add(choix2A);
		choixActive.add(choix3A);
		if(e.getActivation() == 0)
		{
			choix1A.setSelected(true);
		}
		else if(e.getActivation() == 1)
		{
			choix2A.setSelected(true);
		}
		else
		{
			choix3A.setSelected(true);
		}
		permanent = new JLabel(I18nList.CheckLang("Permanent ? : "));
		choixPermanent = new ButtonGroup();
		choix1P = new JRadioButton(I18nList.CheckLang("Yes"));
		choix2P = new JRadioButton(I18nList.CheckLang("No"));


		if(e.isPermanent())
		{
			choix1P.setSelected(true);
		}
		else
		{
			choix2P.setSelected(true);
		}
		choixPermanent.add(choix1P);
		choixPermanent.add(choix2P);

		SaveEffect = new JButton(I18nList.CheckLang("Save Effect"));
		SaveEffect.addActionListener(this);
		SaveEffect.setActionCommand("SaveEffect");
		
		RemoveEffect = new JButton(I18nList.CheckLang("Remove Effect"));

		RemoveEffect.addActionListener(this);
		RemoveEffect.setActionCommand("RemoveEffect");
		
		Box b1 =  Box.createHorizontalBox();
		b1.setAlignmentX(LEFT_ALIGNMENT);
		b1.setAlignmentY(LEFT_ALIGNMENT);
		
		b1.add(nomEffet);
		b1.add(nomEffetText);
		
		Box b2 = Box.createHorizontalBox();
		b2.setAlignmentX(LEFT_ALIGNMENT);
		b2.setAlignmentY(LEFT_ALIGNMENT);
		
		b2.add(target);
		b2.add(choix1T);
		b2.add(choix2T);
		b2.add(choix3T);
		
		Box b3 = Box.createHorizontalBox();
		b3.setAlignmentX(LEFT_ALIGNMENT);
		b3.setAlignmentY(LEFT_ALIGNMENT);
		
		b3.add(nomCible);
		b3.add(listeCibles);
		
		Box b4 = Box.createHorizontalBox();
		b4.setAlignmentX(LEFT_ALIGNMENT);
		b4.setAlignmentY(LEFT_ALIGNMENT);
		
		b4.add(modifValue);
		b4.add(modifValueText);
		
		Box b5 = Box.createHorizontalBox();
		b5.setAlignmentX(LEFT_ALIGNMENT);
		b5.setAlignmentY(LEFT_ALIGNMENT);
		
		b5.add(permanent);
		b5.add(choix1P);
		b5.add(choix2P);
		
		Box b6 = Box.createHorizontalBox();
		b6.setAlignmentX(LEFT_ALIGNMENT);
		b6.setAlignmentY(LEFT_ALIGNMENT);
		
		b6.add(nomType);
		b6.add(choix1Ty);
		b6.add(choix2Ty);
		b6.add(choix3Ty);
		
		Box b7 = Box.createHorizontalBox();
		b7.setAlignmentX(LEFT_ALIGNMENT);
		b7.setAlignmentY(LEFT_ALIGNMENT);
		
		b7.add(nomActive);
		b7.add(choix1A);
		b7.add(choix2A);
		b7.add(choix3A);
		

		b8.add(b1);
		b8.add(b2);
		b8.add(b3);
		b8.add(b5);
		b8.add(b6);
		b8.add(b4);
		b8.add(b7);
		b8.add(SaveEffect);
		b8.add(RemoveEffect);
		this.add(b8);
		this.RendreVisible();
	}
	
	public void RendreVisible()
	{
		nomEffet.setVisible(true);
		nomEffetText.setVisible(true);
		target.setVisible(true);
		choix1T.setVisible(true);
		choix2T.setVisible(true);
		choix3T.setVisible(true);
		nomCible.setVisible(true);
		listeCibles.setVisible(true);
		modifValue.setVisible(true);
		modifValueText.setVisible(true);
		permanent.setVisible(true);
		choix1P.setVisible(true);
		choix2P.setVisible(true);
		nomType.setVisible(true);
		choix1Ty.setVisible(true);
		choix2Ty.setVisible(true);
		choix3Ty.setVisible(true);
		nomActive.setVisible(true);
		choix1A.setVisible(true);
		choix2A.setVisible(true);
		choix3A.setVisible(true);
	}
	
	public Effect performedChange(Effect e)
	{
		e.setName(nomEffetText.getText());
		
		return e;
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{

		if(e.getActionCommand() == "SaveEffect")
		{
			
			Effect ef = new Effect();
			ef.setName(nomEffetText.getText());
			if(choix1T.isSelected())
			{
				ef.setTarget("attribut");
			}
			else if(choix2T.isSelected())
			{
				ef.setTarget("cogniton");
			}
			else
			{
				ef.setTarget("action");
			}
			ef.setVarget(listeCibles.getSelectedItem().toString());
			ef.setValue(Double.parseDouble(modifValueText.getText()));
			if(choix1P.isSelected())
			{
				ef.setPermanent(true);
			}
			else
			{
				ef.setPermanent(false);
			}
			if(choix1Ty.isSelected())
			{
				ef.setType(0);
			}
			else
			{
				if(choix2Ty.isSelected())
				{
					ef.setType(1);
				}
				else
				{
					ef.setType(2);
				}
			}
			if(choix1A.isSelected())
			{
				ef.setActivation(0);
			}
			else if(choix2A.isSelected())
			{
				ef.setActivation(1);
			}
			else
			{
				ef.setActivation(2);
			}
			
			int i = 0;
			this.effet = ef;
			if(Configuration.getEffectByName(ef.getName()) != null)
			{
				int k = 0;
				while(  k < Configuration.effets.size() && !Configuration.effets.get(k).getName().equals(ef.getName()))
				{
					k++;
				}
				if(k < Configuration.effets.size())
				{
					Configuration.effets.remove(k);
				}
				
			}
			Configuration.effets.add(ef);

		}
		else if(e.getActionCommand() == "Attributes" || e.getActionCommand() == "Cognitons" || e.getActionCommand() == "Actions")
		{
			if(choix1T.isSelected())
			{
				listeCibles.removeAllItems();
				for(int i = 0; i < Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().size();++i)
				{
					listeCibles.addItem(Configuration.getSchemaCognitifEnCourEdition().getAttributesNames().get(i));
				}
			}
			else if(choix2T.isSelected())
			{
				listeCibles.removeAllItems();
				for(int i = 0; i < Configuration.getSchemaCognitifEnCourEdition().getCognitons().size();++i)
				{
					listeCibles.addItem(Configuration.getSchemaCognitifEnCourEdition().getCognitons().get(i).getNom());
				}
			}
			else
			{
				listeCibles.removeAllItems();
				for(int i = 0; i < Configuration.actions.size();++i)
				{
					listeCibles.addItem(Configuration.actions.get(i).getSimpleName());
				}
			}
			
		}
		else if(e.getActionCommand() == "delete")
		{
			if(this.nomEffetText.getText() != null || !this.nomEffetText.getText().equals(""))
			{
				if(Configuration.getEffectByName(this.nomEffetText.getText()) != null)
				{
					int k = 0;
					while(  k < Configuration.effets.size() && !Configuration.effets.get(k).getName().equals(this.nomEffetText.getText()))
					{
						k++;
					}
					if(k < Configuration.effets.size())
					{
						Configuration.effets.remove(k);
					}
				}
				this.remove(b8);
			}
			else
			{
				this.remove(b8);
			}
		}
		else if(e.getActionCommand() == "Remove")
		{
			this.modifValue.setVisible(false);
			this.modifValueText.setText("0");
			this.modifValueText.setVisible(false);
		}
		
	}

	public Effect returnEffect()
	{
		return this.effet;
	}
}
