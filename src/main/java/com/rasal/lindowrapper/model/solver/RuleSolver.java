package com.rasal.lindowrapper.model.solver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lindo.Lindo;
import com.rasal.awslambda.errorhandling.LindoResponseValidator;
import com.rasal.awslambda.lindoutils.LinearEquationsBuilder;
import com.rasal.lindowrapper.LindoEnvironmentSetup;
import com.springmock.Autowired;
import com.springmock.Component;

@Component
public class RuleSolver {
	
	@Autowired
	private LindoEnvironmentSetup environmentCreator;
	
	@Autowired
	private LindoResponseValidator lindoResponseValidator;
	
	public Map<String, Double> solve(LinearEquationsBuilder objectiveSpecification, String objectiveFnx) {
		Object nativeLindoModel = createNativeLindoModel();
		
		loadNativeModelWithSpecification(objectiveSpecification, objectiveFnx, nativeLindoModel);
		solveModel(nativeLindoModel);
		Map<String, Double> solutionValues = getSolutionValuesFromLindoSolution(objectiveSpecification, nativeLindoModel);
		
		return solutionValues;
	}

	private Map<String, Double> getSolutionValuesFromLindoSolution(LinearEquationsBuilder objectiveSpecification, Object nativeLindoModel) {
		double[] solution = getSolution(nativeLindoModel);
		Map<String, Double> solutionValues = mapSolutionValuesToVariableNames(solution, objectiveSpecification.getVariablesName());
		return solutionValues;
	}

	private void solveModel(Object nativeLindoModel) {
		int[] solutionStatus = new int[1];
		int response = Lindo.LSoptimize(nativeLindoModel, 0, solutionStatus);
		
		lindoResponseValidator.validateFeasibleResponse(solutionStatus[0]);
		lindoResponseValidator.validateLindoResponse(response, "Error solving system of linear inequalities.");
	}

	private Map<String, Double> mapSolutionValuesToVariableNames(double[] solution, List<String> variablesName) {
		Map<String, Double> retVal = new HashMap<>();
		
		int i = 0;
		for (String s : variablesName) {
			retVal.put(s, solution[i++]);
		}
			
		return retVal;
	}

	private double[] getSolution(Object nativeLindoModel) {
		int[] numberOfVariables = new int[1];
		final int infoQueryResponse = Lindo.LSgetInfo(nativeLindoModel, Lindo.LS_IINFO_NUM_VARS, numberOfVariables);
		lindoResponseValidator.validateLindoResponse(infoQueryResponse, "Failed to retrieve information on variables from the model.");

		double[] primalSolution = new double[numberOfVariables[0]];
		int getSolutionResponse;
		getSolutionResponse = Lindo.LSgetPrimalSolution(nativeLindoModel, primalSolution);
		
		lindoResponseValidator.validateLindoResponse(getSolutionResponse, "Failed to retrieve solution values from the model.");

		return primalSolution;
	}

	private void loadNativeModelWithSpecification(LinearEquationsBuilder objectiveSpecification, String objectiveFnx,
			Object nativeModel) {
		
		int optimizationDirection = objectiveSpecification.getDirection();
		double[] objectiveFunctionCoefficients = objectiveSpecification.getObjectiveFunctionCoeffs(objectiveFnx);
		double objectiveFunctionConstantTerm = 0.0;
		int numberOfConstraints = objectiveSpecification.getNumberOfConstraints();
		String constraintTypes = objectiveSpecification.getSymbols();
		double[] constraintRightHandSides = objectiveSpecification.getRightHandVector();
		int[] columnStartArray = objectiveSpecification.getColumnStartVector();
		double[] nonZeroConstraintCoefficients = objectiveSpecification.getNonZeroVector();
		int numberOfNonZeroConstraintCoefficients = nonZeroConstraintCoefficients.length;
		int[] rowIndexArray = objectiveSpecification.getRowIndexVector();
		int numberOfVariables = objectiveSpecification.getNumberOfVariables();
		
		int response = Lindo.LSloadLPData(nativeModel, numberOfConstraints, numberOfVariables, optimizationDirection,
				objectiveFunctionConstantTerm, objectiveFunctionCoefficients, constraintRightHandSides, constraintTypes,
				numberOfNonZeroConstraintCoefficients, columnStartArray, null, nonZeroConstraintCoefficients, rowIndexArray, null, null);
		
		lindoResponseValidator.validateLindoResponse(response, "Error loading lindo model.");
	}
	
	private Object createNativeLindoModel() {
		int[] response = new int[1];
		return Lindo.LScreateModel(environmentCreator.getEnv(), response);
	}
}
