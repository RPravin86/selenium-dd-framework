package com.demo.qa.tests;

import com.demo.qa.core.BaseTest;
import com.demo.qa.core.ObjectRepo;
import com.demo.qa.pageobjects.CartPage;
import com.demo.qa.pageobjects.HomePage;
import com.demo.qa.pageobjects.ProductDetailsPage;
import com.demo.qa.pageobjects.ProductsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * End-to-end cart tests for Automation Exercise.
 */
public class AddToCartTest extends BaseTest {

    @Test(description = "Verify product can be added to cart")
    public void verifyProductAddedToCart() {
        HomePage homePage = ObjectRepo.getInstance().getHomePage();

        Assert.assertTrue(
                homePage.isHomePageDisplayed(),
                "Automation Exercise home page is not displayed"
        );

        ProductsPage productsPage = homePage.navigateToProductsPage();

        Assert.assertTrue(
                productsPage.isProductsPageDisplayed(),
                "Products page is not displayed"
        );

        ProductDetailsPage productDetailsPage = productsPage.openFirstProductDetails();
        CartPage cartPage = productDetailsPage.addProductAndViewCart();

        Assert.assertEquals(
                cartPage.getProductName(),
                "Blue Top",
                "Unexpected product in cart"
        );

        Assert.assertEquals(
                cartPage.getProductPrice(),
                "Rs. 500",
                "Unexpected product price"
        );

        Assert.assertEquals(
                cartPage.getProductQuantity(),
                "1",
                "Unexpected product quantity"
        );

        Assert.assertEquals(
                cartPage.getProductTotal(),
                "Rs. 500",
                "Unexpected cart total"
        );
    }
}
