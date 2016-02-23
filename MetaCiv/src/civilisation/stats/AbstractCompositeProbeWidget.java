package civilisation.stats;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

import civilisation.Configuration;
import civilisation.DefinePath;
import civilisation.individu.plan.NPlan;

public class AbstractCompositeProbeWidget extends AbstractProbeWidget {
	JComboBox<String> selector = new JComboBox<String>();
	HashMap<String, HashMap<String,JFreeChart>> charts = new HashMap<String, HashMap<String,JFreeChart>>();
	ArrayList <String> chartTypeList = new ArrayList<String>();
	int selectedChartType = 0;
	WidgetPanelChartType chartTypeFilter;
    
    ChartPanel chartPanel = new ChartPanel(null);
    
	
	public AbstractCompositeProbeWidget(String title) {
		super(title);
		this.add(chartPanel);
		
		selector.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chartPanel.setChart(charts.get(selector.getSelectedItem()).get(chartTypeList.get(selectedChartType)));
			}
		});
		this.add(selector,BorderLayout.NORTH);
		
		JButton chartTypeBTN =new JButton("switch chart type");
		chartTypeBTN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cycleChartType();
			}
		});
		
		optionsBar.add(chartTypeBTN);
		chartTypeFilter = new WidgetPanelChartType();
		}

	protected void changeLegendState() {
	
	}
	
	public JTabbedPane getOptionTab() {
		JTabbedPane t = super.getOptionTab();
		t.add("Chart Type", getChartTypeSelector());
		return t;
	}
	
	private WidgetPanelChartType getChartTypeSelector() {
		return chartTypeFilter;
	}

	protected void generateChartsForProbes()
	{
		chartTypeList.clear();
		for(JCheckBox ck: chartTypeFilter.check)
		{
			if(ck.isSelected())
			{
				chartTypeList.add(ck.getText());
			}
		}
		if(chartTypeList.size() == 0)
			selectedChartType = 0;
		else
			selectedChartType = selectedChartType % chartTypeList.size();
				/*
		
		for(String W : humanProbes.keySet())
		{
			HashMap<String, JFreeChart> col = charts.get(W);
			for(JCheckBox ck: chartTypeFilter.check)
			{
				if(ck.isSelected())
				{
					if(col == null)
					{
						col = new HashMap<String, JFreeChart>();
						col.put(ck.getText(), allocChartFromType(ck.getText()));
					}
					else if(col.get(ck.getText()) == null)
						col.put(ck.getText(), allocChartFromType(ck.getText()));
				}
				else
				{
					if(col != null)
						col.remove(ck.getText());
				}
			}
			if(col != null)
			{
				charts.put(W, col);
				if(((DefaultComboBoxModel<String>)selector.getModel()).getIndexOf(W)==-1)
					selector.addItem(W);
			}
		}
		if(selector.getItemCount() >0)
			selector.setSelectedIndex(0);
	*/
	}
	
	private JFreeChart allocChartFromType(String text) {
		JFreeChart result = null;
		if(text.equals(WidgetPanelChartType.CTYP_Area))
		{
			result = allocDefaultAreaChart();
		}
		else if(text.equals(WidgetPanelChartType.CTYP_Pie))
		{
			result = allocDefaultPieChart();			
		}
		
		return result;
	}

	protected void addChart(String title)
	{
		HashMap<String, JFreeChart> col = charts.get(title);
		for(JCheckBox ck: chartTypeFilter.check)
		{
			if(ck.isSelected())
			{
				if(col == null)
				{
					col = new HashMap<String, JFreeChart>();
					col.put(ck.getText(), allocChartFromType(ck.getText()));
				}
				else if(col.get(ck.getText()) == null)
					col.put(ck.getText(), allocChartFromType(ck.getText()));
			}
			else
			{
				if(col != null)
					col.remove(ck.getText());
			}
		}
		if(col != null)
		{
			charts.put(title, col);
			if(((DefaultComboBoxModel<String>)selector.getModel()).getIndexOf(title)==-1)
				selector.addItem(title);
		}
		/*if(selector.getItemCount() >0)
			selector.setSelectedIndex(0);*/
	}
	
	private JFreeChart allocDefaultPieChart() {
		JFreeChart chart = ChartFactory.createPieChart3D(null,          // chart title
		new DefaultPieDataset(),                // data
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

	private JFreeChart allocDefaultAreaChart() {
		final JFreeChart chart = ChartFactory.createStackedBarChart(
		null, // chart title
		"Ticks", // domain axis label
		"Value", // range axis label
		new DefaultCategoryDataset(), // data
		PlotOrientation.VERTICAL, // the plot orientation
		true, // legend
		true, // tooltips
		false // urls
		);

		GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
		KeyToGroupMap map = new KeyToGroupMap("G");
		for (NPlan plan : Configuration.getSchemaCognitifEnCourEdition()
				.getPlans()) {
			map.mapKeyToGroup(plan.getNom(), "G");
		}

		renderer.setSeriesToGroupMap(map);
		renderer.setBarPainter(new StandardBarPainter());
		renderer.setItemMargin(0.0f);

		SubCategoryAxis domainAxis = new SubCategoryAxis("Tick");

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setDomainAxis(domainAxis);
		plot.setRenderer(renderer);
		return chart;
	}
	
	public void applyOptions()
	{
		super.applyOptions();
	}
	
	protected void cycleChartType()
	{
		if(chartTypeList.size() == 0)
			selectedChartType = 0;
		else
			selectedChartType = (selectedChartType+1) % chartTypeList.size();
		chartPanel.setChart(charts.get(selector.getSelectedItem()).get(chartTypeList.get(selectedChartType)));
	}
	
	@Override
	public void toFile(File tabFolder) {
		super.toFile(tabFolder);
		File widgetFolder = new File(tabFolder.getAbsolutePath()+"/"+this.getTitle());
		
		System.out.println(this.getClass());
		if(chartTypeFilter != null)
		{
			chartTypeFilter.toFile(widgetFolder);
		}
		
		}
}
