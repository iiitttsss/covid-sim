package util;

import covidSimulation.Global;

public class Timer
{
	private static int startTime;
	private static int endTime;

	public static void start()
	{
		startTime = Global.getPro().millis();
	}

	public static void time()
	{
		endTime = Global.getPro().millis();
		int deltaTime = endTime - startTime;
		System.out.println("timer took: " + deltaTime + " miliseconds");
	}

	public static void reset()
	{
		Timer.time();
		Timer.start();
	}

	public static void end()
	{
		Timer.time();
		System.out.println();

	}
}
