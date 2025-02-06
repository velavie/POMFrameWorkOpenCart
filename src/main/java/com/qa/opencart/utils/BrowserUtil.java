package com.qa.opencart.utils;

import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.qa.opencart.exceptions.BrowserException;

//To create all the important, generic and reusable functions to be called whenever required

/**
 * 
 * @author VictorUfomadu
 * 
 */
public class BrowserUtil {

	WebDriver driver; // webdriver global reference

	/**
	 * This method is used to Initialize/launch the driver based on browser name
	 * 
	 * @param browserName
	 * @return - this returns driver
	 */
	public WebDriver launchBrowser(String browserName) { // return WebDriver and not void
		System.out.println("Browser Name is: " + browserName);

		switch (browserName.toLowerCase().trim()) {
		case "chrome":
			driver = new ChromeDriver();
			break;

		case "firefox":
			driver = new FirefoxDriver();
			break;

		case "edge":
			driver = new EdgeDriver();
			break;

		default:
			System.out.println("Wrong Browser :" + browserName + " : Please, enter correct browser Name");
			throw new BrowserException("BROWSER NOT FOUND"); // from 'BrowserException' class
		// break; remove - 'throw' and 'break' cannot co-exist
		}

		return driver;
	}

	// 2. Launch url - after launching browser. You can add checks/validations -
	// like below
	// get method
	public void launchURL(String url) {
		if (url == null) { // null check
			throw new BrowserException("URL NOT FOUND");
		}

		if (url.indexOf("http") == 0) { // http check - if url starts with 'http' or 'https', enter url
			driver.get(url);
		} else {
			throw new BrowserException("HTTP/s MISSING IN URL");
		}
	}

	// OR = to method
	public void launchURL(URL url) {
		if (url == null) { // null check
			throw new BrowserException("URL NOT FOUND");
		}

		String appurl = String.valueOf(url); // convert url to String and store in appurl
		if (appurl.indexOf("http") == 0) {
			// driver.get(appurl);
			driver.navigate().to(url);
		} else {
			throw new BrowserException("HTTP/s MISSING IN URL");
		}
	}

	// driver.

	// 3. Get page title
	public String getPageTitle() {
		// can add validations
		return driver.getTitle();
	}

	// 4. get page url
	public String getPageUrl() {
		// can add validations
		return driver.getCurrentUrl();
	}

	// 5. Close browser
	public void closeBrowser() {
		driver.close();
	}

	// OR
	public void quitBrowser() {
		driver.quit();
	}

}
