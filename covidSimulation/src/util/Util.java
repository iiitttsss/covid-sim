package util;

public class Util
{
	/**
	 * 
	 * @param min - the minimum value
	 * @param max - the maximum value
	 * @return - a random number between min and max
	 */
	public static float randomInRange(float min, float max)
	{
		return min + (float) (Math.random() * (max - min));
	}
}
