/*
 * 
 */
package com.rebtel.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * This class contains log methods.
 * 
 */
public class LogUtil {

	/**
	 * Gets the log.
	 *
	 * @param className
	 *            the class name
	 * @param fileName
	 *            the file name
	 * @return the log
	 */
	public static Log getLog(Class<?> className, String fileName) {
		Log logger = LogFactory.getLog(className);
		DOMConfigurator.configure(fileName);
		return logger;
	}

	/**
	 * This will create the log with the default file name log4j.xml
	 *
	 * @param className
	 *            the class name
	 * @return the log
	 */
	public static Log getLog(Class<?> className) {
		return getLog(className, "log4j.xml");
	}

	/**
	 * Prints the log message.
	 *
	 * @param message
	 *            the message
	 * @return the string
	 */
	public String printLogMessage(String message) {
		return message;
	}

}
