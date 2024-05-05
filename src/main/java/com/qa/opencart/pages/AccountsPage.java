package com.qa.opencart.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.utils.ElementUtil;

public class AccountsPage {
	
	private WebDriver driver;
	private ElementUtil eleUtil;
	
	//1. Private By Locators
	private By logoutLink = By.linkText("Logout");
	private By myAccountLink = By.linkText("My Account");
	private By headers = By.xpath("//div/h2");
	private By searchField = By.name("search");
	private By searchButton = By.cssSelector("div#search button");
	
	//2. Page Class Constructor - has to have same name as the className loginPage
	public AccountsPage(WebDriver driver) {    //whenever an object of LoginPage, give me the driver HERE
		this.driver = driver;               //make driver available to the public methods in #3 below
		eleUtil = new ElementUtil(driver);
	}
	
	
	//3. Public Page Actions/Methods
	public String getAcctPageTitle() {	
		String title = eleUtil.waitForTitleIs(AppConstants.ACCOUNTS_PAGE_TITLE, 5);
		System.out.println("title of Accounts Page is: " + title);
		return title;
	}
	
	public String getAcctPageURL() {
		String url = eleUtil.waitForURLContains(AppConstants.ACC_PAGE_URL_FRACTION, 5);
		System.out.println("Url of Accounts Page is: " + url);
		return url;
	}
	
	public boolean islogoutLinkExist() {
		return eleUtil.waitForElementVisible(logoutLink, 5).isDisplayed();
	}
	
	public boolean isMyAccountLinkExist() {
		return eleUtil.waitForElementVisible(myAccountLink, 5).isDisplayed();
	}
	
	public List<String> getAccountPageHeadersList() {
		List<WebElement> headersEleList = eleUtil.getElements(headers);
		List<String> headersList = new ArrayList<String>();
		for(WebElement e : headersEleList) {
			String header = e.getText();
			headersList.add(header);
		}
		return headersList;
	}
	
	//to perform search in the search field
	public SearchResultsPage doSearch(String SearchKey) {
		System.out.println("searching for : " + SearchKey);
		eleUtil.doSendKeys(searchField, SearchKey);
		eleUtil.doClick(searchButton);		
		return new SearchResultsPage(driver);
	}

}
