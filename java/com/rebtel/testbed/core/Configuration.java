package com.rebtel.testbed.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Properties;

import org.apache.commons.logging.Log;

import com.rebtel.util.LogUtil;

/**
 * This is configuration class
 * This class loads, reads and returns values of/for configuration parameters
 * */
public class Configuration {

	private final File configFile;
	private final Properties properties;
	private static final Log logger = LogUtil.getLog(Configuration.class);

	/**
	 * Constructor :- 
	 * Initiates configFile and properties
	 * */	
	public Configuration(File configFile){
		this.configFile = configFile;
		this.properties = new Properties();

		try{
			this.load(configFile);
		} catch(Exception e){
			logger.error(e.getMessage());
		}
	}

	/**
	 * load :- 
	 * Loads properties file
	 * */
	private void load(File file) throws IOException{
		try{
			if(file==null){
				throw new FileNotFoundException("Given file is null.");
			}else {
				this.properties.load(new FileInputStream(file));
			}
		}catch(FileNotFoundException e){
			logger.error("Config file not found :- " + e.getMessage());
		}catch(IOException e){
			logger.error("IO Exception encountered :- " + e.getMessage());
			logger.error("Unable to load " + file.getCanonicalPath() + ".\n");
		}catch(NullPointerException e){
			logger.error("Null Pointer Exception :- " + e.getMessage());
		}
	}

	/**
	 * getValue :- 
	 * gets property value of a key
	 * */
	public String getValue(String key){
		String value = "";
		try{
			String systemValue = System.getProperty(key.toLowerCase());
			if(systemValue == null){
				systemValue = System.getProperty(key.toUpperCase());
			}
			String configValue = this.properties.getProperty(key.toLowerCase());
			if(configValue == null){
				configValue = this.properties.getProperty(key.toUpperCase());
			}
			value = (systemValue == null ? configValue : systemValue);
		}catch(SecurityException  se){
			logger.error("Security Exception encountered - " + se.getMessage());
		}catch(NullPointerException ne){
			logger.error("Null Pointer exception for key " + key);
		}catch(IllegalArgumentException ie){
			logger.error("Illeagal Argument Exception - " + ie.getMessage());
		}
		logger.info("Found '" + value +  "' value for key: " + key);
		return value;
	}

	/**
	 * Method to get value
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getValue(String key, String defaultValue) {
		String value = this.getValue(key);
		String finalValue = (value == null ? defaultValue : value);
		logger.info("Returning '" + finalValue +  "' value for key: " + key);
		return finalValue;
	}

	@Override
	public String toString() {
		return "Configuration(" + this.properties.toString() + ")";
	}
}
