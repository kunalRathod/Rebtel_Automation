package com.rebtel.testbed.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.openqa.selenium.net.UrlChecker;

import com.rebtel.util.LogUtil;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.AndroidServerFlag;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import org.apache.commons.logging.Log;

public class AppiumLauncher {

	private static String OS = System.getProperty("os.name").toLowerCase();
	private static Log logger = LogUtil.getLog(AppiumLauncher.class);
	private static ThreadLocal<AppiumDriverLocalService> appiumDriverLocalService = new ThreadLocal<>();
	
	
	/**
	 * launchAppiumSession :- Launches appium session
	 * @param portNumber testBedConfig
	 * @param portNumber testBedName
	 */
	public static boolean launchAppiumSession(TestBedConfig testBedConfig, String testBedName) {
		try {
			logger.info("***************Starting Appium Server for Android Device:::" + testBedName+ " on OS :::"+OS.toUpperCase());
			CommandLine command = null;
			if (isWindows()) {
				logger.info("Preparing command for Windows:::TestBed:::" + testBedName);
				command = commandForWindows(testBedConfig, testBedName);
			} else if (isMac()) {
				logger.info("Preparing command for Mac");
				command = commandForMac(testBedConfig, testBedName);
			}
			if (command != null) {
				executeCommand(command);
				logger.info("Waiting for Appium server to start");
				String server_url = String.format("http://127.0.0.1:%d/wd/hub",
						Integer.parseInt(testBedConfig.getCurrentPortMap().get(testBedName)));
				if (!waitUntilAppiumStarts(server_url)) {
					logger.info("Failed to start Appium servers");
				}
			}
		} catch (Exception ex) {
			logger.error("Exception occured on launchAppiumSession method :::" + ex.getMessage());
			logger.error(ex.getStackTrace().toString());
			ex.printStackTrace();
		}
		return true;
	}

	/**
	 * startAppiumServerForAndroid :- Launches appium session for Mac machines
	 * @param portNumber testBedConfig
	 * @param portNumber testBedName
	 */
	public static void startAppiumServerForAndroid(TestBedConfig testBedConfig, String testBedName) throws Exception {
		try{
		logger.info("***************Starting Appium Server for Android Device:::" + testBedName+ " on OS :::"+OS.toUpperCase());
		AppiumDriverLocalService appiumDriverLocalService;

		AppiumServiceBuilder builder = new AppiumServiceBuilder().withAppiumJS(new File(testBedConfig.getAppiumJSPath()))
				.withArgument(GeneralServerFlag.LOG_LEVEL, "warn:error").withLogFile(new File(System.getProperty("user.dir")
						+ "/target/" + testBedName + "_"+new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()) + "__d.txt"))

				.withIPAddress("127.0.0.1").withArgument(AndroidServerFlag.SUPPRESS_ADB_KILL_SERVER)

				.usingPort(Integer.parseInt(getPortStatus(testBedConfig.currentPortMap.get(testBedName),testBedName,testBedConfig)));

		appiumDriverLocalService = builder.build();
		appiumDriverLocalService.start();
		setServer(appiumDriverLocalService);
		}
		catch(Exception e){
			logger.error("Unable to start appium for Mac OS:::"+e.getMessage());
			e.printStackTrace();
		}
	}

	private static void setServer(AppiumDriverLocalService server) {
		appiumDriverLocalService.set(server);
	}

	/**
	 * closeAppiumSession :- Close all open appium session
	 * @param portNumber
	 */
	public static boolean closeAppiumSession(String portNumber) {
		try {
			if (isWindows()) {
				logger.info("Executing closeAppiumSession commmand on OS:::"+OS.toUpperCase()+" for port number:::"+portNumber);
				executeCommand("cmd /c taskkill /f /im node.exe");
				executeCommand("cmd /c taskkill /f /im adb.exe");
			} else if (isMac()) {
				logger.info("Executing closeAppiumSession commmand on OS:::"+OS.toUpperCase()+" for port number:::"+portNumber);
				executeCommand("sh " + AppiumLauncher.class.getClassLoader().getResource("killport_mac.sh").getFile()+ " " + portNumber);
			}
		} catch (Exception ex) {
			logger.error("Exception occured on closeAppiumSession method :::" + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * xecuteCommand :- execute commandLine on terminal or windows
	 * @param command
	 */
	private static void executeCommand(CommandLine command) {
		logger.info(command);
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);
		try {
			executor.execute(command, resultHandler);
			logger.info("Command Execution done");
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			logger.info(e.getMessage());
		} catch (ExecuteException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * executeCommand :- execute commandLine on terminal or windows
	 * @param command
	 */
	public static void executeCommand(String command) {
		String s = null;
		try {
			Process p = Runtime.getRuntime().exec(command);
			logger.info("Inside executeCommand****************" + command);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			logger.info("Inside executeCommand  Input****************" + stdInput);
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			logger.info("Inside executeCommand error****************" + stdError);
			// read the output from the command
			logger.info("Standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				logger.debug(s);
			}
			// read any errors from the attempted command
			logger.info("Standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				logger.debug(s);
			}
		} catch (IOException e) {
			logger.error("IOException encountered in executeCommand::: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception encountered in executeCommand::: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 *executeCommand :- execute commandLine on terminal or windows
	 * @param command
	 */
	public static String getExecuteCommandResult(String command) {
		String s = "";
		String value = "";
		try {
			Process p = Runtime.getRuntime().exec(command);
			logger.info("Inside executeCommand****************" + command);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			logger.info("Inside executeCommand  Input****************" + stdInput);
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			logger.info("Inside executeCommand error****************" + stdError);
			// read the output from the command
			logger.info("Standard output of the command:\n");
			while ((s = stdInput.readLine()) != null) {
				value=value.concat(s);
				logger.debug(s);
			}
			// read any errors from the attempted command
			logger.info("Standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				logger.debug(s);
			}
		} catch (IOException e) {
			logger.error("IOException encountered in executeCommand::: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Exception encountered in executeCommand::: " + e.getMessage());
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * commandForWindows :- Execute commands on windows
	 * @param TestBedConfig
	 * @param TestBedName
	 */
	private static CommandLine commandForWindows(TestBedConfig testBedConfig, String testBedName) {
		CommandLine command = new CommandLine("cmd");
		command.addArgument("/c");
		command.addArgument("\"" + testBedConfig.getNodePath() + "\"");
		command.addArgument("\"" + testBedConfig.getAppiumJSPath() + "\"");
		command.addArgument("--address", false);
		command.addArgument("127.0.0.1");
		command.addArgument("--port", false);
		command.addArgument(getPortStatus(testBedConfig.currentPortMap.get(testBedName),testBedName, testBedConfig));
		command.addArgument("-bp", false);
		command.addArgument(getBootstrapPort(getPortStatus(testBedConfig.currentPortMap.get(testBedName),testBedName,testBedConfig)));
		command.addArgument("-U", false);
		command.addArgument(testBedConfig.currentTestBedMap.get(testBedName));
		command.addArgument("--no-reset", false);
		return command;
	}

	/**
	  commandForMac :- Execute commands on Mac
	 *  @param TestBedConfig
	 *  @param TestBedName
	 */
	private static CommandLine commandForMac(TestBedConfig testBedConfig,String testBedName) {
		CommandLine command = new CommandLine(testBedConfig.getNodePath());
		command.addArgument(testBedConfig.getAppiumJSPath());
		command.addArgument("--address", false);
		command.addArgument("127.0.0.1");
		command.addArgument("--port", false);
		command.addArgument(getPortStatus(testBedConfig.currentPortMap.get(testBedName),testBedName,testBedConfig));
		command.addArgument("-bp", false);
		command.addArgument(getBootstrapPort(getPortStatus(testBedConfig.currentPortMap.get(testBedName),testBedName,testBedConfig)));
		command.addArgument("-U", false);
		command.addArgument(testBedConfig.currentTestBedMap.get(testBedName));
		command.addArgument("--no-reset", false);
		return command;
	}

	/**
	  getBootstrapPort :- Get Bootstrap port number
	 *  @param port
	 */
	private static String getBootstrapPort(String port) {
		logger.info("Port number "+port);
		Integer bpPort = Integer.valueOf(port) + 100;
		return bpPort.toString();
	}

	/**
	 * isWindows :- Returns True for Windows machine
	 */
	public static boolean isWindows() {
		return OS.indexOf("win") >= 0;
	}

	/**
	 * isMac :- Returns True for Mac machine
	 */
	public static boolean isMac() {
		return OS.indexOf("mac") >= 0;
	}

	/**
	 * waitUntilAppiumStarts :- Returns True if Appium server starts and false
	 * otherwise
	 * @param server_url
	 */
	private static boolean waitUntilAppiumStarts(String server_url) throws Exception {
		long timeOut = 20000;
		final URL status = new URL(server_url + "/sessions");
		try {
			logger.info("Waiting for Appium server to start");
			new UrlChecker().waitUntilAvailable(timeOut, TimeUnit.MILLISECONDS, status);
			logger.info("Appium Server started");
			return true;
		} catch (UrlChecker.TimeoutException e) {
			logger.error("Exception in waitUntilAppiumStarts method:::" + e.getMessage());
			return false;
		} catch (Exception e) {
			logger.error("Exception in waitUntilAppiumStarts method:::" + e.getMessage());
			return false;
		}
	}
	
	public static void taskKill(){
		executeCommand("taskkill /f /im node.exe");
		executeCommand("taskkill /f /im adb.exe");
	}
	
	/**
	 * executeCommand :- 
	 * Find JSON value from JSON object
	 * @param jsonObj
	 * @param reqData
	 * */
	public static String getPortStatus(String port, String testBedName, TestBedConfig testBedConfig){
		boolean flag = true;
		int portNo=Integer.parseInt(port);
		try {
			for(int i = portNo; i < 5000; i++){
				Process p = executeCommandForPort("netstat -an");
				logger.info("Getting Port Status");
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String inputLine;
				flag = true;
				logger.info("Checking for port " + portNo);
				while ((inputLine = stdInput.readLine()) != null) {
					if(inputLine.contains("127.0.0.1:"+portNo)){
						flag = false;
						logger.info(portNo + " Is currently in use");
					}
				}
				if(flag){
					break;
				}
				portNo++;
				stdInput.close();
			}
			if(flag){
				logger.info(port);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("Unable to find available port");
			logger.info(e);
		}
		testBedConfig.setCurrentPortMap(Integer.toString(portNo),testBedName);
		return Integer.toString(portNo);
	}
		
		/**
		 * executeCommand :- 
		 * Find JSON value from JSON object
		 * @param jsonObj
		 * @param reqData
		 * */
		public static Process executeCommandForPort(String command){
			Process p = null;
			try {
				p = Runtime.getRuntime().exec(command);
			} catch (IOException e) {
				logger.info("IO Exception encountered");
				e.printStackTrace();
			}
			return p;
		}
}
