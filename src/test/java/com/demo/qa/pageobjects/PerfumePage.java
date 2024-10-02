package com.demo.qa.pageobjects;

import com.aventstack.extentreports.Status;
import com.demo.qa.reportmanager.Report;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PerfumePage {

	WebDriver driver;

	public By productTitle = By.cssSelector("div[class='text top-brand']");
	public String facetDDOptionLabel = "//div[@class='facet-option__label']//div[text() = '%s']/ancestor::a";
	public String facetDDLabel = "//div[@class='facet']//div[text() = '%s']";
	public String selectedFacetValue = "//button[@class='selected-facets__value' and text() = '%s']";


	public PerfumePage(WebDriver driver) {
		this.driver = driver;
	}

	public void applyFilter(Map<String, String> filterMap){
		for(String key : filterMap.keySet()) {
			String value =  filterMap.get(key);
			WebElement element = driver.findElement(By.xpath(String.format(facetDDLabel, key)));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			element.click();
			element = driver.findElement(By.xpath(String.format(facetDDOptionLabel, value)));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			element.click();
			waitFor(5);
			element = driver.findElement(By.xpath(String.format(selectedFacetValue, value)));
			Assert.assertTrue(element.isDisplayed(), "Filter not applied: " + value);
			Report.log(Status.PASS, "Filter applied: [ " + key + ": " + value + " ]");
		}
	}

	public void fetchListing(){

		List<WebElement> elementList = driver.findElements(productTitle);
		List<String> productList = new ArrayList<>();
		for(WebElement element : elementList){
			productList.add(element.getText());
		}
		Report.log(Status.PASS, "Product listed: " + productList);

	}

	public void waitFor(int timeout){
        try {
            Thread.sleep(1000L * timeout);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
