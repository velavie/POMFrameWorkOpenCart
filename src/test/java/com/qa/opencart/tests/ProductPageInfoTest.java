package com.qa.opencart.tests;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.utils.ExcelUtil;

public class ProductPageInfoTest extends BaseTest{
	
	@BeforeClass
	public void prodInfoPageSetup() {
		accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
	}
	
	@DataProvider
	public Object[][] getProductSearchData() {
		return new Object[][] {
			{"macbook", "MacBook Pro"},
			{"imac", "iMac"},
			{"samsung", "Samsung SyncMaster 941BW"},
			{"samsung", "Samsung Galaxy Tab 10.1"},

		};
	}	
	
	@Test(dataProvider = "getProductSearchData")
	public void productHeaderTest() {
		searchResultsPage = accPage.doSearch("macbook");
		productDetailsPage = searchResultsPage.selectProduct("MacBook Pro");
		Assert.assertEquals(productDetailsPage.getProductHeader(), "MacBook Pro");
	}
	
	
	@DataProvider
	public Object[][] getProductImagesData() {
		return new Object[][] {
			{"macbook", "MacBook Pro", 4},
			{"imac", "iMac", 3},
			{"samsung", "Samsung SyncMaster 941BW", 1},
			{"samsung", "Samsung Galaxy Tab 10.1", 7},
		};
	}
	
	@DataProvider
	public Object[][] getProductImagesDataFromExcel() {
		return ExcelUtil.getTestData(AppConstants.PRODUCT_SHEET_NAME);
	}
	
	@Test(dataProvider = "getProductImagesDataFromExcel")
	public void productImagesCountTest(String searchKey, String productName, String imagesCount) {
		searchResultsPage = accPage.doSearch(searchKey);
		productDetailsPage = searchResultsPage.selectProduct(productName);
		Assert.assertEquals(productDetailsPage.getproductImagesCount(), Integer.parseInt(imagesCount));
	}
	
	@Test
	public void productImagesCountTest() {
		searchResultsPage = accPage.doSearch("macbook");
		productDetailsPage = searchResultsPage.selectProduct("MacBook Air");
		Assert.assertEquals(productDetailsPage.getproductImagesCount(), 4);
	}
	
	@Test
	public void productInfoTest() {
		searchResultsPage  = accPage.doSearch("macbook");
		productDetailsPage = searchResultsPage.selectProduct("MacBook Pro");
		Map<String, String> productActDetailsMap = productDetailsPage.getProductDetailMap();
		softAssert.assertEquals(productActDetailsMap.get("Brand"), "Apple");
		softAssert.assertEquals(productActDetailsMap.get("Product Code"), "Product 18");
		softAssert.assertEquals(productActDetailsMap.get("Availability"), "In Stock");
		softAssert.assertEquals(productActDetailsMap.get("productprice"), "$2,000.00");
		softAssert.assertEquals(productActDetailsMap.get("extaxprice"), "$2,000.00");
		softAssert.assertAll();
	}
	

}
