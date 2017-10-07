package tools;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 *  This class works with the WebDriver object
 * @author Arpo Adhikari
 *
 */
public class BrowserFactory {

	private WebDriver driver = null;

	/** Setter method of driver
	 * @param driver
	 */
	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	/** Getter method of driver
	 * @return WebDriver
	 */
	public WebDriver getDriver() {
		return driver;
	}

	/**
	 * Return Session Id of the browserSession
	 * @return Session Id as string
	 */
	public String getSessionId() {
		String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
		return sessionId;
	}

	/**
	 * Opens Browser
	 * @param serverURL
	 * @param browserName
	 * @return logs as string
	 * @throws MalformedURLException
	 * @throws URISyntaxException 
	 */
	public String openBrowser(String serverURL, String browserName) throws MalformedURLException, URISyntaxException {

		String msg = "";
		String sysArch = System.getProperty("os.arch");
		String chromedriverPath ;
		String iedriverPath;
		String geckodriverPath;
		
		chromedriverPath = "binaries/chromedriver.exe";
		
		if (sysArch.endsWith("64")) {
			geckodriverPath = "binaries/x64/geckodriver.exe";
			iedriverPath = "binaries/x64/IEDriverServer.exe";
		}
		else {
			geckodriverPath = "binaries/x86/geckodriver.exe";
			iedriverPath = "binaries/x86/IEDriverServer.exe";
		}

		if (driver == null) {
			switch (browserName) {
			case "Firefox":
				System.setProperty("webdriver.gecko.driver", geckodriverPath);
				driver = new RemoteWebDriver(new URL(serverURL), DesiredCapabilities.firefox());
				break;
			case "Chrome":
				System.setProperty("webdriver.chrome.driver", chromedriverPath);
				driver = new RemoteWebDriver(new URL(serverURL), DesiredCapabilities.chrome());
				break;
			case "Internet Explorer":
				System.setProperty("webdriver.ie.driver", iedriverPath);
				DesiredCapabilities cap_IE = DesiredCapabilities.internetExplorer();
				cap_IE.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
				cap_IE.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				cap_IE.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
				cap_IE.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
				cap_IE.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				cap_IE.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, "accept");
				driver = new RemoteWebDriver(new URL(serverURL), cap_IE);
				break;
			default:
				break;
			}
			msg = "Browser launched : ["+browserName+"]\n";
		}
		else {
			msg = "Browser is already OPEN.\n";
		}
		return msg;
	}

	/**
	 *  Close the Browser
	 */
	public String closeBrowser() {
		String msg;
		if (driver != null) {
			driver.quit();
			driver = null;
			msg = "Browser is Closed.\n";
		}
		else {
			msg = "Browser is NOT OPEN.\n";
		}
		return msg;
	}

}
