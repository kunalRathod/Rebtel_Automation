package com.rebtel.test;

import java.util.HashMap;

import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeTest;

import com.rebtel.testbed.core.AppiumLauncher;
import com.rebtel.testbed.core.DeviceCapabilities;
import com.rebtel.testbed.core.TestBedConfig;
import com.rebtel.util.LogUtil;

import org.apache.commons.logging.Log;

public class TestBed extends TestBedConfig {

	protected TestBedConfig testBedConfig;
	/** The log. */
	private static Log log = LogUtil.getLog(TestBed.class);
	private String currentTestBedName = "";
	public static HashMap<Long, String> currentTestBedNameMap = new HashMap<Long, String>();
	public static String currentTestBedNameReport ="";
	public String currentDeviceUdid = null;
	public static HashMap<Long, String> currentDeviceUdidMap = new HashMap<Long, String>();
	public static String failureScreenShotFolderPath = null;
	protected static HashMap<String, String> commonElements = null;
	

	/**
	 * prepareBeforeSuite :- Reads configuration file and initializes driver
	 */
	@BeforeTest(alwaysRun = true)
	public void prepareBeforeTest(ITestContext configParameters) {
		try {
			
			setCurrentTestBedName(configParameters.getCurrentXmlTest().getName());
			currentDeviceUdidMap.put(Thread.currentThread().getId(), currentTestBedMap.get(getCurrentTestBedName()));
			log.info("Current device UDID::: "+currentDeviceUdidMap.get(Thread.currentThread().getId()));
			testBedConfig = TestBedConfig.getInstance();
			AppiumLauncher.closeAppiumSession(testBedConfig.currentPortMap.get(getCurrentTestBedName()));
			
			if (clearAppData.equalsIgnoreCase("true"))
				clearAppData();
			driverInit(getCurrentTestBedName());
			currentTestBedNameMap.put(Thread.currentThread().getId(), getCurrentTestBedName());
			currentTestBedNameReport = currentTestBedNameMap.get(Thread.currentThread().getId());
			log.info("Current testbed Name:::" + currentTestBedNameMap.get(Thread.currentThread().getId()));
			
			if (clearAppData.equalsIgnoreCase("true"))
				clearAppData();
			closeAndRelaunchApp();
			synchronized (this) {
			}
		} catch (Exception iex) {
			iex.printStackTrace();
			log.error("Unable to load the property file::: " + iex.getMessage());
		}
	}

	/**
	 * driverInit :- Initialize Android driver
	 */
	private void driverInit(String testBedName) {
		testBedConfig = TestBedConfig.getInstance();
		log.info("Initializing Android driver for testBedName:::"+testBedName);
		try {
			if (AppiumLauncher.isMac()) {
				AppiumLauncher.startAppiumServerForAndroid(testBedConfig, testBedName);
			} else if (AppiumLauncher.isWindows()) {
				AppiumLauncher.launchAppiumSession(testBedConfig, testBedName);
			}
			driver = createDriver.getDriver(DeviceCapabilities.setMobileDeviceCapabilities(testBedConfig, testBedName),
					testBedConfig, testBedName);
			log.info("Driver created is :::" + driver.toString());
			testBedConfig.setDriver(driver);
		} catch (Exception e) {
			log.error("Unable to initialize driver :::" + e.getMessage());
			e.printStackTrace();
		}
	}

	public long systemCurrentTime() {
		long startTime = System.currentTimeMillis();
		return startTime;
	}

	/**
	 * Method to clear and re launch app
	 * 
	 * @throws InterruptedException
	 * 
	 */
	protected void clearAppDataAndLaunchApp() throws InterruptedException {
		clearAppData();
		launchApp();
	}

	/**
	 * Method to clear app data
	 * 
	 * @throws InterruptedException
	 * 
	 */
	protected boolean clearAppData() throws InterruptedException {
		testBedConfig = TestBedConfig.getInstance();
		try {
			AppiumLauncher.executeCommand("adb -s " + currentDeviceUdidMap.get(Thread.currentThread().getId())
					+ " shell pm clear " + testBedConfig.config.getValue("APPPACKAGE"));
			Thread.sleep(3000);
			log.info("Cleared App data");
		} catch (Exception e) {
			log.error("Unable to clear App data:::" + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Method to launch app
	 * 
	 * @throws InterruptedException
	 * 
	 */
	protected boolean launchApp() throws InterruptedException {
		testBedConfig = TestBedConfig.getInstance();
		try {
			AppiumLauncher.executeCommand("adb -s " + currentDeviceUdidMap.get(Thread.currentThread().getId())
					+ " shell am start -n " + testBedConfig.config.getValue("APPPACKAGE") + "/"
					+ testBedConfig.config.getValue("APPACTIVITY"));
			Thread.sleep(3000);
			log.info("Relaunched App");
		} catch (Exception e) {
			log.error("Unable to re launch App:::" + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @throws InterruptedException
	 */
	public void closeAndRelaunchApp() throws InterruptedException {
		log.info("In closeAndRelaunchApp()::::");
		closeApp();
		relaunchApp();
	}

	/**
	 * Method to close app
	 */
	public void closeApp() {
		testBedConfig = TestBedConfig.getInstance();
		AppiumLauncher.executeCommand("adb -s " + currentDeviceUdidMap.get(Thread.currentThread().getId())
		+ " shell am force-stop "+testBedConfig.appPackage);
		log.info("Closing the app");
	}

	/**
	 * Method to re-launch app using appium driver
	 * 
	 * @throws InterruptedException
	 */
	public void relaunchApp() throws InterruptedException {
		testBedConfig = TestBedConfig.getInstance();
		AppiumLauncher.executeCommand("adb -s " + currentDeviceUdidMap.get(Thread.currentThread().getId())
		+ " shell am start -n "+testBedConfig.appPackage+"/"+testBedConfig.appActivity);
		log.info("Re launching app after closing app");
		Thread.sleep(3000);
		log.info("App Relaunched");
	}

	/**
	 * driverCleanUp :- Clean up driver and Appium session once test execution
	 * is completed
	 */
	@AfterSuite(alwaysRun = true)
	public void driverCleanUp() throws InterruptedException {
		testBedConfig = TestBedConfig.getInstance();
		try {
			
			if (testBedConfig.getDriver() == null) {
				log.error("Unable to close appium as driver is null");
			} else {
				testBedConfig.getDriver().quit();
			}
			
		} catch (Exception e) {
			log.error("Exception encountered in driverCleanUp():::" + e.getMessage());
			e.printStackTrace();
		}finally{
			if (!AppiumLauncher.closeAppiumSession(testBedConfig.currentPortMap.get(getCurrentTestBedName()))){
				log.error("Appium session clean up not successfull");
			}
		}
	}

	/**
	 * Method to set current test bed name
	 */
	public void setCurrentTestBedName(String testBedName) {
		this.currentTestBedName = testBedName;
	}

	/**
	 *  Method to get current test bed name
	 */
	public String getCurrentTestBedName() {
		return this.currentTestBedName;
	}

	/**
	 *  Method to set current device UDID
	 */
	public void setCurrentDeviceUdid(String currentDeviceUdid) {
		this.currentDeviceUdid = currentDeviceUdid;
	}

	/**
	 *  Method to get current device UDID
	 */
	public String getcurrentDeviceUdid() {
		return this.currentDeviceUdid;
	}
}
