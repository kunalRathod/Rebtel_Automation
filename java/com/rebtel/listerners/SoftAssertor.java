package com.rebtel.listerners;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.rebtel.test.MobileTest;
import com.rebtel.test.TestBed;
import com.rebtel.testbed.core.TestBedConfig;
import com.rebtel.util.LogUtil;

public class SoftAssertor extends TestBedConfig{

	private static Map<ITestResult, List<String>> verificationFailuresMap = new HashMap<ITestResult, List<String>>();
	public static String failureScreenShotFolderPath = null;

	/** The log. */
	static Log log = LogUtil.getLog(SoftAssertor.class);
	static TestBedConfig testBedConfig =  TestBedConfig.getInstance();
	/**
	 * Method to capture screenshot
	 * @param methodName
	 * @throws IOException
	 */
	public static void captureScreenshot() throws IOException {
		try {
			getScreenShotFolderPath();
			if(failureScreenShotFolderPath==null) {
				log.error(" failureScreenShotFolderPath iS NULL");
				return;
			}
			if(MobileTest.failureMap.get(MobileTest.testCaseDesc)==null){
				MobileTest.failureMap.put(MobileTest.testCaseDesc, 1);
			}else{
				MobileTest.failureMap.put(MobileTest.testCaseDesc, MobileTest.failureMap.get(MobileTest.testCaseDesc)+1);
			}
			
			log.info("Capturing screenshot for:::" + MobileTest.testCaseDesc);
			File scrFile = ((TakesScreenshot) testBedConfig.getDriver()).getScreenshotAs(OutputType.FILE);
			File targetFile = new File(failureScreenShotFolderPath + MobileTest.testCaseDesc +"_"+MobileTest.failureMap.get(MobileTest.testCaseDesc)+ ".png");
			log.info("ScreenShotPath>>>" + targetFile);
			FileUtils.copyFile(scrFile, targetFile);
			log.info("Screenshot successfully saved for::: " + MobileTest.testCaseDesc);
		} catch (WebDriverException e) {
			log.error("Failed to capture screenshot for WebDriverException:::" + MobileTest.testCaseDesc + ":::due to " + e.getMessage());
			e.printStackTrace();
		}catch (NullPointerException e) {
			log.error("Failed to capture screenshot for NullPointerException:::" + MobileTest.testCaseDesc + ":::due to " + e.getMessage());
			e.printStackTrace();
		}catch (IOException e) {
			log.error("Failed to capture screenshot for IOException:::" + MobileTest.testCaseDesc + ":::due to " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Method to get screenshot folder path
	 * @return
	 */
	public static String getScreenShotFolderPath(){
		try{
			if(failureScreenShotFolderPath ==null){
				String getTestBedName = TestBed.currentTestBedNameMap.get(Thread.currentThread().getId());
				String dateFormat = new SimpleDateFormat("ddMMMyyyy_HHmmss").format(Calendar.getInstance().getTime());
				failureScreenShotFolderPath = getScreenshotPath()+ getTestBedName+"/"+dateFormat+"/";
			}
		}
		catch(Exception e){
			log.error("Failed to get screen shot folder path :::"+e.getMessage());
			e.printStackTrace();
		}
		return failureScreenShotFolderPath;
	}

	/**
	 * Method for SoftassertTrue
	 * 
	 * @param condition
	 * @param errMsg
	 * @return
	 */
	public static Boolean assertTrue(boolean condition, String errMsg) {
		try {
			Assert.assertTrue(condition);
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"pass");
			return true;
		} catch (Throwable e) {
			log.error("SoftAssertor.assertTrue failed !!! Error Message : " + errMsg);
			addVerificationFailure(errMsg + " Exception msg: " + e.getMessage());
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"FAIL");
			try {
				captureScreenshot();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Method for SoftAssertFalse
	 * 
	 * @param condition
	 * @param errMsg
	 * @return
	 */
	public static Boolean assertFalse(boolean condition, String errMsg) {

		try {
			Assert.assertFalse(condition);
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"pass");
			return true;
		} catch (Throwable e) {
			log.error("SoftAssertor.assertFalse failed !!! Error Message : " + errMsg);
			addVerificationFailure(errMsg + " Exception msg: " + e.getMessage());
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"FAIL");
			try {
				captureScreenshot();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Method for SoftAssertEquals
	 * 
	 * @param actual
	 * @param expected
	 * @param errMsg
	 * @return
	 */
	public static Boolean assertEquals(Object actual, Object expected, String errMsg) {

		try {
			Assert.assertEquals(actual, expected);
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"pass");
			return true;
		} catch (Throwable e) {
			log.error("SoftAssertor.assertEquals failed !!! Error Message : " + errMsg);
			addVerificationFailure(errMsg + " Exception msg: " + e.getMessage());
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"FAIL");
			try {
				captureScreenshot();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Method for SoftAssertEquals
	 * 
	 * @param condition
	 * @return
	 */
	public static Boolean assertTrue(boolean condition) {

		try {
			Assert.assertTrue(condition);
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"pass");
			return true;
		} catch (Throwable e) {
			log.error("SoftAssertor.assertTrue failed !!! Error Message : " + e.getMessage());
			addVerificationFailure(" Exception msg: " + e.getMessage());
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"FAIL");
			try {
				captureScreenshot();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Method for SoftAssertFalse
	 * 
	 * @param condition
	 * @return
	 */
	public static Boolean assertFalse(boolean condition) {

		try {
			Assert.assertFalse(condition);
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"pass");
			return true;
		} catch (Throwable e) {
			log.error("SoftAssertor.assertFalse failed !!! Error Message : " + e.getMessage());
			addVerificationFailure(" Exception msg: " + e.getMessage());
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"FAIL");
			try {
				captureScreenshot();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Method for SoftAssertEquals
	 * 
	 * @param actual
	 * @param expected
	 * @return
	 */
	public static Boolean assertEquals(Object actual, Object expected) {
		try {
			Assert.assertEquals(actual, expected);
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"pass");
			return true;
		} catch (Throwable e) {
			log.error("SoftAssertor.assertEquals failed !!! Error Message : " + e.getMessage());
			addVerificationFailure(" Exception msg: " + e.getMessage());
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"FAIL");
			try {
				captureScreenshot();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Method for SoftAssertNotNull
	 * 
	 * @param actual
	 * @param errMsg
	 * @return
	 */
	public static Boolean assertNotNull(Object actual, String errMsg) {

		try {
			Assert.assertNotNull(actual);
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"pass");
			return true;
		} catch (Throwable e) {
			log.error("SoftAssertor.assertNotNull failed !!! Error Message : " + errMsg);
			addVerificationFailure(errMsg + " Exception msg: " + e.getMessage());
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"FAIL");
			try {
				captureScreenshot();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Method to fail the test case
	 * 
	 * @return
	 */
	public static Boolean fail() {
		try {
			Assert.fail();
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"pass");
			return true;
		} catch (Throwable e) {
			log.error("SoftAssertor.fail failed !!! Error Message : " + e.getMessage());
			addVerificationFailure(" Exception msg: " + e.getMessage());
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"FAIL");
			try {
				captureScreenshot();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Method to fail the test case
	 * 
	 * @param errMsg
	 * @return
	 */
	public static Boolean fail(String errMsg) {
		try {
			Assert.fail(errMsg);
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"pass");
			return true;
		} catch (Throwable e) {
			log.error("SoftAssertor.fail failed !!! Error Message : " + errMsg);
			addVerificationFailure(errMsg + " Exception msg: " + e.getMessage());
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"FAIL");
			try {
				captureScreenshot();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Method to fail the test case
	 * 
	 * @param msg
	 * @param ex
	 * @return
	 */
	public static Boolean fail(String msg, Throwable ex) {
		try {
			ex.printStackTrace();
			Assert.fail(msg, ex);
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"pass");
			return true;
		} catch (Throwable e) {
			log.error("SoftAssertor.fail failed !!! Error Message : " + msg);
			addVerificationFailure(" Exception msg: " + e.getMessage());
			reporter.AddStepDetailsForNestedReporting("Status", reporter,
					"FAIL");
			try {
				captureScreenshot();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Method to get failures list for reporting
	 * 
	 * @return
	 */
	public static List<String> getVerificationFailures() {
		List<String> verificationFailures = verificationFailuresMap.get(Reporter.getCurrentTestResult());
		return verificationFailures == null ? new ArrayList<String>() : verificationFailures;
	}

	/**
	 * Method to add failures list for reporting
	 * 
	 * @param e
	 */
	public static void addVerificationFailure(String e) {
		List<String> verificationFailures = getVerificationFailures();
		verificationFailures.add(e);
		verificationFailuresMap.put(Reporter.getCurrentTestResult(), verificationFailures);
	}

	/**
	 * Display errors. This method logs all the errors and fails the test method
	 * with the errors captured.
	 */
	public static void displayErrors() {
		String errorsForTest = readErrorsForTest();
		refreshVerificationFailures();
		if (errorsForTest != null && errorsForTest.length() > 0)
			Assert.fail(errorsForTest);
	}

	/**
	 * Display errors for test.
	 *
	 * @param errors
	 *            the errors
	 */
	public void displayErrorsForTest(String errors) {
		if (errors != null && errors.length() > 0)
			Assert.fail(errors);
	}

	/**
	 * Read errors for test.
	 *
	 * @return the string
	 */
	public static String readErrorsForTest() {
		String errMsg = "";
		if (verificationFailuresMap != null && verificationFailuresMap.size() > 0) {
			Set keys = verificationFailuresMap.keySet();
			java.util.Iterator<ITestResult> itr = keys.iterator();
			while (itr.hasNext()) {
				List<String> errorList = verificationFailuresMap.get(itr.next());
				if (errorList != null && errorList.size() > 0) {
					for (int index = 0; index < errorList.size(); index++) {
						log.error(errorList.get(index));
						errMsg = errMsg + errorList.get(index) + "\n";
					}
				}
			}
		}
		return errMsg;
	}

	/**
	 * Refresh verification failures.
	 */
	private static void refreshVerificationFailures() {
		verificationFailuresMap = new HashMap<ITestResult, List<String>>();
	}

}