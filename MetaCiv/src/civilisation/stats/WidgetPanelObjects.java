package civilisation.stats;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagLayoutInfo;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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

public class WidgetPanelObjects extends JPanel {
    ArrayList<JCheckBox> check;
    JPanel pane = new JPanel();

    public WidgetPanelObjects() {
        check = new ArrayList<JCheckBox>();
        JCheckBox box;
        pane.setLayout(new BoxLayout(pane, BoxLayout.Y_AXIS));
        pane.add(new JLabel("Select the objects you want to track"));
        /**
         box = new JCheckBox("All");
         check.add(box);
         pane.add(box);
         **/
        for (Objet o : Configuration.objets) {
            box = new JCheckBox(o.getNom());
            check.add(box);
            pane.add(box);
        }
        this.add(new JScrollPane(pane));
    }

    public void toFile(File folder) {

        if (folder.exists() && folder.isDirectory()) {
            PrintWriter out;
            File objList = null;
            try {
                objList = new File(folder.getPath() + "/" + URLEncoder.encode(DefinePath.ADVStatsWidgetObjectFilterFile, "UTF-8") + Configuration.getExtension());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                out = new PrintWriter(new FileWriter(objList.getPath()));
                for (JCheckBox ck : check) {
                    if (ck.isSelected())
                        out.println(ck.getText());
                }
                out.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void loadFromFile(File folder) {
        if (folder.exists() && folder.isDirectory()) {
            File objFilter = null;
            try {
                objFilter = new File(folder.getPath() + "/" + URLEncoder.encode(DefinePath.ADVStatsWidgetObjectFilterFile, "UTF-8") + Configuration.getExtension());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (objFilter.exists() && objFilter.isFile()) {
                Scanner scanner = null;
                try {
                    scanner = new Scanner(new FileReader(objFilter));
                    String str;
                    while (scanner.hasNextLine()) {
                        str = scanner.nextLine();
                        for (JCheckBox ck : check) {
                            if (ck.getText().equals(str))
                                ck.setSelected(true);
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    scanner.close();
                }
            }
        }
    }

    public void checkAllObjects(){
        for (JCheckBox jc : check){
            jc.setSelected(true);
        }
    }
}
