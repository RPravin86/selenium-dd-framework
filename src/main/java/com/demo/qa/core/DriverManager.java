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

/**
 * Manages the WebDriver lifecycle for parallel TestNG execution.
 *
 * <p>A {@link ThreadLocal} WebDriver is maintained so that each test thread
 * receives and operates on its own browser session. This prevents different
 * parallel tests from sharing the same WebDriver instance.</p>
 *
 * <p>The class also centralizes browser creation, browser validation,
 * session configuration, and safe WebDriver cleanup.</p>
 */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final Logger LOG = LogManager.getLogger(DriverManager.class);

    /**
     * Maximum time allowed for a page load to complete.
     *
     * <p>Element synchronization is intentionally handled through explicit
     * waits in the page objects rather than relying on implicit waits.</p>
     */
    private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(60);

    private DriverManager() {
        // Prevent external instantiation.
    }

    /**
     * Returns the WebDriver associated with the current test thread.
     *
     * @return the current thread's WebDriver, or {@code null} when a driver
     *         has not been initialized
     */
    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    /**
     * Creates and configures a WebDriver for the current test thread.
     *
     * @param browserName browser configured through TestNG or properties
     * @throws IllegalArgumentException when the browser name is invalid
     */
    public static void initialize(String browserName) {
        BrowserType browserType = BrowserType.from(browserName);

        if (DRIVER.get() != null) {
            quit();
        }

        WebDriver driver = createDriver(browserType);
        DRIVER.set(driver);

        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT);

        LOG.info(
                "WebDriver initialized: browser={}, thread={}",
                browserType,
                Thread.currentThread().getId()
        );
    }

    /**
     * Creates the requested browser driver.
     *
     * <p>Browser-specific construction stays centralized here so future
     * capabilities can be added without changing test classes.</p>
     */
    private static WebDriver createDriver(BrowserType browserType) {
        return switch (browserType) {
            case CHROME -> new ChromeDriver();

            case CHROME_HEADLESS -> new ChromeDriver(
                    new ChromeOptions()
                            .addArguments("--headless=new"));

            case FIREFOX -> new FirefoxDriver();

            case FIREFOX_HEADLESS -> new FirefoxDriver(
                    new FirefoxOptions()
                            .addArguments("-headless"));

            case EDGE -> new EdgeDriver();

            case SAFARI -> new SafariDriver();
        };
    }

    /**
     * Verifies whether the current test thread is using the requested
     * browser implementation.
     *
     * @param browserName expected browser name
     * @return {@code true} when the current WebDriver matches the requested
     *         browser; otherwise {@code false}
     */
    public static boolean isDriverInstanceOf(String browserName) {
        WebDriver driver = getDriver();

        if (driver == null || browserName == null || browserName.isBlank()) {
            return false;
        }

        BrowserType browserType;
        try {
            browserType = BrowserType.from(browserName);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return switch (browserType) {
            case CHROME, CHROME_HEADLESS -> driver instanceof ChromeDriver;
            case FIREFOX, FIREFOX_HEADLESS -> driver instanceof FirefoxDriver;
            case EDGE -> driver instanceof EdgeDriver;
            case SAFARI -> driver instanceof SafariDriver;
        };
    }

    /**
     * Terminates the current WebDriver session and removes the driver
     * reference from the current thread.
     */
    public static void quit() {
        WebDriver driver = DRIVER.get();

        if (driver == null) {
            return;
        }

        try {
            driver.quit();

            LOG.info(
                    "WebDriver session terminated: thread={}",
                    Thread.currentThread().getId()
            );
        } finally {
            DRIVER.remove();
        }
    }
}
