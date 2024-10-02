package com.demo.qa.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

import java.time.Duration;

public class DriverManager {

	protected static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();
	static Logger log = LogManager.getLogger(DriverManager.class);

	private DriverManager()	{
		// No external instantiation allowed
	}

	/**
	 * This method is used to get the driver.
	 *
	 * @return WebDriver
	 */
	public static WebDriver getDriver() {
		return driverThread.get();
	}


	/**
	 * This method initializes the driver and launches browser.
	 *
	 * @param browser: Name of browser
	 */
	public static void initialize(String browser)  {
		driverThread.set(getDriver(browser));
        log.info("Browser configured: {}", browser.toUpperCase());

		getDriver().manage().window().maximize();
		getDriver().manage().deleteAllCookies();

		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
	}

	/**
	 * This method is used to get the driver WRT browser parameter.
	 * Supported browsers - Chrome, Chrome-headless Firefox, Firefox-headless, and Edge.
	 *
	 * @param browserName: Name of browser
	 * @return WebDriver
	 */
	public static WebDriver getDriver(String browserName) {
		WebDriver driver = null;

		switch (browserName) {
			case "chrome":
				driver = new ChromeDriver();
				break;
			case "chrome-headless":
				driver = new ChromeDriver(new ChromeOptions().addArguments("--headless=new"));
				break;
			case "firefox":
				driver = new FirefoxDriver();
				break;
			case "firefox-headless":
				driver = new FirefoxDriver(new FirefoxOptions().addArguments("-headless"));
				break;
			case "edge":
				driver = new EdgeDriver();
				break;
			case "safari":
				driver = new SafariDriver();
				break;
			default:
				System.out.println("No driver available for:" + browserName);
		}
		return driver;
	}

	public static boolean isDriverInstanceOf(String browserName){
		boolean flag = false;
		switch (browserName) {
			case "chrome":
			case "chrome-headless":
				flag = getDriver() instanceof ChromeDriver;
				break;
			case "firefox":
			case "firefox-headless":
				flag = getDriver() instanceof FirefoxDriver;
				break;
			case "edge":
				flag = getDriver() instanceof EdgeDriver;
				break;
			case "safari":
				flag = getDriver() instanceof SafariDriver;
				break;
			default:
				System.out.println("No driver available for:" + browserName);
		}
		return flag;
	}

	/**
	 * This method is used to close the browser
	 *
	 */
	public static void quit() {
		getDriver().manage().deleteAllCookies();
		getDriver().close();
	}

	/**
	 * This method is used to remove the ThreadLocal driver.
	 */
	public static void terminate() {
		driverThread.remove();
	}

}
