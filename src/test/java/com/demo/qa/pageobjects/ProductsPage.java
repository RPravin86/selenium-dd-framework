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
import java.util.List;

/**
 * Page Object representing the Automation Exercise products page.
 *
 * <p>Encapsulates product catalogue interactions such as searching
 * for products and validating search results.</p>
 */
public class ProductsPage {

    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(20);

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By productsHeading =
            By.xpath("//h2[contains(@class,'title') and contains(text(),'All Products')]");

    private final By searchInput = By.id("search_product");
    private final By searchButton = By.id("submit_search");

    private final By searchedProductsHeading =
            By.xpath("//h2[contains(@class,'title') and contains(text(),'Searched Products')]");

    private final By productNames = By.cssSelector(".productinfo p");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    }

    /**
     * Verifies that the products catalogue has loaded successfully.
     */
    public void verifyProductsPageLoaded() {
        WebElement heading = wait.until(
                ExpectedConditions.visibilityOfElementLocated(productsHeading)
        );

        Assert.assertTrue(
                heading.isDisplayed(),
                "All Products heading is not displayed"
        );

        Report.log(Status.PASS, "Products page loaded successfully");
    }

    /**
     * Searches the product catalogue using the supplied search term.
     *
     * @param searchTerm product text to search
     */
    public void searchProduct(String searchTerm) {
        WebElement searchBox = wait.until(
                ExpectedConditions.visibilityOfElementLocated(searchInput)
        );

        searchBox.clear();
        searchBox.sendKeys(searchTerm);

        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(searchedProductsHeading)
        );

        Report.log(Status.INFO, "Product search performed for: " + searchTerm);
    }

    /**
     * Verifies that the expected product is present in the search results.
     *
     * @param expectedProduct expected product name
     */
    public void verifySearchResults(String expectedProduct) {
        List<WebElement> products = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(productNames)
        );

        List<String> actualProductNames = products.stream()
                .map(WebElement::getText)
                .map(String::trim)
                .toList();

        boolean productFound = actualProductNames.stream()
                .anyMatch(product -> product.equalsIgnoreCase(expectedProduct));

        Assert.assertTrue(
                productFound,
                "Expected product '" + expectedProduct
                        + "' was not found. Actual products: " + actualProductNames
        );

        Report.log(Status.PASS, "Expected product found: " + expectedProduct);
    }
}
