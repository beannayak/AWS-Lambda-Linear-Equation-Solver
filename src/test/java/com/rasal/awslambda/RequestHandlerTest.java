package com.rasal.awslambda;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RequestHandlerTest extends TestCase {
	public RequestHandlerTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(RequestHandlerTest.class);
	}

	public void testApp() {
		assertTrue(true);
	}
}
