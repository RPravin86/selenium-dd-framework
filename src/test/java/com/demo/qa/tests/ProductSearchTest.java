package com.demo.qa.tests;

import com.demo.qa.core.AppConfig;
import com.demo.qa.core.BaseTest;
import com.demo.qa.core.ObjectRepo;
import com.demo.qa.pageobjects.HomePage;
import com.demo.qa.pageobjects.ProductsPage;
import com.demo.qa.utilities.JsonFileReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Data-driven product search tests for Automation Exercise.
 */
public class ProductSearchTest extends BaseTest {

    /**
     * Verifies that products can be searched and that the expected
     * product appears in the search results.
     *
     * @param testDataMap search test data
     */
    @Test(
            dataProvider = "productSearchData",
            description = "Verify product search results"
    )
    public void verifyProductSearch(Map<String, String> testDataMap) {
        HomePage homePage = ObjectRepo.getInstance().getHomePage();

        String searchTerm = testDataMap.get("searchTerm");
        String expectedProduct = testDataMap.get("expectedProduct");

        Assert.assertTrue(
                homePage.isHomePageDisplayed(),
                "Automation Exercise home page is not displayed"
        );

        Assert.assertTrue(
                homePage.getCurrentUrl().startsWith(AppConfig.BASE_URL),
                "Unexpected application URL: " + homePage.getCurrentUrl()
        );

        ProductsPage productsPage = homePage.navigateToProductsPage();

        Assert.assertTrue(
                productsPage.isProductsPageDisplayed(),
                "All Products heading is not displayed"
        );

        productsPage.searchProduct(searchTerm);

        List<String> actualProductNames = productsPage.getProductNames();
        String normalizedExpectedProduct = normalizeText(expectedProduct);

        boolean productFound = actualProductNames.stream()
                .anyMatch(product -> product.equalsIgnoreCase(normalizedExpectedProduct));

        Assert.assertTrue(
                productFound,
                "Expected product '" + normalizedExpectedProduct
                        + "' was not found. Actual products: " + actualProductNames
        );
    }

    /**
     * Supplies JSON-based product search data to the test and allows
     * independent datasets to execute concurrently.
     *
     * @return TestNG data-provider dataset
     */
    @DataProvider(
            name = "productSearchData",
            parallel = true
    )
    public Object[][] productSearchData() throws Exception {
        List<Map<String, String>> searchData = JsonFileReader.readJson(
                AppConfig.TEST_RESOURCE_PATH + "/product-search.json",
                "SearchData"
        );

        return searchData.stream()
                .map(data -> new Object[]{data})
                .toArray(Object[][]::new);
    }

    private String normalizeText(String text) {
        return text
                .trim()
                .replaceAll("\\s+", " ");
    }
}
