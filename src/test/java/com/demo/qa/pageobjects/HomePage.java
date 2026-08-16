package com.demo.qa.pageobjects;

import com.aventstack.extentreports.Status;
import com.demo.qa.core.AppConfig;
import com.demo.qa.core.DriverManager;
import com.demo.qa.reportmanager.Report;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class HomePage {

    private static final Duration PAGE_LOAD_TIMEOUT = Duration.ofSeconds(20);

    private final WebDriver driver;
    private final WebDriverWait wait;

    public By logo = By.cssSelector("div.logo");
    public By perfumeMenu = By.xpath("//li[@data-uid='FragrancesNavNode_DE']//a");
    public By shadowHostCookie = By.cssSelector("#usercentrics-root");
    public By acceptAllCookieButton = By.cssSelector("button[data-testid='uc-accept-all-button']");
    public By leftContents = By.xpath("//div[contains(@class, 'left-content-slot')]");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, PAGE_LOAD_TIMEOUT);
    }

    /**
     * Verifies that the home page is loaded and handles the cookie consent dialog
     * when it is supported by the current browser.
     */
    public void verifyHomepageLoaded() {
        Assert.assertEquals(driver.getCurrentUrl(), AppConfig.BASE_URL);
        Report.log(Status.PASS, "Url: " + driver.getCurrentUrl());
        Assert.assertTrue(driver.findElement(logo).isDisplayed(), "Logo not displayed");
        Report.log(Status.PASS, "Home page loaded");
        if (!DriverManager.isDriverInstanceOf("safari")) {
            WebElement shadowHost = driver.findElement(shadowHostCookie);
            SearchContext shadowRoot = shadowHost.getShadowRoot();
            shadowRoot.findElement(acceptAllCookieButton).click();
            Report.log(Status.PASS, "Accepted all Cookies");
        }
    }

    public void navigateToPerfumePage() {
        driver.findElement(perfumeMenu).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(leftContents));
        Report.log(Status.PASS, "Navigated to the Perfume page");
    }
}
