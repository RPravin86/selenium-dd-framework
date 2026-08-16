package com.demo.qa.utilities;

import com.aventstack.extentreports.Status;
import com.demo.qa.core.DriverManager;
import com.demo.qa.reportmanager.Report;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TestNG listener responsible for test lifecycle logging, reporting,
 * and failure screenshots.
 *
 * <p>The listener integrates TestNG execution events with the framework
 * reporting layer and captures a screenshot when a failed test has a
 * screenshot-capable WebDriver.</p>
 */
public final class TestListener implements ITestListener {

    private static final Logger LOG = LogManager.getLogger(TestListener.class);

    private static final DateTimeFormatter SCREENSHOT_TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info("***** Test Executing: {}", result.getName());
        Report.startTest(
                result.getMethod().getMethodName(),
                result.getMethod().getDescription());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        LOG.info("Test Passed: {}", result.getName());
        Report.log(Status.PASS, "\tTest Passed", result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOG.error("Test Failed: {}", result.getName(), result.getThrowable());

        try {
            WebDriver driver = DriverManager.getDriver();

            if (driver instanceof TakesScreenshot screenshotDriver) {
                String timestamp = LocalDateTime.now().format(SCREENSHOT_TIMESTAMP);
                Path destination = Path.of(
                        System.getProperty("user.dir"),
                        "screenshots",
                        result.getName() + "_" + timestamp + ".png");

                Files.createDirectories(destination.getParent());

                File source = screenshotDriver.getScreenshotAs(OutputType.FILE);
                Files.copy(
                        source.toPath(),
                        destination,
                        StandardCopyOption.REPLACE_EXISTING);

                Report.log(
                        Status.FAIL,
                        "\tTest Failed " + result.getThrowable(),
                        result.getName());
                Report.getTest().addScreenCaptureFromPath(destination.toString());
            } else {
                LOG.warn("Screenshot unavailable for failed test: {}", result.getName());
                Report.log(
                        Status.FAIL,
                        "\tTest Failed " + result.getThrowable(),
                        result.getName());
            }
        } catch (IOException e) {
            LOG.error("Unable to save screenshot for failed test: {}", result.getName(), e);
            Report.log(
                    Status.FAIL,
                    "\tTest Failed " + result.getThrowable(),
                    result.getName());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.info("Test Skipped: {}", result.getName());
        Report.log(
                Status.SKIP,
                "\tTest Skipped " + result.getThrowable(),
                result.getName());
    }

    /**
     * Intentionally unused because the framework does not configure
     * TestNG success-percentage based test execution.
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Intentionally not handled.
    }

    @Override
    public void onStart(ITestContext context) {
        LOG.info("---------------- TEST EXECUTION STARTED ----------------");
    }

    @Override
    public void onFinish(ITestContext context) {
        Report.endTest();
        LOG.info("---------------- TEST EXECUTION FINISHED ----------------");
    }
}
