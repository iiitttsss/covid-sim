package covidSimulation;


import covidSimulation.simulation.SimulationBox;
import covidSimulation.simulation.stats.Graph;
import processing.core.*;

public class Main extends PApplet
{

	private SimulationBox sim;
	int numberOfAgents = 800;
	int numberOfSickAgents = 0;
	final int simSize = 1100;


	public static void main(String[] args)
	{
		PApplet.main(new String[] { Main.class.getName() });
	}

	@Override
	public void settings()
	{
		size(3200, 1400);
		this.noSmooth();

		// size(1600, 1000);

	}

	@Override
	public void setup()
	{
		frameRate(400);

		System.out.println("start program");
		Global.setPro(this);
		sim = new SimulationBox(simSize, simSize, numberOfAgents, numberOfSickAgents);
//		for (int i = 0; i < 1000; i++)
//		{
//			sim.updateMove();
//		}
	}

	@Override
	public void draw()
	{
		update();
		render();



		// if there are no more sick agents - stop the simulation
		if (sim.getStatsCollector().getSickAgents().get(sim.getStatsCollector().getSickAgents().size() - 1).y == 0
				&& sim.getNumberOfUpdates() > 30000)
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
		background(Colors.SIMULATION_BACKGROUND_COLOR);

		image(sim.render(), 100, 150);
		frame(100, 150, simSize, simSize);
		// image(sim.getStatsCollector().render(), 800, 0);

		textSize(10);
//		Graph.renderWithLib(150 + simSize, 150, simSize, simSize / 2, sim.getStatsCollector().getSickAgents(),
//				Float.NaN, Colors.AGENT_COLOR_SICK);
		Graph.renderWithLib(150 + simSize, 150, simSize, simSize / 2, sim.getStatsCollector().getSickAgents(),
				Float.NaN, Colors.AGENT_COLOR_SICK);
		frame(150 + simSize, 150, simSize, simSize / 2);
		
		Graph.renderWithLib(150 + simSize, 150 + simSize / 2, simSize, simSize / 2,
				sim.getStatsCollector().getPredictedRAverage(),
				5,
				Colors.AGENT_COLOR_SICK);
		frame(150 + simSize, 150 + simSize / 2, simSize, simSize / 2);

		// Graph.renderWithLib(800, 400, 800, 400,
		// sim.getStatsCollector().getCurrentR(), 10);

		fill(Colors.TEXT_COLOR);
		textSize(100);
		text("pandemic simulation", 100, 100);
		text(sim.getCurrentR(), 200 + simSize * 2, 250);
		text(sim.getPredictedR(), 200 + simSize * 2, 350);
	}

	private void frame(int x, int y, int w, int h)
	{
		int off = 0;
		noFill();
		stroke(Colors.TEXT_COLOR);
		strokeWeight(3);
		rect(x + off, y + off, w - off, h - off);
	}
}
