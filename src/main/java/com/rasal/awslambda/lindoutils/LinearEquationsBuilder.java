package com.rasal.awslambda.lindoutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lindo.Lindo;

public class LinearEquationsBuilder {

	private Map<String, List<Double>> valuesMatrix = new HashMap<>();
	private StringBuilder symbols = new StringBuilder();
	private List<Double> rightHandValues = new ArrayList<>();
	private double[] values;
	private int[] columnStart;
	private int numberOfConstraints;
	private int[] rowIndex;
	private int direction;
	
	public double[] getObjectiveFunctionCoeffs(String objectiveFnx) {
		Map<String, Double> values = new HashMap<>();
		String[] terms = objectiveFnx.split(";");
		for (String term : terms) {
			String[] coefficientAndVariable = term.split("_");
			Double termCoefficient = Double.valueOf(coefficientAndVariable[0]);
			String termVariable = coefficientAndVariable[1];
			values.put(termVariable, termCoefficient);
		}
		
		List<String> variables = new ArrayList<>(valuesMatrix.keySet());
		double[] retVals = new double[variables.size()];
		for (int i=0; i<variables.size(); i++) {
			Double value = values.get(variables.get(i));
			retVals[i] = value == null ? 0.0 : value;
		}
		
		return retVals;
	}
	
	public void addLinearEquation(String linearequation) {
		this.numberOfConstraints += 1;
		Map<String, Double> values = new HashMap<>();
		String[] leftAndRightHandSide = linearequation.split("[><=]");
		String[] terms = leftAndRightHandSide[0].split(";");
		double rightHandValue = Double.valueOf(leftAndRightHandSide[1]);
		
		for (String term : terms) {
			String[] coefficientAndVariable = term.split("_");
			Double termCoefficient = Double.valueOf(coefficientAndVariable[0]);
			String termVariable = coefficientAndVariable[1];
			values.put(termVariable, termCoefficient);
		}
		
		Set<String> allVariables = new HashSet<>(valuesMatrix.keySet());
		allVariables.addAll(values.keySet());
		for (String variable : allVariables) {
			addVariableToValuesMatrix(variable, values, valuesMatrix);
		}
		rightHandValues.add(rightHandValue);
		addSymbol(linearequation);
	}
	
	public int getNumberOfConstraints() {
		return this.numberOfConstraints;
	}
	
	public double[] getRightHandVector() {
		return DoubleUtils.getDoubleArray(rightHandValues);
	}
	
	public void setupValues() {
		List<Double> values = new ArrayList<>();
		List<Integer> rowIndices = new ArrayList<>();
		List<Integer> columnStart = new ArrayList<>();
		columnStart.add(0);
		
		for (String variable : valuesMatrix.keySet()) {
			int rowIndex = 0;
			for (Double d : valuesMatrix.get(variable)) {
				if (!DoubleUtils.areEqual(d, 0.0)) {
					values.add(d);
					rowIndices.add(rowIndex);
				}
				rowIndex++;
			}
			columnStart.add(values.size());
		}

		this.values = DoubleUtils.getDoubleArray(values);
		this.columnStart = DoubleUtils.getIntegerArray(columnStart);
		this.rowIndex = DoubleUtils.getIntegerArray(rowIndices);
	}

	public double[] getNonZeroVector() {
		return this.values;
	}
	
	public int[] getColumnStartVector() {
		return this.columnStart;
	}
	
	public int[] getRowIndexVector() {
		return this.rowIndex;
	}
	
	public int getNumberOfVariables() {
		return valuesMatrix.keySet().size();
	}
	
	public String getSymbols() {
		return symbols.toString();
	}

	private void addSymbol(String linearequation) {
		if (linearequation.contains("=")) {
			symbols.append("E");
		} else if (linearequation.contains(">")) {
			symbols.append("G");
		} else {
			symbols.append("L");
		}
	}
	
	public List<String> getVariablesName() {
		return new ArrayList<>(this.valuesMatrix.keySet());
	}

	private void addVariableToValuesMatrix(String variable, Map<String, Double> values,
			Map<String, List<Double>> valuesMatrix2) {
		
		Double value = values.get(variable);
		addValuesToMatrix(value != null ? value : 0.0, valuesMatrix2, variable);
	}

	private void addValuesToMatrix(double value, Map<String, List<Double>> valuesMatrix2, String variable) {
		List<Double> values = valuesMatrix2.get(variable);
		if (values == null) {
			values = new ArrayList<>();
			valuesMatrix2.put(variable, values);
		}
		values.add(value);
	}

	public void setDirection(OptimizationType direction) {
		if (OptimizationType.MINIMIZE.equals(direction)) {
			this.direction = Lindo.LS_MIN;
		} else {
			this.direction = Lindo.LS_MAX;
		}
	}
	
	public int getDirection() {
		return this.direction;
	}
}
