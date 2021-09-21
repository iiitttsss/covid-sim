package covidSimulation.simulation.agents;

import java.util.ArrayList;

import covidSimulation.Colors;
import processing.core.PGraphics;
import processing.core.PVector;
import util.Util;

public class Agent
{
	// general
	private int id;
	public static int numberOfAgents;

	// render
	public static final float SIZE = 7;

	// movment
	// -- random walk
	public static final float MAX_SPEED = 0.5f;
	public static final float ACCELERATION = MAX_SPEED / 10.0f;
	private PVector position;
	private PVector velocity;
	// -- move to center
	private boolean goingToCenter;
	public static final float GO_TO_CENTER_CHANCE = 0.00002f;
	public static final boolean CAN_GO_TO_CENTER = true;

	// condition
	public static final float INFECTION_RADIUS = 10;
	public static final int INCUBATION_PERIOD = 800; // number of update until infecteous
	public static final int SHOW_SYMPTOMS = 1000;
	public static final int RECOVERY_TIME = 1500; // number of updates until the agent is not sick anymore
	public static final float BASIC_CHANCE_OF_INFECTION = 0.026f;
	public static final float RESISTANCE_CHANGE = 0.00001f * 0;
	public static final int ONE_OUT_OF_WONT_HAVE_SYMPTOMES = 10;
	public static final float RANDOM_SICK_CHANCE = 0.00001f;

	private boolean sick;
	private int sickDays; // number of days this agent was sick
	private float resistance;
	private boolean inIsolation;

	// contact tracing
	public static final boolean CAN_BE_CONTACT_TRACED = true;
	public static final int NUMBER_OF_UPDATES_FOR_ISOLATION = 1;

	// stats
	private int currentR;
	private float predictedR;
	private float realR;
	private int interactions;


	public Agent(boolean isSick, int boxWidth, int boxHeight)
	{
		// general
		this.setId(Agent.numberOfAgents);
		Agent.numberOfAgents++;

		// movement
		this.setPosition(new PVector(Util.randomInRange(0, boxWidth), Util.randomInRange(0, boxHeight)));
		this.setVelocity(PVector.random2D().mult(Agent.MAX_SPEED));

		// condition
		this.setSick(isSick);

		// contact tracing
		// this.setContactTracing(new HashMap<Integer, Integer>());

	}

	public boolean isInfectious()
	{
		return (this.isSick() && this.getSickDays() > Agent.INCUBATION_PERIOD);
	}

	public void update(ArrayList<Agent> infectiousAgents, int boxWidth, int boxHeight, float percentSick)
	{
		this.updateInfections(infectiousAgents, percentSick);
		this.updateResistance();
		this.updateIsolation();

		this.move(boxWidth, boxHeight);
	}

	/**
	 * handle agent movement
	 */
	private void move(int boxWidth, int boxHeight)
	{
		if (this.isGoingToCenter())
		{
			this.goToPosition(new PVector(550, 550));
		}
		else
		{
			this.randomWalk();
			this.stayInsideBox(boxWidth, boxHeight);
			
			if (Agent.CAN_GO_TO_CENTER)
			{
				if (Math.random() < Agent.GO_TO_CENTER_CHANCE)
				{
					this.setGoingToCenter(true);
				}
			}
		}
	}


	/**
	 * 
	 * @param agents - the list of all the agents
	 * @return - the number of times this agent was near another agent - note: each
	 *         interaction is counted two times, each agent count the interaction
	 */
	public int countNumberOfInteractions(ArrayList<Agent> agents)
	{
		int count = 0;
		for (Agent other : agents)
		{
			if (this != other)
			{
				if (this.position.dist(other.getPosition()) <= Agent.INFECTION_RADIUS)
				{
					count++;
				}
			}
		}
		return count;
	}

	public void postUpdate()
	{
		if (this.isSick()) // if the agents is sick
		{
			this.setPredictedR(this.calculatePredictedR());
			this.setRealR(this.calculateRealR());
		}
	}

	private float calculateRealR()
	{
		// return this.getPersonalR()
		return (this.getCurrentR() + this.getPredictedR()) / 2.0f;
	}

	private void updateIsolation()
	{
		if (this.isSick() && this.getSickDays() > Agent.SHOW_SYMPTOMS
				&& this.getId() % Agent.ONE_OUT_OF_WONT_HAVE_SYMPTOMES != 0)
		{
			this.setInIsolation(true);
		}
	}

	public void updateInfections(ArrayList<Agent> infectiousAgents, float percentSick)
	{
		if (this.isSick()) // if the agents is sick
		{
			this.sickDays++;

			if (this.getSickDays() >= Agent.RECOVERY_TIME)
			{
				this.recover();
			}
		}
		else // if the agents is not sick - it can get sick
		{
			//list of the IDs of the other agents, this agent was in range of 
			for (Agent otherAgent : infectiousAgents)
			{
				// check if it was close to an infectious agent
				if (this.position.dist(otherAgent.getPosition()) < Agent.INFECTION_RADIUS)
				{
					// try to get sick
					if (Math.random() < (Agent.BASIC_CHANCE_OF_INFECTION))
					{
						if (Math.random() > this.getResistance())
						{
							this.setSick(true);
							otherAgent.increaseCurrentR();
							break;
						}
					}
				}
			}

			if (Math.random() < Agent.RANDOM_SICK_CHANCE /* * percentSick */)
			{
				this.setSick(true);
			}
		}
	}

	private void updateResistance()
	{
		this.resistance += Agent.RESISTANCE_CHANGE;

		if (this.getResistance() > 1)
		{
			this.setResistance(1);
		}
	}

	private float calculatePredictedR()
	{
		if (this.getSickDays() == 0)
		{
			return 0;
		}
		return (float) this.getCurrentR() * (Agent.RECOVERY_TIME - Agent.INCUBATION_PERIOD)
				/ (this.getSickDays() - Agent.INCUBATION_PERIOD);
	}

	/**
	 * this method is called when an agent is revored from covid
	 */
	private void recover()
	{
		this.setSick(false);
		this.setSickDays(0);
		this.setCurrentR(0);
		this.setInIsolation(false);
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

	private void randomWalk()
	{
		PVector acceleration = PVector.random2D();
		acceleration.mult(Agent.ACCELERATION);
		this.velocity.add(acceleration);
		velocity.limit(Agent.MAX_SPEED);
		this.position.add(velocity);
	}

	/**
	 * move toward the position
	 * 
	 * @param targetPos - the position it moves toward
	 */
	private void goToPosition(PVector targetPos)
	{
		PVector step = PVector.sub(targetPos, this.getPosition());

		if (step.mag() >= Agent.MAX_SPEED)
		{
			step.setMag(Agent.MAX_SPEED);
			this.position.add(step);
		}
		else
		{
			this.setGoingToCenter(false);
		}
	}

	public void render(PGraphics pg)
	{
		this.setDisplayStyle(pg);
		if (!this.inIsolation)
		{
			pg.circle(this.position.x, this.position.y, Agent.SIZE);
			if (this.isInfectious())
			{
				pg.noFill();
				pg.circle(this.position.x, this.position.y, 2 * Agent.INFECTION_RADIUS);
			}
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
			if (this.getId() % Agent.ONE_OUT_OF_WONT_HAVE_SYMPTOMES == 0)
			{
				c = Colors.AGENT_COLOR_NO_SYMPTOMES;

			}
			else
			{
				c = Colors.AGENT_COLOR_SICK;
			}
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

	public int getCurrentR()
	{
		return currentR;
	}

	public void setCurrentR(int personalR)
	{
		this.currentR = personalR;
	}

	public void increaseCurrentR()
	{
		this.currentR++;
	}

	public float getPredictedR()
	{
		return predictedR;
	}

	public void setPredictedR(float predictedR)
	{
		this.predictedR = predictedR;
	}

	public float getRealR()
	{
		return realR;
	}

	public void setRealR(float realR)
	{
		this.realR = realR;
	}

	public int getInteractions()
	{
		return interactions;
	}

	public void setInteractions(int interactions)
	{
		this.interactions = interactions;
	}

	public float getResistance()
	{
		return resistance;
	}

	public void setResistance(float resistance)
	{
		this.resistance = resistance;
	}

	public boolean isInIsolation()
	{
		return inIsolation;
	}

	public void setInIsolation(boolean inIsolation)
	{
		this.inIsolation = inIsolation;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public boolean isGoingToCenter()
	{
		return goingToCenter;
	}

	public void setGoingToCenter(boolean goingToCenter)
	{
		this.goingToCenter = goingToCenter;
	}
}
