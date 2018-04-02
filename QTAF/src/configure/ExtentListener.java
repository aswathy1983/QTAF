package configure;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import base.Constants;
import utility.Utility;

public class ExtentListener implements IReporter {
	
	ExtentReports extentReport;

	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
			String outputDirectory) {
		extentReport = new ExtentReports(System.getProperty("user.dir")	+ "/test-output-extent/" + Constants.projectName+"_automation_report.html", true);
		
		for (ISuite suite : suites) {
			Map<String, ISuiteResult> result = suite.getResults();

			for (ISuiteResult r : result.values()) {
				ITestContext context = r.getTestContext();
				try {
					buildTestNodes(context.getPassedTests(), LogStatus.PASS);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					buildTestNodes(context.getFailedTests(), LogStatus.FAIL);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					buildTestNodes(context.getSkippedTests(), LogStatus.SKIP);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		extentReport.flush();
		extentReport.close();
		Utility.customiseReport();
	}

	private void buildTestNodes(IResultMap tests, LogStatus status) throws IOException {
		ExtentTest test;	
		String screenCaptureHtml;
		String finalScreenshot;
		
		if (tests.size() > 0) {
			for (ITestResult result : tests.getAllResults()) {
				test = extentReport.startTest(result.getMethod().getMethodName());
				test.setStartedTime(getTime(result.getStartMillis()));
				test.setEndedTime(getTime(result.getEndMillis()));
				String[] testLog = utility.Utility.getTestLog(result.getName()).split("\n");
						for (String step : testLog){
					test.log(LogStatus.INFO, step);
					}
				for (String group : result.getMethod().getGroups())
					test.assignCategory(group);
				if (result.getThrowable() != null) {
					test.log(status, "Test " + status.toString().toLowerCase()+ "ed");
					test.log(status, result.getThrowable());
				} 
			else {
					test.log(status, "Test " + status.toString().toLowerCase()+ "ed");
				}
				if (status == LogStatus.FAIL) {	
					screenCaptureHtml = test.addScreenCapture(System.getProperty("user.dir")+ "/Screenshots/"+ result.getName() + ".png");
					String screenShotPath = System.getProperty("user.dir")+ "/Screenshots/"+ result.getName() + ".png";
					String resizedScreenShotPath = System.getProperty("user.dir")+ "/Screenshots/"+ result.getName() + "_new.png";
					File resizedFile = Utility.imageResizer(screenShotPath, resizedScreenShotPath, 0.5);
					String encodedImage = Utility.encodeFileToBase64Binary(resizedFile);
					finalScreenshot = Utility.finalScreenShot(screenCaptureHtml,encodedImage);
					test.log(status,"Snapshot below: "+ finalScreenshot);
				}
				extentReport.endTest(test);
			}
		}
	}

	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}
}





