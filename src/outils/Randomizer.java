package outils;

import java.util.Random;

public class Randomizer {
	public static Random randomGenerator = new Random();
	public static void setSeed(long seed) {
		randomGenerator = new Random(seed);
	}
}
