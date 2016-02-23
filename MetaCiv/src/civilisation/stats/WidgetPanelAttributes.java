package civilisation.stats;

import java.awt.GridBagLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import civilisation.Civilisation;
import civilisation.Configuration;
import civilisation.DefinePath;
import civilisation.inventaire.Objet;
import civilisation.stats.WidgetPanelSocialFilter.CheckNode;

public class WidgetPanelAttributes extends JPanel{
	HashMap<String,ArrayList<JCheckBox>> check;
	JPanel pane = new JPanel();
	
	public WidgetPanelAttributes()
	{
		check = new HashMap<String , ArrayList<JCheckBox>>();
		JCheckBox box;
		pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
		pane.add(new JLabel("Select the attributes you want to track"));
		for(Civilisation c : Configuration.civilisations)
		{
			ArrayList<JCheckBox> civ = new ArrayList<JCheckBox>();
			pane.add(new JLabel(c.getNom()));
			for(String Att : c.getAttributesNames())
			{
				box = new JCheckBox(Att);
				civ.add(box);
				pane.add(box);
			}
			check.put(c.getNom(),civ);
		}
		this.add(new JScrollPane(pane));
	}	
	
	public void toFile(File folder) {
		
		if(folder.exists() && folder.isDirectory())
		{
		PrintWriter out;	
		File objList=null;
		try {
			objList = new File(folder.getPath()+"/"+URLEncoder.encode(DefinePath.ADVStatsWidgetAttributeFilterFile,"UTF-8")+Configuration.getExtension());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			out = new PrintWriter(new FileWriter(objList.getPath()));
			for(String civ : check.keySet())
			{
				for(JCheckBox ck : check.get(civ))
				{
					if(ck.isSelected())
						out.println(civ + "/"+ck.getText());
				}
			}
			out.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	public void loadFromFile(File folder) {
			if(folder.exists() && folder.isDirectory())
			{
			File objFilter=null;
			try {
				objFilter = new File(folder.getPath()+"/"+URLEncoder.encode(DefinePath.ADVStatsWidgetAttributeFilterFile,"UTF-8")+Configuration.getExtension());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if(objFilter.exists()&&objFilter.isFile())
			{
				 Scanner scanner = null;
				try {
					scanner = new Scanner(new FileReader(objFilter));
					 String str;
					 while (scanner.hasNextLine()) {
					     str = scanner.nextLine();
					     StringTokenizer stok = new StringTokenizer(str, "/");
					     String civ = stok.nextToken();
					     String att = stok.nextToken();
					     for(JCheckBox ck : check.get(civ))
					     {
					    	 if(ck.getText().equals(att))
					    		 ck.setSelected(true);
					     }
							
					 }			
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally{
				scanner.close();
				}
			}
		}	
		}
}