package civilisation.inspecteur;

import java.awt.Color;
import java.awt.GradientPaint;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.Rotation;

import I18n.I18nList;

import civilisation.Configuration;
import civilisation.individu.Human;
import civilisation.inventaire.Objet;
import civilisation.world.World;

/**
 * Show advanced informations about the agent using jFreeChart
 * @author Julien Nigon
 *
 */


public class PanelAgentData extends JPanel{
	
	Human h;
	
	ChartPanel chartPanelAttributes;
	JFreeChart chartAttributes;
	XYSeriesCollection datasetAttributes;
	
	ChartPanel chartPanelObjets;
	JFreeChart chartObjets;
	XYSeriesCollection datasetObjets;
	
	public PanelAgentData(Human h) {
		this.h = h;
		
		createDataset();

        
        chartObjets = createChartObjets();
        chartPanelObjets = new ChartPanel(chartObjets);
        chartPanelObjets.setPreferredSize(new java.awt.Dimension(500, 270));
        this.add(chartPanelObjets);
        
        chartAttributes = createChartAttributes();
        chartPanelAttributes = new ChartPanel(chartAttributes);
        chartPanelAttributes.setPreferredSize(new java.awt.Dimension(500, 270));
        this.add(chartPanelAttributes);
	}
	


    private JFreeChart createChartObjets() {
    	final XYDataset data1 = datasetObjets;
        final XYItemRenderer renderer1 = new StandardXYItemRenderer();
        final NumberAxis rangeAxis1 = new NumberAxis("Range 1");
        final XYPlot subplot1 = new XYPlot(data1, null, rangeAxis1, renderer1);
        subplot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);


        // parent plot...
        final CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new NumberAxis("Domain"));
        plot.setGap(10.0);
        
        // add the subplots...
        plot.add(subplot1, 1);
        plot.setOrientation(PlotOrientation.VERTICAL);

        // return a new chart containing the overlaid plot...
        return new JFreeChart("Objets",
                              JFreeChart.DEFAULT_TITLE_FONT, plot, true);
	}



	private JFreeChart createChartAttributes() {
    	 // create subplot 1...
        final XYDataset data1 = datasetAttributes;
        final XYItemRenderer renderer1 = new StandardXYItemRenderer();
        final NumberAxis rangeAxis1 = new NumberAxis("Range 1");
        final XYPlot subplot1 = new XYPlot(data1, null, rangeAxis1, renderer1);
        subplot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);


        // parent plot...
        final CombinedDomainXYPlot plot = new CombinedDomainXYPlot(new NumberAxis("Domain"));
        plot.setGap(10.0);
        
        // add the subplots...
        plot.add(subplot1, 1);
        plot.setOrientation(PlotOrientation.VERTICAL);

        // return a new chart containing the overlaid plot...
        return new JFreeChart("Attributes",
                              JFreeChart.DEFAULT_TITLE_FONT, plot, true);
	}



	private  void createDataset() {

    	XYSeriesCollection collection = new XYSeriesCollection();
          
      	for (String attr : h.getAttr().keySet()) {
      		collection.addSeries(new XYSeries(attr));
      	}
      	this.datasetAttributes = collection;
      	
      	XYSeriesCollection collection2 = new XYSeriesCollection();
        
      	for (Objet attr : Configuration.objets) {
      		collection2.addSeries(new XYSeries(attr.getNom()));     		
      	}
      	this.datasetObjets = collection2;
    }
    
    
    public void updateData() {

    	
    	for(String attr : h.getAttr().keySet())
    	{
    		datasetAttributes.getSeries(attr).add(World.getInstance().getTick(), h.getAttr().get(attr));
    	}
    	
    	for (String attr : h.getInventaire().getListeObjets().keySet()) {
    		datasetObjets.getSeries(attr).add(World.getInstance().getTick(), h.getInventaire().possede(Configuration.getObjetByName(attr)));
      	}
   }
    
/**
     * Creates a chart
     */

    private JFreeChart createChart(PieDataset dataset, String title) {
        
        JFreeChart chart = ChartFactory.createPieChart3D(title,          // chart title
            dataset,                // data
            false,                   // include legend
            true,
            false);

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.75f);
        plot.setBackgroundPaint(this.getBackground());
        return chart;
        
    }
    
    private JFreeChart createChartPlanUse(final CategoryDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
        		I18nList.CheckLang("Bar Chart Demo"),         // chart title
        		I18nList.CheckLang("Category"),               // domain axis label
        		I18nList.CheckLang("Value"),                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            false,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );


        chart.setBackgroundPaint(Color.white);

        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        
        final GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f, Color.blue, 
            0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.green, 
            0.0f, 0.0f, Color.lightGray
        );
        final GradientPaint gp2 = new GradientPaint(
            0.0f, 0.0f, Color.red, 
            0.0f, 0.0f, Color.lightGray
        );
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        
        return chart;
        
    }
}
