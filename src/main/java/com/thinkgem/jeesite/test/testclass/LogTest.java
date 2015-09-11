package com.thinkgem.jeesite.test.testclass;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class LogTest {
	private Logger logger=LogManager.getLogger(LogTest.class);
	@Test
	public void testLog1(){
		logger.entry("this is entry");
		logger.debug("this is debug");
		logger.info("this is info");
		logger.warn("this is warn");
		logger.error("this is error");
		logger.fatal("this is fatal");
	}
}
