package com.rebtel.driver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.apache.commons.logging.Log;

import com.rebtel.testbed.core.TestBedConfig;
import com.rebtel.util.LogUtil;

import io.appium.java_client.android.AndroidDriver;

public class CreateDriver {

	private static Log logger = LogUtil.getLog(CreateDriver.class);
	private ThreadLocal<AndroidDriver> mobileDriver = new ThreadLocal<AndroidDriver>();
	private URL serverUrl = null;
	
	/**
	 * Method to create local driver
	 * @param capabilities
	 * @param testBedConfig
	 */
	public void createLocalDriver(DesiredCapabilities capabilities, TestBedConfig testBedConfig, String testBedName){
		AndroidDriver driver = null;
		try {
			serverUrl = new URL("http://" + "127.0.0.1"+ ":" + testBedConfig.currentPortMap.get(testBedName) +"/wd/hub");
			logger.info("createLocalDriver ::: serverUrl:::"+serverUrl);
			logger.info("Driver Capabilities:::"+capabilities);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try{
			driver = new AndroidDriver(serverUrl, capabilities);
			driver.manage().timeouts().implicitlyWait(
					Long.parseLong(testBedConfig.config.getValue("IMPLICIT_WAIT_TIME_SECONDS")), TimeUnit.SECONDS);
			setDriver(driver);
		}catch(Exception ex){
			logger.error("Error while createLocalDriver:::"+ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	/**
	 * Method to get driver
	 * @param capabilities
	 * @param testBedConfig
	 * @return
	 */
	public AndroidDriver getDriver(DesiredCapabilities capabilities, TestBedConfig testBedConfig, String testBedName){
		AndroidDriver driver = null;
		if(mobileDriver.get()==null){
			createLocalDriver(capabilities, testBedConfig, testBedName);
		}
		return mobileDriver.get();
	}
	
	/**
	 * Method to set driver
	 * @param localDriver
	 */
	public void setDriver(AndroidDriver localDriver){
		mobileDriver.set(localDriver);
	}
	
	/**
	 * Method to get driver
	 * @return
	 */
	public AndroidDriver getDriver(){
		return mobileDriver.get();
	}
	
}
