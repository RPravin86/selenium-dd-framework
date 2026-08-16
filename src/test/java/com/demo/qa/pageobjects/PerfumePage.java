package com.demo.qa.pageobjects;

import com.aventstack.extentreports.Status;
import com.demo.qa.reportmanager.Report;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PerfumePage {

    private static final Duration FILTER_WAIT_TIMEOUT = Duration.ofSeconds(10);

    private static final String FACET_OPTION_LABEL =
            "//div[@class='facet-option__label']//div[text() = '%s']/ancestor::a";
    private static final String FACET_LABEL =
            "//div[@class='facet']//div[text() = '%s']";
    private static final String SELECTED_FACET_VALUE =
            "//button[@class='selected-facets__value' and text() = '%s']";

    private final WebDriver driver;
    private final By productTitle = By.cssSelector("div[class='text top-brand']");
    private final WebDriverWait wait;

    public PerfumePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, FILTER_WAIT_TIMEOUT);
    }

    public void applyFilter(Map<String, String> filterMap) {
        for (Map.Entry<String, String> entry : filterMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            WebElement element = driver.findElement(By.xpath(String.format(FACET_LABEL, key)));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            element.click();
            element = driver.findElement(By.xpath(String.format(FACET_OPTION_LABEL, value)));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            element.click();

            // Wait for the selected filter to become visible instead of using a fixed thread sleep.
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(String.format(SELECTED_FACET_VALUE, value))));

            Assert.assertTrue(element.isDisplayed(), "Filter not applied: " + value);
            Report.log(Status.PASS, "Filter applied: [ " + key + ": " + value + " ]");
        }
    }

    public void fetchListing() {
        List<WebElement> elementList = driver.findElements(productTitle);
        List<String> productList = new ArrayList<>();
        for (WebElement element : elementList) {
            productList.add(element.getText());
        }
        Report.log(Status.PASS, "Product listed: " + productList);
    }
}
