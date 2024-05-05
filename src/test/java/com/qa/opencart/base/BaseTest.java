package com.qa.opencart.base;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.qa.opencart.factory.DriverFactory;
import com.qa.opencart.pages.AccountsPage;
import com.qa.opencart.pages.LoginPage;
import com.qa.opencart.pages.ProductDetailsPage;
import com.qa.opencart.pages.RegistrationPage;
import com.qa.opencart.pages.SearchResultsPage;

import io.qameta.allure.Step;

public class BaseTest {
	
	WebDriver driver;	
	protected Properties prop;
	DriverFactory df;  //call this class to have access to the driver already initialized there

	protected LoginPage loginPage;//call this class/'protected to make it available not public
	protected AccountsPage accPage;
	protected SearchResultsPage searchResultsPage;
	protected ProductDetailsPage productDetailsPage;
	protected RegistrationPage registrationPage;
	protected SoftAssert softAssert;
	
	@Step("Setup: launching {0} browser & init the properties")
	@Parameters({"browser"})
	@BeforeTest
	public void setup(String browserName) {
		df = new DriverFactory(); //create an object of DriverFactory df
		prop = df.initProp();	  //fetch the entire prop
		
		if(browserName!=null) {
			prop.setProperty("browser", browserName);
		}
		
		driver = df.initDriver(prop);  //give the prop to initDriver method to fetch needed data		
		loginPage = new LoginPage(driver);//create an object of the LoginPage class
		softAssert = new SoftAssert();
	}
	
	@Step("Closing browser")
	@AfterTest
	public void tearDown() {
		driver.quit();
	}
	
}
