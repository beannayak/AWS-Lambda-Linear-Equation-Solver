package com.rasal.lindowrapper.model.solver;

import java.util.Map;
import java.util.stream.Collectors;

import com.springmock.Component;

@Component
public class JsonResponseCreator {

	public String createResponse(Map<String, Double> solutionValues) {
		StringBuilder sb = new StringBuilder("{");
		String solutionValuesString = solutionValues.keySet().stream()
				.map(key -> {
					return "\"" + key + "\" : " + solutionValues.get(key);
				})
				.collect(Collectors.joining(", "));
		sb.append(solutionValuesString).append("}\n");
		
		return sb.toString();
	}
}
