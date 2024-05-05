package com.qa.opencart.tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;

public class AccountsPageTest extends BaseTest {

	@BeforeClass
	public void accSetup() {
		accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
	}

	@Test
	public void accPageTitleTest() {
		String accTitle = accPage.getAcctPageTitle();
		Assert.assertEquals(accTitle, AppConstants.ACCOUNTS_PAGE_TITLE);
	}

	@Test
	public void accPageURLTest() {
		String accURL = accPage.getAcctPageURL();
		Assert.assertTrue(accURL.contains(AppConstants.ACC_PAGE_URL_FRACTION));
	}
	
	@Test
	public void isLogoutLinkExistTest() {
		Assert.assertTrue(accPage.islogoutLinkExist());
	}
	
	@Test
	public void isMyAccountLinkExistTest() {
		Assert.assertTrue(accPage.isMyAccountLinkExist());
	}
	
	@Test
	public void accPageHeadersTest() {
		List<String> accHeadersList =  accPage.getAccountPageHeadersList();
		System.out.println(accHeadersList);
	}
	
	@Test
	public void searchTest() {
		accPage.doSearch("macbook");
		
	}
}
