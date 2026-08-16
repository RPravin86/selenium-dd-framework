package com.demo.qa.core;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

/**
 * Base test class responsible for managing the WebDriver lifecycle
 * for TestNG test classes.
 *
 * <p>A new WebDriver instance is created before every test method and
 * safely terminated after every test method. The browser can be
 * overridden through TestNG XML parameters.</p>
 *
 * <p>This class is intentionally abstract because it provides common
 * test infrastructure and is not intended to be executed directly.</p>
 */
public abstract class BaseTest {

    /*
     * TestListener is registered centrally through testng.xml.
     *
     * Keeping listener registration in one place avoids duplicate listener
     * callbacks when multiple test classes extend this base class.
     *
     * If programmatic listener registration is required in the future,
     * the following annotation can be enabled:
     *
     * @Listeners(com.demo.qa.utilities.TestListener.class)
     */

    /**
     * Initializes the WebDriver before each test method and navigates
     * to the configured application URL.
     *
     * <p>The browser can be supplied through the TestNG XML parameter.
     * When no browser is specified, the default browser from
     * {@link AppConfig#BROWSER_NAME} is used.</p>
     *
     * @param browser browser requested by the TestNG configuration
     */
    @Parameters({"browser"})
    @BeforeMethod
    public void setUp(@Optional("") String browser) {

        String selectedBrowser = browser.isBlank()
                ? AppConfig.BROWSER_NAME
                : browser;

        DriverManager.initialize(selectedBrowser);
        DriverManager.getDriver().get(AppConfig.BASE_URL);
    }

    /**
     * Terminates the WebDriver session after each test method.
     *
     * <p>{@code alwaysRun = true} ensures that browser cleanup is
     * attempted even when the test method fails.</p>
     */
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quit();
    }
}
