package testmethods;

import org.testng.Assert;
import org.testng.annotations.Test;
import base.Settings;
import pageobjects.SpectrumHomePage;
import utility.Log;

public class verifySpectrumLogoTest extends Settings{
	
	@Test
	public void logoVerification() {
		try {
			open(getPageURL());
			SpectrumHomePage spectrumHomePage = new SpectrumHomePage(driver);
			SpectrumHomePage.verifyPageTitle();
			spectrumHomePage.verifyLogo();
			Log.addMessage("Logo verified");
		}catch(Exception e) {
			Log.addMessage("Logo verification failed");
			Assert.assertTrue(false, "Failed to verify logo");
			e.printStackTrace();
		}
	}
}