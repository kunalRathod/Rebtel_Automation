package com.rebtel.testbed.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.TestNG;
import org.testng.annotations.Test;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.rebtel.test.TestBed;
import com.rebtel.util.LogUtil;

public class TestExecution {
	private static Log log = LogUtil.getLog(TestBed.class);
	private static boolean failFlag = false;
	private static boolean skipFlag = false;
	
	/**
	 * main :- 
	 * Main method to start execution  
	 */
	public static void main(String[] args){
		String filePath = TestExecution.class.getClassLoader().getResource("Config.txt").getFile();
		System.out.println("TestBedName "+System.getProperty("TESTBEDNAME"));
		TestExecution test = new TestExecution();
		TestBedConfig.readConfig(filePath);
		try {
			new TestExecution().startTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}finally{
			if(failFlag||skipFlag){
				Assert.assertTrue(false,"There failures or skip in tests");
			}
		}
	}
	
	/**
	 * startTest :- 
	 * Start testng execution  
	 */
	public void startTest() {
		XmlSuite suite = prepareTestNGSuite();
		log.info("TestNG Suite prepared");
		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		suites.add(suite);
		TestNG tng = new TestNG();
		tng.setXmlSuites(suites);

		List<XmlSuite> existingSuiteList = new ArrayList<XmlSuite>();
		existingSuiteList.add(suite);
		printTestNG(suites);
		synchronized(this){
			tng.run();
			failFlag = tng.hasFailure();
			skipFlag = tng.hasSkip();
		}
	}

	/**
	 * printTestNG :- 
	 * Create testng xml 
	 */
	public void printTestNG(List<XmlSuite> suite){
		for (XmlSuite s : suite) {
			System.out.println(s.toXml());
		}
	}
	
	/**
	 * getTestName :- 
	 * gets TestBedName for current execution
	 */
	public String getTestName(int count){
		String testNames = TestBedConfig.getTestBedName();
		return testNames.split(",")[count-1];
	}
	
	/**
	 * prepareTestNGSuite :- 
	 * Prepares testng suite  
	 */
	public XmlSuite prepareTestNGSuite(){
		XmlSuite suite = new XmlSuite();
		suite.setName("RebtelTestSuite");
		return addListnerToSuite(suite);
	}
	
	/**
	 * addListnerToSuite :- 
	 * Add testng listeners 
	 */
	public XmlSuite addListnerToSuite(XmlSuite suite){
		int count = TestBedConfig.getTestBedListnerSize();
		while(count!=0){
			suite.addListener(getLisnter(count));
			count--;
		}
		return addTestsToSuite(suite);
	}
	
	/**
	 * getLisnter :- 
	 * Get listeners from testbedConfig  
	 */
	public String getLisnter(int count){
		String listners = TestBedConfig.getListner();
		return listners.split(",")[count-1];
	}
	
	/**
	 * addParallelism :- 
	 * Add parallel param  
	 */
	public XmlSuite addParallelism(XmlSuite suite){
		int count = TestBedConfig.getTestBedSize();
		suite.setParallel(suite.PARALLEL_TESTS);
		suite.setThreadCount(count);;
		return addIncludes(suite);
	}
	
	/**
	 * addIncludes :- 
	 * Add groups for exectuion 
	 */
	public XmlSuite addIncludes(XmlSuite suite){
		suite.addIncludedGroup(TestBedConfig.getTestCoverage());
		return suite;
	}
	
	/**
	 * addTestsToSuite :- 
	 * Add test node  
	 */
	public XmlSuite addTestsToSuite(XmlSuite suite){
		int count  = TestBedConfig.getTestBedSize();
		System.out.println("Count --------" + count);
		while(count!=0){
			XmlTest xmlTest = new XmlTest(suite);
			xmlTest.setName(getTestName(count));
			xmlTest = addClassToSuite(xmlTest);
			count--;
		}
		return addParallelism(suite);
	}
	
	/**
	 * addClassToSuite :- 
	 * Add classes for execution
	 */
	public XmlTest addClassToSuite(XmlTest xmlTest){
		List<XmlClass> classes = new ArrayList<XmlClass>();
		classes.add(new XmlClass(TestBedConfig.getClassName()));
		xmlTest.setXmlClasses(classes) ;
		return xmlTest;
	}
}
