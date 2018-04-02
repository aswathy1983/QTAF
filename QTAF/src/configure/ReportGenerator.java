package configure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utility.Log;
import utility.PropertyUtility;
import utility.Screenshot;


public class ReportGenerator implements ISuiteListener, ITestListener {
	static int testRun = 0;
	private static int testPassed = 0;
	private static int testFailed = 0;
	private static int testSkipped = 0;
	static Map<Long, Boolean> passStatusMap = new HashMap<>();
	String filename;
	String buildTime;
	String buildDate;
	static List<String> passedTestName = new ArrayList<>();
	static List<String> failedTestName = new ArrayList<>();
	static String htmlStr = "";
	PropertyUtility read = new PropertyUtility();
	
	@Override
	public void onStart(ITestContext arg0) {
		htmlStr += "<tr style=\"height:21px\"><td style=\"vertical-align:bottom\"align=\"center\"  colspan=\"2\">"
				+ "<span style=\"font-family:arial;font-size:small\">"
				+ "<b>"
				+ arg0.getName() + "</b>" + "</span></td></tr>";
		Log.addMessage(arg0.getName()+" of "+arg0.getSuite().getName()+'\n');
	}

	@Override
	public void onTestStart(ITestResult iTestResult) {
		Log.clearBuffer();
		Log.addMessage("Started " + iTestResult.getTestContext().getName()
				+ "->" + iTestResult.getName());
		Log.startTestCase(iTestResult.getName());
		testRun++;
	}

	@Override
	public void onTestSuccess(ITestResult iTestResult) {
		htmlStr += "<tr style=\"height:21px\"><td style=\"vertical-align:bottom\"align=\"center\">"
				+ "<span style=\"font-family:arial;font-size:small\">"
				+ iTestResult.getName()
				+ "</span></td><td style=\"vertical-align:bottom;background-color:rgb(0,190,0)\" align=\"center\"><span style=\"font-family:arial;font-size:small;font color=rgb(255,255,255)\">Passed</span><br></td></tr>";
		Log.addMessage(iTestResult.getTestContext().getName() + "->"+ iTestResult.getName() + "--" + " passed");
		Log.addMessage("");
		Log.addEndMessage();
		Log.info();
		testPassed++;
	}

	/**
	 * Method Name: onTestFailure() Description: This function will be called
	 * when a test case fails.
	 */
	
	@Override
	public void onTestFailure(ITestResult iTestResult) {
		if (iTestResult.getName() == "") {
			htmlStr += "<tr style=\"height:21px\"><td style=\"vertical-align:bottom\"align=\"center\">"
					+ "<span style=\"font-family:arial;font-size:small\">"
					+ "Nil" + "</span></td></tr>";
		}else {
			htmlStr += "<tr style=\"height:21px\"><td style=\"vertical-align:bottom\"align=\"center\">"
					+ "<span style=\"font-family:arial;font-size:small\">"
					+ iTestResult.getName()
					+ "</span></td><td style=\"vertical-align:bottom;background-color:rgb(244,89,90)\" align=\"center\"><span style=\"font-family:arial;font-size:small;font color=rgb(255,255,255)\">Failed</span><br></td></tr>";
		}
		Log.addMessage(iTestResult.getTestContext().getName() + "->"+ iTestResult.getName() + "--" + " failed");
		Log.addMessage("");
		Screenshot.takeScreenshot(iTestResult);
		Log.addEndMessage();
		Log.error();
		testFailed++;
		passStatusMap.put(Thread.currentThread().getId(), false);
	}

	/**
	 * Method Name: onTestSkipped() Description: This function will be called
	 * when a test case execution is skipped.
	 */
	
	@Override
	public void onTestSkipped(ITestResult iTestResult) {
		Log.clearBuffer();
		Log.addMessage(iTestResult.getTestContext().getName() + "->"+ iTestResult.getName() + " skipped" + '\n');
		Log.startTestCase(iTestResult.getName());
		Log.addEndMessage();
		Log.warn();
		testSkipped++;
		passStatusMap.put(Thread.currentThread().getId(), false);
	}

	/**
	 * Method Name: onFinish() Description: This function will be called after
	 * all the test cases are executed. This method will also send a mail
	 * regarding the execution status.
	 */
	
	public static String getTestSummaryReport() {
		String mailText = "<table cellspacing=\"0\" cellpadding=\"0\" border=\"1\" style=\"table-layout:fixed;font-size:13px;font-family:arial,sans,sans-serif;border-collapse:collapse;border:1px solid rgb(204,204,204)\">"
				+ "<colgroup><col width=\"249\"><col width=\"143\"></colgroup><tbody><tr style=\"height:21px\"><td style=\"padding:2px 3px;vertical-align:bottom;background-color:rgb(153,153,153)\" rowspan=\"1\" colspan=\"2\" align=\"center\"><b>Test Run Summary Report</b></td>"
				+ "</tr><tr style=\"height:21px\"><td style=\"vertical-align:bottom\" align=\"center\"><span style=\"font-family:arial;font-size:small\">Total Tests Run</span><br></td><td  style=\"vertical-align:bottom\" align=\"center\">"
				+ testRun
				+ "</td></tr><tr style=\"height:21px\"><td style=\"vertical-align:bottom\"align=\"center\">"
				+ "<span style=\"font-family:arial;font-size:small\">Tests Passed</span></td><td  style=\"vertical-align:bottom\"align=\"center\">"
				+ testPassed
				+ "</td></tr><tr style=\"height:21px\">"
				+ "<td style=\"vertical-align:bottom\"align=\"center\"><span style=\"font-family:arial;font-size:small\">Tests Failed"
				+ "</span></td><td  style=\"vertical-align:bottom\" align=\"center\">"
				+ testFailed
				+ "</td></tr><tr style=\"height:21px\"><td style=\"vertical-align:bottom\"align=\"center\">"
				+ "<span style=\"font-family:arial;font-size:small\">Tests Skipped</span></td><td  style=\"vertical-align:bottom\"align=\"center\">"
				+ testSkipped + "</td></tr></tbody></table>";
		String testStatusList = "<table cellspacing=\"0\" cellpadding=\"0\" border=\"1\" style=\"table-layout:fixed;font-size:13px;font-family:arial,sans,sans-serif;border-collapse:collapse;border:1px solid rgb(204,204,204)\">"
				+ "<colgroup><col width=\"249\"><col width=\"143\"></colgroup><tbody><tr style=\"height:21px\"><td style=\"padding:2px 3px;vertical-align:bottom;background-color:rgb(153,153,153)\" rowspan=\"1\" colspan=\"2\" align=\"center\"><b>Test Status Report</b></td>"
				+ htmlStr + "</tbody></table>";
		return mailText + testStatusList;
	}

	/**
	 * Method Name: onFinish() Description: This function will be called after
	 * all the test cases are executed. This method will also send a mail
	 * regarding the execution status.
	 */
	
	@Override
	public void onFinish(ISuite arg0) {
		try {
			PropertyUtility.setTempPropertyValue(Integer.toString(testPassed) + "", Integer.toString(testFailed) + "",Integer.toString(testSkipped )+ "", Integer.toString(testRun) + "",htmlStr);
		} catch (IOException e) {
			Log.addIOExceptionMessage("IO exception. Please refer error log for more details", e);
		}
	}
	
	@Override
	public void onStart(ISuite arg0) {
		Screenshot.createScreenshotDirectory();
	}
	
	
	@Override
	public void onFinish(ITestContext iTestContext) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// TODO Auto-generated method stub
	}
}