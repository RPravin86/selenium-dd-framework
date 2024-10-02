package com.demo.qa.core;

import org.testng.annotations.*;

@Listeners(com.demo.qa.utilities.TestListener.class)

public class BaseTest {
	public ObjectRepo p = ObjectRepo.getInstance();
	/**
	 * This method initializes the driver and launches browser. It maximizes the browser window.
	 * It is called before each test.
	 * 
	 * @param browser: Name of browser
	 */
	@Parameters({ "browser" })
	@BeforeMethod
	public void init(@Optional("") String browser)  {
		browser = (browser.isBlank()) ? AppConfig.BROWSER_NAME : browser;
		DriverManager.initialize(browser);
		DriverManager.getDriver().get(AppConfig.BASE_URL);
	}
	
	/**
	 * quit() method is called after every test. It closes the browser
	 * 
	 */
	@AfterMethod
	public void quit() {
		DriverManager.quit();
	
	}
	
	/**
	 * terminate() method is called after every class. It removes the ThreadLocal driver.
	 */
	@AfterClass
	public void tearDown() {
		DriverManager.terminate();
	}

}
