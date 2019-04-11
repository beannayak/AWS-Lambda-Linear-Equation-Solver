package com.rasal.awslambda;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.rasal.awslambda.lindoutils.OptimizationType;

public class OptimizationRequest {
	
	@NotNull (message = "Invalid optimization direction")
	private OptimizationType direction;
	
	@NotEmpty (message = "ObjectiveFunction should NOT be empty.")
	@Pattern(regexp="((-)?(\\d+).(\\d+)_([^\\W_]+))(;(-)?(\\d+).(\\d+)_([^\\W_]+))*", message="Objective function is not formatted properly.")
	private String objectiveFunction;
	
	@NotNull (message = "Constraints must present in optimization request.")
	private List<@NotBlank(message = "Constraints should NOT be blank.") 
			     @Pattern(regexp="((-)?(\\d+).(\\d+)_([^\\W_]+))(;(-)?(\\d+).(\\d+)_([^\\W_]+))*[><=](-)?(\\d+).(\\d+)", message="Constraints NOT formatted properly.") 
				 String> constraints;
	
	public OptimizationType getDirection() {
		return direction;
	}
	
	public void setDirection(OptimizationType direction) {
		this.direction = direction;
	}
	
	public List<String> getConstraints() {
		return constraints;
	}
	
	public void setConstraints(List<String> constraints) {
		this.constraints = constraints;
	}

	public String getObjectiveFunction() {
		return objectiveFunction;
	}

	public void setObjectiveFunction(String objectiveFunction) {
		this.objectiveFunction = objectiveFunction;
	}
}
