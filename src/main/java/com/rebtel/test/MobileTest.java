package com.rebtel.test;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.rebtel.listerners.SoftAssertor;
import com.rebtel.pages.HomePage;
import com.rebtel.pages.Login;
import com.rebtel.testbed.core.TestBedConfig;
import com.rebtel.util.ExcelReader;
import com.rebtel.util.LogUtil;

public class MobileTest extends TestBed {

	/** The log. */
	private static Log log = LogUtil.getLog(MobileTest.class);
	private boolean isLoginSuccess = true;
	public static String testCaseDesc = "";
	public static HashMap<String, Integer> failureMap = new HashMap<String, Integer>();
	
	/**
	 * Verify End to End flow for Rebtel App data
	 * 
	 * @param testCaseDescription
	 * @param runMode
	 * @param login
	 * @param homeScreen
	 * @throws InterruptedException
	 * @throws IOException
	 */
	@Test(dataProvider = "TestData", groups = { "Sanity", "Smoke", "Regression" })
	public void testRebtelAssignment(String testCaseDescription, String runMode, String login, String homeScreen) 
			throws InterruptedException, IOException {
		log.info("******START***** Test case description :::" + testCaseDescription + " **********");
		testCaseDesc = testCaseDescription;
		
		if (runMode.equalsIgnoreCase("N"))
			return;
		
		if(!login.equalsIgnoreCase("N")) {
			Login loginObject = new Login(testBedConfig.getDriver());
			isLoginSuccess = loginObject.verifyUserLogin();
		}
		
		if(!homeScreen.equalsIgnoreCase("N")&&isLoginSuccess) {
		HomePage homePage = new HomePage(testBedConfig.getDriver());
		homePage.verifyCallingFunctionality();
		}
		
		log.info("******END***** Test case description :::" + testCaseDescription + " **********");
	}

	/**
	 * DataProvider for the tests
	 * 
	 * @return
	 */
	@DataProvider(name = "TestData")
	private Object[][] getTestData(ITestContext context) {
		String testType = context.getCurrentXmlTest().getIncludedGroups().toString();
		testType = testType.substring(1, testType.indexOf("]"));
		log.info("Test Type to be Executed is :::" + testType);
		Object[][] testData = ExcelReader.readExcelData(TestBedConfig.getDataSheetPath(), testType, "RebtelAssignment");
		return testData;
	}
}
