package com.demo.qa.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
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

    private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(60);

    private DriverManager() {
        // Prevent external instantiation.
    }

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
     * Creates the requested browser. Browser identity is kept separate from
     * runtime options such as headless mode and certificate handling.
     */
    private static WebDriver createDriver(BrowserType browserType) {
        return switch (browserType) {
            case CHROME -> new ChromeDriver(createChromeOptions());
            case FIREFOX -> new FirefoxDriver(createFirefoxOptions());
            case EDGE -> new EdgeDriver(createEdgeOptions());
            case SAFARI -> new SafariDriver();
        };
    }

    private static ChromeOptions createChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        if (AppConfig.HEADLESS) {
            options.addArguments("--headless=new");
        }

        options.setAcceptInsecureCerts(AppConfig.ACCEPT_INSECURE_CERTS);
        return options;
    }

    private static FirefoxOptions createFirefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();

        if (AppConfig.HEADLESS) {
            options.addArguments("-headless");
        }

        options.setAcceptInsecureCerts(AppConfig.ACCEPT_INSECURE_CERTS);
        return options;
    }

    private static EdgeOptions createEdgeOptions() {
        EdgeOptions options = new EdgeOptions();

        if (AppConfig.HEADLESS) {
            options.addArguments("--headless=new");
        }

        options.setAcceptInsecureCerts(AppConfig.ACCEPT_INSECURE_CERTS);
        return options;
    }

    /**
     * Verifies whether the current test thread is using the requested
     * browser implementation.
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
            case CHROME -> driver instanceof ChromeDriver;
            case FIREFOX -> driver instanceof FirefoxDriver;
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
