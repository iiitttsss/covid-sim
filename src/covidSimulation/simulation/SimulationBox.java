package covidSimulation.simulation;

import java.util.ArrayList;

import covidSimulation.Colors;
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
	private final int collectDataEveryNumberOfUpdates = 30; // how many updates before collect data

	private float currentR;
	private float predictedR;
	private float percentSick;


	public SimulationBox(int boxWidth, int boxHeight, int numberOfAgents, int numberOfSickAgents)
	{
		this.setRenderBox(Global.getPro().createGraphics(boxWidth, boxHeight));
		numberOfUpdates = 0;
		this.setStatsCollector(new StatsCollector());

		this.setAgents(new ArrayList<Agent>());

		// add patient zero
		for (int i = 0; i < numberOfSickAgents; i++)
		{
			this.agents.add(new Agent(true, this.getRenderBox().width, this.getRenderBox().height));
		}
		// add healthy agents
		for (int i = 0; i < numberOfAgents - numberOfSickAgents; i++)
		{
			this.agents.add(new Agent(false, this.getRenderBox().width, this.getRenderBox().height));
		}


		this.setCurrentR(0);
		this.setPredictedR(0);

		this.setPercentSick(0);
	}

	public float calculatePercentSick(ArrayList<Agent> infectiousAgents)
	{
		return (float) infectiousAgents.size() / this.agents.size();
	}

	public float calculateAverageNumberOfInteractions()
	{
		int sum = 0;
		for (Agent agent : agents)
		{
			sum += agent.countNumberOfInteractions(agents);
		}
		return (float) sum / agents.size();
	}

	public void postUpdate(ArrayList<Agent> infectiousAgents)
	{
		for (int i = 0; i < agents.size(); i++)
		{
			Agent agent = agents.get(i);
			agent.postUpdate();
		}
		this.setPredictedR(this.calculatePredictedR(infectiousAgents));

		this.setPercentSick(this.calculatePercentSick(infectiousAgents));
	}

	private void update(ArrayList<Agent> infectiousAgents)
	{
		if (this.getNumberOfUpdates() % collectDataEveryNumberOfUpdates == 0)
		{
			this.statsCollector.collectData(this);
		}

		this.setCurrentR(this.calculateCurrentR(infectiousAgents));

		numberOfUpdates++;
	}

	public void updateMove()
	{
		ArrayList<Agent> infectiousAgents = this.getInfectiousAgents();

		for (int i = 0; i < agents.size(); i++)
		{
			Agent agent = agents.get(i);
			agent.update(infectiousAgents, this.getRenderBox().width, this.getRenderBox().height,
					this.getPercentSick());
		}

		this.update(infectiousAgents);
		this.postUpdate(infectiousAgents);
	}

	public void updateRoom()
	{
		ArrayList<Agent> infectiousAgents = this.getInfectiousAgents();

		for (int i = 0; i < agents.size(); i++)
		{
			Agent agent = agents.get(i);
			agent.updateInfections(infectiousAgents, this.getPercentSick());
		}

		this.update(infectiousAgents);
		this.postUpdate(infectiousAgents);
	}

	private float calculatePredictedR(ArrayList<Agent> infectiousAgents)
	{
		if (infectiousAgents.size() == 0) // if there are no infectious agents - return 0
		{
			return 0;
		}

		float rSum = 0;

		for (int i = 0; i < infectiousAgents.size(); i++)
		{
			Agent agent = infectiousAgents.get(i);
			rSum += agent.getPredictedR();
		}

		return rSum / infectiousAgents.size();

	}

	private float calculateCurrentR(ArrayList<Agent> infectiousAgents)
	{
		if (infectiousAgents.size() == 0) // if there are no infectious agents - return 0
		{
			return 1;
		}

		float rSum = 0;

		for (int i = 0; i < infectiousAgents.size(); i++)
		{
			Agent agent = infectiousAgents.get(i);
			rSum += agent.getCurrentR();
		}

		return rSum / infectiousAgents.size();
	}

	private ArrayList<Agent> getInfectiousAgents()
	{
		ArrayList<Agent> infectiousAgents = new ArrayList<Agent>();

		for (int i = 0; i < agents.size(); i++)
		{
			Agent agent = agents.get(i);
			if (agent.isInfectious() && !agent.isInIsolation())
			{
				infectiousAgents.add(agent);
			}
		}

		return infectiousAgents;
	}

	public PGraphics render()
	{
		this.pg.beginDraw();
		this.pg.background(Colors.SIMULATION_BACKGROUND_COLOR);

		for (int i = 0; i < agents.size(); i++)
		{
			Agent agent = agents.get(i);
			if (!agent.isInIsolation())
			{
				agent.render(this.getRenderBox());
			}
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

	public float getCurrentR()
	{
		return currentR;
	}

	public void setCurrentR(float currentR)
	{
		this.currentR = currentR;
	}

	public float getPredictedR()
	{
		return predictedR;
	}

	public void setPredictedR(float predictedR)
	{
		this.predictedR = predictedR;
	}

	public float getPercentSick()
	{
		return percentSick;
	}

	public void setPercentSick(float percentSick)
	{
		this.percentSick = percentSick;
	}
}
