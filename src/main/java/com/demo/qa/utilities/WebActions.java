package com.demo.qa.utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Provides reusable synchronized Selenium web interactions.
 *
 * <p>Common interaction behavior is centralized here so page objects can
 * focus on application-specific workflows and validations.</p>
 */
public final class WebActions {

    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(20);

    private final WebDriver driver;
    private final WebDriverWait wait;

    public WebActions(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    }

    /**
     * Waits until an element is visible.
     *
     * @param locator element locator
     * @return visible WebElement
     */
    public WebElement waitForVisible(By locator) {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
    }

    /**
     * Clicks an element after waiting for it to become clickable.
     *
     * <p>If another UI element temporarily intercepts the click, the target
     * is scrolled into the center of the viewport and a normal Selenium click
     * is retried. JavaScript click is intentionally not used as a fallback.</p>
     *
     * @param locator element locator
     */
    public void click(By locator) {
        WebElement element = wait.until(
                ExpectedConditions.elementToBeClickable(locator)
        );

        try {
            element.click();
        } catch (ElementClickInterceptedException exception) {
            scrollIntoView(element);

            wait.until(
                    ExpectedConditions.elementToBeClickable(locator)
            ).click();
        }
    }

    /**
     * Clears an input and enters the supplied text.
     *
     * @param locator input locator
     * @param text text to enter
     */
    public void type(By locator, String text) {
        WebElement element = waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Returns trimmed visible text for an element.
     *
     * @param locator element locator
     * @return trimmed element text
     */
    public String getText(By locator) {
        return waitForVisible(locator)
                .getText()
                .trim();
    }

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                element
        );
    }
}
