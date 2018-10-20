package com.rebtel.pages;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.logging.Log;

import com.rebtel.core.elementactions.Element;
import com.rebtel.listerners.SoftAssertor;
import com.rebtel.util.ExcelReader;
import com.rebtel.util.LogUtil;

import io.appium.java_client.android.AndroidDriver;

public class Login extends Element {

	/** The log. */
	private static Log log = LogUtil.getLog(Login.class);
	public volatile static List<String> newUserTodelete = new ArrayList<String>();
	private boolean runFlag = false;
	public volatile static String emailId = "";
	/** Get current task test data for login screen **/
	private LinkedHashMap<String, String> testData;
	private LinkedHashMap<String, String> loginTestData;

	/**
	 * 
	 * @param driver
	 */
	public Login(AndroidDriver driver) {
		super(driver);
		testData = ExcelReader.getDataInHashMap("login", "loginEmailData");
		loginTestData = ExcelReader.getDataInHashMap("login", "loginTestData");
	}

	/**
	 * Verify LaunchScreen contents and cta text
	 * 
	 * @return
	 */
	public boolean verifyUserLogin() {
		boolean flag =  false;
		flag = verifyLaunchScreen();
		flag = clickGetStarted();
		flag = acceptPermission();
		flag = verifySignUpScreen();
		flag = enterPhoneNumberAndContinue();
		flag = verifyAddressBookScreen();
		flag = clickContinue();
		flag = acceptPermission();
		flag = isLoginSuccessFul();
		
		return flag;
	}
	
	/**
	 * Verify LaunchScreen contents and cta text
	 * 
	 * @return
	 */
	public boolean verifyLaunchScreen() {
		boolean flag = false;
		flag = isElementPresent(commonElements.get("RebtelLogo"))&&
				isElementPresent(commonElements.get("Slogan"))&&
				isElementPresent(commonElements.get("GetStartedCTA"))&&
				isElementPresent(commonElements.get("LegalBindingText"));
		if(!flag)
			SoftAssertor.assertTrue(flag, "All assets not displayed on Launch screen, refer screenshot");

		flag = SoftAssertor.assertEquals(getElementText(commonElements.get("Slogan")), testData.get("LaunchScreenTitle"), 
				"Expected :- "+testData.get("LaunchScreenTitle")+" Actual :- " + getElementText(commonElements.get("Slogan")));

		flag = SoftAssertor.assertEquals(getElementText(commonElements.get("GetStartedCTA")), testData.get("GetStartedCTAText"), 
				"Expected :- "+testData.get("GetStartedCTAText")+" Actual :- " + getElementText(commonElements.get("GetStartedCTA")));

		flag = SoftAssertor.assertEquals(getElementText(commonElements.get("LegalBindingText")), testData.get("LegalBindingText"), 
				"Expected :- "+testData.get("LegalBindingText")+" Actual :- " + getElementText(commonElements.get("LegalBindingText")));

		return flag;
	}

	/**
	 * Click on Get Started CTA
	 * 
	 * @return
	 */
	public boolean clickGetStarted() {
		boolean flag = false;
		flag = SoftAssertor.assertTrue(click(commonElements.get("GetStartedCTA")));
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

	/**
	 * Verify Signup contents and cta text
	 * 
	 * @return
	 */
	public boolean verifySignUpScreen() {
		boolean flag = false;
		flag = isElementPresent(commonElements.get("SignUpHeadLine"))&&
				isElementPresent(commonElements.get("SignUpSubHeadLine"))&&
				isElementPresent(commonElements.get("CountryFlag"))&&
				isElementPresent(commonElements.get("PhoneNumberInput"))&&
				isElementPresent(commonElements.get("LoginContinueCTA"));
		if(!flag)
			SoftAssertor.assertTrue(flag, "All assets not displayed on Launch screen, refer screenshot");

		flag = SoftAssertor.assertEquals(getElementText(commonElements.get("SignUpHeadLine")), testData.get("SignUpHeadLine"), 
				"Expected :- "+testData.get("SignUpHeadLine")+" Actual :- " + getElementText(commonElements.get("SignUpHeadLine")));

		flag = SoftAssertor.assertEquals(getElementText(commonElements.get("SignUpSubHeadLine")), testData.get("SignUpSubHeadLine"), 
				"Expected :- "+testData.get("SignUpSubHeadLine")+" Actual :- " + getElementText(commonElements.get("SignUpSubHeadLine")));

		flag = SoftAssertor.assertEquals(getElementText(commonElements.get("LoginContinueCTA")), testData.get("LoginContinueCTA"), 
				"Expected :- "+testData.get("LoginContinueCTA")+" Actual :- " + getElementText(commonElements.get("LoginContinueCTA")));

		return flag;
	}

	/**
	 * Enter phone number and continue
	 * 
	 * @return
	 */
	public boolean enterPhoneNumberAndContinue() {
		boolean flag = false;
		flag = sendKeys(commonElements.get("PhoneNumberInput"), loginTestData.get("PhoneNumberInput"));
		if(flag)
			flag = click(commonElements.get("LoginContinueCTA"));
		return flag;
	}

	/**
	 * Verify Address book screen and cta text
	 * 
	 * @return
	 */
	public boolean verifyAddressBookScreen() {
		boolean flag = false;
		flag = isElementPresent(commonElements.get("AddressBookHeadLine"))&&
				isElementPresent(commonElements.get("AddressBookSubHeadLine"))&&
				isElementPresent(commonElements.get("AddressBookContinueCTA"));
		if(!flag)
			SoftAssertor.assertTrue(flag, "All assets not displayed on Launch screen, refer screenshot");

		flag = SoftAssertor.assertEquals(getElementText(commonElements.get("AddressBookHeadLine")), testData.get("AddressBookHeadLine"), 
				"Expected :- "+testData.get("AddressBookHeadLine")+" Actual :- " + getElementText(commonElements.get("AddressBookHeadLine")));

		flag = SoftAssertor.assertEquals(getElementText(commonElements.get("AddressBookSubHeadLine")), testData.get("AddressBookSubHeadLine"), 
				"Expected :- "+testData.get("AddressBookSubHeadLine")+" Actual :- " + getElementText(commonElements.get("AddressBookSubHeadLine")));

		flag = SoftAssertor.assertEquals(getElementText(commonElements.get("AddressBookContinueCTA")), testData.get("AddressBookContinueCTA"), 
				"Expected :- "+testData.get("AddressBookContinueCTA")+" Actual :- " + getElementText(commonElements.get("AddressBookContinueCTA")));

		return flag;
	}

	/**
	 * Verify if Login is successful
	 * 
	 * @return
	 */
	public boolean isLoginSuccessFul() {
		boolean flag = false;
		flag = isElementPresent(commonElements.get("DialPadButton"));
		if(!flag)
			SoftAssertor.assertTrue(flag, "All assets not displayed on Launch screen, refer screenshot");

		return flag;
	}

	/**
	 * Click on Continue CTA
	 * 
	 * @return
	 */
	public boolean clickContinue() {
		boolean flag = false;
		flag = SoftAssertor.assertTrue(click(commonElements.get("AddressBookContinueCTA")));
		return flag;
	}

}
