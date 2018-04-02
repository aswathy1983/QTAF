package base;

import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import utility.PropertyUtility;

public class Constants {
	   
  //Mobile Constants
	public static IOSDriver<MobileElement> driver;
    protected static String dataFile = PropertyUtility.getProperty("iOSDatafile");
    protected static String logfile = PropertyUtility.getProperty("Logfile");
    protected static String reportfile = PropertyUtility.getProperty("Reportfile");
    protected static String ip = PropertyUtility.getProperty("Ip");
    protected static String url=PropertyUtility.getProperty("Url");
	protected static String InputData = PropertyUtility.getProperty("InputData");
	public static String projectName=PropertyUtility.getProperty("ProjectName");

}
