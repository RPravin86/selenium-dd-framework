package com.demo.qa.tasks;

import com.demo.qa.annotations.Step;
import com.demo.qa.core.ObjectRepo;
import com.demo.qa.pageobjects.CartPage;
import com.demo.qa.pageobjects.HomePage;
import com.demo.qa.pageobjects.ProductDetailsPage;
import com.demo.qa.pageobjects.ProductsPage;

public class CartTasks {

    private ProductsPage productsPage;
    private ProductDetailsPage productDetailsPage;

    @Step("Navigate to the Products page")
    public void navigateToProducts() {
        HomePage homePage = ObjectRepo.getInstance().getHomePage();
        productsPage = homePage.navigateToProductsPage();
    }

    @Step("Open the first product details")
    public void openFirstProduct() {
        productDetailsPage = productsPage.openFirstProductDetails();
    }

    @Step("Add the product to cart and view cart")
    public CartPage addProductToCart() {
        return productDetailsPage.addProductAndViewCart();
    }
}
