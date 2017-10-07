package tools;

import java.awt.AWTException;
import java.net.MalformedURLException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import tools.customDriver.AttachedWebDriver;

/**
 *  This class is for testing purpose
 * @author Arpo Adhikari
 *
 */
public class SessionTest {

	public static void main(String[] args) throws MalformedURLException, AWTException, InterruptedException {
		
		String sessionId = "6f692e2a-f434-4ae3-82c3-1eeb52392e82";
		WebDriver driver = new AttachedWebDriver(sessionId);
		System.out.println(driver.getCurrentUrl());
		driver.get("https://www.google.com");
		//Thread.sleep(5000);
		driver.findElement(By.name("q")).sendKeys("Selenium"+Keys.ENTER);

	}

}
