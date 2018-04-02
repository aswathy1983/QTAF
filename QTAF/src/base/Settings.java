package base;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import utility.Log;
import utility.PropertyUtility;

public class Settings extends Constants{

	public static WebDriver driver;
	public static String appType;
	public static PropertyUtility prop = new PropertyUtility();
	public static String environment;
	public static DesiredCapabilities iOSCapabilities = new DesiredCapabilities();
	public static DesiredCapabilities androidCapabilities = new DesiredCapabilities();
	public static DesiredCapabilities webCapabilities = new DesiredCapabilities();
	public static Properties sysProps = System.getProperties();
	public static String userDirectory = sysProps.getProperty("user.dir");
	String currentURL = "";
	
	@Parameters({ "appType" })
	@BeforeClass
	public void beforeClassInBase(String appType) {
		DOMConfigurator.configure("Log4j/log4j.xml");
		if (driver == null) {
			try {
				if (appType.equals("web")) 
					webSettings();
				else if (appType.equals("device")) 
					deviceSettings();
				else  
					webSettings();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
				
	public static void deviceSettings() throws InterruptedException, IOException {
		String device = sysProps.getProperty("device");
		
		if (device.equals("iOS"))
			iOSSettings();
		else if (device.equals("Android"))
			androidSettings();
		else
			iOSSettings();
	}
	
	@BeforeSuite
	public static void iOSSettings() {
		
			//stopServer();
			//startServer();
			File appDir = new File(PropertyUtility.getProperty("iOSAppDir"));
			File app = new File(appDir, PropertyUtility.getProperty("iOSApp"));
			iOSCapabilities.setCapability("AUTOMATION_NAME",PropertyUtility.getProperty("iOSAUTOMATION_NAME"));
			iOSCapabilities.setCapability("platformName",PropertyUtility.getProperty("iOSPlatformName"));
			iOSCapabilities.setCapability("platformVersion", PropertyUtility.getProperty("iOSVersion"));
			iOSCapabilities.setCapability("deviceName", PropertyUtility.getProperty("iOSDeviceName"));
			iOSCapabilities.setCapability("udid", PropertyUtility.getProperty("Udid"));
			iOSCapabilities.setCapability("app", app.getAbsolutePath());
			iOSCapabilities.setCapability("appPackage", PropertyUtility.getProperty("iOSAppPackage"));
			iOSCapabilities.setCapability("newCommandTimeout", "120");
			iOSCapabilities.setCapability("noReset", true);
			iOSCapabilities.setCapability("clearSystemFiles", true);
			iOSCapabilities.setCapability("xcodeConfigfile", PropertyUtility.getProperty("Xcodeconfigfile"));
			iOSCapabilities.setCapability("realDeviceLogger", PropertyUtility.getProperty("Devicelogger"));
			try {
			driver = new IOSDriver<>(new URL(url), iOSCapabilities);
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		}catch(Exception e) {
			Log.addMessage("Appium Server not started. Plesae refer error log for more details");
		}
	}
	
	public static void androidSettings() {
//		// android device settings here
		try {
			stopServer();
			startServer();
			File appDir = new File(PropertyUtility.getProperty("androidAppDir"));
			File app = new File(appDir, PropertyUtility.getProperty("androidApp"));
			androidCapabilities.setCapability("AUTOMATION_NAME",PropertyUtility.getProperty("androidAUTOMATION_NAME"));
			androidCapabilities.setCapability("platformName",PropertyUtility.getProperty("androidPlatformName"));
			androidCapabilities.setCapability("platformVersion", PropertyUtility.getProperty("androidVersion"));
			androidCapabilities.setCapability("deviceName", PropertyUtility.getProperty("androidDeviceName"));
			androidCapabilities.setCapability("app", app.getAbsolutePath());
			androidCapabilities.setCapability("appPackage", PropertyUtility.getProperty("androidAppPackage"));
			androidCapabilities.setCapability("newCommandTimeout", "120");
			androidCapabilities.setCapability("noReset", true);
			androidCapabilities.setCapability("clearSystemFiles", true);
			driver = new AndroidDriver<>(new URL(url), androidCapabilities);
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		}catch(Exception e) {
			Log.addMessage("Appium Server not started. Plesae refer error log for more details");
		}
	}
		
	public static void startServer() throws IOException {	
		Log.addMessage("Starting the Appium Server");
		CommandLine command = new CommandLine("/usr/local/Cellar/node/8.4.0/bin/node");
		command.addArgument("/usr/local/lib/node_modules/appium/build/lib/main.js",false);
		command.addArgument("--address", false);
		command.addArgument(ip);
		command.addArgument("--port", false);
		command.addArgument("4723");
		command.addArgument("--no-reset", true);
		command.addArgument("--native-instruments-lib", true);
		DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);
		try {
			executor.execute(command, resultHandler);
			Thread.sleep(5000);
			Log.addMessage("Appium server started.");
		}catch (InterruptedException e) {
			Log.addIntExceptionMessage("Appium server not started",e);
			}		
	}

	public static void stopServer() throws InterruptedException {
		String[] command = { "/usr/bin/killall", "-KILL", "node" };
		try {
			Log.addMessage("Appium Server Stopped!!!");
			Runtime.getRuntime().exec(command);
			Thread.sleep(4000);
			Log.addMessage("Appium server stopped.");
		}catch (IOException e) {
			Log.addIOExceptionMessage("Appium server encountered an error ", e);
			}
	}		
	
	public static void webSettings() {
		String browser = sysProps.getProperty("browser");
		environment = sysProps.getProperty("environment");
		if (browser.equals("chrome")) 
			chromeSettings();
		else if (browser.equals("firefox")) 
			fireFoxSettings();
		else if (browser.equals("safari")) 
			safariSettings();
		else if (browser.equals("IE")) 
			IESettings();
		else 
		 	chromeSettings();
		}
	
	public static void chromeSettings(){
		ChromeOptions options = new ChromeOptions();
		webCapabilities = DesiredCapabilities.chrome();
		options.addArguments("test-type");
		System.setProperty("webdriver.chrome.driver",userDirectory+"/Driver/chromedriver");
		driver= new ChromeDriver(webCapabilities);
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		}
	
	public static void fireFoxSettings() {
		System.setProperty("webdriver.gecko.driver",userDirectory+"/Driver/geckodriver");
		driver = new FirefoxDriver();		
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		WebElement html = driver.findElement(By.tagName("html"));
		html.sendKeys(Keys.chord(Keys.CONTROL, "0"));
		driver.manage().window().maximize();
		}
	
	public static void safariSettings() {
		webCapabilities = new DesiredCapabilities();
		webCapabilities.setPlatform(Platform.WINDOWS);
		driver = new SafariDriver();
		driver.manage().window().maximize();
		}
	
	public static void IESettings() {
		webCapabilities = new DesiredCapabilities();
		webCapabilities.setPlatform(Platform.WINDOWS);
		driver = new InternetExplorerDriver();
		driver.manage().window().maximize();
		}
	
	public void open(String url) {
		driver.get(url);
	}
				
	
	public String getPageURL() {
		String url = null;
		if (environment.equals("production"))
			url = prop.getProperty("prodUrl");
		else if(environment.equals("test"))
			url = prop.getProperty("testUrl");
		else if (environment.equals("staging"))
			url = prop.getProperty("stagingUrl");		
		return(url);
	}
				
				
//CODE for Sitecheck
				
	public int getStatusCode(String pageUrl) throws Exception  {
		URL url = new URL(pageUrl);
		HttpURLConnection huc = (HttpURLConnection)url.openConnection();
		huc.setRequestMethod("GET");
		huc.connect();
		return huc.getResponseCode();
	}
		

	@AfterSuite
	/**
	 * To ensure that the driver has been closed or not
	 */
	public void tearDown() throws InterruptedException {
//		boolean hasQuit = (driver.toString().contains("null")) ? true : false;
//		if (hasQuit == false) {
			driver.quit();
			stopServer();
		}
	//}
}

	
	

	
