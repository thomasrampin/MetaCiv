package civilisation.inspecteur;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import madkit.gui.menu.LaunchAgentsMenu;
import madkit.kernel.AbstractAgent;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.SubCategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.KeyToGroupMap;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.StandardGradientPaintTransformer;

import turtlekit.kernel.Turtle;
import civilisation.CivLauncher;
import civilisation.Civilisation;
import civilisation.Configuration;
import civilisation.DefineConstants;
import civilisation.SchemaCognitif;
import civilisation.individu.Human;
import civilisation.individu.plan.NPlan;
import civilisation.individu.plan.NPlanPondere;
import civilisation.inspecteur.viewer.ViewerTabbed;
import civilisation.stats.AdvancedStatsWindows;
import civilisation.world.World;

public class PanelCharts extends JPanel {

	JComboBox<Civilisation> civilisations;

	HashMap<String, Integer> mapping;

	ArrayList<ChartPanel> chartPanel;
	ArrayList<JFreeChart> chart;
	ArrayList<DefaultCategoryDataset> dataset;

	ArrayList<ChartPanel> chartPanelPopulation;
	ArrayList<JFreeChart> chartPopulation;
	ArrayList<XYSeriesCollection> datasetPopulation;
	JButton advancedCharts = new JButton("Advanced");
	
	BoxLayout layout;
	
	int selectedIndex = 0;

	public PanelCharts() {
		layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);
		mapping = new HashMap<String, Integer>();

		chartPanel = new ArrayList<ChartPanel>();
		chart = new ArrayList<JFreeChart>();
		dataset = new ArrayList<DefaultCategoryDataset>();

		chartPanelPopulation = new ArrayList<ChartPanel>();
		chartPopulation = new ArrayList<JFreeChart>();
		datasetPopulation = new ArrayList<XYSeriesCollection>();
		Civilisation[] civ = new Civilisation[Configuration.civilisations.size()];
		civilisations = new JComboBox<Civilisation>(Configuration.civilisations.toArray(civ));

		for (int i = 0; i < Configuration.civilisations.size(); i++) {
			mapping.put(Configuration.civilisations.get(i).getNom(), i);
			addCivilisationMapped(Configuration.civilisations.get(i), i);
		}

		civilisations.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				changeDisplayedCiv();
			}
		});
		this.add(civilisations);
		
		advancedCharts.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Configuration.viewer.launchAgent(new AdvancedStatsWindows());
			}
		});
		this.add(advancedCharts);
		civilisations.setSelectedIndex(selectedIndex);
		this.add(chartPanel.get(mapping.get(((Civilisation) civilisations
				.getSelectedItem()).getNom())));
		this.add(chartPanelPopulation.get(mapping
				.get(((Civilisation) civilisations.getSelectedItem()).getNom())));

	}

	public void changeDisplayedCiv()
	{
		this.remove(chartPanel.get(selectedIndex));
		this.remove(chartPanelPopulation.get(selectedIndex));
		selectedIndex = mapping.get(((Civilisation)civilisations.getSelectedItem()).getNom());
		this.add(chartPanel.get(selectedIndex));
		this.add(chartPanelPopulation.get(selectedIndex));
		this.revalidate();
		this.repaint();
	}
	
	public void addCivilisation(Civilisation c) {
		mapping.put(c.getNom(), Configuration.civilisations.size());
		addCivilisationMapped(c, Configuration.civilisations.size());
	}

	public void addCivilisationMapped(Civilisation c, int i) {

		dataset.add(i, createDataset());
		chart.add(i, createChart(dataset.get(i)));
		chartPanel.add(i, new ChartPanel(chart.get(i)));
		chartPanel.get(i).setPreferredSize(new java.awt.Dimension(590, 350));

		datasetPopulation.add(i, this.createDatasetPopulation());
		chartPopulation.add(i, createChartPopulation(datasetPopulation.get(i)));
		chartPanelPopulation.add(i, new ChartPanel(chartPopulation.get(i)));
		chartPanelPopulation.get(i).setPreferredSize(
				new java.awt.Dimension(500, 270));
	}

	/*
	 * private JFreeChart createChartAttributes() {
	 * 
	 * // create subplot 1... final XYDataset data1 = datasetAttributes; final
	 * XYItemRenderer renderer1 = new StandardXYItemRenderer(); final NumberAxis
	 * rangeAxis1 = new NumberAxis("Range 1"); final XYPlot subplot1 = new
	 * XYPlot(data1, null, rangeAxis1, renderer1);
	 * subplot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);
	 * 
	 * 
	 * // parent plot... final CombinedDomainXYPlot plot = new
	 * CombinedDomainXYPlot(new NumberAxis("Domain")); plot.setGap(10.0);
	 * 
	 * // add the subplots... plot.add(subplot1, 1);
	 * plot.setOrientation(PlotOrientation.VERTICAL);
	 * 
	 * // return a new chart containing the overlaid plot... return new
	 * JFreeChart("Attributes plot", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
	 * }
	 */
	private JFreeChart createChartPopulation(XYDataset inData) {

		// create subplot 1...
		final XYDataset data1 = inData;
		final XYItemRenderer renderer1 = new StandardXYItemRenderer();
		final NumberAxis rangeAxis1 = new NumberAxis("Range 1");
		final XYPlot subplot1 = new XYPlot(data1, null, rangeAxis1, renderer1);
		subplot1.setRangeAxisLocation(AxisLocation.BOTTOM_OR_LEFT);

		// parent plot...
		final CombinedDomainXYPlot plot = new CombinedDomainXYPlot(
				new NumberAxis("Domain"));
		plot.setGap(10.0);

		// add the subplots...
		plot.add(subplot1, 1);
		plot.setOrientation(PlotOrientation.VERTICAL);

		// return a new chart containing the overlaid plot...
		return new JFreeChart("Population", JFreeChart.DEFAULT_TITLE_FONT,
				plot, true);
	}

	private XYSeriesCollection createDatasetAttributes() {

		XYSeriesCollection collection = new XYSeriesCollection();

		for (String attr : Configuration.getSchemaCognitifEnCourEdition()
				.getAttributesNames()) {
			collection.addSeries(new XYSeries(attr));
		}

		return collection;

        }

	private XYSeriesCollection createDatasetPopulation() {

		XYSeriesCollection collection = new XYSeriesCollection();

		for (Civilisation attr : Configuration.civilisations) {
			//System.out.println(attr.getNom());
			collection.addSeries(new XYSeries(attr.getNom()));
		}

		return collection;

	}

	private JFreeChart createChart(CategoryDataset dataset) {
		final JFreeChart chart = ChartFactory.createStackedBarChart(
				"Plan weight", // chart title
				"Category", // domain axis label
				"Value", // range axis label
				dataset, // data
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

	private DefaultCategoryDataset createDataset() {
		DefaultCategoryDataset result = new DefaultCategoryDataset();
		return result;
	}

	public void updateData() {
		if (World.getInstance().getTick() % 20 == 0) {
			for (Civilisation c : Configuration.civilisations) {
				HashMap<NPlan, Float> planWeight = new HashMap<NPlan, Float>();
				for (NPlan plan : c.getPlans()) {
					planWeight.put(plan, 0.0f);
				}

				for (Turtle turtle : World.getInstance().getTurtlesWithRoles(
						DefineConstants.Role_Human)) {
					Human h = (Human) turtle;

					if (h.getCiv().equals(c)) {
						for (NPlanPondere p : h.getEsprit().getPlans()) {
							/*
							 * System.out.println("TEST");
							 * System.out.println(p.getPlan());
							 * System.out.println(p.getPoids());
							 */
							planWeight
									.put(p.getPlan(),
											p.getPoids()
													+ Math.max(p.getPoids(),
															0.0f));

						}
					}
				}

				float totalWeight = 0;
				for (NPlan plan : c.getPlans()) {
					totalWeight += Math.max(planWeight.get(plan), 0.0f);
				}
				totalWeight /= 100.0f;

				for (NPlan plan : c.getPlans()) {
					dataset.get(mapping.get(c.getNom())).addValue(
							Math.max(planWeight.get(plan) / totalWeight, 0.0f),
							plan.getNom(),
							String.valueOf(World.getInstance().getTick()));
				}
			}
		}
		
		if (World.getInstance().getTick() % 15 == 0
				|| World.getInstance().getTick() == 3) {

			HashMap<String, Float> populationValues = new HashMap<String, Float>();
			for (Civilisation attr : Configuration.civilisations) {
				populationValues.put(attr.getNom(), 0.0f);
			}

			for (Turtle turtle : World.getInstance().getTurtlesWithRoles(
					DefineConstants.Role_Human)) {
				Human h = (Human) turtle;
				// System.out.println(h.getCiv().getNom());
				if (populationValues.containsKey(h.getCiv().getNom())) {
					populationValues
							.put(h.getCiv().getNom(), (float) (populationValues
									.get(h.getCiv().getNom()) + 1));
				}

			}

			for (Civilisation attr : Configuration.civilisations) {
				datasetPopulation.get(mapping.get(attr.getNom())).getSeries(attr.getNom()).add(
						World.getInstance().getTick(),
						populationValues.get(attr.getNom()));
			}
		}

	}
}