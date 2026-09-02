package com.demo.qa.pageobjects;

import com.aventstack.extentreports.Status;
import com.demo.qa.reportmanager.Report;
import com.demo.qa.utilities.WebActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

/**
 * Page Object representing the Automation Exercise products page.
 *
 * <p>Encapsulates product catalogue interactions such as searching
 * for products, validating search results, and opening product details.</p>
 */
public class ProductsPage {

    private final WebActions webActions;

    private final By productsHeading =
            By.xpath("//h2[contains(@class,'title') and contains(text(),'All Products')]");

    private final By searchInput = By.id("search_product");
    private final By searchButton = By.id("submit_search");

    private final By searchedProductsHeading =
            By.xpath("//h2[contains(@class,'title') and contains(text(),'Searched Products')]");

    private final By productNames = By.cssSelector(".productinfo p");

    private final By firstProductViewLink =
            By.cssSelector("a[href='/product_details/1']");

    public ProductsPage(WebDriver driver) {
        this.webActions = new WebActions(driver);
    }

    /**
     * Verifies that the products catalogue has loaded successfully.
     */
    public void verifyProductsPageLoaded() {
        WebElement heading = webActions.waitForVisible(productsHeading);

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
        webActions.type(searchInput, searchTerm);
        webActions.click(searchButton);
        webActions.waitForVisible(searchedProductsHeading);

        Report.log(Status.INFO, "Product search performed for: " + searchTerm);
    }

    /**
     * Verifies that the expected product is present in the search results.
     *
     * @param expectedProduct expected product name
     */
    public void verifySearchResults(String expectedProduct) {
        List<String> actualProductNames = webActions.getTexts(productNames)
                .stream()
                .map(this::normalizeText)
                .toList();

        String normalizedExpectedProduct = normalizeText(expectedProduct);

        boolean productFound = actualProductNames.stream()
                .anyMatch(product ->
                        product.equalsIgnoreCase(normalizedExpectedProduct));

        Assert.assertTrue(
                productFound,
                "Expected product '" + normalizedExpectedProduct
                        + "' was not found. Actual products: " + actualProductNames
        );

        Report.log(
                Status.PASS,
                "Expected product found: " + normalizedExpectedProduct
        );
    }

    /**
     * Opens the details page for the first product in the catalogue.
     */
    public void openFirstProductDetails() {
        webActions.click(firstProductViewLink);
        webActions.waitForUrlContains("/product_details/");

        Report.log(Status.INFO, "Opened first product details page");
    }

    /**
     * Normalizes UI text so harmless differences in whitespace do not
     * cause product-name validations to fail.
     *
     * @param text text to normalize
     * @return trimmed text with consecutive whitespace collapsed to one space
     */
    private String normalizeText(String text) {
        return text
                .trim()
                .replaceAll("\\s+", " ");
    }
}
