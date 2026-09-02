package com.demo.qa.pageobjects;

import com.demo.qa.utilities.WebActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object representing the Automation Exercise cart page.
 *
 * <p>Exposes cart state while test classes remain responsible for
 * expected values and assertions.</p>
 */
public class CartPage {

    private final WebActions webActions;

    private final By productName =
            By.cssSelector("#product-1 .cart_description h4 a");

    private final By productPrice =
            By.cssSelector("#product-1 .cart_price p");

    private final By productQuantity =
            By.cssSelector("#product-1 .cart_quantity button");

    private final By productTotal =
            By.cssSelector("#product-1 .cart_total_price");

    public CartPage(WebDriver driver) {
        this.webActions = new WebActions(driver);
    }

    public String getProductName() {
        return webActions.getText(productName);
    }

    public String getProductPrice() {
        return webActions.getText(productPrice);
    }

    public String getProductQuantity() {
        return webActions.getText(productQuantity);
    }

    public String getProductTotal() {
        return webActions.getText(productTotal);
    }
}
