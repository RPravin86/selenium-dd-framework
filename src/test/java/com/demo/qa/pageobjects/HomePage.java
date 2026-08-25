package com.demo.qa.pageobjects;

import com.aventstack.extentreports.Status;
import com.demo.qa.core.AppConfig;
import com.demo.qa.reportmanager.Report;
import com.demo.qa.utilities.WebActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

/**
 * Page Object representing the Automation Exercise home page.
 *
 * <p>The page exposes only high-level user actions and validations so
 * test classes remain independent from Selenium locator details.</p>
 */
public class HomePage {

    private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(20);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final WebActions webActions;

    private final By productsMenu =
            By.cssSelector("a[href='/products']");

    private final By cartMenu =
            By.cssSelector("a[href='/view_cart']");

    private final By signupLoginMenu =
            By.cssSelector("a[href='/login']");

    private final By automationExerciseLogo =
            By.cssSelector(".logo.pull-left img");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, PAGE_LOAD_TIMEOUT);
        this.webActions = new WebActions(driver);
    }

    /**
     * Verifies that the Automation Exercise home page loaded successfully.
     */
    public void verifyHomepageLoaded() {
        webActions.waitForVisible(automationExerciseLogo);

        Assert.assertTrue(
                driver.getCurrentUrl().startsWith(AppConfig.BASE_URL),
                "Unexpected application URL: " + driver.getCurrentUrl()
        );

        Assert.assertTrue(
                driver.findElement(automationExerciseLogo).isDisplayed(),
                "Automation Exercise logo is not displayed"
        );

        Report.log(
                Status.PASS,
                "Automation Exercise home page loaded successfully"
        );
    }

    /**
     * Navigates from the home page to the products catalogue.
     */
    public void navigateToProductsPage() {
        webActions.click(productsMenu);

        wait.until(
                ExpectedConditions.urlContains("/products")
        );

        Report.log(
                Status.PASS,
                "Navigated to Products page"
        );
    }

    /**
     * Navigates to the shopping cart.
     */
    public void navigateToCartPage() {
        webActions.click(cartMenu);

        wait.until(
                ExpectedConditions.urlContains("/view_cart")
        );

        Report.log(
                Status.PASS,
                "Navigated to Cart page"
        );
    }

    /**
     * Navigates to the login/sign-up page.
     *
     * <p>This method is available for future authentication scenarios
     * even though login tests are not part of the first migration phase.</p>
     */
    public void navigateToLoginPage() {
        webActions.click(signupLoginMenu);

        wait.until(
                ExpectedConditions.urlContains("/login")
        );

        Report.log(
                Status.PASS,
                "Navigated to Login page"
        );
    }
}
