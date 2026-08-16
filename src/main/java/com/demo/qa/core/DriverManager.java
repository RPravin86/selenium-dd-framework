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

    /**
     * Private constructor prevents accidental instantiation of this
     * utility-style class.
     */
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
     * <p>If a driver already exists for the current thread, it is safely
     * terminated before a new session is created. This prevents stale
     * browser sessions from being reused accidentally.</p>
     *
     * @param browserName browser to initialize:
     *                    chrome, chrome-headless, firefox,
     *                    firefox-headless, edge, or safari
     * @throws IllegalArgumentException when the browser name is null,
     *                                  blank, or unsupported
     */
    public static void initialize(String browserName) {
        if (browserName == null || browserName.isBlank()) {
            throw new IllegalArgumentException(
                    "Browser name must not be null or blank");
        }

        if (DRIVER.get() != null) {
            quit();
        }

        WebDriver driver = createDriver(browserName);
        DRIVER.set(driver);

        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(PAGE_LOAD_TIMEOUT);

        LOG.info(
                "WebDriver initialized: browser={}, thread={}",
                browserName,
                Thread.currentThread().getId()
        );
    }

    /**
     * Creates the requested browser driver.
     *
     * <p>Keeping browser creation in one place makes it easier to extend
     * the framework with additional browser-specific capabilities without
     * changing the test classes.</p>
     *
     * @param browserName requested browser name
     * @return initialized WebDriver instance
     * @throws IllegalArgumentException when the browser is unsupported
     */
    private static WebDriver createDriver(String browserName) {
        String browser = browserName.trim().toLowerCase(Locale.ROOT);

        return switch (browser) {
            case "chrome" -> new ChromeDriver();

            case "chrome-headless" -> new ChromeDriver(
                    new ChromeOptions()
                            .addArguments("--headless=new"));

            case "firefox" -> new FirefoxDriver();

            case "firefox-headless" -> new FirefoxDriver(
                    new FirefoxOptions()
                            .addArguments("-headless"));

            case "edge" -> new EdgeDriver();

            case "safari" -> new SafariDriver();

            default -> throw new IllegalArgumentException(
                    "Unsupported browser: " + browserName
                            + ". Supported browsers: chrome, chrome-headless, "
                            + "firefox, firefox-headless, edge, safari"
            );
        };
    }

    /**
     * Verifies whether the current test thread is using the requested
     * browser implementation.
     *
     * <p>This helper can be used by framework-level validation or diagnostics
     * without exposing browser-specific implementation details to tests.</p>
     *
     * @param browserName expected browser name
     * @return {@code true} when the current WebDriver matches the requested
     *         browser; otherwise {@code false}
     */
    public static boolean isDriverInstanceOf(String browserName) {
        WebDriver driver = getDriver();

        if (driver == null || browserName == null) {
            return false;
        }

        String browser = browserName.trim().toLowerCase(Locale.ROOT);

        return switch (browser) {
            case "chrome", "chrome-headless" ->
                    driver instanceof ChromeDriver;

            case "firefox", "firefox-headless" ->
                    driver instanceof FirefoxDriver;

            case "edge" ->
                    driver instanceof EdgeDriver;

            case "safari" ->
                    driver instanceof SafariDriver;

            default -> false;
        };
    }

    /**
     * Terminates the current WebDriver session and removes the driver
     * reference from the current thread.
     *
     * <p>{@link ThreadLocal#remove()} is important after the browser session
     * ends because it prevents the WebDriver reference from remaining
     * associated with a reusable TestNG worker thread.</p>
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
