package com.rebtel.listerners;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.testng.IInvokedMethod;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.collections.Lists;
import org.testng.internal.Utils;
import org.testng.xml.XmlSuite;

import com.rebtel.test.MobileTest;
import com.rebtel.test.TestBed;
import com.rebtel.testbed.core.TestBedConfig;

public class TestNGCustomReportListener implements IReporter{

	private PrintWriter writer;
	private int m_row;
	private Integer m_testIndex;
	private int m_methodIndex;
	private String reportFileName = "CustomExecutionReport.html";
	private static String customizedReportPath = null ;

	/** Creates summary of the run */
	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
			String outdir) {
		try {
			writer = createWriter(outdir);
		} catch (IOException e) {
			System.err.println("Unable to create output file");
			e.printStackTrace();
			return;
		}

		startHtml(writer);
		generateSuiteSummaryReport(suites);
		generateMethodSummaryReport(suites);
		generateMethodDetailReport(suites);
		endHtml(writer);
		writer.flush();
		writer.close();
		try {
			File source = new File(outdir, reportFileName);
			File dest = new File(getCustomizedReportPath());
			FileUtils.copyFileToDirectory(source, dest);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to get customized report folder path
	 * @return
	 */
	public static String getCustomizedReportPath(){
		try{
			if(customizedReportPath ==null){
				String getTestBedName = TestBed.currentTestBedNameReport;
				String dateFormat = new SimpleDateFormat("ddMMMyyyy_HHmmss").format(Calendar.getInstance().getTime());
				customizedReportPath = TestBedConfig.getCustomizedReportPath()+ getTestBedName+ "/"+dateFormat+"/";
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return customizedReportPath;
	}

	protected PrintWriter createWriter(String outdir) throws IOException {
		new File(outdir).mkdirs();
		return new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, reportFileName))));
	}

	/**
	 * Creates a table showing the highlights of each test method with links to
	 * the method details
	 */
	protected void generateMethodSummaryReport(List<ISuite> suites) {
		m_methodIndex = 0;
		startResultSummaryTable("methodOverview");
		int testIndex = 1;
		for (ISuite suite : suites) {
			if (suites.size() >= 1) {
				titleRow(suite.getName(), 5);
			}

			Map<String, ISuiteResult> r = suite.getResults();
			for (ISuiteResult r2 : r.values()) {
				ITestContext testContext = r2.getTestContext();
				String testName = testContext.getName();
				m_testIndex = testIndex;
				resultSummary(suite, testContext.getFailedConfigurations(), testName, "failed", " (configuration methods)");
				resultSummary(suite, testContext.getFailedTests(), testName, "failed", "");
				resultSummary(suite, testContext.getSkippedConfigurations(), testName, "skipped", " (configuration methods)");
				resultSummary(suite, testContext.getSkippedTests(), testName, "skipped", "");
				resultSummary(suite, testContext.getPassedTests(), testName, "passed", "");
				testIndex++;
			}
		}
		writer.println("</table>");
	}

	/** Creates a section showing known results for each method */
	protected void generateMethodDetailReport(List<ISuite> suites) {
		m_methodIndex = 0;
		for (ISuite suite : suites) {
			Map<String, ISuiteResult> r = suite.getResults();
			for (ISuiteResult r2 : r.values()) {
				ITestContext testContext = r2.getTestContext();
				if (r.values().size() > 0) {
					writer.println("<h1>" + testContext.getName() + "</h1>");
				}
				resultDetail(testContext.getFailedConfigurations());
				resultDetail(testContext.getFailedTests());
				resultDetail(testContext.getSkippedConfigurations());
				resultDetail(testContext.getSkippedTests());
				resultDetail(testContext.getPassedTests());
			}
		}
	}
	
	/** Creates a section showing known results for each method */
	protected void generateMethodScreenshots(List<ISuite> suites) {
		m_methodIndex = 0;
		for (ISuite suite : suites) {
			Map<String, ISuiteResult> r = suite.getResults();
			for (ISuiteResult r2 : r.values()) {
				ITestContext testContext = r2.getTestContext();
				if (r.values().size() > 0) {
					writer.println("<h1>" + testContext.getName() + "</h1>");
				}
				screenShotDetail(testContext.getFailedConfigurations());
				screenShotDetail(testContext.getFailedTests());
			}
		}
	}

	private void resultSummary(ISuite suite, IResultMap tests, String testname,
			String style, String details) {
		List<ITestNGMethod> failedTests = null;
		int mq = 0;
		int cq = 0;
		int rq = 0;
		String lastClassName = "";
		if (tests.getAllResults().size() > 0) {
			StringBuffer buff = new StringBuffer();
			Map<String, ISuiteResult> testsFailed = suite.getResults();
			for (ISuiteResult r : testsFailed.values()) {
				ITestContext overview = r.getTestContext();
				for(ITestResult tresult : tests.getAllResults()){
					m_methodIndex += 1;
					String className = tresult.getTestClass().getName();
					String id = (m_testIndex == null ? null : "t"
							+ Integer.toString(m_testIndex));
					if(mq==0){
						titleRow(testname + " &#8212; " + style + details, 5, id);
						rq = 1;
						m_testIndex = null;
					}
					
					if (!className.equalsIgnoreCase(lastClassName)) {
						if (mq > 0) {
							cq += 1;
							writer.print("<tr class=\"" + style
									+ (cq % 2 == 0 ? "even" : "odd") + "\">"
									+ "<td");
							if (mq > 1) {
								writer.print(" rowspan=\"" + mq + "\"");
							}
							writer.println(">" + lastClassName + "</td>" + buff);
						}
						mq = 0;
						buff.setLength(0);
						lastClassName = className;
					}

					long end = Long.MIN_VALUE;
					long start = Long.MAX_VALUE;
					long startMS=0;
					String firstLine="";

					if (tresult.getEndMillis() > end) {
						end = tresult.getEndMillis()/1000;
					}
					if (tresult.getStartMillis() < start) {
						startMS = tresult.getStartMillis();
						start =startMS/1000;
					}

					Throwable exception=tresult.getThrowable();
					boolean hasThrowable = exception != null;
					if(hasThrowable){
						String str = Utils.stackTrace(exception, true)[0];
						Scanner scanner = new Scanner(str);
						firstLine = scanner.nextLine();
					}

					DateFormat formatter = new SimpleDateFormat("hh:mm:ss");
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(startMS);

					String description = tresult.getMethod().getDescription();
					String testInstanceName = tresult.getParameters()[0].toString();
					mq += 1;
					if (mq > 1) {
						buff.append("<tr class=\"" + style
								+ (cq % 2 == 0 ? "odd" : "even") + "\">");
					}
					buff.append("<td><a href=\"#m"
							+ m_methodIndex
							+ "\">"
							+ testInstanceName
							+ " "
							+ "<td style=\"text-align:right\">" + formatter.format(calendar.getTime()) + "</td>" + "<td class=\"numi\">"
							+ timeConversion(end - start) + "</td>" + "</tr>");


					
				}
				if (mq > 0) {
					cq += 1;
					writer.print("<tr class=\"" + style + (cq % 2 == 0 ? "even" : "odd") + "\">" + "<td");
					if (mq > 1) {
						writer.print(" rowspan=\"" + mq + "\"");
					}
					writer.println(">" + lastClassName + "</td>" + buff);
					buff.setLength(0);
				}
			}
		}
	}



	private String timeConversion(long seconds) {

		final int MINUTES_IN_AN_HOUR = 60;
		final int SECONDS_IN_A_MINUTE = 60;

		int minutes = (int) (seconds / SECONDS_IN_A_MINUTE);
		seconds -= minutes * SECONDS_IN_A_MINUTE;

		int hours = minutes / MINUTES_IN_AN_HOUR;
		minutes -= hours * MINUTES_IN_AN_HOUR;

		return prefixZeroToDigit(hours) + ":" + prefixZeroToDigit(minutes) + ":" + prefixZeroToDigit((int)seconds);
	}

	private String prefixZeroToDigit(int num){
		int number=num;
		if(number<=9){
			String sNumber="0"+number;
			return sNumber;
		}
		else
			return ""+number;

	}

	/** Starts and defines columns result summary table */
	private void startResultSummaryTable(String style) {
		tableStart(style, "summary");
		writer.println("<tr><th>Class</th>"
				+ "<th>Method</th><th>Start Time </th><th>Time<br/></th></tr>");
		m_row = 0;
	}
	private String qualifiedName(ITestNGMethod method,Set<ITestResult> set) {
		StringBuilder addon = new StringBuilder();
		List<ITestResult> testResultsList = new ArrayList<ITestResult>(set);
		Object[] parameterArray = testResultsList.get(0).getParameters();
		return "<b>" + parameterArray[0] + "</b> " + addon;
	}

	private void resultDetail(IResultMap tests) {
		Set<ITestResult> testResults=tests.getAllResults();
		List<ITestResult> testResultsList = new ArrayList<ITestResult>(testResults);
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		System.setProperty("java.util.Collections.useLegacyMergeSort", "true");
		Collections.sort(testResultsList, new TestResultsSorter());
		for (ITestResult result : testResultsList) {
			ITestNGMethod method = result.getMethod();
			m_methodIndex++;
			String cname = method.getTestClass().getName();
			writer.println("<h2 id=\"m" + m_methodIndex + "\">" + result.getParameters()[0] + "</h2>");
			Set<ITestResult> resultSet = tests.getResults(method);
			generateResult(result, method, resultSet.size());
			writer.println("<p class=\"totop\"><a href=\"#summary\">back to summary</a></p>");

		}
	}
	
	private String screenShotHover(ITestResult result, int failCount){
		String base64 = "";
			ITestNGMethod method = result.getMethod();
			String cname = method.getTestClass().getName();
			byte[] imageBytes;
			try {
				
				String url = SoftAssertor.failureScreenShotFolderPath;
				url = "file:///" + System.getProperty("user.dir")+ "/" +url+"/"+result.getParameters()[0]+"_"+failCount+".png";
				imageBytes = IOUtils.toByteArray(new URL(url));
				base64 = Base64.getEncoder().encodeToString(imageBytes);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return "data:image/png;base64,"+base64;
	}
	
	private void screenShotDetail(IResultMap tests) {
		Set<ITestResult> testResults=tests.getAllResults();
		List<ITestResult> testResultsList = new ArrayList<ITestResult>(testResults);
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		System.setProperty("java.util.Collections.useLegacyMergeSort", "true");
		Collections.sort(testResultsList, new TestResultsSorter());
		for (ITestResult result : testResultsList) {
			ITestNGMethod method = result.getMethod();
			m_methodIndex++;
			String cname = method.getTestClass().getName();
			writer.println("<h2 id=\"s" + m_methodIndex + "\">" +  " "
					+ result.getParameters()[0] + "</h2>");
			writer.print("<table>");
			byte[] imageBytes;
			try {
				String url = TestBed.failureScreenShotFolderPath;
				url = "file:///" + System.getProperty("user.dir")+ "/" +url+"/"+result.getParameters()[0]+".png";
				System.out.println(url);
				imageBytes = IOUtils.toByteArray(new URL(url));
				String base64 = Base64.getEncoder().encodeToString(imageBytes);
				writer.println("<img heingt=\"300\" width=\"150\" src=\"data:image/png;base64,"+base64+"\">");
				writer.print("</table>");
				writer.println("<a href=\"#summary\">back to summary</a>");
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void generateResult(ITestResult ans, ITestNGMethod method,
			int resultSetSize) {
		Object[] parameters = ans.getParameters();
		int method_index = 0;
		boolean hasParameters = parameters != null && parameters.length > 0;
		if (hasParameters) {
			tableStart("result", null);
			writer.print("<tr class=\"param\">");
			for (int x = 1; x <= parameters.length; x++) {
				writer.print("<th>Parameter #" + x + "</th>");
			}
			writer.println("</tr>");
			writer.print("<tr class=\"param stripe\">");
			for (Object p : parameters) {
				writer.println("<td>" + Utils.escapeHtml(Utils.toString(p))
				+ "</td>");
			}
			writer.println("</tr>");
		}
		List<String> msgs = Reporter.getOutput(ans);
		boolean hasReporterOutput = msgs.size() > 0;
		Throwable exception = ans.getThrowable();
		boolean hasThrowable = exception != null;
		if (hasReporterOutput || hasThrowable) {

			if (hasParameters) {
				writer.print("<tr><td");
				if (parameters.length > 1) {
					writer.print(" colspan=\"" + parameters.length + "\"");
				}
				writer.println(">");
			} else {
				writer.println("<table>");
				writer.println("<tr><td");
				writer.print(" colspan=\"" + 1 + "\"");
				writer.print(">");
				writer.println("<br/>");

			}
			if (hasReporterOutput) {
				method_index ++;
				if (hasThrowable) {
					writer.print("<tr><td");
					if (parameters.length > 1) {
						writer.print(" colspan=\"" + parameters.length + "\"");
					}
					writer.println(">");
					writer.print("<h3>Messages</h3>     ");
					for(int i = 1; i <= MobileTest.failureMap.get(ans.getParameters()[0]); i++){
						writer.print("     <a style='font-size:1.2em;position:relative;margin:0.8em' href=\"#s"+m_methodIndex+ "\"> Screenshot_"+i+"  " );
						writer.print("<span><img height=\"500\" width=\"300\" src=\""+screenShotHover(ans,i)+"\" /></span>");
						writer.print("</a>");
						writer.print("\t");
					}
					writer.println("</td></tr>");
				}
				for (String line : msgs) {
					writer.print("<tr><td");
					if (parameters.length > 1) {
						writer.print(" colspan=\"" + parameters.length + "\"");
					}
					writer.println(">");
					writer.print(line + "<br/>");
					writer.println("</td></tr>");
				}
			}
			if (hasThrowable) {
				writer.print("<tr><td");
				if (parameters.length > 1) {
					writer.print(" colspan=\"" + parameters.length + "\"");
				}
				writer.println(">");
				boolean wantsMinimalOutput = ans.getStatus() == ITestResult.SUCCESS;
				if (hasReporterOutput) {
					writer.print("<h3>"
							+ (wantsMinimalOutput ? "Expected Exception"
									: "Exception") + "</h3>");
				}
				generateExceptionReport(exception, method);
				writer.println("</td></tr>");
			}
		}
		if (hasParameters) {
			writer.println("</table>");
		}else{
			writer.println("</table>");
		}
	}

	protected void generateExceptionReport(Throwable exception, ITestNGMethod method) {
		writer.print("<div class=\"stacktrace\">");
		writer.print(Utils.stackTrace(exception, false)[0]);
		writer.println("</div>");
	}

	/**
	 * Since the methods will be sorted chronologically, we want to return the
	 * ITestNGMethod from the invoked methods.
	 */
	private Collection<ITestNGMethod> getMethodSet(IResultMap tests, ISuite suite) {

		List<IInvokedMethod> r = Lists.newArrayList();
		List<IInvokedMethod> invokedMethods = suite.getAllInvokedMethods();
		for (IInvokedMethod im : invokedMethods) {
			if (tests.getAllMethods().contains(im.getTestMethod())) {
				r.add(im);
			}
		}

		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		System.setProperty("java.util.Collections.useLegacyMergeSort", "true");
		Collections.sort(r,new TestSorter());
		List<ITestNGMethod> result = Lists.newArrayList();

		// Add all the invoked methods
		for (IInvokedMethod m : r) {
			for (ITestNGMethod temp : result) {
				if (!temp.equals(m.getTestMethod()))
					result.add(m.getTestMethod());
			}
		}

		// Add all the methods that weren't invoked (e.g. skipped)
		Collection<ITestNGMethod> allMethodsCollection=tests.getAllMethods();
		List<ITestNGMethod> allMethods=new ArrayList<ITestNGMethod>(allMethodsCollection);
		Collections.sort(allMethods, new TestMethodSorter());

		for (ITestNGMethod m : allMethods) {
			if (!result.contains(m)) {
				result.add(m);
			}
		}
		return result;
	}

	@SuppressWarnings("unused")
	public void generateSuiteSummaryReport(List<ISuite> suites) {
		tableStart("testOverview", null);
		writer.print("<tr>");
		tableColumnStart("Test");
		tableColumnStart("# Total");
		tableColumnStart("# Passed");
		tableColumnStart("# skipped");
		tableColumnStart("# failed");
		tableColumnStart("Total<br/>Time(hh:mm:ss)");
		tableColumnStart("Included Groups");
		tableColumnStart("Excluded Groups");

		writer.println("</tr>");
		NumberFormat formatter = new DecimalFormat("#,##0.0");
		int qty_tests = 0;
		int qty_pass_m = 0;
		int qty_pass_s = 0;
		int qty_skip = 0;
		long time_start = Long.MAX_VALUE;
		int qty_fail = 0;
		long time_end = Long.MIN_VALUE;
		m_testIndex = 1;
		for (ISuite suite : suites) {
			if (suites.size() >= 1) {
				titleRow(suite.getName(), 10);
			}
			Map<String, ISuiteResult> tests = suite.getResults();
			for (ISuiteResult r : tests.values()) {
				ITestContext overview = r.getTestContext();
				startSummaryRow(overview.getName());
				int q =overview.getPassedTests().size()
						+overview.getFailedTests().size()
						+overview.getSkippedTests().size(); 
				qty_tests += q;
				summaryCell(qty_tests, Integer.MAX_VALUE, 0);
				q = overview.getPassedTests().size();
				qty_pass_m += q;
				summaryCell(qty_pass_m, Integer.MAX_VALUE, 1);
				q = overview.getSkippedTests().size();
				qty_skip += q;
				summaryCell(qty_skip, 0, 2);
				q = overview.getFailedTests().size();
				qty_fail += q;
				summaryCell(qty_fail, 0, 3);

				time_start = Math.min(overview.getStartDate().getTime(), time_start);
				time_end = Math.max(overview.getEndDate().getTime(), time_end);
				summaryCell(timeConversion((overview.getEndDate().getTime() - overview.getStartDate().getTime()) / 1000), true, 0);
				
				summaryCell(overview.getIncludedGroups());
				summaryCell(overview.getExcludedGroups());
				writer.println("</tr>");
				m_testIndex++;
			}
		}
		writer.println("</table>");
	}


	private void summaryCell(String[] val) {
		StringBuffer b = new StringBuffer();
		for (String v : val) {
			b.append(v + " ");
		}
		summaryCell(b.toString(), true, 0);
	}

	private void summaryCell(String v, boolean isgood, int status) {
		if(status==0){
			writer.print("<td class=\"numi_attn" + "\">" + v
					+ "</td>");
		}else if(status==1){
			writer.print("<td class=\"numi_pass" + "\">" + v
					+ "</td>");
		}else if(status==2){
			writer.print("<td class=\"numi_skip" + "\">" + v
					+ "</td>");
		}else if(status==3){
			writer.print("<td class=\"numi_fail" + "\">" + v
					+ "</td>");
		}
	}

	private void startSummaryRow(String label) {
		m_row += 1;
		writer.print("<tr"
				+ (m_row % 2 == 0 ? " class=\"stripe\"" : "")
				+ "><td style=\"text-align:left;padding-right:2em\"><a href=\"#t"
				+ m_testIndex + "\"><b>" + label + "</b></a>" + "</td>");

	}

	private void summaryCell(int v, int maxexpected, int status) {
		summaryCell(String.valueOf(v), v <= maxexpected, status);
	}

	private void tableStart(String cssclass, String id) {
		writer.println("<table cellspacing=\"0\" cellpadding=\"0\""
				+ (cssclass != null ? " class=\"" + cssclass + "\""
						: " style=\"padding-bottom:2em\"")
				+ (id != null ? " id=\"" + id + "\"" : "") + ">");
		m_row = 0;
	}

	private void tableColumnStart(String label) {
		writer.print("<th>" + label + "</th>");
	}

	private void titleRow(String label, int cq) {
		titleRow(label, cq, null);
	}

	private void titleRow(String label, int cq, String id) {
		writer.print("<tr");
		if (id != null) {
			writer.print(" id=\"" + id + "\"");
		}
		writer.println("><th colspan=\"" + cq + "\">" + label + "</th></tr>");
		m_row = 0;
	}

	protected void writeReportTitle(String title) {
		writer.print("<center><h1>" + title + " - " + getDateAsString() + "</h1></center>");
	}


	/*
	 * Method to get Date as String
	 */
	private String getDateAsString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	/** Starts HTML stream */
	protected void startHtml(PrintWriter out) {
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
		out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		out.println("<head>");
		out.println("<title>TestNG Report</title>");
		out.println("<style type=\"text/css\">");
		out.println("table {margin-bottom:10px;border-collapse:collapse;empty-cells:show}");
		out.println("td,th {border:1px solid #009;padding:.25em .5em}");
		out.println(".result th {vertical-align:bottom}");
		out.println(".param th {padding-left:1em;padding-right:1em}");
		out.println(".param td {padding-left:.5em;padding-right:2em}");
		out.println(".stripe td,.stripe th {background-color: #E6EBF9}");
		out.println(".numi,.numi_attn,.numi_skip,.numi_pass,.numi_fail {text-align:right}");
		out.println(".total td {font-weight:bold}");
		out.println(".passedodd td {background-color: #66ff99}");
		out.println(".passedeven td {background-color: #66ff99}");
		out.println(".skippedodd td {background-color: #CCC}");
		out.println(".skippedodd td {background-color: #DDD}");
		out.println(".failedodd td {background-color: #ffb3b3}");
		out.println(".numi_skip {background-color: #ffff00}");
		out.println(".numi_fail {background-color: #ffb3b3}");
		out.println(".numi_pass {background-color: #66ff99}");
		out.println(".failedeven td,.stripe .numi_attn {background-color: #cc0000}");
		out.println(".stacktrace {white-space:pre;font-family:monospace}");
		out.println(".totop {font-size:85%;text-align:center;border-bottom:2px solid #000}");
		out.println("</style>");
		out.println("<style type=\"text/css\">");
		out.println("a>span { display: none; }");
		out.println("a:hover>span { display: block;position: absolute;top: 0px;left: 170px;width: 320px;margin: 0px;padding: 10px;}");
		out.println("</style>");
		out.println("</head>");
		out.println("<body>");

	}

	/** Finishes HTML stream */
	protected void endHtml(PrintWriter out) {
		out.println("<center> TestNG Report </center>");
		out.println("</body></html>");
	}

	// ~ Inner Classes --------------------------------------------------------
	/** Arranges methods by classname and method name */
	private class TestSorter implements Comparator<IInvokedMethod> {
		// ~ Methods
		// -------------------------------------------------------------

		/** Arranges methods by classname and method name */
		@Override
		public int compare(IInvokedMethod obj1, IInvokedMethod obj2) {
			int r = obj1.getTestMethod().getTestClass().getName().compareTo(obj2.getTestMethod().getTestClass().getName());
			return r;
		}
	}

	private class TestMethodSorter implements Comparator<ITestNGMethod> {
		@Override
		public int compare(ITestNGMethod obj1, ITestNGMethod obj2) {
			int r = obj1.getTestClass().getName().compareTo(obj2.getTestClass().getName());
			if (r == 0) {
				r = obj1.getMethodName().compareTo(obj2.getMethodName());
			}
			return r;
		}
	}

	private class TestResultsSorter implements Comparator<ITestResult> {
		@Override
		public int compare(ITestResult obj1, ITestResult obj2) {
			int result = obj1.getTestClass().getName().compareTo(obj2.getTestClass().getName());
			if (result == 0) {
				result = obj1.getMethod().getMethodName().compareTo(obj2.getMethod().getMethodName());
			}
			return result;
		}
	}

}