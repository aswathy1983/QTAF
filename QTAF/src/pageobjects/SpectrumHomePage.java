package pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import base.Settings;
import utility.Log;

public class SpectrumHomePage extends Settings{
	
	//@FindBy(xpath="//div[@id='header']/a/img[@alt='Spectrum Brands']")
	@FindBy(xpath="//div[@id='header']/a/img[@alt='']")
	WebElement spectrumLogo;
	
	public SpectrumHomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public static void verifyPageTitle() {
		try {
			String actualTitle=driver.getTitle();
			String expectedTitle= "Spectrum Brands | Providing Quality and Value to Consumers Worldwide.";
			Assert.assertEquals(actualTitle, expectedTitle, "Page title doesn't match");
			Log.addMessage("Verified the page title");
		} catch (Exception e) {
			Log.addMessage("Page title verification failed");
			System.out.println(e.getMessage().toString());
			Assert.assertTrue(false, "Page title not verified");
		}
	}

	public void verifyLogo() {
		try {
			Assert.assertTrue(spectrumLogo.isDisplayed(), "Spectrum Logo is not visible");
			Log.addMessage("Spectrum Logo is visible");
		}catch(Exception e) {
			Log.addMessage("Spectrum logo is not visible");
			System.out.println(e.getMessage().toString());
			Assert.assertTrue(false, "Failed to display spectrum logo");
		}
	}
}
