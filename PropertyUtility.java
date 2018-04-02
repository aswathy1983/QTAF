package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 *
 * ClassName: PropertyUtility Description: This class is for reading the
 * property values written inside config.properties file
 */
public class PropertyUtility {
	public static final String FILE_CONFIG = "propertyFiles/config.properties";
	public static final String TEMP_FILE = "propertyFiles/temp.properties";
	HashMap<String, String> hmData = new HashMap<String, String>();
	public HashMap<String, String> getTestdata(String fileName) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("propertyFiles/" + fileName));
			hmData.put("SenderEmail", prop.getProperty("Sendermailid"));
			hmData.put("SenderEmailPassword",prop.getProperty("Senderpassword"));
			hmData.put("Recipient", prop.getProperty("Recipientmailid"));
			hmData.put("Application", prop.getProperty("Application"));
			hmData.put("InputFileLocation", prop.getProperty("Inputfile"));
			hmData.put("LogFileName", prop.getProperty("logFileName"));
			hmData.put("LogFilePath", prop.getProperty("logFilePath"));
			hmData.put("TestsFailed", prop.getProperty("testFailed"));
			hmData.put("TestsRun", prop.getProperty("testRun"));
			hmData.put("TestsSkipped", prop.getProperty("testSkipped"));
			hmData.put("TestsPassed", prop.getProperty("testPassed"));
		}catch (IOException e) {
			e.printStackTrace();
		}
		return hmData;
	}

	/*
	 * Method Name: getProperty() Description: This function returns a property
	 * value when the property name is passed as parameter
	 */


	public static String getProperty(String propertyName) { 
		Properties prop = new Properties();
		String propertyValue = null; 
		try { prop.load(new FileInputStream(FILE_CONFIG)); 
		propertyValue = prop.getProperty(propertyName);
		} catch (IOException ex) {
			ex.printStackTrace(); 
		}
		return propertyValue; 
		}
	
	
	public static String getPropertyTemp(String propertyName) { 
		Properties prop = new Properties();
		String propertyValue = null; 
		try { prop.load(new FileInputStream(TEMP_FILE)); 
		propertyValue = prop.getProperty(propertyName);
		} catch (IOException ex) {
			ex.printStackTrace(); 
		}
		return propertyValue; 
		}

	public static void setPropertyValue(String propertyName,String propertyValue) throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(FILE_CONFIG));
		prop.setProperty(propertyName, propertyValue);
		prop.store(new FileOutputStream("config.properties"), null);
	}

	public static void setTempPropertyValue(String testPassed,String testFailed, String testSkipped, String testRun, 
			String body)throws FileNotFoundException, IOException { 
		
		// Creating properties files
		
		Properties props = new Properties();
		FileOutputStream fos = new FileOutputStream(TEMP_FILE);
		props.setProperty("testPassed", testPassed);
		props.setProperty("testFailed", testFailed);
		props.setProperty("testSkipped", testSkipped);
		props.setProperty("testRun", testRun);
		props.setProperty("bodyContent", body);

		// writing properties into properties file from Java
		
		props.store(fos, "Temporary file for storing the test results");
		fos.close();
	}

	public static void deleteTempFile() {
		File file = new File(TEMP_FILE);
		file.delete();
	}
}
