package utility;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import base.Constants;
import base.Settings;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Utility extends Settings {
	/*
	 * Method to wait for an element 
	 */
	public static void waitForElementToBeVisible( IOSDriver<?> driver, MobileElement element, int time){
	    WebDriverWait wait = new WebDriverWait(driver, time);
	    wait.until(ExpectedConditions.visibilityOf(element));
	}
	
	/*
	 * Method to wait for an element by Element to be Clickable
	 */
	public static void waitForElementToBeClickable( IOSDriver<?> driver, MobileElement element, int time){
	    WebDriverWait wait = new WebDriverWait(driver, time);
	    wait.until(ExpectedConditions.elementToBeClickable(element));
	}
	
	/*
	 * Method to wait for an element Present
	 */
	public static void waitForPresenceOfElement( IOSDriver<?> driver, int time,String locator){
	    WebDriverWait wait = new WebDriverWait(driver, time);
	    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
	}
	
	/*
	 * Simple wait
	 */
	public static void simpleWait(int milliSeconds) throws InterruptedException{
	  Thread.sleep(milliSeconds);
	}
	
	/*
	 * Method to wait for an element Present
	 */
	public void explicitWaitforWebElement(WebDriver driver, WebElement myWebElement) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 90);
			wait.until(ExpectedConditions.visibilityOf(myWebElement));
		} catch (Exception e) {
			System.out.println(e.getMessage().toString());
		}

	}
	
	public void scrollingToElementofAPage(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
	}

	 public void scrollTo(WebDriver driver, WebElement element) {
	        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
	    }
	 
	 public void scrollToBottom(WebDriver driver) {
	        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
	    }
	
	/*
	 * Method to accept Alert
	 */
	public static synchronized void acceptAlert( IOSDriver<?> driver){
	    WebDriverWait wait = new WebDriverWait(driver, 5);
	    wait.until(ExpectedConditions.alertIsPresent());
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}
	
	/*
	 * Scrolling the page down
	 */
	public static void scrollDown(IOSDriver<?> driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		HashMap<String, String> scrollObject = new HashMap<String, String>();
		scrollObject.put("direction", "down");
		js.executeScript("mobile: scroll", scrollObject);
	}
	
	/*
	 * Scrolling the page up
	 */
	public static void scrollUp(IOSDriver<?> driver) {
	JavascriptExecutor js = (JavascriptExecutor) driver;
	HashMap<String, String> scrollObject = new HashMap<String, String>();
	scrollObject.put("direction", "up");
	js.executeScript("mobile: scroll", scrollObject);
	}
	
	
	/*
	 * Scrolling the page up
	 */
	public static void swipeUp(IOSDriver<?> driver,  MobileElement FromElement,MobileElement ToElement) {
		
		Point startLocation=FromElement.getLocation();
		Point endLocation=ToElement.getLocation();
		int startx=startLocation.getX();
		int starty=startLocation.getY();
		int endx=endLocation.getX();
		int endy=endLocation.getY();
		JavascriptExecutor js = driver;
		Map<String, Object> params = new HashMap<>();
		params.put("duration", 1.0);
		params.put("fromX", startx);
		params.put("fromY", starty);
		params.put("toX", endx);
		params.put("toY", endy);
		js.executeScript("mobile: dragFromToForDuration", params);
	}
	
	
	/*
	 * Scrolling the page up
	 */
	public static void swipeUpTrial(IOSDriver<?> driver,  MobileElement FromElement,MobileElement ToElement) {
		
		int leftX = FromElement.getLocation().getX();
		int rightX = leftX + FromElement.getSize().getWidth();
		int middleX = (rightX + leftX) / 2;
		int upperY = FromElement.getLocation().getY();
		int lowerY = upperY + FromElement.getSize().getHeight();
		int middleY = (upperY + lowerY) / 2;
		
		int leftX1 = ToElement.getLocation().getX();
		int rightX1 = leftX1 + ToElement.getSize().getWidth();
		int middleX1 = (rightX1 + leftX1) / 2;
		int upperY1 = ToElement.getLocation().getY();
		int lowerY1 = upperY1 + ToElement.getSize().getHeight();
		int middleY1 = (upperY1 + lowerY1) / 2;
		middleY = -middleY;
		TouchAction swipe = new TouchAction(driver).press(middleX1, middleY1)
				.waitAction(Duration.ofSeconds(1)).moveTo( middleX, middleY)
				.release();
		swipe.perform();
	}
	
	
	/*
	 * Swiping the element left
	 */
	public static void swipeLeft(IOSDriver<?> driver,  MobileElement swipeElement) {
		JavascriptExecutor js = driver;
		Map<String, Object> params = new HashMap<>();
		params.put("direction", "left");
		params.put("element", swipeElement.getId());
		js.executeScript("mobile: swipe", params);
	}

	/*
	 * Swiping the page Up
	 */
	public static void swipeUp2(IOSDriver<?> driver,  MobileElement firstElement,MobileElement secondElement,Dimension size) {
	    TouchAction swipe = new TouchAction(driver).press(secondElement, size.width / 2 + 2, size.height / 2)
			.waitAction(Duration.ofSeconds(1)).moveTo(firstElement, size.width / 2 + 2, size.height / 2)
			.release();
	    swipe.perform();
	}
	
	/*
	 * Swiping the page left
	 */
	public static void swipeElement(IOSDriver<?> driver, MobileElement roomcell) {
		Point p = roomcell.getCenter();
		Dimension size = roomcell.getSize();
		int startx = p.getX() + size.width / 2;
		int starty = p.getY();
		int endx = p.getX() - size.width / 2;
		int endy = p.getY();
		TouchAction action = new TouchAction (driver);
		action.press(roomcell, startx, starty).moveTo(roomcell, endx, endy).release();
		action.perform();
		
	}
	
	public static void swipeLeft3(IOSDriver<?> driver, MobileElement element,Dimension size) {
		TouchAction swipe = new TouchAction(driver).press(element, size.width, size.height / 2)
			.waitAction(Duration.ofSeconds(3)).moveTo(element, size.width / 2, size.height / 2)
			.release();
		swipe.perform();	
	}

	/*
	 * Random String generator
	 */

	static final String AB = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static Random rnd = new Random();
	static String uNameAppend;

	public static String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
		sb.append(AB.charAt(rnd.nextInt(AB.length())));
		Log.addMessage(sb.toString());
		uNameAppend = sb.toString();
		return sb.toString();
	}

	public static void checkForAlerts() {
		Log.addMessage("Looking for open alerts");
		 try{
			 IOSElement ok = (IOSElement)
			 driver.findElement(By.name("icon dismiss x white"));
			 if(ok.isDisplayed()){
			 Log.addMessage("Alert window is displayed");
			 ok.click();
			 }
		 	}catch(Exception e){
			 Log.addMessage("Alert window is not displayed");
		 	}
	}
	

	/*
	 * Return todays date in given format
	 */
	
	public static String getTodaysDate(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}

	/*
	 * Return test log
	 */
	
	public static String getTestLog(String testname) {
		try {
			File file = new File("Log4j/Logs/Logfile.log");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			Boolean setFlag = false;
			int i = 0;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.trim().equals(testname)) {
					setFlag = true;
				}
				if (setFlag.equals(true)) {
					if (("-E---N---D-").equals(line.trim())) {
						break;
					}
					i++;
					if (i > 3) {
						stringBuffer.append(line);
						stringBuffer.append("\n");
					}
				}
			}
			fileReader.close();
			return stringBuffer.toString();
		} catch (IOException e) {
			return "Logging failed!" + e;
		}
	}
	
	/*
	 * Customize Report
	 */
	
	public static void customiseReport() {			
		try{		
			File readFile = new File(System.getProperty("user.dir")+"/test-output-extent/"+Constants.projectName+"_automation_report.html");
			FileReader fileReader = new FileReader(readFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String textHtml = "";
			String line;
			
			while ((line = bufferedReader.readLine()) != null) {
				textHtml = textHtml.concat(line);
			}
			textHtml = textHtml.replace("ExtentReports 2.0", Constants.projectName);
			textHtml = textHtml.replace("ExtentReports", Constants.projectName);
			textHtml = textHtml.replace("logo-content", "logo-content_"+Constants.projectName);
			textHtml = textHtml.replace("http://extentreports.relevantcodes.com", "https://"+Constants.projectName+".com/");
			textHtml = textHtml.replace("v2.41.1", " ");
			
			textHtml = textHtml.replaceAll("</body></html>", 
					"<script>"
					+ "document.getElementById(\"clear-filters\").style.display = \"none\";"
					+ "document.getElementById(\"enableDashboard\").style.display = \"none\";"
					+ "document.getElementById(\"refreshCharts\").style.display = \"none\";"
					+ "document.getElementsByClassName(\"step-filters right\")[0].style.display=\"none\";"	
					+ "</script>"
					+ "</body></html>");
						
			fileReader.close();
			
			File writeFile = new File(System.getProperty("user.dir")+"/test-output-extent/"+Constants.projectName+"_automation_report.html");
			FileWriter fileWriter = new FileWriter(writeFile.getAbsoluteFile());
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(textHtml);
			bufferedWriter.close();			
			} 
		catch (IOException e) {
				System.out.println(e);
			}
	}
	
	public static String encodeFileToBase64Binary(File file){
			String encodedfile = null;
			try {
				FileInputStream fileInputStreamReader = new FileInputStream(file);
				byte[] bytes = new byte[(int)file.length()];
				fileInputStreamReader.read(bytes);
				encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
				fileInputStreamReader.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return encodedfile;
		}
		
		public static File imageResizer(String inputImagePath,String outputImagePath, double percent) throws IOException {
			File inputFile = new File(inputImagePath);
		    BufferedImage inputImage = ImageIO.read(inputFile);
		    int scaledWidth = (int) (inputImage.getWidth() * percent);
		    int scaledHeight = (int) (inputImage.getHeight() * percent);
		    BufferedImage outputImage = new BufferedImage(scaledWidth,scaledHeight, inputImage.getType());
		    Graphics2D g2d = outputImage.createGraphics();
		    g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
		    g2d.dispose();
		    String formatName = outputImagePath.substring(outputImagePath.lastIndexOf(".") + 1);
		    ImageIO.write(outputImage, formatName, new File(outputImagePath));
		    return new File(outputImagePath);
		}
		
		public static String finalScreenShot(String screenShotHtml,String encodedScreenShot) {
			screenShotHtml = screenShotHtml.replaceAll("<a[^>]*>", "");
			screenShotHtml = screenShotHtml.replaceAll("</a>", "");
			String[] parts = screenShotHtml.split("src='");
			String part1 = parts[0]; 
			String part2 = parts[1]; 
			String[] part3 = part2.split("' /");
			part3[0] = encodedScreenShot;
			String screenshot = part1+"src='data:image/png;base64,"+ part3[0]+"' /"+part3[1];
			return screenshot;
		}		
}
