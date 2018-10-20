package com.rebtel.testbed.core;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.rebtel.core.elementactions.Element;
import com.rebtel.driver.CreateDriver;
import com.rebtel.test.TestBed;
import com.rebtel.util.LogUtil;
import com.rebtel.util.Report;

import org.apache.commons.logging.Log;
import io.appium.java_client.android.AndroidDriver;

public class TestBedConfig {

	public static DesiredCapabilities cap = null;
	public static String deviceType = null;
	public static int maxWaitSeconds = 85;
	public static int maxImplicitWaitSeconds = 5;
	public static int maxInvisibletWaitSeconds = 5;
	public static Configuration config = null; 
	protected static Properties props;
	protected AndroidDriver driver = null;
	protected static String configFile = null;
	protected static String appiumVersion = null;
	protected static String nodeJSPath = null;
	protected static String appiumJSPath = null;
	protected static String appPackage = null;
	protected static String appActivity = null;
	protected static String udid = null;
	protected static String device = null;
	protected static String osVersion = null;
	protected static String port = "4444";
	protected static String reportPath = null;
	protected static String invokeReporting = null;
	protected static String clearAppData = null;
	private static Log logger = LogUtil.getLog(TestBedConfig.class); ;
	public AppiumLauncher appiumLauncher ;
	public CreateDriver createDriver;
	public static Report reporter = new Report();
	public static String takeScreenshot = null;
	public static String dataSheetPath = null;
	public static String screenshotPath = null;
	public static String customizedReportPath = null;
	public static HashMap<String,String> currentTestBedMap = new HashMap<String,String>();
	public static HashMap<String,String> currentPortMap = new HashMap<String,String>();
	public static HashMap<String,String> driverMap = new HashMap<String,String>();
	public static String listeners = null;
	public static String testCoverage = null;
	public static String className = null;
	public static String testBedNames = null;	
	private static TestBedConfig instance;
	
	public TestBedConfig(){
		appiumLauncher = new AppiumLauncher();
		createDriver = new CreateDriver();
	}
	
	public static TestBedConfig getInstance(){
	    if(instance == null){
	        synchronized (TestBedConfig.class) {
	            if(instance == null){
	                instance = new TestBedConfig();
	            }
	        }
	    }
	    return instance;
	}
	/**
	 * readConfig :- 
	 * Reads config file 
	 */
	public static void readConfig(String configFilePath){
		try{
			props = new Properties();
			File configFile = new File(configFilePath);
			logger.info("Absoulte file path - " + configFile.getAbsolutePath());
			FileReader configReadder = new FileReader(configFile.getAbsolutePath());
			props.load(configReadder);
			config = new Configuration(configFile);
			setVariable(props);
		}catch(Exception e){
			logger.error("Exception in readConfig:::"+e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * setVariable :- 
	 * Sets variables for test env  
	 */
	private static void setVariable(Properties props) {
		try{
			maxWaitSeconds = Integer.parseInt(props.getProperty("MAX_WAIT_TIME_SECONDS"));
			maxImplicitWaitSeconds = Integer.parseInt(props.getProperty("IMPLICIT_WAIT_TIME_SECONDS"));
			maxInvisibletWaitSeconds = Integer.parseInt(props.getProperty("MAX_INVISIBLE_WAIT_TIME_SECONDS"));
			port = props.getProperty("PORT");
			osVersion = props.getProperty("OS");
			nodeJSPath = props.getProperty("NODEJSPATH");
			appiumJSPath = props.getProperty("APPIUMJSPATH");
			udid = props.getProperty("UDID");
			device = props.getProperty("DEVICE");
			port = props.getProperty("PORT");
			appActivity = props.getProperty("APPACTIVITY");
			appPackage = props.getProperty("APPPACKAGE");
			deviceType = props.getProperty("DEVICETYPE");
			clearAppData=props.getProperty("CLEARAPPDATA");
			dataSheetPath=props.getProperty("DATASHEETPATH");
			screenshotPath=props.getProperty("SCREENSHOTPATH");
			testBedNames = props.getProperty("TESTBED");
			device = props.getProperty("TESTBED");
			listeners = props.getProperty("LISTENER");
			className = props.getProperty("CLASSNAME");
			testCoverage = props.getProperty("TESTCOVERAGE");
			customizedReportPath = props.getProperty("CUSTOMIZEDREPORTPATH");
			prepareConfigMap();
		}catch(Exception e){
			logger.error("Exception in setVariable:::"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * getPort :- 
	 * Get port number
	 */
	public static String getPort(){
		if(System.getProperty("PORTNO")==null){
			return port;	
		}
		port=System.getProperty("PORTNO");
		return System.getProperty("PORTNO");
	}
	
	/**
	 * getDevice :- 
	 * Get device
	 */
	public String getDevice(){
		return device;
	}
	
	/**
	 * getUdid :- 
	 * Get UDID
	 */
	public String getUdid(){
		return udid;
	}

	/**
	 * getCustomizedReportPath :- 
	 * Get CustomizedReportPath
	 */
	public static String getCustomizedReportPath(){
		return customizedReportPath;
	}
	
	/**
	 * getNodePath :- 
	 * Get NodeJS path
	 */
	public String getNodePath(){
		return nodeJSPath;
	}
	
	/**
	 * getAppiumJSPath :- 
	 * Get Appium JS path  
	 */
	public String getAppiumJSPath(){
		return appiumJSPath;
	}
	
	/**
	 * setDriver :- 
	 * Set Android driver for current object
	 */
	public void setDriver(AndroidDriver dr){
		logger.info(dr.toString());
		this.driver = dr;
	}
	
	/**
	 * getDriver :- 
	 * Get current Android driver 
	 */
	public AndroidDriver getDriver(){
		logger.info(this.driver.toString());
		return this.driver;
	}
	
	/**
	 * getClearAppData :- 
	 * Get Clear App data value  
	 */
	public static String getClearAppData(){		
		return clearAppData;		
	}
	
	/**
	 * getDataSheetPath :- 
	 * Get getDataSheetPath value  
	 */
	public static String getDataSheetPath(){
		return dataSheetPath;
	}
	
	/**
	 * getDataSheetPath :- 
	 * Get getDataSheetPath value  
	 */
	public static String getScreenshotPath(){
		return screenshotPath;
	}
	
	
	/**
	 * getTestBedName :- 
	 * Get TestBedName value  
	 */
	public static String getTestBedName(){
		if(System.getProperty("TESTBEDNAME")==null){
			return testBedNames.trim(); 
		}
		 return System.getProperty("TESTBEDNAME");
	}
	
	/**
	 * getTestBedListnerSize :- 
	 * Get total test bed listeners value  
	 */
	public static int getTestBedListnerSize(){
		return listeners.trim().split(",").length;
	}
	public static String getListner(){
		return listeners.trim();
	}
	
	/**
	 * getTestBedSize :- 
	 * Get total test beds value  
	 */
	public static int getTestBedSize(){
		if(System.getProperty("TESTBEDNAME")==null){
			return testBedNames.trim().split(",").length; 
		}
		 return System.getProperty("TESTBEDNAME").split(",").length;
	}
	
	/**
	 * getTestCoverage :- 
	 * Get test coverage value  
	 */
	public static String getTestCoverage(){
		return testCoverage.trim();
	}
	
	public static String getClassName(){
		logger.info(className);
		return className.trim();
	}
	
	/**
	 * prepareConfigMap :- 
	 * Prepare config map for port and testbed name  
	 */
	public static void prepareConfigMap(){
		int portNumber = Integer.parseInt(getPort());
		for(String testBedName: getTestBedName().split(",")){
			currentTestBedMap.put(testBedName.trim(), props.getProperty(testBedName.trim()).trim());
			currentPortMap.put(testBedName.trim(), Integer.toString(portNumber++));
			logger.info(props.getProperty(testBedName.trim()));
		}
	}
	
	/**
	 * getCurrentPortMap :- 
	 * Get current portMap value  
	 */
	public static HashMap<String, String> getCurrentPortMap(){
		logger.info("Size of Port Map" +currentPortMap.size() );
		if(currentPortMap.size()==0){
			currentPortMap.put(System.getProperty("TESTBEDNAME"), port);
		}
		return currentPortMap;
	}
	
	/**
	 * setCurrentPortMap :- 
	 * Set port Map value  
	 */
	public static void setCurrentPortMap(String port, String testBedName){
			currentPortMap.put(testBedName, port);
	}
	
	/**
	 * getTestBedMap :- 
	 * Get testbed Map value  
	 */
	public static HashMap<String, String> getTestBedMap(){
		return currentTestBedMap;
	}
	
}
