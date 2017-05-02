package civilisation.stats;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.jfree.chart.plot.Pannable;

import turtlekit.viewer.TKDefaultViewer;
import civilisation.Configuration;
import civilisation.DefineConstants;
import civilisation.DefinePath;
import civilisation.inspecteur.PanelCharts;
import civilisation.inspecteur.simulation.GTrigger;
import civilisation.inspecteur.simulation.dialogues.DialogEditGroupOptions;

import com.keypoint.PngEncoder;

public class AdvancedStatsWindows extends TKDefaultViewer {
    int width = 1280;
    int height = 720;

    JTabbedPane tabs = new JTabbedPane();

    /* menu items */
    JMenuItem MI_PROB_PLAN = new JMenuItem("Create Plan Probe");
    JMenuItem MI_PROB_COG = new JMenuItem("Create Cogniton Probe");
    JMenuItem MI_PROB_FAC = new JMenuItem("Create Facility Probe");
    JMenuItem MI_PROB_OBJ = new JMenuItem("Create Object Probe");
    JMenuItem MI_PROB_ATT = new JMenuItem("Create Attribute Probe");
    JMenuItem MI_PROB_POP = new JMenuItem("Create Population Probe");

    JMenuItem MI_TAB_NEW = new JMenuItem("Create new Tab Model");
    JMenuItem MI_TAB_RNAM = new JMenuItem("Rename Tab Model");
    JMenuItem MI_TAB_SAVE = new JMenuItem("Save current Tab Model");
    JMenuItem MI_TAB_LOAD = new JMenuItem("Load Tab Model");
    JMenuItem MI_TAB_SALL = new JMenuItem("Save all Tab Models");
    JMenuItem MI_TAB_DEL = new JMenuItem("Delete current Tab Model");

    public void setupFrame(JFrame frame) {

		/*
         * création d'un dashbord paramétrable avec des "objets"(widget)
		 * 
		 * objet plan weights : options grph/camembert
		 * 
		 * objet cognitons weights : options grph/camembert
		 * 
		 * objet corbes quantités aménagement : par société / par groupes /
		 * moyennes agent
		 * 
		 * objet courbes objets (baies/outils/viande) : par société / par
		 * groupes / moyennes agent / en stock dans aménagements
		 * 
		 * objet courbes attributs agent : par société / par groupes / moyennes
		 * agent / roles
		 * 
		 * objet courbes population : civ/groupes/roles
		 * 
		 * pour touts : groupes/roles affichage part type ou instances
		 * 
		 */
        frame.setSize(width, height + 60);
        frame.setLocation(320, 180);

        JMenuBar mb = new JMenuBar();
        frame.setJMenuBar(mb);
        JMenu fm = new JMenu("Menu");
        mb.add(fm);

        JMenu mTabs = new JMenu("Manage Tabs");
        mb.add(mTabs);

        ASWListener listener = new ASWListener(this);

        MI_TAB_NEW.addActionListener(listener);
        mTabs.add(MI_TAB_NEW);
        MI_TAB_RNAM.addActionListener(listener);
        mTabs.add(MI_TAB_RNAM);
        MI_TAB_LOAD.addActionListener(listener);
        mTabs.add(MI_TAB_LOAD);
        MI_TAB_SAVE.addActionListener(listener);
        mTabs.add(MI_TAB_SAVE);
        MI_TAB_SALL.addActionListener(listener);
        mTabs.add(MI_TAB_SALL);
        MI_TAB_DEL.addActionListener(listener);
        mTabs.add(MI_TAB_DEL);

        JMenu mProb = new JMenu("Add probe");
        mb.add(mProb);

        MI_PROB_PLAN.addActionListener(listener);
        mProb.add(MI_PROB_PLAN);
        MI_PROB_COG.addActionListener(listener);
        mProb.add(MI_PROB_COG);
        MI_PROB_ATT.addActionListener(listener);
        mProb.add(MI_PROB_ATT);
        MI_PROB_FAC.addActionListener(listener);
        mProb.add(MI_PROB_FAC);
        MI_PROB_OBJ.addActionListener(listener);
        mProb.add(MI_PROB_OBJ);
        MI_PROB_POP.addActionListener(listener);
        mProb.add(MI_PROB_POP);

        frame.setContentPane(tabs);

        loadDesktop(new File(DefinePath.pathToRessource + "/" + DefinePath.ADVStatsRootDirectory));
    }

    private void loadDesktop(File folder) {
        if (folder.exists()) {
            File[] tabFiles = folder.listFiles();
            if (tabFiles != null && tabFiles.length > 0) {
                for (File tab : tabFiles) {
                    JDesktopPane jdp = createEmptyTab(tab.getName());
                    loadTabFromFile(jdp, tab);
                }
            } else
                loadDefaultDesktop();
        } else
            loadDefaultDesktop();
    }

    private void loadTabFromFile(JDesktopPane jdp, File tab) {
        File[] wFiles = tab.listFiles();
        if (wFiles != null && wFiles.length > 0) {
            for (File widget : wFiles) {
                addWidgetToTab(ADVSWidgetFactory.loadWidgetFromFile(widget), jdp);
            }
        }
    }

    private void loadDefaultDesktop() {
        createDefaultSimpleTab();
        createDefaultCompleteTab();
        createEmptyTab("Personal Tab");
        /**
         * sauvegarde des tabs par defaut pour eviter la creation des objets à chaque fois
         */
        System.out.println("save all tabs");

        for (int i = 0; i < tabs.getTabCount(); i++) {
            saveTab(tabs.getTitleAt(i), (JDesktopPane) tabs.getComponentAt(i));
        }
    }

    public void observe() {

    }

    private class ASWListener implements ActionListener {
        AdvancedStatsWindows win;

        public ASWListener(AdvancedStatsWindows inwin) {
            win = inwin;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem source = (JMenuItem) e.getSource();

            if (source.equals(MI_PROB_ATT)) {
                System.out.println("create attribute probe");
                AttributeProbeWidget p = new AttributeProbeWidget();
                DialogStatOptions d = new DialogStatOptions(win, true, p);
                d.setVisible(true);
            } else if (source.equals(MI_PROB_COG)) {
                System.out.println("create cogniton probe");
                CognitonProbeWidget p = new CognitonProbeWidget();
                DialogStatOptions d = new DialogStatOptions(win, true, p);
                d.setVisible(true);
            } else if (source.equals(MI_PROB_FAC)) {
                System.out.println("MI_PROB_FAC");

            } else if (source.equals(MI_PROB_OBJ)) {
                System.out.println("create object probe");
                ObjectProbeWidget p = new ObjectProbeWidget();
                DialogStatOptions d = new DialogStatOptions(win, true, p);
                d.setVisible(true);
            } else if (source.equals(MI_PROB_PLAN)) {
                System.out.println("create plan probe");
                PlanProbeWidget p = new PlanProbeWidget();
                DialogStatOptions d = new DialogStatOptions(win, true, p);
                d.setVisible(true);
            } else if (source.equals(MI_PROB_POP)) {
                System.out.println("create population probe");
                PopulationProbeWidget p = new PopulationProbeWidget();
                DialogStatOptions d = new DialogStatOptions(win, true, p);
                d.setVisible(true);
            } else if (source.equals(MI_TAB_NEW)) {
                System.out.println("create new tab");
                String s = (String) JOptionPane.showInputDialog(win.getFrame(), "create new tab",
                        "enter a name for the tab", JOptionPane.PLAIN_MESSAGE, null, null, null);
                if (s != null) {
                    if (win.checkIfTabExists(s))
                        s += "_" + tabs.getTabCount();
                    createEmptyTab(s);
                }

            } else if (source.equals(MI_TAB_SAVE)) {
                System.out.println("save selected tab");
                saveTab(tabs.getTitleAt(tabs.getSelectedIndex()),
                        (JDesktopPane) tabs.getComponentAt(tabs.getSelectedIndex()));
            } else if (source.equals(MI_TAB_SALL)) {
                System.out.println("save all tabs");

                for (int i = 0; i < tabs.getTabCount(); i++) {
                    saveTab(tabs.getTitleAt(i), (JDesktopPane) tabs.getComponentAt(i));
                }

            } else if (source.equals(MI_TAB_LOAD)) {
                System.out.println("MI_TAB_LOAD");
            } else if (source.equals(MI_TAB_RNAM)) {
                System.out.println("MI_TAB_RNAM");

            } else if (source.equals(MI_TAB_DEL)) {
                if (win.tabs.getTabCount() > 0) {
                    File tabFile = new File(DefinePath.pathToRessource + "/" + DefinePath.ADVStatsRootDirectory + "/"
                            + win.tabs.getTitleAt(win.tabs.getSelectedIndex()));
                    deleteFolderRec(tabFile);
                    win.tabs.remove(win.tabs.getSelectedIndex());
                    if (win.tabs.getTabCount() > 0)
                        win.tabs.setSelectedIndex(0);
                }
            }
        }

    }

    public void saveTab(String title, JDesktopPane p) {
        File root = new File(DefinePath.pathToRessource + "/" + DefinePath.ADVStatsRootDirectory);
        root.mkdirs();
        File tabFolder = new File(DefinePath.pathToRessource + "/" + DefinePath.ADVStatsRootDirectory + "/" + title);
        if (tabFolder.exists()) {
            deleteFolderRec(tabFolder);
        }
        tabFolder.mkdirs();

        for (JInternalFrame widget : p.getAllFrames()) {
            ((AbstractProbeWidget) widget).toFile(tabFolder);
        }
    }

    private void deleteFolderRec(File tabFolder) {
        for (File f : tabFolder.listFiles()) {
            if (f.isFile())
                f.delete();
            else
                deleteFolderRec(f);
        }
        tabFolder.delete();
    }

    public boolean checkIfTabExists(String s) {

        for (int i = 0; i < tabs.getTabCount(); i++) {
            if (tabs.getTitleAt(i).equals(s))
                return true;
        }
        return false;
    }

    public void addWidgetToCurrentTab(AbstractProbeWidget cont) {
        JDesktopPane d = (JDesktopPane) tabs.getSelectedComponent();
        addWidgetToTab(cont, d);
    }

    public void addWidgetToTab(AbstractProbeWidget cont, JDesktopPane d) {

        if (cont != null) {
            System.out.println("adding widget");
            for (JInternalFrame intF : d.getAllFrames()) {
                if (intF.equals(cont))
                    return;
            }
            System.out.println(cont.getTitle());
            d.add(cont);
            cont.setVisible(true);
            System.out.println("widget added");
        } else {
            System.out.println("widget is null");
        }
    }

    public JDesktopPane createEmptyTab(String title) {
        JDesktopPane result = new JDesktopPane();
        tabs.add(title, result);
        return result;
    }

    /**
     * Creation d'un panel simple avec les Probs classiques
     *
     * @return
     */
    public JDesktopPane createDefaultSimpleTab() {
        int widthProb = width / 2 - 30;
        int heightProb = height / 2 - 30;

        //création des différentes sondes
        ObjectProbeWidget obj = new ObjectProbeWidget();
        PopulationProbeWidget pop = new PopulationProbeWidget();
        PlanProbeWidget pla = new PlanProbeWidget();
        //configuration par défaut
        pla.getSocFilter().checkTotalInWorld();
        pla.getChartTypeSelector().getPie().setSelected(true);
        pla.applyOptions();
        pop.getSocFilter().checkTotalInWorld();
        pop.applyOptions();
        obj.getSocFilter().checkTotalInWorld();
        obj.getObjFilter().checkAllObjects();
        obj.applyOptions();
        //positionnement des graphes
        obj.setSize(widthProb, heightProb);
        pop.setSize(widthProb, heightProb);
        pla.setSize(widthProb, heightProb);

        pla.setLocation(15, 15);
        int pos = widthProb + 15;
        pop.setLocation(pos, 15);
        pos = heightProb + 15;
        obj.setLocation(15 + widthProb / 2, pos);

        JDesktopPane result = new JDesktopPane();
        addWidgetToTab(obj, result);
        addWidgetToTab(pop, result);
        addWidgetToTab(pla, result);
        tabs.add("Default Simple Tab", result);
        return result;
    }

    /**
     * Création d'un panel plus complet avec toutes les probs possibles
     *
     * @return
     */
    public JDesktopPane createDefaultCompleteTab() {
        int widthProb = width / 3 - 30;
        int heightProb = height / 2 - 30;

        //crétaion des sondes
        ObjectProbeWidget obj = new ObjectProbeWidget();
        PopulationProbeWidget pop = new PopulationProbeWidget();
        PlanProbeWidget pla = new PlanProbeWidget();
        AttributeProbeWidget att = new AttributeProbeWidget();
        CognitonProbeWidget cog = new CognitonProbeWidget();
        //configuration
        pla.getSocFilter().checkTotalInWorld();
        pla.getChartTypeSelector().getPie().setSelected(true);
        pla.applyOptions();
        pop.getSocFilter().checkTotalInWorld();
        pop.applyOptions();
        obj.getSocFilter().checkTotalInWorld();
        obj.getObjFilter().checkAllObjects();
        obj.applyOptions();
        att.getSocFilter().checkTotalInWorld();
        att.getAttrFilter().checkAllAttributes();
        att.applyOptions();
        cog.getSocFilter().checkTotalInWorld();
        cog.getChartTypeSelector().getPie().setSelected(true);
        cog.applyOptions();
        //placement des graphes
        obj.setSize(widthProb, heightProb);
        pop.setSize(widthProb, heightProb);
        pla.setSize(widthProb, heightProb);
        att.setSize(widthProb, heightProb);
        cog.setSize(widthProb, heightProb);

        pla.setLocation(15, 15);
        int pos = widthProb + 15;
        pop.setLocation(pos, 15);
        pos += widthProb;
        obj.setLocation(pos, 15);
        pos = heightProb + 15;
        att.setLocation(widthProb / 2 + 15, pos);
        cog.setLocation(widthProb + widthProb / 2 + 15, pos);

        JDesktopPane result = new JDesktopPane();
        addWidgetToTab(obj, result);
        addWidgetToTab(pop, result);
        addWidgetToTab(pla, result);
        addWidgetToTab(att, result);
        addWidgetToTab(cog, result);
        tabs.add("Default Complete Tab", result);
        return result;
    }
}