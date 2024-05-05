package com.qa.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.logger.Log;
import com.qa.opencart.utils.ElementUtil;
import com.qa.opencart.utils.TimeUtil;

import io.qameta.allure.Step;

public class LoginPage {
	
//	NO testNG Annotation
//	NO Assertion
//	NO TestNG code
		
	//Page Class/Page Library/Page Object - with Private By Locator and Public Methods
	private WebDriver driver;
	private ElementUtil eleUtil;
	
	//1. Private By Locators
	private By emailid = By.id("input-email");
	private By password = By.id("input-password");
	private By forgotPWdLink = By.linkText("Forgotten Password");
	private By loginButton = By.xpath("//input[@value='Login']");
	private By registerLink = By.linkText("Register");
	
	
	
	//2. Page Class Constructor - has to have same name as the className loginPage
	public LoginPage(WebDriver driver) {    //whenever an object of LoginPage, give me the driver HERE
		this.driver = driver;               //make driver available to the public methods in #3 below
		eleUtil = new ElementUtil(driver);
	}
	
	
	//3. Public Page Actions/Methods
	@Step("getting login page title....")
	public String getLoginPageTitle() {	
		String title = eleUtil.waitForTitleIs(AppConstants.LOGIN_PAGE_TITLE, TimeUtil.DEFAULT_Medium_TIME);
		//System.out.println("title of Login Page is: " + title);
		Log.info("Login page title : " + title);
		return title;
	}
	
	@Step("getting login page URL....")
	public String getLoginPageURL() {
		String url = eleUtil.waitForURLContains(AppConstants.LOGIN_PAGE_URL_FRACTION, TimeUtil.DEFAULT_Medium_TIME);
		System.out.println("Url of Login Page is: " + url);
		return url;
	}
	
	@Step("waiting for status of forgot pwd link...")
	public boolean isForgotPwdLinkExist() {
		return eleUtil.isElementDisplayed(forgotPWdLink);
	}
	
	//encapsulation - accessing the private class above from the public class here
	@Step("login with username: {0} and password: {1}")
	public AccountsPage doLogin(String username, String pwd) {
		System.out.println("user credentials :" +username +": " +pwd);
		eleUtil.waitForElementVisible(emailid, TimeUtil.DEFAULT_Long_TIME).sendKeys(username);
		eleUtil.doSendKeys(password, pwd);
		eleUtil.doClick(loginButton);	
		return new AccountsPage(driver); //New landing page Class Object - enables next page
	}
	
	@Step("navigating to the registration page...")
	public RegistrationPage navigateToRegistrationPage() {
		eleUtil.waitForElementVisible(registerLink, TimeUtil.DEFAULT_Long_TIME).click();
		return new RegistrationPage(driver);
	}

}
