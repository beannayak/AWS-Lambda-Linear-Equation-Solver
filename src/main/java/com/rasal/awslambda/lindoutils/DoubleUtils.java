package com.rasal.awslambda.lindoutils;

import java.util.List;

public class DoubleUtils {

	public static final double delta = 0.0000001;
	
	public static boolean areEqual(double a, double b) {
		if (Math.abs(a - b) <= delta) {
			return true;
		}
		return false;
	}
	
	public static double[] getDoubleArray(List<Double> values) {
		double[] retVals = new double[values.size()];
		int i = 0;
		for (double d : values) {
			retVals[i] = d;
			i++;
		}
		return retVals;
	}
	
	public static int[] getIntegerArray(List<Integer> values) {
		int[] retVals = new int[values.size()];
		int i = 0;
		for (int d : values) {
			retVals[i] = d;
			i++;
		}
		return retVals;
	}
}
