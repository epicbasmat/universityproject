package org.basmat.map.util;

public class CubicInterpolation {
	
	private int seed;

	public CubicInterpolation(int seed) {
		this.seed = seed;
	}


	/**
	 * Hash courtesy of perlin noise @ Ken Perlin
	 * @param x
	 * @param y
	 * @return
	 */
	public float hash(int x, int y) {
		int n = x + y * seed;
		n = (n << 13) ^ n; 
	    return (float) (1.0-((n*(n*n*15731 + 789221)+1376312589)&0x7fffffff)/1073741824.0);
	}
	
	public float noiseGenerator(float floatX, float floatY, float stretch) {
		/**
		 * A stretch value "stretches" the target number over an amount of specified points, useful for "magnifying" a map.
		 */
		floatX /= stretch;
		floatY /= stretch;
		
		//Setting the whole coordinate into integers.
		int x = (int) Math.floor(floatX);
		int y = (int) Math.floor(floatY);
		
		//Calculating difference between integer and float
		float fractionalX = floatX - x;
		float fractionalY = floatY - y;
		
		double[] p = new double[4];
		for (int j = 0; j < 4; j++) { 
			double[] p2 = new double[4];
			for (int i = 0; i < 4; i++) { 
				p2[i] = hash(x + i - 1, y + j - 1);
			}
			p[j] = cubicInterpolation(p2, fractionalX);
		}
	    return (float) cubicInterpolation(p, fractionalY);
	}
	
	public int getSeed() { 
		return this.seed;
	}

	public static double cubicInterpolation(double[] p, float fractionalX) {
		return cubicInterpolation(p[0], p[1], p[2], p[3], fractionalX);
	}

	public static double cubicInterpolation(double v0, double v1, double v2, double v3, double fractionalX) {
	      double P = (v3 - v2) - (v0 - v1);
		  double Q = (v0 - v1) - P;
		  double R = v2 - v0;
		  double S = v1;
		  return P * fractionalX * fractionalX * fractionalX + Q * fractionalX * fractionalX + R * fractionalX + S;
	}
	
}
