package covidSimulation.simulation;

import java.util.ArrayList;

import covidSimulation.Global;
import covidSimulation.simulation.agents.Agent;
import covidSimulation.simulation.stats.StatsCollector;
import processing.core.PGraphics;

public class SimulationBox
{
	private PGraphics pg;
	private ArrayList<Agent> agents;
	private StatsCollector statsCollector;

	private int numberOfUpdates;
	private final int collectDataEveryNumberOfUpdates = 50; // how many updates before collect data


	public SimulationBox(int boxWidth, int boxHeight)
	{
		this.setRenderBox(Global.getPro().createGraphics(boxWidth, boxHeight));
		numberOfUpdates = 0;
		this.setStatsCollector(new StatsCollector());

		this.setAgents(new ArrayList<Agent>());
		// add healthy agents
		for (int i = 0; i < 1000; i++)
		{
			this.agents.add(new Agent(false, this.getRenderBox().width, this.getRenderBox().height));
		}
		// add sick agents
		for (int i = 0; i < 1; i++)
		{
			this.agents.add(new Agent(true, this.getRenderBox().width, this.getRenderBox().height));
		}
	}

	public void update()
	{
		ArrayList<Agent> infectiousAgents = this.getInfectiousAgents();

		for (int i = 0; i < agents.size(); i++)
		{
			Agent agent = agents.get(i);
			agent.update(this.getRenderBox().width, this.getRenderBox().height, infectiousAgents);
		}

		if (this.getNumberOfUpdates() % collectDataEveryNumberOfUpdates == 0)
		{
			this.statsCollector.collectData(this);
		}
		numberOfUpdates++;
	}

	private ArrayList<Agent> getInfectiousAgents()
	{
		ArrayList<Agent> infectiousAgents = new ArrayList<Agent>();

		for (int i = 0; i < agents.size(); i++)
		{
			Agent agent = agents.get(i);
			if (agent.isInfectious())
			{
				infectiousAgents.add(agent);
			}
		}

		return infectiousAgents;
	}

	public PGraphics render()
	{
		this.pg.beginDraw();
		this.pg.background(0);

		for (int i = 0; i < agents.size(); i++)
		{
			Agent agent = agents.get(i);
			agent.render(this.getRenderBox());
		}
		
		this.pg.endDraw();

		return this.getRenderBox();
	}

	public ArrayList<Agent> getAgents()
	{
		return agents;
	}

	public void setAgents(ArrayList<Agent> agents)
	{
		this.agents = agents;
	}

	public PGraphics getRenderBox()
	{
		return pg;
	}

	public void setRenderBox(PGraphics renderBox)
	{
		this.pg = renderBox;
	}



	public int getNumberOfUpdates()
	{
		return numberOfUpdates;
	}

	public void setNumberOfUpdates(int numberOfUpdates)
	{
		this.numberOfUpdates = numberOfUpdates;
	}

	public StatsCollector getStatsCollector()
	{
		return statsCollector;
	}

	public void setStatsCollector(StatsCollector statsCollector)
	{
		this.statsCollector = statsCollector;
	}
}
