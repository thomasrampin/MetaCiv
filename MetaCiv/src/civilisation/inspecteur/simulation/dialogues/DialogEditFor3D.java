package civilisation.inspecteur.simulation.dialogues;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import I18n.I18nList;
import civilisation.Configuration;
import civilisation.world.World;
import renderEngine.renderMain;

public class DialogEditFor3D extends JDialog implements ActionListener,PropertyChangeListener{
	JOptionPane optionPane;
	JSpinner jSpinnerSnowHeight;
	JSpinner jSpinnerSnowDistAtt;
	JSpinner jSpinnerSnowDensity;
	
	JSpinner jSpinnerRoadTiling;
	JSpinner jSpinnerCliffTiling;
	ArrayList<Object> selectors = new ArrayList<Object>();
	public DialogEditFor3D(Frame f,boolean modal){
		super(f,modal);
		
		Box boxSnowHeight = Box.createHorizontalBox();
		boxSnowHeight.add(new JLabel(I18nList.CheckLang("Snow Height : ")));
		SpinnerModel spinModel = new SpinnerNumberModel(World.getSnowHeight(), 0, 1000, 0.1);
		jSpinnerSnowHeight = new JSpinner(spinModel);
		boxSnowHeight.add(jSpinnerSnowHeight);
		
		Box boxSnowDistanceAtt = Box.createHorizontalBox();
		boxSnowDistanceAtt.add(new JLabel(I18nList.CheckLang("Snow Distance Attenuation : ")));
		spinModel = new SpinnerNumberModel(World.getSnowDistanceAtt(), 0, 1000, 0.1);
		jSpinnerSnowDistAtt = new JSpinner(spinModel);
		boxSnowDistanceAtt.add(jSpinnerSnowDistAtt);
		
		Box boxSnowDensity = Box.createHorizontalBox();
		boxSnowDensity.add(new JLabel(I18nList.CheckLang("Snow Density : ")));
		spinModel = new SpinnerNumberModel(World.getSnowDensity()*100, 0, 1000, 0.1);
		jSpinnerSnowDensity = new JSpinner(spinModel);
		boxSnowDensity.add(jSpinnerSnowDensity);
		
		this.setTitle(I18nList.CheckLang("Setting 3D"));
		Box box = Box.createVerticalBox();
		TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),I18nList.CheckLang("Snow"));
		border.setTitleJustification(TitledBorder.LEFT);
		box.setBorder(border);
		box.add(boxSnowHeight);
		box.add(boxSnowDistanceAtt);
		box.add(boxSnowDensity);
		
		
		Box boxRoadTiling = Box.createHorizontalBox();
		boxRoadTiling.add(new JLabel(I18nList.CheckLang("Road Tiling : ")));
		spinModel = new SpinnerNumberModel(World.getRoadTiling(), 0, 1000, 0.1);
		jSpinnerRoadTiling = new JSpinner(spinModel);
		boxRoadTiling.add(jSpinnerRoadTiling);
		
		Box boxCliffTiling = Box.createHorizontalBox();
		boxCliffTiling.add(new JLabel(I18nList.CheckLang("Cliff Tiling : ")));
		spinModel = new SpinnerNumberModel(World.getCliffTiling(), 0, 1000, 0.1);
		jSpinnerCliffTiling = new JSpinner(spinModel);
		boxCliffTiling.add(jSpinnerCliffTiling);
		
		Box box2 = Box.createVerticalBox();
		TitledBorder border2 = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), I18nList.CheckLang("Tiling"));
		border2.setTitleJustification(TitledBorder.LEFT);
		box2.setBorder(border2);
		box2.add(boxRoadTiling);
		box2.add(boxCliffTiling);
		
		selectors.add(box);
		selectors.add(box2);
		Object[] array = selectors.toArray();
	    Object[] options = {"OK" , "Cancel"};
	    optionPane = new JOptionPane(array,
	                                    JOptionPane.QUESTION_MESSAGE,
	                                    JOptionPane.YES_NO_OPTION,
	                                    null,
	                                    options,
	                                    options[0]); 
	    setContentPane(optionPane);
	        
	    optionPane.addPropertyChangeListener(this);
	    ImageIcon icone = new ImageIcon(System.getProperty("user.dir")+"/civilisation/graphismes/LogoMedium.png");
		optionPane.setIcon(icone);
		this.pack();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		if (isVisible() && (optionPane.getValue().equals("OK") || optionPane.getValue().equals("Cancel"))){
			if (optionPane.getValue().equals("OK")){
				float h = (float)(double)jSpinnerSnowHeight.getValue();
				float dA = (float)(double)jSpinnerSnowDistAtt.getValue();
				float d = (float)(double)jSpinnerSnowDensity.getValue()/100.0f;
				float rT = (float)(double)jSpinnerRoadTiling.getValue();
				float rC = (float)(double)jSpinnerCliffTiling.getValue();
				World.setSnowHeight(h);
				World.setSnowDistanceAtt(dA);
				World.setSnowDensity(d);
				World.setRoadTiling(rT);
				World.setCliffTiling(rC);
			}		
	        setVisible(false);
		}
	}

}
