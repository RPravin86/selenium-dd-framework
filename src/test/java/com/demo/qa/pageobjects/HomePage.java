package com.demo.qa.pageobjects;

import com.aventstack.extentreports.Status;
import com.demo.qa.reportmanager.Report;
import com.demo.qa.utilities.WebActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object representing the Automation Exercise home page.
 *
 * <p>The page exposes high-level user actions and UI state while test
 * classes remain responsible for business assertions.</p>
 */
public class HomePage {

    private final WebDriver driver;
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
        this.webActions = new WebActions(driver);
    }

    /**
     * Returns whether the Automation Exercise logo is displayed.
     *
     * @return true when the home-page logo is visible
     */
    public boolean isHomePageDisplayed() {
        return webActions.waitForVisible(automationExerciseLogo).isDisplayed();
    }

    /**
     * Returns the current browser URL.
     *
     * @return current URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Navigates from the home page to the products catalogue.
     *
     * @return products page reached after navigation
     */
    public ProductsPage navigateToProductsPage() {
        webActions.click(productsMenu);
        webActions.waitForUrlContains("/products");

        Report.log(
                Status.PASS,
                "Navigated to Products page"
        );

        return new ProductsPage(driver);
    }

    /**
     * Navigates to the shopping cart.
     */
    public void navigateToCartPage() {
        webActions.click(cartMenu);
        webActions.waitForUrlContains("/view_cart");

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
        webActions.waitForUrlContains("/login");

        Report.log(
                Status.PASS,
                "Navigated to Login page"
        );
    }
}
