package com.demo.qa.pageobjects;

import com.demo.qa.utilities.WebActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object representing an Automation Exercise product details page.
 *
 * <p>Exposes product information displayed by the application while test
 * classes remain responsible for expected values and assertions.</p>
 */
public class ProductDetailsPage {

    private final WebActions webActions;

    private final By productName = By.cssSelector(".product-information h2");
    private final By category = By.cssSelector(".product-information p");
    private final By price = By.cssSelector(".product-information span span");

    private final By availability = By.xpath(
            "//div[@class='product-information']//b[text()='Availability:']/parent::p"
    );

    private final By condition = By.xpath(
            "//div[@class='product-information']//b[text()='Condition:']/parent::p"
    );

    private final By brand = By.xpath(
            "//div[@class='product-information']//b[text()='Brand:']/parent::p"
    );

    public ProductDetailsPage(WebDriver driver) {
        this.webActions = new WebActions(driver);
    }

    public String getProductName() {
        return webActions.getText(productName);
    }

    public String getCategory() {
        return webActions.getText(category);
    }

    public String getPrice() {
        return webActions.getText(price);
    }

    public String getAvailability() {
        return webActions.getText(availability);
    }

    public String getCondition() {
        return webActions.getText(condition);
    }

    public String getBrand() {
        return webActions.getText(brand);
    }
}
