
/*
 * 
 */
package com.rebtel.listerners;

import org.apache.commons.logging.Log;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;

import com.rebtel.util.LogUtil;

/**
 * The listener interface for receiving suite events. The class that is
 * interested in processing a suite event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addSuiteListener<code> method. When the suite event occurs,
 * that object's appropriate method is invoked.
 *
 */

public class SuiteListener implements ISuiteListener {

	/** The log. */
	static Log log = LogUtil.getLog(SuiteListener.class);

	/**
	 * Suite listener method for on start method
	 */
	public void onStart(ISuite arg0) {
		log.info("Suite Name :::" + arg0.getName() + " :::- Start");
	}

	/**
	 * Suite listener method for on finish method
	 */
	public void onFinish(ISuite arg0) {
		log.info("Suite Name :::" + arg0.getName() + ":::- End");
		log.info("********Results*******");
	}
}
