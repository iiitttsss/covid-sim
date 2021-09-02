package covidSimulation;


import covidSimulation.simulation.SimulationBox;
import processing.core.*;

public class Main extends PApplet
{

	private SimulationBox sim;

	public static void main(String[] args)
	{
		PApplet.main(new String[] { Main.class.getName() });
	}

	@Override
	public void settings()
	{
		size(800, 1200);
	}

	@Override
	public void setup()
	{
		System.out.println("start program");
		Global.setPro(this);
		sim = new SimulationBox(800, 800);
	}

	@Override
	public void draw()
	{
		update();
		render();
	}

	private void update()
	{
		sim.update();
	}

	private void render()
	{
		image(sim.render(), 0, 0);
		image(sim.getStatsCollector().render(), 0, 800);
	}
}
