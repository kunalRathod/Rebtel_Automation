package com.rebtel.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

import com.rebtel.test.TestBed;
import com.rebtel.testbed.core.TestBedConfig;

public class Report extends TestBedConfig {
	private static Logger logger = Logger.getLogger(Report.class.getSimpleName());
	private int iPassStepcount;
	private int iFailStepCount;
	private BufferedWriter bw;
	public Map<String, String> mNestedMapForStep;
	public Map<String, String> mSummaryReport;
	
	public Report() {
		iPassStepcount = 0;
		iFailStepCount = 0;
		mNestedMapForStep = new HashMap<String, String>();
		mSummaryReport = new HashMap<String, String>();
	}
	
	public Boolean CreateFolderForReport(String sReportName) {
		Calendar cal = Calendar.getInstance();
		String sMonthName = new SimpleDateFormat("MMM").format(cal.getTime())
				.toString();
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(Calendar.getInstance().getTime());
		String[] asDate = timeStamp.split("_");
		String sDate = asDate[0];
		String sReportFolderPath = TestBed.reportPath + "/" + sMonthName + "/" + sDate
				+ "/" + System.getProperty("TestCaseName");
		String sSummaryReportFilePath = TestBed.reportPath + "/" + sMonthName + "/"
				+ sDate + "/Summary_Report";
		String sReportPathTillPages = sReportFolderPath + "/" + "pages";
		String sSummaryPathTillPages = sSummaryReportFilePath + "/" + "pages";
		String sReportPathTillScreenShot = sReportFolderPath + "/"
				+ "Screenshots";
		// File system object
		File createFolderTillReportPath = new File(sReportFolderPath);
		File createFolderTillReportPages = new File(sReportPathTillPages);
		File createFolderTillReportScreenshot = new File(
				sReportPathTillScreenShot);
		File createFolderTillSummary = new File(sSummaryReportFilePath);
		File createSummaryPathTillPages = new File(sSummaryPathTillPages);
		// till
		if (!createFolderTillReportPath.exists()) {
			System.out.println("creating directory: " + sReportFolderPath);
			createFolderTillReportPath.mkdirs();
		}
		if (!createFolderTillReportPages.exists()) {
			System.out.println("creating directory: " + sReportPathTillPages);
			createFolderTillReportPages.mkdir();
		}
		if (!createSummaryPathTillPages.exists()) {
			System.out.println("creating directory: " + sSummaryPathTillPages);
			createSummaryPathTillPages.mkdir();
		}
		if (!createFolderTillReportScreenshot.exists()) {
			System.out.println("creating directory: "
					+ sReportPathTillScreenShot);
			createFolderTillReportScreenshot.mkdir();
		}
		if (!createFolderTillSummary.exists()) {
			System.out.println("creating directory: " + sSummaryReportFilePath);
			createFolderTillSummary.mkdirs();
		}
		// File location for CSS
		File filePathTillCss = new File(TestBed.reportPath + "/" + "pages");
		// Copy CSS folder

		try {
			FileUtils.copyDirectory(filePathTillCss,
					createFolderTillReportPages);
			FileUtils
					.copyDirectory(filePathTillCss, createSummaryPathTillPages);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String sbwPath = null;
		if (sReportName.contains("Summary")) {
			sbwPath = sSummaryReportFilePath + "/" + sReportName;
		} else {
			sbwPath = sReportFolderPath + "/" + sReportName;
		}

		File fileReportPath = new File(sbwPath);
		try {

			fileReportPath.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.setProperty("FullReportPath", sbwPath);
		System.setProperty("Screenshot", sReportPathTillScreenShot);
		createFolderTillReportPath = null;
		createFolderTillReportPages = null;
		createFolderTillReportScreenshot = null;
		createSummaryPathTillPages = null;
		createSummaryPathTillPages = null;

		filePathTillCss = null;
		return true;
	}

	public Boolean ReportGenerator(String pageName, String pageUrl) {
		if (TestBed.invokeReporting != null) {
			if (TestBed.invokeReporting.equalsIgnoreCase("Yes")) {
				System.setProperty("TestCaseName", pageName);
				System.setProperty("TestCaseDesc", pageUrl);
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
						.format(Calendar.getInstance().getTime());
				String sbwPath = System.getProperty("TestCaseName") + "_"
						+ timeStamp + ".html";
				CreateFolderForReport(sbwPath);
				String sReportPath = System.getProperty("FullReportPath");
				File fileReportPath = new File(sReportPath);
				if (!fileReportPath.exists()) {
					try {
						fileReportPath.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				FileWriter fw = null;
				try {
					fw = new FileWriter(fileReportPath.getAbsoluteFile(), true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bw = new BufferedWriter(fw);
				try {
					bw.write("<link href='http://fonts.googleapis.com/css?family=Exo+2' rel='stylesheet' type='text/css'> ");
					bw.write("<style type='text/css'>");
					bw.write(".box1{");
					bw.write("font-family: 'Exo 2', sans-serif;");
					bw.write("font-size:22px;");
					bw.write("background-color: #FFB733;");
					bw.write("opacity: 0.8;");
					bw.write("color:#000;");
					bw.write("height:50px;");
					bw.write("font-weight:100;");
					bw.write("width:1024px;");
					bw.write("border-color:#000;");
					bw.write("border-width:2;");
					bw.write("border-top-left-radius: 4px;");
					bw.write("border-top-right-radius:4px;");
					bw.write("border-bottom-left-radius: 4px;");
					bw.write("border-bottom-right-radius:4px;");
					bw.write("box-shadow: 3px 3px #B4B4B4;}");

					bw.write(".gap{");
					bw.write("background-color:#FFF;");
					bw.write("height:10px;");
					bw.write("width:10px;}");

					bw.write(".box2{");
					bw.write("background-color:#C2C5CA;");
					bw.write("opacity: 0.6;");
					bw.write("width: 507px;");
					bw.write("height: auto;");
					bw.write("border-color:#000;");
					bw.write("border-width:2.5;");
					bw.write("border-top-left-radius: 4px;");
					bw.write("border-top-right-radius:4px;");
					bw.write("border-bottom-left-radius: 4px;");
					bw.write("border-bottom-right-radius:4px;");
					bw.write("box-shadow: 3px 3px #B4B4B4;}");

					bw.write(".box3{");
					bw.write("font-family: 'Exo 2', sans-serif;");
					bw.write("font-size:16px;");
					bw.write("font-weight:100;");
					bw.write("color:#000;");
					bw.write("width: 490px;");
					bw.write("height: auto;");
					bw.write("vertical-align:top;");
					bw.write("padding-left: 15px;");
					bw.write("padding-top: 15px;");
					bw.write("padding-bottom: 15px;}");

					bw.write(".box4{");
					bw.write("background-color:#95D295;");
					bw.write("opacity: 0.6;");
					bw.write("width: 507px;");
					bw.write("height: auto;");
					bw.write("border-color:#000;");
					bw.write("border-width:2;");
					bw.write("border-top-left-radius: 4px;");
					bw.write("border-top-right-radius:4px;");
					bw.write("border-bottom-left-radius: 4px;");
					bw.write("border-bottom-right-radius:4px;");
					bw.write("box-shadow: 3px 3px #B4B4B4;}");

					bw.write(".f3{");
					bw.write("font-size: 13px;");
					bw.write("font-family:Calibri;}");
					bw.write(".f3:HOVER{;");
					bw.write("background-color: white;}");

					bw.write(".tabledesign{");
					bw.write("border-collapse:collapse;");
					bw.write("border-color:#000;");
					bw.write("border-bottom-left-radius: 6px;");
					bw.write("box-shadow: 3px 3px #B4B4B4;}");

					bw.write(".tablestyle{");
					bw.write("font-family: 'Exo 2', sans-serif;");
					bw.write("font-size:16px;");
					bw.write("font-weight:100;");
					bw.write("color:#000;");
					bw.write("padding-left: 8px;");
					bw.write("padding-top: 8px;");
					bw.write("padding-bottom: 8px;");
					bw.write("padding-right: 8px;}");

					bw.write(".tableheaderstyle{");
					bw.write("font-family: 'Exo 2', sans-serif;");
					bw.write("font-size:16px;");
					bw.write("font-weight:100;");
					bw.write("color:#fff;");
					bw.write("padding-left: 8px;");
					bw.write("opacity:0.8;");
					bw.write("padding-top: 8px;");
					bw.write("padding-bottom: 8px;");
					bw.write("padding-right: 8px;");
					bw.write("background-color:#000;}");
					bw.write("</style>");
					// 'end of css

					bw.write("<!doctype html>");
					bw.write("<html>");
					bw.write("<head>");

					bw.write("<script type=" + "\"text/javascript\"" + "src="
							+ "\"./pages/js/jquery.min.js\"" + "></script>");
					bw.write("<script type=" + "\"text/javascript\"" + "src="
							+ "\"./pages/js/jquery.magnifier.js\""
							+ "></script>");

					bw.write("<meta charset= " + "\"utf-8\"" + ">");
					bw.write("<title>Test Case Execution Report</title>");
					bw.write("</head>");

					bw.write("<script>");
					bw.write("function toggle(thisname, value) { ");
					bw.write("tr=document.getElementsByTagName('tr'); ");
					bw.write("for (i=0;i<tr.length;i++){ ");
					bw.write("if (tr[i].getAttribute(thisname) == value){ ");
					bw.write("if ( tr[i].style.display=='none' ){ ");
					bw.write("tr[i].style.display = '';");
					bw.write("}");
					bw.write("else {");
					bw.write("tr[i].style.display = 'none';");
					bw.write("}}}}");
					bw.write("</script>");

					bw.write("<script>");
					bw.write("function OpenCollapse(thisname, value) { ");
					bw.write("tr=document.getElementsByTagName('tr'); ");
					bw.write("for (i=0;i<tr.length;i++){ ");
					bw.write("if (tr[i].getAttribute(thisname)){ ");
					bw.write("if ( value == 'Expand' ){ ");
					bw.write("tr[i].style.display = '';");
					bw.write("}");
					bw.write("else {");
					bw.write("tr[i].style.display = 'none';");
					bw.write("}}}}");
					bw.write("</script>");

					bw.write("<body>");
					bw.write("<div align=" + "\"center\"" + "style="
							+ "\"height:auto\"" + ">");
					bw.write("<table width=" + "\"1024\"" + " border="
							+ "\"0\"" + "><tr>");
					bw.write("<img alt=" + "Intuit" + " src="
							+ "\"./pages/images/Intuit.png\"" + "width ="
							+ "\"180\"" + " height=" + "\"70\"" + " align = "
							+ "\"center\"" + ">");
					bw.write("</tr></table>");

					bw.write("<table width=" + "\"1024\"" + "border=" + "\"3\""
							+ ">");
					bw.write("<tr><th colspan=" + "\"5\"" + " class="
							+ "\"box1\"" + "style=" + "\"border-color:\""
							+ "; scope=" + "\"col\""
							+ " >Test Case Execution Report</th></tr>");
					bw.write("<tr><th colspan=" + "\"5\"" + " class="
							+ "\"gap\"" + "scope=" + "\"col\"" + "></th></tr>");
					bw.write("<tr>");
					bw.write("<td class=" + "\"box2\"" + " valign=" + "\"top\""
							+ ">");
					bw.write("<table border=" + "\"0\"" + " class="
							+ "\"box3\"" + ">");
					bw.write("<tr><td colspan=" + "\"3\""
							+ ">Summary</td></tr>");
					bw.write("<tr><td colspan=" + "\"3\"" + "style="
							+ "\"height:5px\"" + ">&nbsp;</td></tr>");

					bw.write("<tr><td valign=" + "\"top\""
							+ ">Test Case Name</td>");
					bw.write("<td valign=" + "\"top\"" + ">:</td>");
					bw.write("<td valign=" + "\"top\"" + "align="
							+ "\"justify\"" + ">"
							+ System.getProperty("TestCaseName") + "</td></tr>");
					bw.write("<tr><td valign=" + "\"top\""
							+ ">Description</td>");
					bw.write("<td valign=" + "\"top\"" + ">:</td>");
					bw.write("<td valign=" + "\"top\"" + "align="
							+ "\"justify\"" + ">"
							+ System.getProperty("TestCaseDesc") + "</td></tr>");
					bw.write("<tr><td width=" + "\"133\"" + "valign="
							+ "\"top\"" + ">Status</td>");
					bw.write("<td width=" + "\"4\"" + " valign=" + "\"top\""
							+ ">:</td>");
					bw.write("<td width=" + "\"356\"" + "valign=" + "\"top\""
							+ "align=" + "\"justify\"" + "style="
							+ "\"color:green\"" + ">NA</td></tr>");
					bw.write("<tr><td valign=" + "\"top\"" + "width ="
							+ "\"250\"" + ">Verification points Passed</td>");
					bw.write("<td valign=" + "\"top\"" + ">:</td>");
					bw.write("<td valign=" + "\"top\"" + "align="
							+ "\"justify\"" + " style=" + "\"color:green\""
							+ ">NA</td></tr>");
					bw.write("<tr><td valign=" + "\"top\"" + " width ="
							+ "\"250\"" + ">Verification points Failed</td>");
					bw.write("<td valign=" + "\"top\"" + ">:</td>");
					bw.write("<td valign=" + "\"top\"" + " align="
							+ "\"justify\"" + "style=" + "\"color:red\""
							+ ">NA</td></tr>");
					bw.write("</table></td>");
					bw.write("<td class=" + "\"gap\"" + "></td>");
					bw.write("<td class=" + "\"box4\"" + "valign=" + "\"top\""
							+ ">");
					bw.write("<table border=" + "\"0\"" + "class=" + "\"box3\""
							+ ">");
					bw.write("<tr><td colspan=" + "\"3\""
							+ ">Execution/Platform Details</td></tr>");
					bw.write("<tr><td colspan=" + "\"3\"" + "style="
							+ "\"height:5px\"" + ">&nbsp;</td></tr>");

					bw.write("<tr><td valign=" + "\"top\""
							+ ">Execution Date Time</td>");
					bw.write("<td valign=" + "\"top\"" + ">:</td>");
					bw.write("<td valign=" + "\"top\"" + "align="
							+ "\"justify\"" + ">" + timeStamp + "</td></tr>");
					bw.write("<tr><td valign=" + "\"top\""
							+ ">Total Time Taken</td>");
					bw.write("<td valign=" + "\"top\"" + ">:</td>");
					bw.write("<td valign=" + "\"top\"" + " align="
							+ "\"justify\"" + ">NA</td></tr>");
					String remote = TestBed.config.getValue("remote");
					String browser = TestBed.config.getValue("browser");
					if (remote.equalsIgnoreCase("yes") || remote.equalsIgnoreCase("Sauce")) {
						Iterator it = cap.asMap().entrySet().iterator();
						while (it.hasNext()) {
							Map.Entry p = (Map.Entry) it.next();
							if (!p.getKey().toString().contains("profile")
									&& !p.getKey().toString()
											.contains("loggingPrefs")) {
								bw.write("<tr><td width=" + "\"156\""
										+ "valign=" + "\"top\"" + ">"
										+ p.getKey() + "</td>");
								bw.write("<td width=" + "\"4\"" + "valign="
										+ "\"top\"" + ">:</td>");
								bw.write("<td width=" + "\"316\"" + " valign="
										+ "\"top\"" + " align=" + "\"justify\""
										+ ">" + p.getValue() + "</td></tr>");
							}

						}
					} else {
						bw.write("<tr><td valign=" + "\"top\""
								+ ">Browser</td>");
						bw.write("<td valign=" + "\"top\"" + ">:</td>");
						bw.write("<td valign=" + "\"top\"" + " align="
								+ "\"justify\"" + ">" + browser + "</td></tr>");

						bw.write("<tr><td width=" + "\"156\"" + "valign="
								+ "\"top\"" + ">OS</td>");
						bw.write("<td width=" + "\"4\"" + "valign=" + "\"top\""
								+ ">:</td>");
						bw.write("<td width=" + "\"316\"" + " valign="
								+ "\"top\"" + " align=" + "\"justify\"" + ">"
								+ device + " " + osVersion + "</td></tr>");
					}
					bw.write("<tr><td width=" + "\"156\"" + "valign="
							+ "\"top\"" + ">Enviroment</td>");
					bw.write("<td width=" + "\"4\"" + "valign=" + "\"top\""
							+ ">:</td>");
					bw.write("<td width=" + "\"316\"" + " valign=" + "\"top\""
							+ " align=" + "\"justify\"" + ">" 
							+ "</td></tr>");
					bw.write("<tr class=" + "\"gap\"" + "></tr>");
					bw.write("<tr class=" + "\"gap\"" + "></tr>");
					bw.write("<tr class=" + "\"gap\"" + "></tr>");
					bw.write("<tr><td><button onClick="
							+ "\"OpenCollapse('id','Expand');\""
							+ " style="
							+ "\"border-width: 4px  ;background-color:#FFB733; height:26px; width:160px\""
							+ "><b>Expand All </b></button></td>");
					bw.write("<td></td>");
					bw.write("<td><button onClick="
							+ "\"OpenCollapse('id','Collapse');\""
							+ " style="
							+ "\"border-width: 4px ;background-color:#FFB733; height:26px; width:160px\""
							+ "><b>Collapse All </b></button></td></tr>");
					bw.write("</table></td></tr>");
					bw.write("<tr><td class=" + "\"gap\"" + "></td></tr>");
					bw.write("<tr><th colspan=" + "\"5\"" + " class="
							+ "\"box1\"" + " scope=" + "\"col\""
							+ ">Test Case Details</th></tr>");
					bw.write("<tr><td class=" + "\"gap\"" + "></td></tr>");
					bw.write("<tr><td height=" + "\"-1\"" + "colspan="
							+ "\"5\"" + "valign=" + "\"top\"" + ">");
					bw.write("<table width=" + "\"1024\"" + "  border="
							+ "\"1\"" + "class=" + "\"tabledesign\""
							+ " cellspacing=" + "\"0\"" + ">");
					bw.write("<tr valign=" + "\"top\"" + ">");
					bw.write("<td width=" + "\"48\"" + " class="
							+ "\"tableheaderstyle\"" + ">Sr No</td>");
					bw.write("<td width=" + "\"237\"" + "class="
							+ "\"tableheaderstyle\"" + ">Step Description</td>");
					/*
					 * bw.write("<td width=+" + "\"131\"" + " class=" +
					 * "\"tableheaderstyle\"" + ">Input Value</td>");
					 */
					bw.write("<td width=" + "\"201\"" + " class="
							+ "\"tableheaderstyle\"" + ">Expected Result</td>");
					bw.write("<td width=" + "\"192\"" + " class="
							+ "\"tableheaderstyle\"" + ">Actual Result</td>");
					bw.write("<td width=" + "\"72\"" + " class="
							+ "\"tableheaderstyle\"" + ">Status</td>");
					/*
					 * bw.write("<td width=" + "\"113\"" + "class=" +
					 * "\"tableheaderstyle\"" + ">Time Taken</td>");
					 */
					bw.write("<td width=" + "\"113\"" + "class="
							+ "\"tableheaderstyle\"" + ">Screenshot</td>");
					bw.write("</tr>");
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public Boolean writeStepResult(String iSrNo, String strStepDescription,
			String strTestData, String strExpResult, String strActResult,
			String strStatus, String strTimeTaken, Boolean isScreenshot) {
		if (TestBed.invokeReporting != null) {
			if (TestBed.invokeReporting.equalsIgnoreCase("Yes")) {
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
						.format(Calendar.getInstance().getTime());
				if (isScreenshot) {
					String sScreenShotFilePath = System
								.getProperty("Screenshot")
								+ "\\"
								+ timeStamp
								+ ".png";
						// Get Screenshot COde need to add
				}
				String sReportPath = System.getProperty("FullReportPath");
				File fileReportPath = new File(sReportPath);
				if (!fileReportPath.exists()) {
					try {
						fileReportPath.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				FileWriter fw = null;

				try {
					fw = new FileWriter(fileReportPath.getAbsoluteFile(), true);

					bw = new BufferedWriter(fw);
					bw.write("<tr class=" + "\"tablestyle\"" + " valign="
							+ "\"top\"" + ">");
					bw.write("<td >" + iSrNo + "</td>");
					bw.write("<td >" + strStepDescription + "</td>");
					bw.write("<td >" + strTestData + "</td>");
					bw.write("<td >" + strExpResult + "</td>");
					bw.write("<td >" + strActResult + "</td>");
					if (strStatus.equalsIgnoreCase("pass")) {
						bw.write("<td><font color= " + "\"Green\"" + ">"
								+ strStatus + "</td>");
						iPassStepcount = iPassStepcount + 1;
					} else {
						bw.write("<td><font color=" + "\"Red\"" + ">"
								+ strStatus + "</td>");
						iFailStepCount = iFailStepCount + 1;
					}

					bw.write("<td >" + strTimeTaken + "</td>");
					bw.write("<td >");
					String sRelativePath = null;
					if (isScreenshot) {
						sRelativePath = "./Screenshots/Screenshot_" + timeStamp
								+ ".png";
						bw.write("<img src=" + "\"" + sRelativePath + "\""
								+ " border=" + "\"1\"" + " style="
								+ "\"width:50px;height:30px\"" + "/>");
						bw.write("<a href=#" + "\" rel=\"" + "magnify[sc_"
								+ timeStamp + "]\"" + ">Zoom in</a>");
					} else {
						bw.write("No Screenshot available");
					}

					if (isScreenshot) {
						bw.write("<img class=" + "\"magnify\"" + " type ="
								+ "\"hidden\"" + "id=" + "\"sc_" + timeStamp
								+ "\" src=\"");
						bw.write(sRelativePath + "\" border=\"" + "1"
								+ " data-magnifyby=" + "\"20\"" + "style="
								+ "\"width:50px;height:30px;display: none;\""
								+ " />");
						bw.close();
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public Boolean writeNestedStep(String sNestedTitle) {
		if (TestBed.invokeReporting != null) {
			if (TestBed.invokeReporting.equalsIgnoreCase("Yes")) {
				String sReportPath = System.getProperty("FullReportPath");
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
						.format(Calendar.getInstance().getTime());
				String[] arrStepDescription = null;
				String[] arrTestData = null;
				String[] arrExpResult = null;
				String[] arrActualresult = null;
				String[] arrStatus = null;
				/* String[] arrTimeTaken = null; */
				String[] arrScreenshot = null;
				File fileReportPath = new File(sReportPath);
				if (!fileReportPath.exists()) {
					try {
						fileReportPath.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				FileWriter fw = null;

				if (mNestedMapForStep.size() > 0) {
					arrStepDescription = mNestedMapForStep.get(
							"StepDescription").split("##");
					// arrTestData =
					// mNestedMapForStep.get("TestData").split("##");
					arrExpResult = mNestedMapForStep.get("ExpResult").split(
							"##");
					arrActualresult = mNestedMapForStep.get("Actualresult")
							.split("##");
					arrStatus = mNestedMapForStep.get("Status").split("##");
					/*
					 * arrTimeTaken =
					 * mNestedMapForStep.get("TimeTaken").split("##");
					 */
					arrScreenshot = mNestedMapForStep.get("Screenshot").split(
							"##");
				}
				try {
					fw = new FileWriter(fileReportPath.getAbsoluteFile(), true);

					bw = new BufferedWriter(fw);
					bw.write("<tr valign=" + "\"top\"" + ">");
					bw.write("<td ><span onClick=" + "\"toggle('id','"
							+ sNestedTitle + "');\"" + "> <a href="
							+ "\"javascript:void 0\"" + ">" + sNestedTitle
							+ "</a> </span></td>");
					bw.write("<td class=" + "\"tablestyle\"" + ">"
							+ "Verifying " + sNestedTitle + "</td>");
					bw.write("<td>NA</td>");
					bw.write("<td>NA</td>");
					bw.write("<td>NA</td>");
					bw.write("<td>NA</td>");
					// bw.write("<td>NA</td>");
					bw.write("</tr>");
					if (arrStepDescription != null) {
						for (int iLine = 0; iLine < arrStepDescription.length; iLine++) {

							bw.write("<tr class =" + "\"f3\"" + " valign="
									+ "\"top\"" + "id =" + sNestedTitle
									+ " style=" + "\"display:none\"" + ">");
							bw.write("<td align =" + "\"middle\"" + ">"
									+ sNestedTitle + "." + (iLine + 1)
									+ "</td>");
							bw.write("<td>" + arrStepDescription[iLine]
									+ "</td> ");
							/* bw.write("<td>" + "NA" + "</td>"); */
							try {
								bw.write("<td>" + arrExpResult[iLine] + "</td>");
							} catch (Exception e) {
								bw.write("<td>"
										+ "Expected Result Not Found Please check the Data/Locator "
										+ "</td>");
							}
							try {
								bw.write("<td>" + arrActualresult[iLine]
										+ "</td>");
							} catch (Exception e) {
								bw.write("<td>"
										+ "Actual Result Not Found Please check the Data/Locator "
										+ "</td>");
							}
							try {
								if (arrStatus[iLine].trim().equalsIgnoreCase(
										"pass")) {
									bw.write("<td><font color=" + "\"Green\""
											+ ">" + "PASS" + "</td>");
									iPassStepcount = iPassStepcount + 1;
								} else {
									bw.write("<td><font color=" + "\"Red\""
											+ ">" + "FAIL" + "</td>");
									iFailStepCount = iFailStepCount + 1;
								}
							} catch (Exception e) {
								bw.write("<td>"
										+ "(Status Result Not Found Please check the Verification Points Making Testcase Fail) ");
								bw.write("<font color=" + "\"Red\"" + ">"
										+ "FAIL" + "</td>");
								iFailStepCount = iFailStepCount + 1;
							}

							/* bw.write("<td>" + arrTimeTaken[iLine] + "</td>"); */

							bw.write("<td>");
							try {
								if (!arrScreenshot[iLine].trim()
										.equalsIgnoreCase("NA")) {
									String sRelativePath = arrScreenshot[iLine];
									bw.write("<img src=" + "\"" + sRelativePath
											+ "\"" + " border=" + "\"1\""
											+ " style="
											+ "\"width:50px;height:30px\""
											+ "/>");
									bw.write("<a href=" + "\"#\"" + "\" rel=\""
											+ "magnify[sc_" + iLine + "_"
											+ timeStamp + "]\""
											+ " >Zoom in</a> ");
								} else {
									bw.write("No Screenshot Available");
								}
							} catch (Exception e) {
								bw.write("<td>"
										+ "No Screenshot Found Please check the Verification Point"
										+ "</td>");

							}
							bw.write("</td>");
							bw.write("</tr>");
							if (!arrScreenshot[iLine].trim().equalsIgnoreCase(
									"NA")) {
								bw.write("<img class=" + "\"magnify\""
										+ " type =" + "\"hidden\"" + "id="
										+ "\"sc_" + iLine + "_" + timeStamp
										+ "\" src=\"");
								bw.write(arrScreenshot[iLine]
										+ "\" border="
										+ "\" 1\""
										+ " data-magnifyby="
										+ "\"35\""
										+ "style="
										+ "\"width:50px;height:30px;display: none;\""
										+ " style = " + "\"display:none\""
										+ " />");
							}

						}
					}
					bw.close();
					mNestedMapForStep.clear();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public Boolean CreateSummary() {
		if (TestBed.invokeReporting != null) {
			if (TestBed.invokeReporting.equalsIgnoreCase("Yes")) {
				String strStatusText = null;
				String strVerificationPointsPassText;
				String strVerificationPointsFailText;
				String strTimeTakenText;
				String iPercentageOfPassFail;
				if (iFailStepCount > 0) {
					strStatusText = "Status</td><td width=" + "\"4\""
							+ " valign=" + "\"top\"" + ">:</td><td width="
							+ "\"356\"" + "valign=" + "\"top\"" + "align="
							+ "\"justify\"" + " style=" + "\"color:red\""
							+ ">FAIL";
				} else {
					strStatusText = "Status</td><td width=" + "\"4\""
							+ " valign=" + "\"top\"" + ">:</td><td width="
							+ "\"356\"" + "valign=" + "\"top\"" + "align="
							+ "\"justify\"" + "style=" + "\"color:green\""
							+ ">PASS";
				}
				strVerificationPointsPassText = "Verification points Passed</td><td valign="
						+ "\"top\""
						+ ">:</td><td valign="
						+ "\"top\""
						+ " align="
						+ "\"justify\""
						+ "style="
						+ "\"color:green\"" + ">" + iPassStepcount;
				strVerificationPointsFailText = "Verification points Failed</td><td valign="
						+ "\"top\""
						+ ">:</td><td valign="
						+ "\"top\""
						+ " align="
						+ "\"justify\""
						+ "style="
						+ "\"color:red\"" + ">" + iFailStepCount;
				/*
				 * strTimeTakenText = "Total Time Taken</td><td valign=" +
				 * "\"top\"" + ">:</td><td valign=" + "\"top\"" + " align=" +
				 * "\"justify\"" + ">" + timeElapsed + " s";
				 */
				String sReportPath = System.getProperty("FullReportPath");
				File fileReportPath = new File(sReportPath);
				if (!fileReportPath.exists()) {
					try {
						fileReportPath.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				FileWriter fw = null;
				try {
					fw = new FileWriter(fileReportPath.getAbsoluteFile(), true);

					bw = new BufferedWriter(fw);
					iPercentageOfPassFail = ((iPassStepcount * 100) / (iPassStepcount + iFailStepCount))
							+ "%";
					bw.write("</table></td>");
					bw.write("</tr>");
					bw.write("</table>");
					bw.write("</div></body>	</html>");
					bw.close();
					@SuppressWarnings("resource")
					String sReportContent = new Scanner(new File(sReportPath))
							.useDelimiter("\\Z").next();
					fw = new FileWriter(fileReportPath.getAbsoluteFile());

					bw = new BufferedWriter(fw);
					sReportContent = sReportContent.replace(
							"Status</td><td width=" + "\"4\"" + " valign="
									+ "\"top\"" + ">:</td><td width="
									+ "\"356\"" + "valign=" + "\"top\""
									+ "align=" + "\"justify\"" + "style="
									+ "\"color:green\"" + ">NA", strStatusText);
					sReportContent = sReportContent.replace(
							"Verification points Passed</td><td valign="
									+ "\"top\"" + ">:</td><td valign="
									+ "\"top\"" + "align=" + "\"justify\""
									+ " style=" + "\"color:green\"" + ">NA",
							strVerificationPointsPassText);
					sReportContent = sReportContent.replace(
							"Verification points Failed</td><td valign="
									+ "\"top\"" + ">:</td><td valign="
									+ "\"top\"" + " align=" + "\"justify\""
									+ "style=" + "\"color:red\"" + ">NA",
							strVerificationPointsFailText);
					/*
					 * sReportContent = sReportContent.replace(
					 * "Total Time Taken</td><td valign=" + "\"top\"" +
					 * ">:</td><td valign=" + "\"top\"" + " align=" +
					 * "\"justify\"" + ">NA", strTimeTakenText);
					 */
					bw.write(sReportContent);
					bw.close();
					AddSummaryReportDetails("TestCaseName", TestBed.reporter,
							System.getProperty("TestCaseName"));
					AddSummaryReportDetails("PassCount", TestBed.reporter,
							Integer.toString(iPassStepcount));
					AddSummaryReportDetails("FailCount", TestBed.reporter,
							Integer.toString(iFailStepCount));
					Path pathAbsoute = Paths.get(sReportPath);
					AddSummaryReportDetails("ReportPath", TestBed.reporter,
							pathAbsoute.toAbsolutePath().toString());
					iPassStepcount = 0;
					iFailStepCount = 0;

					System.clearProperty("CurrentExecutingTc");

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public void TestCaseSummaryReport() {
		if (TestBed.invokeReporting != null) {
			if (TestBed.invokeReporting.equalsIgnoreCase("Yes")) {
				int iPassCount = 0;
				int iFailCount = 0;
				String[] asTestCaseName = null;
				String[] asPassCount = null;
				String[] asFailCount = null;
				String[] asReportPath = null;

				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
						.format(Calendar.getInstance().getTime());
				String sSummaryReportName = "SummaryReport" + "_" + timeStamp
						+ ".html";
				if (mSummaryReport.size() > 0) {
					asTestCaseName = mSummaryReport.get("TestCaseName").split(
							"###");
					asPassCount = mSummaryReport.get("PassCount").split("###");
					asFailCount = mSummaryReport.get("FailCount").split("###");
					asReportPath = mSummaryReport.get("ReportPath")
							.split("###");
				}

				for (int i = 0; i < asPassCount.length; i++) {

					iPassCount = iPassCount + Integer.parseInt(asPassCount[i]);
				}
				for (int i = 0; i < asFailCount.length; i++) {

					iFailCount = iFailCount + Integer.parseInt(asFailCount[i]);
				}

				CreateFolderForReport(sSummaryReportName);
				String sSummaryReportPath = System
						.getProperty("FullReportPath");
				File fileReportPath = new File(sSummaryReportPath);
				FileWriter fw;
				try {
					fw = new FileWriter(fileReportPath.getAbsoluteFile(), true);
					bw = new BufferedWriter(fw);
					bw.write("<link href='http://fonts.googleapis.com/css?family=Exo+2' rel='stylesheet' type='text/css'>");
					bw.write("<script type=" + "\"text/javascript\"" + " src="
							+ "\"https://www.google.com/jsapi\"" + "></script>");

					bw.write("<script type=" + "\"text/javascript\"" + ">");
					bw.write("google.load(" + "\"visualization\"" + ","
							+ "\"1\"" + "," + " {packages:[" + "\"corechart\""
							+ "]});");
					bw.write("google.setOnLoadCallback(drawChart);");
					bw.write("function drawChart() {");
					bw.write("var data = google.visualization.arrayToDataTable([");
					bw.write("['Task', 'Hours per Day'],");
					bw.write("['Passed'," + iPassCount + "],");
					bw.write("['Failed'," + iFailCount + "],");
					bw.write("]);");
					bw.write("var options = {");
					bw.write("title: 'Execution %',");
					bw.write("slices: {");
					bw.write("0: { color: '#32CD32' },");
					bw.write("1: { color: '#FF3333' },");
					bw.write("2: { color: '#4D70DB' }");
					bw.write("}};");
					bw.write("var chart = new google.visualization.PieChart(document.getElementById('piechart'));");
					bw.write("chart.draw(data, options);}");
					bw.write("</script>");

					bw.write("<script>");
					bw.write("function toggle(thisname, value) { ");
					bw.write("tr=document.getElementsByTagName('tr');");
					bw.write("for (i=0;i<tr.length;i++){ ");
					bw.write("if (tr[i].getAttribute(thisname) == value){ ");
					bw.write("if ( tr[i].style.display=='none' ){");
					bw.write("tr[i].style.display = '';}");
					bw.write("else {tr[i].style.display = 'none';}}}}");
					bw.write("</script>");
					// css
					bw.write("<style type=" + "\"text/css\"" + ">");
					bw.write(".box1{");
					bw.write("font-family: 'Exo 2', sans-serif;");
					bw.write("font-size:22px;");
					bw.write("background-color: #FFB733;");
					bw.write("opacity: 0.8;");
					bw.write("color:#000;");
					bw.write("height:50px;");
					bw.write("font-weight:100;");
					bw.write("width:1024px;");
					bw.write("border-color:#000;");
					bw.write("border-width:2.5;");
					bw.write("border-top-left-radius: 4px;");
					bw.write("border-top-right-radius:4px;");
					bw.write("border-bottom-left-radius: 4px;");
					bw.write("border-bottom-right-radius:4px;");
					bw.write("box-shadow: 3px 3px #B4B4B4;}");

					bw.write(".gap{");
					bw.write("background-color:#FFF;");
					bw.write("height:10px;");
					bw.write("width:10px;}");

					bw.write(".box2{");
					bw.write("background-color:#c6ccd3;");
					bw.write("opacity: 0.6;");
					bw.write("width: 150px;");
					bw.write("height: auto;");
					bw.write("border-color:#000;");
					bw.write("border-width:2.5;");
					bw.write("border-top-left-radius: 4px;");
					bw.write("border-top-right-radius:4px;");
					bw.write("border-bottom-left-radius: 4px;");
					bw.write("border-bottom-right-radius:4px;");
					bw.write("box-shadow: 3px 3px #B4B4B4;}");

					bw.write(".box3{");
					bw.write("font-family: 'Exo 2', sans-serif;");
					bw.write("font-size:16px;");
					bw.write("font-weight:100;");
					bw.write("color:#000;");
					bw.write("width: 400px;");
					bw.write("height: auto;");
					bw.write("vertical-align:top;");
					bw.write("padding-left: 15px;");
					bw.write("padding-top: 15px;");
					bw.write("padding-bottom: 15px;}");

					bw.write(".box4{");
					bw.write("background-color: #fff;");
					bw.write("opacity: 0.6;");
					bw.write("width: 507px;");
					bw.write("height: auto;");
					bw.write("border-color:#000;");
					bw.write("border-width:2.5;");
					bw.write("border-top-left-radius: 4px;");
					bw.write("border-top-right-radius:4px;");
					bw.write("border-bottom-left-radius: 4px;");
					bw.write("border-bottom-right-radius:4px;");
					bw.write("box-shadow: 3px 3px #B4B4B4;}");

					bw.write(".box5{");
					bw.write("font-family: 'Exo 2', sans-serif;");
					bw.write("font-size:15px;");
					bw.write("background-color: #FFB733;");
					bw.write("opacity: 0.8;");
					bw.write("color:#000;");
					bw.write("height:50px;");
					bw.write("font-weight:100;");
					bw.write("width:1024px;");
					bw.write("border-color:#000;");
					bw.write("border-width:2.5;");
					bw.write("border-top-left-radius: 4px;");
					bw.write("border-top-right-radius:4px;");
					bw.write("border-bottom-left-radius: 4px;");
					bw.write("border-bottom-right-radius:4px;");
					bw.write("box-shadow: 3px 3px #B4B4B4;}");

					bw.write(".tabledesign{");
					bw.write("border-collapse:collapse;");
					bw.write("border-color:#000;");
					bw.write("border-bottom-left-radius: 6px;");
					bw.write("box-shadow: 3px 3px #B4B4B4;}");

					bw.write(".tablestyle{");
					bw.write("font-family: 'Exo 2', sans-serif;");
					bw.write("font-size:16px;");
					bw.write("font-weight:100;");
					bw.write("color:#000;");
					bw.write("padding-left: 8px;");
					bw.write("padding-top: 8px;");
					bw.write("padding-bottom: 8px;");
					bw.write("padding-right: 8px;}");

					bw.write(".tableheaderstyle{");
					bw.write("font-family: 'Exo 2', sans-serif;");
					bw.write("font-size:16px;");
					bw.write("font-weight:100;");
					bw.write("color:#fff;");
					bw.write("padding-left: 8px;");
					bw.write("opacity:0.8;");
					bw.write("padding-top: 8px;");
					bw.write("padding-bottom: 8px;");
					bw.write("padding-right: 8px;");
					bw.write("background-color:#333;}");
					bw.write("</style>");
					// end of css

					bw.write("<!doctype html>");
					bw.write("<html>");
					bw.write("<head>");
					// 'Heading
					bw.write("<meta charset=" + "\"utf-8\"" + ">");
					bw.write("<title>Test Case Summary Report</title>");
					bw.write("</head>");
					bw.write("<body>");
					bw.write("<div align=" + "\"center\"" + "style="
							+ "\"height:auto\"" + ">");
					bw.write("<img alt=" + "Intuit" + " src="
							+ "\"./pages/images/Intuit.png\"" + "width ="
							+ "\"180\"" + " height=" + "\"70\"" + " align = "
							+ "\"center\"" + ">");
					bw.write("<table width=" + "\"1024\"" + "border=" + "\"1\""
							+ ">");
					bw.write("<tr><th colspan=" + "\"5\"" + "class="
							+ "\"box1\"" + "scope=" + "\"col\""
							+ ">Test  Summary Report</th></tr>");
					bw.write("<tr><th colspan=" + "\"5\"" + " class="
							+ "\"gap\"" + "scope=" + "\"col\"" + "></th></tr>");

					// 'Summary report and pie chart

					bw.write("<tr>");
					bw.write("<td class=" + "\"box2\"" + " valign=" + "\"top\""
							+ ">");
					bw.write("<table border=" + "\"0\"" + " class="
							+ "\"box3\"" + ">");
					bw.write("<tr><td colspan=" + "\"3\""
							+ ">Summary</td></tr>");
					bw.write("<tr><td colspan=" + "\"3\"" + "style="
							+ "\"height:5px\"" + ">&nbsp;</td></tr>");
					bw.write("<tr>");
					bw.write("<td width=" + "\"149\"" + "valign=" + "\"top\""
							+ ">Test Case</td>");
					bw.write("<td width=" + "\"4\"" + " valign=" + "\"top\""
							+ ">:</td>");
					bw.write("<td width=" + "\"208\"" + "valign=" + "\"top\""
							+ "align=" + "\"justify\"" + ">" + "Summary"
							+ "</td></tr>");
					bw.write("<td valign=" + "\"top\"" + ">Total Steps</td>");
					bw.write("<td valign=" + "\"top\"" + ">:</td>");
					bw.write("<td valign=" + "\"top\"" + "align="
							+ "\"justify\"" + "style=" + "\"color:blue\"" + ">"
							+ (iPassCount + iFailCount) + "</td></tr>");
					bw.write("<tr>");
					bw.write("<td valign=" + "\"top\"" + ">Passed</td>");
					bw.write("<td valign=" + "\"top\"" + ">:</td>");
					bw.write("<td valign=" + "\"top\"" + "align="
							+ "\"justify\"" + "style=" + "\"color:green\""
							+ ">" + iPassCount + "</td></tr>");
					bw.write("<tr>");
					bw.write("<td valign=" + "\"top\"" + ">Failed</td>");
					bw.write("<td valign=" + "\"top\"" + ">:</td>");
					bw.write("<td valign=" + "\"top\"" + "align="
							+ "\"justify\"" + "style=" + "\"color:red\"" + ">"
							+ iFailCount + "</td></tr>");
					String remote = TestBed.config.getValue("remote");
					String browser = TestBed.config.getValue("browser");
					if (remote.equalsIgnoreCase("yes") || remote.equalsIgnoreCase("sauce")) {
						Iterator it = cap.asMap().entrySet().iterator();
						while (it.hasNext()) {
							Map.Entry p = (Map.Entry) it.next();
							if (!p.getKey().toString().contains("profile")
									&& !p.getKey().toString()
											.contains("loggingPrefs")) {
								bw.write("<tr><td width=" + "\"156\""
										+ "valign=" + "\"top\"" + ">"
										+ p.getKey() + "</td>");
								bw.write("<td width=" + "\"4\"" + "valign="
										+ "\"top\"" + ">:</td>");
								bw.write("<td width=" + "\"316\"" + " valign="
										+ "\"top\"" + " align=" + "\"justify\""
										+ ">" + p.getValue() + "</td></tr>");
							}

						}
					} else {
						bw.write("<tr><td valign=" + "\"top\""
								+ ">Browser</td>");
						bw.write("<td valign=" + "\"top\"" + ">:</td>");
						bw.write("<td valign=" + "\"top\"" + " align="
								+ "\"justify\"" + ">" + browser + "</td></tr>");

						bw.write("<tr><td width=" + "\"156\"" + "valign="
								+ "\"top\"" + ">OS</td>");
						bw.write("<td width=" + "\"4\"" + "valign=" + "\"top\""
								+ ">:</td>");
						bw.write("<td width=" + "\"316\"" + " valign="
								+ "\"top\"" + " align=" + "\"justify\"" + ">"
								+ device + " " + osVersion + "</td></tr>");
					}
					bw.write("<tr><td width=" + "\"156\"" + "valign="
							+ "\"top\"" + ">Enviroment</td>");
					bw.write("<td width=" + "\"4\"" + "valign=" + "\"top\""
							+ ">:</td>");
					bw.write("<td width=" + "\"316\"" + " valign=" + "\"top\""
							+ " align=" + "\"justify\"" + ">" 
							+ "</td></tr>");
					bw.write("</table></td>");
					bw.write("<td class=" + "\"gap\"" + "></td>");
					bw.write("<td class=" + "\"box4\"" + " valign="
							+ "\"top\">");
					bw.write("<div id="
							+ "\"piechart\""
							+ " style="
							+ "\"width:652px; height:325px;font-family: 'Exo 2', sans-serif;\""
							+ "></div></td></tr>");

					// 'Test cases list with links to their detailed report
					bw.write("<tr><td class=" + "\"gap\"" + "></td></tr>");
					bw.write("<tr><th colspan=" + "\"5\"" + "class="
							+ "\"box1\"" + "scope=" + "\"col\""
							+ ">Test Case Execution List</th></tr>");
					bw.write("<tr><td class=" + "\"gap\"" + "></td></tr>");

					bw.write("<tr>");
					bw.write("<td height=" + "\"-1\"" + "colspan=" + "\"5\""
							+ " valign=" + "\"top\"" + ">");
					bw.write("<table width=" + "\"1073\"" + " border="
							+ "\"1\"" + " class=" + "\"tabledesign\""
							+ " cellspacing=" + "\"0\"" + ">");
					bw.write("<tr valign=" + "\"top\"" + ">");
					bw.write("<td width=" + "\"75\"" + "class="
							+ "\"tableheaderstyle\"" + " align=" + "\"center\""
							+ ">" + "Sr No" + "</td>");
					bw.write("<td width=" + "\"200\"" + "class="
							+ "\"tableheaderstyle\"" + " align=" + "\"center\""
							+ ">" + "Test Case Name" + "</td>");
					bw.write("<td width=" + "\"200\"" + "class="
							+ "\"tableheaderstyle\"" + " align=" + "\"center\""
							+ ">" + "Total No of Steps" + "</td>");
					bw.write("<td width=" + "\"200\"" + " class="
							+ "\"tableheaderstyle\"" + " align=" + "\"center\""
							+ ">" + "No of Pass Steps" + "</td>");
					bw.write("<td width=" + "\"200\"" + " class="
							+ "\"tableheaderstyle\"" + "align=" + "\"center\""
							+ ">" + "No of Fail Steps " + "</td>");
					bw.write("<td width=" + "\"150\"" + " class="
							+ "\"tableheaderstyle\"" + " align=" + "\"center\""
							+ ">" + "Status" + "</td>");
					bw.write("<td width=" + "\"250\"" + " class="
							+ "\"tableheaderstyle\"" + " align=" + "\"center\""
							+ ">" + " Report" + "</td>");

					for (int iIteration = 0; iIteration < asTestCaseName.length; iIteration++) {
						int iTotalNumberOfSteps = Integer
								.parseInt(asPassCount[iIteration])
								+ Integer.parseInt(asFailCount[iIteration]);
						int iSrNo = iIteration + 1;
						bw.write("<tr valign=" + "\"middle\"" + ">");
						bw.write("<td class=" + "\"tablestyle\"" + "align="
								+ "\"center\"" + ">" + iSrNo + "</td>");
						bw.write("<td class=" + "\"tablestyle\"" + "align="
								+ "\"center\"" + ">"
								+ asTestCaseName[iIteration] + "</td>");
						bw.write("<td class=" + "\"tablestyle\"" + " align="
								+ "\"center\"" + ">" + iTotalNumberOfSteps
								+ "</td>");
						bw.write("<td class=" + "\"tablestyle\"" + "align="
								+ "\"center\"" + ">" + asPassCount[iIteration]
								+ "</td>");
						bw.write("<td class=" + "\"tablestyle\"" + "align="
								+ "\"center\"" + ">" + asFailCount[iIteration]
								+ "</td>");
						if (Integer.parseInt(asFailCount[iIteration]) == 0) {
							bw.write("<td class=" + "\"tablestyle\"" + "align="
									+ "\"center\"" + " style ="
									+ "\"color:green\"" + ">" + "PASS"
									+ "</td>");
						} else {
							bw.write("<td class=" + "\"tablestyle\""
									+ " align=" + "\"center\"" + " style ="
									+ "\"color:red\"" + ">" + "FAIL" + "</td>");
						}

						bw.write("<td class=" + "\"tablestyle\"" + "align="
								+ "\"center\"" + "><a href=" + "\""
								+ asReportPath[iIteration] + "\"" + ">"
								+ "Report" + "</a></td>");
						bw.write("</tr>");
					}
					bw.write("</table></td>");
					bw.write("</tr></table>");
					bw.write("</div></body></html>");
					bw.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static String theMonth(int month) {
		String[] monthNames = { "January", "February", "March", "April", "May",
				"June", "July", "August", "September", "October", "November",
				"December" };
		return monthNames[month];
	}

	public void AddStepDetailsForNestedReporting(String sKey, Report reporting,
			String sValue) {
		if (TestBed.invokeReporting != null) {
			if (TestBed.invokeReporting.equalsIgnoreCase("Yes")) {
				if (reporting.mNestedMapForStep.containsKey(sKey)) {
					String sCurrentValue = reporting.mNestedMapForStep
							.get(sKey);
					sCurrentValue = sCurrentValue + "##" + sValue;
					reporting.mNestedMapForStep.remove(sKey);
					reporting.mNestedMapForStep.put(sKey, sCurrentValue);
				} else {
					reporting.mNestedMapForStep.put(sKey, sValue);
				}
			}
		}
	}

	public void AddSummaryReportDetails(String sKey, Report reporting,
			String sValue) {
		if (TestBed.invokeReporting != null) {
			if (TestBed.invokeReporting.equalsIgnoreCase("Yes")) {
				if (reporting.mSummaryReport.containsKey(sKey)) {
					String sCurrentValue = reporting.mSummaryReport.get(sKey);
					sCurrentValue = sCurrentValue + "###" + sValue;
					reporting.mSummaryReport.remove(sKey);
					reporting.mSummaryReport.put(sKey, sCurrentValue);
				} else {
					reporting.mSummaryReport.put(sKey, sValue);
				}
			}
		}
	}
}
