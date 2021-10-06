package multipleSimulations;

import java.util.ArrayList;
import java.util.List;

import covidSimulation.Colors;
import covidSimulation.Global;
import covidSimulation.simulation.SimulationBox;
import covidSimulation.simulation.stats.Graph;
import processing.core.PApplet;
import processing.core.PVector;

public class MultipleSimulationsRunner extends PApplet
{
	private final int numberOfSimulations = 200;
	private final int numberOfIterations = 25000;

	private ArrayList<PVector> averageSimData;
	private int numberOfAgents = 800;
	private int numberOfSickAgents = 0;
	private final int simSize = 1100;

	public static void main(String[] args)
	{
		PApplet.main(new String[] { MultipleSimulationsRunner.class.getName() });
	}

	@Override
	public void settings()
	{
		size(3200, 1400);
		this.noSmooth();
	}

	@Override
	public void setup()
	{

		System.out.println("start program");
		Global.setPro(this);
		ArrayList<PVector>[] simData = new ArrayList[numberOfSimulations];

		for (int i = 0; i < simData.length; i++)
		{
			simData[i] = this.createSimulationData(this.numberOfIterations);
			float currentTime = millis() / 1000f / 60f;
			float estimatedTime = (currentTime * simData.length / (i + 1));
			float remainingTime = estimatedTime - currentTime;
			System.out.println("sim " + (i + 1) + " out of " + simData.length + ". estimated time = " + estimatedTime
					+ " min. time remaining = " + remainingTime + " min.");
		}
		this.averageSimData = this.calculateAverageSimulationData(simData, this.numberOfIterations);
		System.out.println("done setup");
		System.out.println(Global.getPro().millis());
		System.out.println(millis() / 1000f / 60f);

	}

	@Override
	public void draw()
	{
		this.render();
		noLoop();
	}

	private ArrayList<PVector> createSimulationData(int numberOfIterations)
	{
		SimulationBox sim = new SimulationBox(simSize, simSize, numberOfAgents, numberOfSickAgents);
		for (int i = 0; i < numberOfIterations; i++)
		{
			sim.updateMove();
		}
		// return sim.getStatsCollector().getNumberOfInteractions();
		return sim.getStatsCollector().getPredictedRAverage();
		// return sim.getStatsCollector().getSickAgentsAverage();

	}


	private ArrayList<PVector> calculateAverageSimulationData(ArrayList<PVector>[] simData, int numberOfIterations)
	{
		ArrayList<PVector> averageData = new ArrayList<PVector>();
		int[] counters = new int[simData.length];
		for(int i = 0; i < numberOfIterations; i++)
		{
			float sum = 0;
			for(int s = 0; s < simData.length; s++)
			{
				sum += simData[s].get(counters[s]).y;
				if (simData[s].get(counters[s]).x == i)
				{
					counters[s]++;
				}

			}
			sum /= simData.length;
			averageData.add(new PVector(i, sum));
		}
		return averageData;
	}


	private void render()
	{
		background(Colors.SIMULATION_BACKGROUND_COLOR);
		//
		Graph.renderWithLib(150, 150, simSize, simSize / 2, this.averageSimData, Float.NaN, Colors.AGENT_COLOR_SICK);

	}


}
