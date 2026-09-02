package com.demo.qa.tests;

import com.demo.qa.core.AppConfig;
import com.demo.qa.core.BaseTest;
import com.demo.qa.core.ObjectRepo;
import com.demo.qa.pageobjects.ProductDetailsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Product details validation tests for Automation Exercise.
 */
public class ProductDetailsTest extends BaseTest {

    @Test(description = "Verify first product details")
    public void verifyFirstProductDetails() {
        ObjectRepo objectRepo = ObjectRepo.getInstance();

        Assert.assertTrue(
                objectRepo.getHomePage().isHomePageDisplayed(),
                "Automation Exercise home page is not displayed"
        );

        Assert.assertTrue(
                objectRepo.getHomePage().getCurrentUrl().startsWith(AppConfig.BASE_URL),
                "Unexpected application URL: " + objectRepo.getHomePage().getCurrentUrl()
        );

        objectRepo.getHomePage().navigateToProductsPage();

        Assert.assertTrue(
                objectRepo.getProductsPage().isProductsPageDisplayed(),
                "All Products heading is not displayed"
        );

        objectRepo.getProductsPage().openFirstProductDetails();

        ProductDetailsPage productDetailsPage = objectRepo.getProductDetailsPage();

        Assert.assertEquals(
                productDetailsPage.getProductName(),
                "Blue Top",
                "Unexpected product name"
        );

        Assert.assertTrue(
                productDetailsPage.getCategory().contains("Women > Tops"),
                "Unexpected product category"
        );

        Assert.assertEquals(
                productDetailsPage.getPrice(),
                "Rs. 500",
                "Unexpected product price"
        );

        Assert.assertTrue(
                productDetailsPage.getAvailability().contains("In Stock"),
                "Product is not shown as available"
        );

        Assert.assertTrue(
                productDetailsPage.getCondition().contains("New"),
                "Unexpected product condition"
        );

        Assert.assertTrue(
                productDetailsPage.getBrand().contains("Polo"),
                "Unexpected product brand"
        );
    }
}
