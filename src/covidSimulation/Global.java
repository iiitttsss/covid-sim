package covidSimulation;

import processing.core.PApplet;

public class Global
{
	private static PApplet pro;

	public static PApplet getPro()
	{
		return pro;
	}

	public static void setPro(PApplet pro)
	{
		Global.pro = pro;
	}
}
