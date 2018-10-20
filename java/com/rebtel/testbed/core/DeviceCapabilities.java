package com.rebtel.testbed.core;

import org.apache.commons.logging.Log;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.rebtel.util.LogUtil;

import io.appium.java_client.remote.AndroidMobileCapabilityType;


public class DeviceCapabilities {
	
	private static final Log logger = LogUtil.getLog(DeviceCapabilities.class);	
	private static DesiredCapabilities capabilities = new DesiredCapabilities();

	/**
	 * setMobileDeviceCapabilities :- 
	 * Sets device capabilities for Android mobile device
	 * */
	public static DesiredCapabilities setMobileDeviceCapabilities(TestBedConfig testBedConfig, String testBedName){
		logger.info("Setting Desiered capabilities as below");
		capabilities.setCapability("deviceName", testBedName);
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("appPackage", TestBedConfig.appPackage);
		capabilities.setCapability("appActivity", TestBedConfig.appActivity);
		capabilities.setCapability("udid", TestBedConfig.getTestBedMap().get(testBedName));
		capabilities.setCapability("platformVersion", TestBedConfig.osVersion);
		capabilities.setCapability("autoAcceptAlerts", true);
		capabilities.setCapability("screenshotWaitTimeout", 60);
		logger.info("Capabilities for " + testBedName+ capabilities.toString());
		return capabilities;
	}
}
