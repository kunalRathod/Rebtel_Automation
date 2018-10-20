package com.rebtel.pages;

import java.util.LinkedHashMap;

import org.apache.commons.logging.Log;

import com.rebtel.core.elementactions.Element;
import com.rebtel.listerners.SoftAssertor;
import com.rebtel.util.ExcelReader;
import com.rebtel.util.LogUtil;

import io.appium.java_client.android.AndroidDriver;

public class HomePage extends Element {

	/** The log. */
	private static Log log = LogUtil.getLog(Login.class);
	private LinkedHashMap<String, String> loginTestData;

	/**
	 * 
	 * @param driver
	 */
	public HomePage(AndroidDriver driver) {
		super(driver);
		loginTestData = ExcelReader.getDataInHashMap("home", "HomeTestData");
	}
	

	/**
	 * Click on DialPad
	 * 
	 * @return
	 */
	public boolean verifyCallingFunctionality() {
		boolean flag = false;
		flag = clickDialPad();
		flag = verifyCallingScreen();		
		flag = enterPhoneNumberAndCall();
		flag = acceptPermission();
		flag = acceptPermission();
		flag = isCallConnected();
		flag = hangUpCall();
		flag = isCallOver();
		flag = navigateToHomeScreen();
		flag = checkCallRecord();
		
		return flag;
	}
	
	
	/**
	 * Click on DialPad
	 * 
	 * @return
	 */
	public boolean clickDialPad() {
		boolean flag = false;
		flag = SoftAssertor.assertTrue(click(commonElements.get("DialPadButton")));
		click(commonElements.get("DialPadButton"));
		return flag;
	}
	
	/**
	 * Verify Dial Screen
	 * 
	 * @return
	 */
	public boolean verifyCallingScreen() {
		boolean flag = false;
		flag = isElementPresent(commonElements.get("CallButton"))&&
				isElementPresent(commonElements.get("DialpadPhoneText"))&&
				isElementPresent(commonElements.get("CountryFlag"));
		if(!flag)
			SoftAssertor.assertTrue(flag, "All assets not displayed on Dial up screen, refer screenshot");

		return flag;
	}
	
	/**
	 * Enter phone number and click call
	 * 
	 * @return
	 */
	public boolean enterPhoneNumberAndCall() {
		boolean flag = false;
		flag = sendKeys(commonElements.get("DialpadPhoneText"), loginTestData.get("CallNumber"));
		if(flag)
			flag = click(commonElements.get("CallButton"));
		return flag;
	}
	
	/**
	 * Enter phone number and click call
	 * 
	 * @return
	 */
	public boolean isCallConnected() {
		boolean flag = false;
		flag = isElementPresent(commonElements.get("HangUpButton"));
		return flag;
	}
	
	/**
	 * Click HangUp Call
	 * 
	 * @return
	 */
	public boolean hangUpCall() {
		boolean flag = false;
		flag = click(commonElements.get("HangUpButton"));
		return flag;
	}
	
	/**
	 * isCallOver :- verify call is over
	 * 
	 * @return
	 */
	public boolean isCallOver() {
		boolean flag = false;
		flag = isElementPresent(commonElements.get("CallButton"));
		return flag;
	}
	
	/**
	 * checkCallRecord
	 * 
	 * @return
	 */
	public boolean checkCallRecord() {
		boolean flag = false;
		flag = isElementPresent(commonElements.get("ContactName"));
		SoftAssertor.assertEquals(getElementText(commonElements.get("ContactName")).replaceAll(" ", ""), loginTestData.get("CallNumber"));
		return flag;
	}
	
	/**
	 * navigateToHomeScreen
	 * 
	 * @return
	 */
	public boolean navigateToHomeScreen() {
		boolean flag = false;
		flag = click(commonElements.get("CallClose"));
		
		return flag;
	}
	
	/**
	 * Allow permissions
	 * 
	 * @return
	 */
	public boolean acceptPermission() {
		boolean flag = false;
		flag = SoftAssertor.assertTrue(click(commonElements.get("PermissionAllowButton")));
		return flag;
	}
}
