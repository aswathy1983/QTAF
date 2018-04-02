package utility;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import base.Settings;

public class Screenshot extends Augmenter {
	
	@Override
    protected RemoteWebDriver extractRemoteWebDriver(WebDriver driver) {
        if (driver.getClass() == RemoteWebDriver.class
            // here we allow enhancing by both CGLib and Mockito
            || driver.getClass().getName().startsWith("org.openqa.selenium.remote.RemoteWebDriver$$Enhancer")
            || driver instanceof RemoteWebDriver) {
            return (RemoteWebDriver) driver;
        } else {
             driver.getClass().getName();
             return null;
        }
    }

	// This function creates a new folder named "Screenshots" if it doesn't exist
	
	public static void createScreenshotDirectory() {
		File directory = new File(System.getProperty("user.dir")+ "/Screenshots");
		if (directory.exists()) {
			try {
				FileUtils.deleteDirectory(directory);
				directory.mkdir();
			}catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			directory.mkdir();
		}
	}

	// This function is used for screen capture
	public static void takeScreenshot(ITestResult iTestResult) {
		WebDriver augmentedDriver = new Screenshot().extractRemoteWebDriver(Settings.driver);
		File file = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
		Log.addMessage(System.getProperty("user.dir"));
		try {
			FileUtils.copyFile(file, new File(System.getProperty("user.dir")+ "/Screenshots/" + iTestResult.getName() + ".png"));
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
