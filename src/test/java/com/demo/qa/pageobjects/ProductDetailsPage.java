package com.demo.qa.pageobjects;

import com.aventstack.extentreports.Status;
import com.demo.qa.reportmanager.Report;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

/**
 * Page Object representing an Automation Exercise product details page.
 */
public class ProductDetailsPage {

    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(20);

    private final WebDriverWait wait;

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
        this.wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    }

    /**
     * Verifies the key details displayed for the first product.
     */
    public void verifyProductDetails() {
        WebElement nameElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(productName)
        );

        Assert.assertEquals(
                nameElement.getText().trim(),
                "Blue Top",
                "Unexpected product name"
        );

        Assert.assertTrue(
                getText(category).contains("Women > Tops"),
                "Unexpected product category"
        );

        Assert.assertEquals(
                getText(price),
                "Rs. 500",
                "Unexpected product price"
        );

        Assert.assertTrue(
                getText(availability).contains("In Stock"),
                "Product is not shown as available"
        );

        Assert.assertTrue(
                getText(condition).contains("New"),
                "Unexpected product condition"
        );

        Assert.assertTrue(
                getText(brand).contains("Polo"),
                "Unexpected product brand"
        );

        Report.log(Status.PASS, "Product details validated successfully");
    }

    /**
     * Returns visible text for the supplied locator.
     *
     * @param locator element locator
     * @return trimmed visible text
     */
    private String getText(By locator) {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        ).getText().trim();
    }
}
