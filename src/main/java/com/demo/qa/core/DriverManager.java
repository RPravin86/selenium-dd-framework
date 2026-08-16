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
import java.util.Locale;

/**
 * Thread-safe WebDriver lifecycle manager for parallel TestNG execution.
 */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final Logger LOG = LogManager.getLogger(DriverManager.class);
    private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(60);

    private DriverManager() {
        // Utility class - no external instantiation.
    }

    /**
     * Returns the WebDriver associated with the current test thread.
     *
     * @return current thread's WebDriver, or null when it has not been initialized
     */
    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    /**
     * Creates and configures a WebDriver for the current test thread.
     *
     * @param browserName browser name: chrome, chrome-headless, firefox,
     *                    firefox-headless, edge, or safari
     */
    public static void initialize(String browserName) {
        if (browserName == null || browserName.isBlank()) {
            throw new IllegalArgumentException("Browser name must not be null or blank");
        }

        if (DRIVER.get() != null) {
            quit();
        }

        WebDriver driver = createDriver(browserName);
        DRIVER.set(driver);

        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT);

        LOG.info("WebDriver initialized: browser={}, thread={}",
                browserName, Thread.currentThread().getId());
    }

    /**
     * Creates a WebDriver for the requested browser.
     */
    private static WebDriver createDriver(String browserName) {
        String browser = browserName.trim().toLowerCase(Locale.ROOT);

        return switch (browser) {
            case "chrome" -> new ChromeDriver();
            case "chrome-headless" -> new ChromeDriver(
                    new ChromeOptions().addArguments("--headless=new"));
            case "firefox" -> new FirefoxDriver();
            case "firefox-headless" -> new FirefoxDriver(
                    new FirefoxOptions().addArguments("-headless"));
            case "edge" -> new EdgeDriver();
            case "safari" -> new SafariDriver();
            default -> throw new IllegalArgumentException(
                    "Unsupported browser: " + browserName
                            + ". Supported browsers: chrome, chrome-headless, "
                            + "firefox, firefox-headless, edge, safari");
        };
    }

    /**
     * Checks whether the current thread is using the requested browser driver.
     */
    public static boolean isDriverInstanceOf(String browserName) {
        WebDriver driver = getDriver();
        if (driver == null || browserName == null) {
            return false;
        }

        String browser = browserName.trim().toLowerCase(Locale.ROOT);
        return switch (browser) {
            case "chrome", "chrome-headless" -> driver instanceof ChromeDriver;
            case "firefox", "firefox-headless" -> driver instanceof FirefoxDriver;
            case "edge" -> driver instanceof EdgeDriver;
            case "safari" -> driver instanceof SafariDriver;
            default -> false;
        };
    }

    /**
     * Terminates the current WebDriver session safely.
     */
    public static void quit() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            return;
        }

        try {
            driver.quit();
            LOG.info("WebDriver session terminated: thread={}",
                    Thread.currentThread().getId());
        } finally {
            DRIVER.remove();
        }
    }

    /**
     * Removes the thread-local driver reference.
     * Prefer {@link #quit()} during normal test teardown so the browser session
     * is also terminated.
     */
    public static void terminate() {
        DRIVER.remove();
    }
}
