package com.qa.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.utils.ElementUtil;

public class SearchResultsPage {
	
	private WebDriver driver;
	private ElementUtil eleUtil;
	
	//1. Private By Locators
	private By searchProducts = By.cssSelector("div.product-thumb");
	
	//2. Page Class Constructor - has to have same name as the className loginPage
	public SearchResultsPage(WebDriver driver) {    //whenever an object of LoginPage, give me the driver HERE
		this.driver = driver;               //make driver available to the public methods in #3 below
		eleUtil = new ElementUtil(driver);
	}
	
	public int getSearchProductCount() {
		return eleUtil.waitForElementsVisible(searchProducts, 10).size();//verify # of products
	}
	
	public ProductDetailsPage selectProduct(String productName) {
		System.out.println("product selected from results is :" + productName);
		eleUtil.waitForElementVisible(By.linkText(productName), 10).click();//click on any prod
		return new ProductDetailsPage(driver);
	}

}
