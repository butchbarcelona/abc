package com.bunny.game.framework;

public final class Random {

	public Random()
	{
	
	}
	
	public static int Next(int i)
	{
		return (int)Math.floor(Math.random()*(i));
	}
	
	public static int Next(int first, int last)
	{
		return (int)Math.floor(Math.random()*(last-first)+first);
	}
	
	public static float Next()
	{
		return (float)Math.random();
	}
}
