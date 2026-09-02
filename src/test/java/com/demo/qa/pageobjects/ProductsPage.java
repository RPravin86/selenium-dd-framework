package com.demo.qa.pageobjects;

import com.aventstack.extentreports.Status;
import com.demo.qa.reportmanager.Report;
import com.demo.qa.utilities.WebActions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

/**
 * Page Object representing the Automation Exercise products page.
 *
 * <p>Encapsulates product catalogue interactions and exposes UI state while
 * test classes remain responsible for business assertions.</p>
 */
public class ProductsPage {

    private final WebDriver driver;
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
        this.driver = driver;
        this.webActions = new WebActions(driver);
    }

    /**
     * Returns whether the products catalogue heading is displayed.
     *
     * @return true when the products page is visible
     */
    public boolean isProductsPageDisplayed() {
        return webActions.waitForVisible(productsHeading).isDisplayed();
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
     * Returns normalized product names displayed in the search results.
     *
     * @return product names with consecutive whitespace collapsed
     */
    public List<String> getProductNames() {
        return webActions.getTexts(productNames)
                .stream()
                .map(this::normalizeText)
                .toList();
    }

    /**
     * Opens the details page for the first product in the catalogue.
     *
     * @return product details page reached after navigation
     */
    public ProductDetailsPage openFirstProductDetails() {
        webActions.click(firstProductViewLink);
        webActions.waitForUrlContains("/product_details/");

        Report.log(Status.INFO, "Opened first product details page");

        return new ProductDetailsPage(driver);
    }

    /**
     * Normalizes UI text so harmless differences in whitespace do not
     * affect product-name comparisons performed by tests.
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
