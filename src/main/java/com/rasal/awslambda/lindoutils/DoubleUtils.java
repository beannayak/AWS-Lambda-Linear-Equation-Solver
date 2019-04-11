package com.rasal.awslambda.lindoutils;

import java.util.List;

public class DoubleUtils {

	public static final double delta = 0.0000001;
	
	public static boolean areEqual(double a, double b) {
		return (Math.abs(a - b) <= delta);
	}
	
	public static double[] getDoubleArray(List<Double> values) {
		return values.stream().mapToDouble(Double::doubleValue).toArray();
	}
	
	public static int[] getIntegerArray(List<Integer> values) {
		return values.stream().mapToInt(Integer::intValue).toArray();
	}
}
