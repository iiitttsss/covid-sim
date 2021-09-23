package covidSimulation;


import covidSimulation.simulation.SimulationBox;
import covidSimulation.simulation.stats.Graph;
import processing.core.*;

public class Main extends PApplet
{

	private SimulationBox sim;
	int numberOfAgents = 1000;
	int numberOfSickAgents = 0;
	final int simSize = 1100;


	public static void main(String[] args)
	{
		PApplet.main(new String[] { Main.class.getName() });
	}

	@Override
	public void settings()
	{
		size(3200, 1500);
		// size(1600, 1000);

	}

	@Override
	public void setup()
	{
		System.out.println("start program");
		Global.setPro(this);
		sim = new SimulationBox(simSize, simSize, numberOfAgents, numberOfSickAgents);
	}

	@Override
	public void draw()
	{
		update();
		render();

		// if there are no more sick agents - stop the simulation
		if (sim.getStatsCollector().getSickAgents().get(sim.getStatsCollector().getSickAgents().size() - 1).y == 0
				&& sim.getNumberOfUpdates() > 3000)
		{
			noLoop();
			System.out.println("simulation stopped - no more sick agents");
		}
	}

	private void update()
	{
		for (int i = 0; i < 2; i++)
		{
			sim.updateMove();
			// sim.updateRoom();
		}
	}

	private void render()
	{
		background(150);

		image(sim.render(), 0, 0);
		frame(0, 0, simSize, simSize);

		// image(sim.getStatsCollector().render(), 800, 0);

		textSize(10);
		Graph.renderWithLib(simSize, 0, simSize, simSize / 2, sim.getStatsCollector().getSickAgents(), Float.NaN);
		frame(simSize, 0, simSize, simSize / 2);
		
		Graph.renderWithLib(simSize, simSize / 2, simSize, simSize / 2, sim.getStatsCollector().getPredictedR(), 10);
		// Graph.renderWithLib(800, 400, 800, 400,
		// sim.getStatsCollector().getCurrentR(), 10);



		textSize(100);
		text(sim.getCurrentR(), simSize, simSize + 70);
		text(sim.getPredictedR(), simSize, simSize + 170);

	}

	private void frame(int x, int y, int w, int h)
	{
		int off = 0;
		noFill();
		stroke(255);
		strokeWeight(5);
		rect(x + off, y + off, w - off, h - off);
	}
}
