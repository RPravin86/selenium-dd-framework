package com.demo.qa.tests;

import com.demo.qa.core.AppConfig;
import com.demo.qa.core.BaseTest;
import com.demo.qa.core.ObjectRepo;
import com.demo.qa.utilities.JsonFileReader;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

/**
 * Data-driven product search tests for Automation Exercise.
 */
public class ProductSearchTest extends BaseTest {

    private final ObjectRepo objectRepo = ObjectRepo.getInstance();

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
        String searchTerm = testDataMap.get("searchTerm");
        String expectedProduct = testDataMap.get("expectedProduct");

        objectRepo.getHomePage().verifyHomepageLoaded();
        objectRepo.getHomePage().navigateToProductsPage();
        objectRepo.getProductsPage().verifyProductsPageLoaded();
        objectRepo.getProductsPage().searchProduct(searchTerm);
        objectRepo.getProductsPage().verifySearchResults(expectedProduct);
    }

    /**
     * Supplies JSON-based product search data to the test.
     *
     * @return TestNG data-provider dataset
     */
    @DataProvider(name = "productSearchData")
    public Object[][] productSearchData() throws Exception {
        List<Map<String, String>> searchData = JsonFileReader.readJson(
                AppConfig.TEST_RESOURCE_PATH + "/product-search.json",
                "SearchData"
        );

        return searchData.stream()
                .map(data -> new Object[]{data})
                .toArray(Object[][]::new);
    }
}
