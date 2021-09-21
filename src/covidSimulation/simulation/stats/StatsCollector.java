package covidSimulation.simulation.stats;

import java.util.ArrayList;

import covidSimulation.Colors;
import covidSimulation.Global;
import covidSimulation.simulation.SimulationBox;
import processing.core.PGraphics;
import processing.core.PVector;

public class StatsCollector
{
	private PGraphics pg;
	private ArrayList<DailyStats> stats;

	private ArrayList<PVector> totalAgents;
	private ArrayList<PVector> sickAgents;
	private ArrayList<PVector> healthyAgents;

	private ArrayList<PVector> currentR;
	private ArrayList<PVector> predictedR;
	private ArrayList<PVector> realR;

	private ArrayList<PVector> numberOfInteractions;


	public StatsCollector()
	{
		this.setStats(new ArrayList<DailyStats>());
		this.setPg(Global.getPro().createGraphics(800, 400));

		this.setTotalAgents(new ArrayList<PVector>());
		this.setSickAgents(new ArrayList<PVector>());
		this.setHealthyAgents(new ArrayList<PVector>());

		this.setCurrentR(new ArrayList<PVector>());
		this.setPredictedR(new ArrayList<PVector>());
		this.setRealR(new ArrayList<PVector>());

		this.setNumberOfInteractions(new ArrayList<PVector>());


	}

	private void addVector(ArrayList<PVector> arr, PVector value)
	{
		if (arr.size() == 0)
		{
			arr.add(value);
			return;
		}

		PVector lastItem = arr.get(arr.size() - 1);
		if (lastItem.y == value.y)
		{
			if (lastItem.x == 0)
			{
				arr.add(value);
			}
			else
			{
				lastItem.x = value.x;
			}
		}
		else
		{
			arr.add(value);
		}
	}

	public void collectData(SimulationBox sim)
	{
		PVector current = new PVector();
		current.x = sim.getNumberOfUpdates();
		
		PVector totalAgents = current.copy();
		PVector sickAgents = current.copy();
		PVector healthyAgents = current.copy();

		PVector currentR = current.copy();
		PVector predictedR = current.copy();
		PVector realR = current.copy();

		PVector numberOfInteractions = current.copy();


		totalAgents.y = sim.getAgents().size();
		for (int i = 0; i < sim.getAgents().size(); i++)
		{
			boolean isAgentSick = sim.getAgents().get(i).isSick();
			
			if (isAgentSick)
			{
				sickAgents.y++;
			}
			else
			{
				healthyAgents.y++;

			}
		}

		currentR.y = sim.getCurrentR();
		predictedR.y = sim.getPredictedR();

		numberOfInteractions.y = sim.calculateAverageNumberOfInteractions();

		this.addVector(this.getTotalAgents(), totalAgents);
		this.addVector(this.getSickAgents(), sickAgents);
		this.addVector(this.getHealthyAgents(), healthyAgents);

		this.addVector(this.getCurrentR(), currentR);
		this.addVector(this.getPredictedR(), predictedR);
		this.addVector(this.getCurrentR(), realR);

		this.addVector(getNumberOfInteractions(), numberOfInteractions);

	}

	public void collectDataOld(SimulationBox sim)
	{
		DailyStats ds = new DailyStats();
		PVector current = new PVector();
		current.x = sim.getNumberOfUpdates();
		
		for (int i = 0; i < sim.getAgents().size(); i++)
		{
			boolean isAgentSick = sim.getAgents().get(i).isSick();
			ds.totalAgents++;
			
			if (isAgentSick)
			{
				ds.sickAgents++;
				current.y++;
			}
			else
			{
				ds.healthyAgents++;
			}
		}
		
		
		ds.currentR = sim.getCurrentR();
		ds.predictedR = sim.getPredictedR();
		this.getSickAgents().add(current);
		this.stats.add(ds);
	}


	public PGraphics render()
	{
		this.pg.beginDraw();
		this.pg.background(Colors.AGENT_COLOR_HEALTHY);

		if (stats.size() == 0)
		{
			this.pg.endDraw();
			return this.pg;
		}

		float barSize = (float) pg.width / stats.size();
		// float barSize = (float) Math.ceil((float) pg.width / stats.size());

		this.pg.beginShape();

		for (int i = 0; i < stats.size(); i++)
		{
			DailyStats ds = stats.get(i);

			float healthyBarHeight = pg.height * ds.healthyAgents / ds.totalAgents;
			this.pg.noStroke();
			// this.pg.fill(Colors.AGENT_COLOR_HEALTHY);
			// this.pg.rect(i * barSize, 0, barSize, healthyBarHeight);

			// float sickBarHeight = pg.height * ds.sickAgents / ds.totalAgents;
			this.pg.noStroke();
			this.pg.fill(Colors.AGENT_COLOR_SICK);
			// this.pg.rect(i * barSize, healthyBarHeight, barSize, sickBarHeight);
			this.pg.vertex((i + 1) * barSize, healthyBarHeight);

		}
		this.pg.vertex(this.pg.width, this.pg.height);
		this.pg.vertex(0, this.pg.height);

		this.pg.endShape();

		this.pg.endDraw();
		return this.pg;
	}

	public ArrayList<DailyStats> getStats()
	{
		return stats;
	}

	public void setStats(ArrayList<DailyStats> stats)
	{
		this.stats = stats;
	}

	public PGraphics getPg()
	{
		return pg;
	}

	public void setPg(PGraphics pg)
	{
		this.pg = pg;
	}

	public ArrayList<PVector> getSickAgents()
	{
		return sickAgents;
	}

	public void setSickAgents(ArrayList<PVector> sickAgents)
	{
		this.sickAgents = sickAgents;
	}

	public ArrayList<PVector> getHealthyAgents()
	{
		return healthyAgents;
	}

	public void setHealthyAgents(ArrayList<PVector> healthyAgents)
	{
		this.healthyAgents = healthyAgents;
	}

	public ArrayList<PVector> getTotalAgents()
	{
		return totalAgents;
	}

	public void setTotalAgents(ArrayList<PVector> totalAgents)
	{
		this.totalAgents = totalAgents;
	}

	public ArrayList<PVector> getCurrentR()
	{
		return currentR;
	}

	public void setCurrentR(ArrayList<PVector> currentR)
	{
		this.currentR = currentR;
	}

	public ArrayList<PVector> getPredictedR()
	{
		return predictedR;
	}

	public void setPredictedR(ArrayList<PVector> predictedR)
	{
		this.predictedR = predictedR;
	}

	public ArrayList<PVector> getRealR()
	{
		return realR;
	}

	public void setRealR(ArrayList<PVector> realR)
	{
		this.realR = realR;
	}

	public ArrayList<PVector> getNumberOfInteractions()
	{
		return numberOfInteractions;
	}

	public void setNumberOfInteractions(ArrayList<PVector> numberOfInteractions)
	{
		this.numberOfInteractions = numberOfInteractions;
	}

}
