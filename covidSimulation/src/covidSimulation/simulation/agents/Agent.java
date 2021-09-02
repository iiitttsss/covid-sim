package covidSimulation.simulation.agents;

import java.util.ArrayList;

import covidSimulation.Colors;
import processing.core.PGraphics;
import processing.core.PVector;
import util.Util;

public class Agent
{


	// render
	public static final float SIZE = 3;

	// movment
	public static final float MAX_SPEED = 0.5f;
	public static final float ACCELERATION = 0.03f;
	private PVector position;
	private PVector velocity;

	// condition
	public static final float INFECTION_RADIUS = 5;
	public static final int INCUBATION_PERIOD = 1000; // number of update until infecteous
	private boolean sick;
	private int sickDays; // number of days this agent was sick


	public Agent(boolean isSick, int boxWidth, int boxHeight)
	{
		// movement
		this.setPosition(new PVector(Util.randomInRange(0, boxWidth), Util.randomInRange(0, boxHeight)));
		this.setVelocity(PVector.random2D().mult(Agent.MAX_SPEED));

		// condition
		this.setSick(isSick);
		this.setSickDays(0);
	}

	public boolean isInfectious()
	{
		return (this.isSick() && this.getSickDays() > Agent.INCUBATION_PERIOD);
	}

	public void update(int boxWidth, int boxHeight, ArrayList<Agent> infectiousAgents)
	{
		this.updateInfections(infectiousAgents);
		this.move();
		this.stayInsideBox(boxWidth, boxHeight);
	}

	private void updateInfections(ArrayList<Agent> infectiousAgents)
	{
		if (!this.isSick()) // if the agents is not sick - it can get sick
		{
			for (Agent agent : infectiousAgents)
			{
				if (this.position.dist(agent.getPosition()) < Agent.INFECTION_RADIUS)
				{
					this.setSick(true);
					break;
				}
			}
		}

		if (this.isSick())
		{
			this.sickDays++;
		}
	}

	private void stayInsideBox(int boxWidth, int boxHeight)
	{
		if (this.position.x >= boxWidth)
		{
			this.position.x = 0;
		}
		else if (this.position.x < 0)
		{
			this.position.x = boxWidth - 1;
		}

		if (this.position.y >= boxHeight)
		{
			this.position.y = 0;
		}
		else if (this.position.y < 0)
		{
			this.position.y = boxHeight - 1;
		}
	}

	private void move()
	{
		PVector acceleration = PVector.random2D();
		acceleration.mult(Agent.ACCELERATION);
		this.velocity.add(acceleration);
		velocity.limit(Agent.MAX_SPEED);
		this.position.add(velocity);
	}

	public void render(PGraphics pg)
	{
		this.setDisplayStyle(pg);
		pg.circle(this.position.x, this.position.y, Agent.SIZE);
		if (this.isInfectious())
		{
			pg.noFill();
			pg.circle(this.position.x, this.position.y, 2 * Agent.INFECTION_RADIUS);
		}
	}

	/**
	 * determining and setting the color this agents will be render in
	 */
	private void setDisplayStyle(PGraphics pg)
	{
		int c = 0;

		if (this.isSick())
		{
			c = Colors.AGENT_COLOR_SICK;
		}
		else
		{
			c = Colors.AGENT_COLOR_HEALTHY;
		}

		pg.stroke(c);
		pg.fill(c);
	}

	public PVector getPosition()
	{
		return position;
	}

	public void setPosition(PVector position)
	{
		this.position = position;
	}


	public PVector getVelocity()
	{
		return velocity;
	}


	public void setVelocity(PVector velocity)
	{
		this.velocity = velocity;
	}

	public boolean isSick()
	{
		return sick;
	}

	public void setSick(boolean sick)
	{
		this.sick = sick;
	}

	public int getSickDays()
	{
		return sickDays;
	}

	public void setSickDays(int sickDays)
	{
		this.sickDays = sickDays;
	}
}
