package covidSimulation.simulation.stats;

import java.util.ArrayList;

import org.gicentre.utils.stat.XYChart;

import covidSimulation.Colors;
import covidSimulation.Global;
import processing.core.PVector;

public class Graph
{
	public static void renderWithLib(int x, int y, int w, int h, ArrayList<PVector> values, float maxY, int lineColor)
	{
		XYChart lineChart;
		lineChart = new XYChart(Global.getPro());

		lineChart.setLineColour(lineColor);
		lineChart.setPointColour(lineColor);
		lineChart.setPointSize(-7);
		lineChart.setLineWidth(3);
		lineChart.setAxisLabelColour(Colors.TEXT_COLOR);

		lineChart.setData(values);

		lineChart.showXAxis(true);
		lineChart.showYAxis(true);
		lineChart.setMinY(0);
		lineChart.setMaxY(maxY);

		// lineChart.setYFormat("$###,###"); // Monetary value in $US
		// lineChart.setXFormat("0000"); // Year



		int change = 20;
		lineChart.draw(x + change, y + 0.5f * change, w - change, h - change);
	}
}
