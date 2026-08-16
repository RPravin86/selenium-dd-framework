package com.demo.qa.tests;

import com.demo.qa.core.AppConfig;
import com.demo.qa.core.BaseTest;
import com.demo.qa.core.ObjectRepo;
import com.demo.qa.utilities.JsonFileReader;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

@Listeners(com.demo.qa.utilities.TestListener.class)

public class ProductFilterTest extends BaseTest {

    private final ObjectRepo objectRepo = ObjectRepo.getInstance();

    @Test(dataProvider = "filterData", description = "Verify perfume filter")
    public void filter_test(Map<String, String> testDataMap) {
        objectRepo.getHomePage().verifyHomepageLoaded();
        objectRepo.getHomePage().navigateToPerfumePage();
        objectRepo.getPerfumePage().applyFilter(testDataMap);
        objectRepo.getPerfumePage().fetchListing();
    }

    @DataProvider
    public Object[][] filterData() throws Exception {
        List<Map<String, String>> filterData = JsonFileReader.readJson(
                AppConfig.TEST_RESOURCE_PATH + "/filter.json",
                "Criteria");

        return filterData.stream()
                .map(data -> new Object[]{data})
                .toArray(Object[][]::new);
    }
}
