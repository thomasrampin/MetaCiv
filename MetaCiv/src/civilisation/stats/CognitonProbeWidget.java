package civilisation.stats;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTabbedPane;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import civilisation.Configuration;
import civilisation.DefinePath;
import civilisation.individu.Human;
import civilisation.individu.cognitons.Cogniton;
import civilisation.individu.plan.NPlanPondere;
import civilisation.world.World;

public class CognitonProbeWidget extends AbstractCompositeProbeWidget {

    public CognitonProbeWidget(String title) {
        super(title);
        createSocialFilter();
    }

    public CognitonProbeWidget() {
        super("Cogniton Probe");
        createSocialFilter();
    }

    public JTabbedPane getOptionTab() {
        JTabbedPane t = super.getOptionTab();
        t.add("Social filter", getSocFilter());
        return t;
    }

    public void toFile(File tabFolder) {
        super.toFile(tabFolder);
        File widgetFolder = new File(tabFolder.getAbsolutePath() + "/" + this.getTitle());
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


        for (String ck : chartTypeList) {
            if (ck.equals(WidgetPanelChartType.CTYP_Area)) {
                Double totalWeight = 0.0;
                if (charts.get(statsWatcher.plotName) == null)
                    addChart(statsWatcher.plotName);
                DefaultCategoryDataset target = (DefaultCategoryDataset) ((CategoryPlot) charts.get(statsWatcher.plotName).get(WidgetPanelChartType.CTYP_Area).getPlot()).getDataset();
                HashMap<String, Double> cognitonWeight = new HashMap<String, Double>();
                for (Human h : statsWatcher.getAllHumans()) {
                    for (Cogniton c : h.getEsprit().getCognitons()) {
                        if (cognitonWeight.get(c.cogniton.getNom()) != null) {
                            cognitonWeight.put(c.cogniton.getNom(), cognitonWeight.get(c.cogniton.getNom()) + Math.max(c.getWeigth(), 0.0));
                        } else {
                            cognitonWeight.put(c.cogniton.getNom(), Math.max(c.getWeigth(), 0.0));
                        }
                    }
                }
                for (Double d : cognitonWeight.values())
                    totalWeight += d;

                for (String cog : cognitonWeight.keySet()) {
                    target.addValue(
                            Math.max(cognitonWeight.get(cog) / totalWeight, 0.0f),
                            cog,
                            String.valueOf(World.getInstance().getTick()));
                }
            } else if (ck.equals(WidgetPanelChartType.CTYP_Pie)) {
                Double totalWeight = 0.0;
                if (charts.get(statsWatcher.plotName) == null)
                    addChart(statsWatcher.plotName);
                DefaultPieDataset target = (DefaultPieDataset) ((PiePlot3D) charts.get(statsWatcher.plotName).get(WidgetPanelChartType.CTYP_Pie).getPlot()).getDataset();
                HashMap<String, Double> cognitonWeight = new HashMap<String, Double>();
                for (Human h : statsWatcher.getAllHumans()) {
                    for (Cogniton c : h.getEsprit().getCognitons()) {
                        if (cognitonWeight.get(c.cogniton.getNom()) != null) {
                            cognitonWeight.put(c.cogniton.getNom(), cognitonWeight.get(c.cogniton.getNom()) + Math.max(c.getWeigth(), 0.0));
                        } else {
                            cognitonWeight.put(c.cogniton.getNom(), Math.max(c.getWeigth(), 0.0));
                        }
                    }
                }
                for (Double d : cognitonWeight.values())
                    totalWeight += d;

                target.clear();
                for (String plan : cognitonWeight.keySet()) {
                    target.setValue(plan, Math.max(cognitonWeight.get(plan) / totalWeight, 0.0f));
                }
            }
        }
    }

    @Override
    public void applyOptions() {
        super.applyOptions();
        generateProbesFromSocFilter();
        generateChartsForProbes();
    }

}
