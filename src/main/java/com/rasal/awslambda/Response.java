package com.rasal.awslambda;

public class Response {

	public Integer statusCode;
	public String body;
	
	public static Response from(int statusCode, String body) {
		Response r = new Response();
		r.statusCode = statusCode;
		r.body = body;
		return r;
	}
}
