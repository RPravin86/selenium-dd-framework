package com.demo.qa.expectations;

import com.demo.qa.annotations.Step;
import com.demo.qa.pageobjects.CartPage;
import org.testng.Assert;

public class CartExpectations {

    @Step("Verify product details in cart")
    public void productShouldBeDisplayed(
            CartPage cartPage,
            String expectedName,
            String expectedPrice,
            String expectedQuantity,
            String expectedTotal) {

        Assert.assertEquals(
                cartPage.getProductName(),
                expectedName,
                "Unexpected product in cart"
        );

        Assert.assertEquals(
                cartPage.getProductPrice(),
                expectedPrice,
                "Unexpected product price"
        );

        Assert.assertEquals(
                cartPage.getProductQuantity(),
                expectedQuantity,
                "Unexpected product quantity"
        );

        Assert.assertEquals(
                cartPage.getProductTotal(),
                expectedTotal,
                "Unexpected cart total"
        );
    }
}
