package covidSimulation.simulation.stats;

import java.util.ArrayList;

import org.gicentre.utils.stat.XYChart;

import covidSimulation.Global;
import processing.core.PVector;

public class Graph
{
	public static void renderWithLib(int x, int y, int w, int h, ArrayList<PVector> values, float maxY)
	{
		XYChart lineChart;
		lineChart = new XYChart(Global.getPro());

		lineChart.setData(values);

		lineChart.showXAxis(true);
		lineChart.showYAxis(true);
		lineChart.setMinY(0);
		lineChart.setMaxY(maxY);

		// lineChart.setYFormat("$###,###"); // Monetary value in $US
		// lineChart.setXFormat("0000"); // Year

		// Symbol colours
		lineChart.setPointColour(Global.getPro().color(180, 50, 50));
		lineChart.setPointSize(5);
		lineChart.setLineWidth(2);

		int change = 20;
		lineChart.draw(x + change, y + 0.5f * change, w - change, h - change);
	}
}
