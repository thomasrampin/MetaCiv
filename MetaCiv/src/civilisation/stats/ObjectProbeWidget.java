package civilisation.stats;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import civilisation.Configuration;
import civilisation.DefinePath;
import civilisation.individu.Human;
import civilisation.world.World;

public class ObjectProbeWidget extends AbstractXYChartProbeWidget {


    WidgetPanelObjects objPanel;

    public ObjectProbeWidget() {
        super("Object Probe");
        createSocialFilter();
        createObjectFilter();
        createEmptyXYChart();
    }

    public ObjectProbeWidget(String title) {
        super(title);
        createSocialFilter();
        createObjectFilter();
        createEmptyXYChart();
    }


    public JTabbedPane getOptionTab() {
        JTabbedPane t = super.getOptionTab();
        t.add("Social filter", getSocFilter());
        t.add("Object filter", getObjFilter());
        return t;
    }

    public WidgetPanelObjects getObjFilter() {
        return objPanel;
    }

    public void toFile(File tabFolder) {
        super.toFile(tabFolder);
        File widgetFolder = new File(tabFolder.getAbsolutePath() + "/" + this.getTitle());
        objPanel.toFile(widgetFolder);
        PrintWriter out;
        File header = null;
        try {
            header = new File(widgetFolder.getPath() + "/" + URLEncoder.encode(DefinePath.ADVStatsWidgetHeaderFile, "UTF-8") + Configuration.getExtension());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            out = new PrintWriter(new FileWriter(header.getPath(), true));
            out.println("type : " + this.getClass());
            out.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }

    }

    public void updateFromProbe(StatsWatcher statsWatcher) {

        for (JCheckBox ck : objPanel.check) {
            if (ck.isSelected()) {
                XYSeries target = null;
                for (XYSeries x : ((List<XYSeries>) dataset.getSeries())) {
                    if (x.getKey().equals(statsWatcher.plotName + " - " + ck.getText()))
                        target = x;
                }
                if (target == null) {
                    target = new XYSeries(statsWatcher.plotName + " - " + ck.getText());
                    dataset.addSeries(target);
                }
                int total = 0;
                for (Human h : statsWatcher.getAllHumans()) {
                    total += h.getInventaire().possede(Configuration.getObjetByName(ck.getText()));
                }

                target.add(World.getInstance().getTick(), total);
            }
        }
    }

    @Override
    public void applyOptions() {
        super.applyOptions();
        generateProbesFromSocFilter();
    }

    private void createObjectFilter() {
        objPanel = new WidgetPanelObjects();
    }

}
