package com.qa.opencart.pages;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qa.opencart.utils.ElementUtil;

public class ProductDetailsPage {

	private WebDriver driver;
	private ElementUtil eleUtil;
	private Map<String, String> prodMap = new HashMap<String, String>();

	// 1. Private By Locators
//	private By productHeader = By.tagName("MacBook Pro");
	private By productHeader = By.cssSelector("div.col-sm-4 h1");
	private By productImages = By.cssSelector("ul.thumbnails img");
	private By productMetaData = By.xpath("(//div[@id='content']//ul[@class='list-unstyled'])[1]/li");
	private By productPriceData = By.xpath("(//div[@id='content']//ul[@class='list-unstyled'])[2]/li");

	// 2. Page Class Constructor - has to have same name as the className loginPage
	public ProductDetailsPage(WebDriver driver) {// whenever an object of LoginPage, give the driver HERE
		this.driver = driver; // make driver available to the public methods in #3 below
		eleUtil = new ElementUtil(driver);
	}

	// 3.
	public String getProductHeader() {
		String prodHeader = eleUtil.doGetElementText(productHeader);
		System.out.println("Product Header is: " +" " + prodHeader);
		return prodHeader;
	}

	public int getproductImagesCount() {
		int totalImages = eleUtil.waitForElementsVisible(productImages, 10).size();
		System.out.println("Images count for " + getProductHeader()+ " : " + totalImages);
		return totalImages;
	}

//	Brand: Apple
//	Product Code: Product 18
//	Reward Points: 800
//	Availability: In Stock
	private void getProductMetaData() {
		List<WebElement> prodMetaList = eleUtil.getElements(productMetaData);
//		List<String> actPordList = new ArrayList<String>();
		for (WebElement e : prodMetaList) {
			String text = e.getText();
			String metakey = text.split(":")[0].trim();
			String metavalue = text.split(":")[1].trim();
			prodMap.put(metakey, metavalue);
		}
	}

//	$2,000.00
//	Ex Tax: $2,000.00
	private void getProductPriceData() {
		List<WebElement> prodPriceList = eleUtil.getElements(productPriceData);
//		List<String> actPriceList = new ArrayList<String>();
//		for(WebElement e : prodPriceList) {
//			String text = e.getText();
//			actPriceList.add(text);
		String price = prodPriceList.get(0).getText();
		String exTaxPrice = prodPriceList.get(1).getText().split(":")[1].trim();
		prodMap.put("prodprice", price);
		prodMap.put("extaxprice", exTaxPrice);

	}
	
	public Map<String, String> getProductDetailMap() { //public - Give every detail about the product
		prodMap.put("header", getProductHeader());
		getproductImagesCount(); //OR
		prodMap.put("Product Images", String.valueOf(getproductImagesCount()));//convert int to String
		getProductMetaData();;
		getProductPriceData();
		System.out.println("product Details: \n" + prodMap);
		return prodMap;
	
	}

}
