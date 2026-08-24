package com.demo.qa.tests;

import com.demo.qa.core.BaseTest;
import com.demo.qa.core.ObjectRepo;
import org.testng.annotations.Test;

/**
 * Product details validation tests for Automation Exercise.
 */
public class ProductDetailsTest extends BaseTest {

    @Test(description = "Verify first product details")
    public void verifyFirstProductDetails() {
        ObjectRepo objectRepo = ObjectRepo.getInstance();

        objectRepo.getHomePage().verifyHomepageLoaded();
        objectRepo.getHomePage().navigateToProductsPage();
        objectRepo.getProductsPage().verifyProductsPageLoaded();
        objectRepo.getProductsPage().openFirstProductDetails();
        objectRepo.getProductDetailsPage().verifyProductDetails();
    }
}
