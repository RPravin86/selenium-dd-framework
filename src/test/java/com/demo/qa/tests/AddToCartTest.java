package com.demo.qa.tests;

import com.demo.qa.core.BaseTest;
import com.demo.qa.expectations.CartExpectations;
import com.demo.qa.pageobjects.CartPage;
import com.demo.qa.tasks.CartTasks;
import org.testng.annotations.Test;

/**
 * End-to-end cart tests for Automation Exercise.
 */
public class AddToCartTest extends BaseTest {

    @Test(description = "Verify product can be added to cart")
    public void verifyProductAddedToCart() {
        CartTasks cartTasks = new CartTasks();
        CartExpectations cartExpectations = new CartExpectations();

        cartTasks.navigateToProducts();
        cartTasks.openFirstProduct();

        CartPage cartPage = cartTasks.addProductToCart();

        cartExpectations.productShouldBeDisplayed(
                cartPage,
                "Blue Top",
                "Rs. 500",
                "1",
                "Rs. 500"
        );
    }
}
