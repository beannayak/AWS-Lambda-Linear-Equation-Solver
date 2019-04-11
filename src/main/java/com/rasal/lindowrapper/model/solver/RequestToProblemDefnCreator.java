package com.rasal.lindowrapper.model.solver;

import com.rasal.awslambda.OptimizationRequest;
import com.rasal.awslambda.lindoutils.LinearEquationsBuilder;
import com.springmock.Component;

@Component
public class RequestToProblemDefnCreator {
	
	public LinearEquationsBuilder create(OptimizationRequest request) {
		LinearEquationsBuilder builder = new LinearEquationsBuilder();
		
		for (String constraint : request.getConstraints()) {
			builder.addLinearEquation(constraint);
		}
		
		builder.setupValues();
		builder.setDirection(request.getDirection());
		
		return builder;
	}
}
