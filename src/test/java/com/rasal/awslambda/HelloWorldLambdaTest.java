package com.rasal.awslambda;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class HelloWorldLambdaTest extends TestCase {
	public HelloWorldLambdaTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(HelloWorldLambdaTest.class);
	}

	public void testApp() {
		assertTrue(true);
	}
}
