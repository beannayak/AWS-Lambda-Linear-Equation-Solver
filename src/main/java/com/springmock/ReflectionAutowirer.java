/*
 ****************************************************************************
 *
 * Copyright (c)2018 The Vanguard Group of Investment Companies (VGI)
 * All rights reserved.
 *
 * This source code is CONFIDENTIAL and PROPRIETARY to VGI. Unauthorized
 * distribution, adaptation, or use may be subject to civil and criminal
 * penalties.
 *
 ****************************************************************************
 Module Description:

 $HeadURL: http://prdsvnrepo:8080/svn/retail/prototype/truffle-lp/trunk/src/main/java/com/vanguard/service/entity/linearoptimization/ReflectionAutowirer.java $
 $LastChangedRevision: 2059099 $
 $Author: UC7E $
 $LastChangedDate: 2018-10-29 12:17:36 -0400 (Mon, 29 Oct 2018) $
*/
package com.springmock;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.PostConstruct;

public class ReflectionAutowirer {

	private ReflectionAutowirer() {}
	
	public static <T> T getAutowiredInstance(Class<T> c) {
		Field[] fields = c.getDeclaredFields();
		
		T retVal = getBlankInstance(c);
		for (Field f : fields) {
			if (f.isAnnotationPresent(Autowired.class)) {
				try {
					f.setAccessible(true);
					f.set(retVal, getAutowiredInstance(f.getType()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new RuntimeException("Cannot Instantiate Object.", e);
				}
			} 
		}
		
		invokePostConstructMethodIfExists(c, retVal);
		
		return retVal;
	}

	private static <T> void invokePostConstructMethodIfExists(Class<T> c, T retVal) {
		for (Method m : c.getDeclaredMethods()) {
			if (m.isAnnotationPresent(PostConstruct.class)) {
				try {
					m.setAccessible(true);
					m.invoke(retVal);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new RuntimeException("Cannot Instantiate Object.", e);
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <T> T getBlankInstance(Class<T> c) {
		try {
			for (Constructor e : c.getDeclaredConstructors()) {
				if (e.getParameterTypes().length == 0) {
					e.setAccessible(true);
					return (T) e.newInstance();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return null;
	}
}
