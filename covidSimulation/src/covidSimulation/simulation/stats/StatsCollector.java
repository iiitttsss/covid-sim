package covidSimulation.simulation.stats;

import java.util.ArrayList;

import covidSimulation.Colors;
import covidSimulation.Global;
import covidSimulation.simulation.SimulationBox;
import processing.core.PGraphics;

public class StatsCollector
{
	private PGraphics pg;
	private ArrayList<DailyStats> stats;

	public StatsCollector()
	{
		this.setStats(new ArrayList<DailyStats>());
		this.setPg(Global.getPro().createGraphics(800, 400));
	}

	public void collectData(SimulationBox sim)
	{
		DailyStats ds = new DailyStats();
		for (int i = 0; i < sim.getAgents().size(); i++)
		{
			boolean isAgentSick = sim.getAgents().get(i).isSick();
			ds.totalAgents++;
			
			if (isAgentSick)
			{
				ds.sickAgents++;
			}
			else
			{
				ds.healthyAgents++;
			}
		}
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

		float barSize = (float) Math.ceil((float) pg.width / stats.size());

		this.pg.beginShape();

		for (int i = 0; i < stats.size(); i++)
		{
			DailyStats ds = stats.get(i);

			float healthyBarHeight = pg.height * ds.healthyAgents / ds.totalAgents;
			this.pg.noStroke();
			this.pg.fill(Colors.AGENT_COLOR_HEALTHY);
			this.pg.rect(i * barSize, 0, barSize, healthyBarHeight);

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

}
