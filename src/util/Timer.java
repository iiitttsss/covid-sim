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

	public static void stop()
	{
		endTime = Global.getPro().millis();
		int deltaTime = endTime - startTime;
		System.out.println("timer took: " + deltaTime + " miliseconds");
	}
}
