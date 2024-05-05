package com.qa.opencart.utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.opencart.exceptions.ElementException;
import com.qa.opencart.factory.DriverFactory;

import io.qameta.allure.Step;


public class ElementUtil {
	
	private WebDriver driver;// private: not want anyone to create object of ElementUtil and alter
	private JavaScriptUtil jsUtil;
	
	private final String DEFAULT_ELEMENT_TIME_OUT_MESSAGE = "Time out... Element is not found...";
	private final String DEFAULT_ALERT_TIME_OUT_MESSAGE = "Time out... Alert is not found...";

	// ENCAPSULATION: private driver accessed through public constructor or public
	// methods below

	public ElementUtil(WebDriver driver) {
		this.driver = driver;
		jsUtil = new JavaScriptUtil(driver);
	}

	// custom value check - 'doSelectByVisibleText' and 'doSelectByVisibleText'
	// classes below
	private void nullBlankCheck(String value) { // private - user won't see this
		if (value == null || value.length() == 0) { // checks
			throw new ElementException(value + "Value Texts Cannot Be Null or Blank");
		}
	}

	public By getBy(String locatorType, String locatorValue) { // for #8 - String Based utility
		By locator = null;

		switch (locatorType.toLowerCase().trim()) {
		case "id": // For String Based id Utility only
			locator = By.id(locatorValue);
			break;
		case "name":
			locator = By.name(locatorValue);
			break;
		case "classname":
			locator = By.className(locatorValue);
			break;
		case "xpath":
			locator = By.xpath(locatorValue);
			break;
		case "css":
			locator = By.cssSelector(locatorValue);
			break;
		case "linktext":
			locator = By.linkText(locatorValue);
			break;
		case "partiallinktext":
			locator = By.partialLinkText(locatorValue);
			break;
		case "tagname":
			locator = By.tagName(locatorValue);
			break;

		default:
			break;
		}

		return locator;

	}
	
	private void checkHighlight(WebElement element) {
		if (Boolean.parseBoolean(DriverFactory.highlight)) {
			jsUtil.flash(element);
		}
	}
	
	public WebElement getElement(String locatorType, String locatorValue) { // overloaded #8
		WebElement element = driver.findElement(getBy(locatorType, locatorValue));
		checkHighlight(element);
		return element;	
	}
	
	@Step("getting web element using locator {0}")
	public WebElement getElement(By locator) {
		WebElement element = null;
		// if element is not found on page - throw needed exception
		try {
			element = driver.findElement(locator);
			checkHighlight(element);
		} catch (NoSuchElementException e) {
			System.out.println("Element is Not Found On Page");
			e.printStackTrace();
		}
		return element; // when element if present, return element

	}
	
	@Step("entering value: {1} in element : {0}")
	public void doSendKeys(By locator, String value) {
		nullBlankCheck(value); // null check applied here
		getElement(locator).clear();
		getElement(locator).sendKeys(value);
	}

	public void doSendKeys(By locator, String value, int timeOut) {
		nullBlankCheck(value);
		waitForElementVisible(locator, timeOut).sendKeys(value);
	}

	public void doSendKeys(String locatorType, String locatorValue, String value) { // overloaded #8
		getElement(locatorType, locatorValue).sendKeys(value);
	}
	
	@Step("clicking on element using locator: {0}")
	public void doClick(By locator) {
		getElement(locator).click();
	}

	public void doClick(By locator, int timeOut) {
		waitForElementVisible(locator, timeOut).click();
	}

	public String doGetElementText(By locator) {
		return getElement(locator).getText();
	}

	public String doElementGetAttribute(By locator, String attrName) {
		return getElement(locator).getAttribute(attrName);
	}
	
	@Step("checking element {0} is displayed...")//displays locator below
	public boolean isElementDisplayed(By locator) {
		return getElement(locator).isDisplayed();
	}

	public boolean isElementExist(By locator) {// 1 element - preferred to isElementDisplayed method
		if (getElements(locator).size() == 1) {
			return true;
		}
		return false;
	}

	public boolean MultiElementExist(By locator) { // if multiple elements exist
		if (getElements(locator).size() > 0) {
			return true;
		}
		return false;
	}

	public boolean multipleElementsExist(By locator, int elementCount) {
		if (getElements(locator).size() == elementCount) {
			return true;
		}
		return false;
	}

	public List<WebElement> getElements(By locator) {
		return driver.findElements(locator);
	}

	public int getElementsCount(By locator) {
		return getElements(locator).size();
	}

	public ArrayList<String> getElementsTextList(By locator) {
		List<WebElement> elemList = getElements(locator);
		ArrayList<String> elemTextList = new ArrayList<String>();

		for (WebElement e : elemList) {
			String text = e.getText();

			if (text.length() != 0) {
				elemTextList.add(text);
				System.out.println(text);
			}
		}

		return elemTextList;
	}

	public ArrayList<String> getElementAttributeList(By locator, String attrName) {
		List<WebElement> eleList = getElements(locator);
		ArrayList<String> eleAttrList = new ArrayList<String>();

		for (WebElement e : eleList) {
			String attrValue = e.getAttribute(attrName);

			if (attrValue.length() != 0) {
				eleAttrList.add(attrValue);
			}
		}

		return eleAttrList;
	}

//******************************Select based  Drop Down Util**********************************//
	public void doSelectByIndex(By locator, int index) {
		Select select = new Select(getElement(locator));
		select.selectByIndex(index);
	}

	public void doSelectByVisibleText(By locator, String visibletext) {
		nullBlankCheck(visibletext);
		Select select = new Select(getElement(locator));

		try {
			select.selectByVisibleText(visibletext);
		} catch (NoSuchElementException e) {
			System.out.println("visble text is not present in the drop down");
			e.printStackTrace();
			throw new ElementException("text not present");
		}
	}

	public void doSelectByValue(By locator, String value) {
		nullBlankCheck(value);
		Select select = new Select(getElement(locator));
		select.selectByValue(value);
	}

	public List<String> doGetDropDownOptionsTextList(By locator) {// get List of string
		List<WebElement> optionsList = doGetDropDownOptionsList(locator);
		List<String> optionsTextList = new ArrayList<String>();

		for (WebElement e : optionsList) {
			String optionsText = e.getText(); // get all the countries and print each
			optionsTextList.add(optionsText);
		}
		return optionsTextList;
	}

	public List<WebElement> doGetDropDownOptionsList(By locator) {// get list of webelements
//		WebElement element = driver.findElement(locator);
		Select select = new Select(getElement(locator)); // call 'getElement' class instead
		return select.getOptions();
	}

	public int doGetDropDownValuesCount(By locator) { // get count
		return doGetDropDownOptionsList(locator).size();
	}

	public void doSelectAllDropDownValue(By locator, String value) {// select a specific option
		nullBlankCheck(value); // null check exception
		List<WebElement> OptionsList = doGetDropDownOptionsList(locator);
		for (WebElement e : OptionsList) {
			String text = e.getText(); // get all the countries and print each
			System.out.println(text);

			if (text.equals(value)) { // if option becomes 'Nigeria', select it to display & stop
				e.click();
				break;
			}
		}
	}

	public void printSelectAllDropDownValue(By locator) {// print only
		List<WebElement> OptionsList = doGetDropDownOptionsList(locator);
		for (WebElement e : OptionsList) {
			String text = e.getText();
			System.out.println(text);
		}
	}

	// without select class, select drop down value
	public void doSelectValueFromDropDown(By locator, String value) {// w/out select class
		nullBlankCheck(value);
		List<WebElement> OptionsList = getElements(locator);
		System.out.println(OptionsList.size());
		for (WebElement e : OptionsList) {
			String text = e.getText();
			if (text.equals(value)) {
				e.click();
				break;
			}
		}
	}

//************************any Search Field, selecting a suggestion from lists**************************	
	public void doSearch(By searchlocator, By searchSuggestions, String SearchKey, String value)
			throws InterruptedException {
		// driver.findElement(searchlocator).sendKeys(SearchKey);
		doSendKeys(searchlocator, SearchKey); // call existing 'dSsendkeys' method instead
		Thread.sleep(3000);
		List<WebElement> suggList = getElements(searchSuggestions);

		System.out.println(suggList.size());

		for (WebElement e : suggList) {
			String text = e.getText();
			System.out.println(text);

			if (text.contains(value)) {
				e.click();
				break;
			}
		}
	}

	// **********************Actions Utils ************************//
	public void handleMenuandSebMenuLevel2(By parentMenuLocator, By SubMenuLocator) throws InterruptedException {
		Actions act = new Actions(driver);
		act.moveToElement(getElement(parentMenuLocator)).perform();
		Thread.sleep(2000);
		doClick(SubMenuLocator); // use existing doClick method

	}

	public void handleMenuSubMenuLevel4(By parentMenu, By level1Menu, By level2Menu, By level3Menu)
			throws InterruptedException {
		Actions act = new Actions(driver);
		doClick(parentMenu);
		Thread.sleep(2000);
		act.moveToElement(getElement(level1Menu)).perform();
		Thread.sleep(2000);
		act.moveToElement(getElement(level2Menu)).perform();
		Thread.sleep(4000);
		doClick(level3Menu);
	}

	public void handleMenuSubMenuLevel4MouseHover(By level1Menu, By level2Menu, By level3Menu, By level4Menu)
			throws InterruptedException {

		Actions act = new Actions(driver);

		act.moveToElement(getElement(level1Menu)).perform();
		Thread.sleep(1500);
		act.moveToElement(getElement(level2Menu)).perform();
		Thread.sleep(1500);
		act.moveToElement(getElement(level3Menu)).perform();
		Thread.sleep(1500);
		doClick(level4Menu);
	}

	// **********************Actions Click and actions Sendkeys
	// ************************//
	public void doActionsClick(By locator) {
		Actions act = new Actions(driver);
		act.click(getElement(locator)).perform();
	}

	public void doActionsSendKeys(By locator, String value) {
		Actions act = new Actions(driver);
		act.sendKeys(getElement(locator), value).perform();
	}

	// ******************Wait utils*********8//

	/**
	 * An expectation for checking an element is visible and enabled such that you
	 * can click it.
	 * 
	 * @param locator
	 * @param timeOut
	 */
	public void clickWhenReady(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
	}

	/**
	 * An expectation for checking that an element is present on the DOM of a page.
	 * This does not necessarily mean that the element is visible.
	 * 
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	public WebElement waitForElementPresence(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		checkHighlight(element);
		return element;
	}

	/**
	 * An expectation for checking that an element is present on the DOM of a page
	 * and visible. Visibility means that the element is not only displayed but also
	 * has a height and width that is greater than 0.
	 * 
	 * @param locator
	 * @param timeOut default interval time = 500 ms
	 * @return
	 */
	@Step("waiting for element using locator: {0} within timeout of {1}")
	public WebElement waitForElementVisible(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		checkHighlight(element);
		return element;
	}

	public WebElement waitForElementVisible(By locator, int timeOut, int intervalTime) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut), Duration.ofSeconds(intervalTime));
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
		checkHighlight(element);
		return element;
	}

	/**
	 * An expectation for checking that there is at least one element present on a
	 * web page.
	 * 
	 * @param locator
	 * @param timeOut
	 * @return
	 */
	public List<WebElement> waitForElementsPresence(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
	}

	public List<WebElement> waitForElementsPresenceWithFluentWait(By locator, int timeOut, int pollingTime) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofSeconds(pollingTime)).ignoring(NoSuchElementException.class)
				.withMessage(DEFAULT_ELEMENT_TIME_OUT_MESSAGE);
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
	}

	/**
	 * An expectation for checking that all elements present on the web page that
	 * match the locator are visible. Visibility means that the elements are not
	 * only displayed but also have a height and width that is greater than 0.
	 */
	public List<WebElement> waitForElementsVisible(By locator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	public String waitForTitleContains(String titleFraction, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));

		try {
			if (wait.until(ExpectedConditions.titleContains(titleFraction))) {
				return driver.getTitle();
			}
		} catch (Exception e) {
			System.out.println("title is not found within : " + timeOut);
		}
		return null;

	}
	
	@Step("waiting for the expected title...")
	public String waitForTitleIs(String title, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));

		try {
			if (wait.until(ExpectedConditions.titleIs(title))) {
				return driver.getTitle();
			}
		} catch (Exception e) {
			System.out.println("title is not found within : " + timeOut);
		}
		return driver.getTitle();

	}

	@Step("waiting for the expected URL...")
	public String waitForURLContains(String urlFraction, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));

		try {
			if (wait.until(ExpectedConditions.urlContains(urlFraction))) {
				return driver.getCurrentUrl();
			}
		} catch (Exception e) {
			System.out.println("url fraction is not found within : " + timeOut);
		}
		return driver.getCurrentUrl();

	}

	public String waitForURLIs(String url, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));

		try {
			if (wait.until(ExpectedConditions.urlToBe(url))) {
				return driver.getCurrentUrl();
			}
		} catch (Exception e) {
			System.out.println("url is not found within : " + timeOut);
		}
		return driver.getCurrentUrl();

	}

	public Alert waitForJSAlertWithFluentWait(int timeOut, int pollingTime) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofSeconds(pollingTime)).ignoring(NoAlertPresentException.class)
				.withMessage(DEFAULT_ALERT_TIME_OUT_MESSAGE);
		return wait.until(ExpectedConditions.alertIsPresent());
	}

	public Alert waitForJSAlert(int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.alertIsPresent());
	}

	public String getAlertText(int timeOut) {
		return waitForJSAlert(timeOut).getText();
	}

	public void acceptAlert(int timeOut) {
		waitForJSAlert(timeOut).accept();
	}

	public void dismissAlert(int timeOut) {
		waitForJSAlert(timeOut).dismiss();
	}

	public void alertSendKeys(int timeOut, String value) {
		waitForJSAlert(timeOut).sendKeys(value);
	}

	public boolean waitForWindow(int totalNumberOfWindowsToBe, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.numberOfWindowsToBe(totalNumberOfWindowsToBe));
	}

	public void waitForFrameAndSwitchToIt(By frameLocator, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
	}

	public void waitForFrameAndSwitchToIt(int frameIndex, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameIndex));
	}

	public void waitForFrameAndSwitchToIt(WebElement frameElement, int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
	}

	public WebElement retryingElement(By locator, int timeOut) {

		WebElement element = null;
		int attempts = 0;

		while (attempts < timeOut) {

			try {
				element = getElement(locator);
				System.out.println("element is found...." + locator + " in attempt " + attempts);
				break;
			} catch (NoSuchElementException e) {
				System.out.println("element is not found.... " + " in attempts " + attempts);
				TimeUtil.defaultTime();
			}
			attempts++;
		}

		if (element == null) {
			System.out.println("element is not found.....tried for " + timeOut + " times " + " with the interval of "
					+ 500 + " milliseconds...");
			throw new ElementException("No Such Element");
		}
		return element;

	}

	public WebElement retryingElement(By locator, int timeOut, int intervalTime) {

		WebElement element = null;
		int attempts = 0;

		while (attempts < timeOut) {

			try {
				element = getElement(locator);
				System.out.println("element is found...." + locator + " in attempt " + attempts);
				break;
			} catch (NoSuchElementException e) {
				System.out.println("element is not found.... " + " in attempts " + attempts);
				TimeUtil.applyWait(intervalTime);
			}
			attempts++;

		}

		if (element == null) {
			System.out.println("element is not found.....tried for " + timeOut + " times " + " with the interval of "
					+ intervalTime + " seconds...");
			throw new ElementException("No Such Element");
		}
		return element;

	}

}
