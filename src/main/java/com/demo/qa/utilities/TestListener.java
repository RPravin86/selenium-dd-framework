package com.demo.qa.utilities;

import com.aventstack.extentreports.Status;
import com.demo.qa.core.AppConfig;
import com.demo.qa.core.DriverManager;
import com.demo.qa.reportmanager.Report;
import com.demo.qa.reportmanager.ReportPathManager;
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
 * and configurable failure screenshots.
 */
public final class TestListener implements ITestListener {

    private static final Logger LOG = LogManager.getLogger(TestListener.class);

    private static final DateTimeFormatter SCREENSHOT_TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS");

    @Override
    public void onTestStart(ITestResult result) {
        LOG.info("***** Test Executing: {}", result.getName());
        Report.startTest(
                result.getMethod().getMethodName(),
                result.getMethod().getDescription());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        try {
            LOG.info("Test Passed: {}", result.getName());
            Report.log(Status.PASS, "\tTest Passed", result.getName());
        } finally {
            Report.removeTest();
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            LOG.error("Test Failed: {}", result.getName(), result.getThrowable());

            Report.log(
                    Status.FAIL,
                    "\tTest Failed " + result.getThrowable(),
                    result.getName());

            WebDriver driver = DriverManager.getDriver();

            if (!(driver instanceof TakesScreenshot screenshotDriver)) {
                LOG.warn("Screenshot unavailable for failed test: {}", result.getName());
                return;
            }

            try {
                switch (AppConfig.SCREENSHOT_MODE) {
                    case BASE64 -> captureBase64Screenshot(screenshotDriver);
                    case FILE -> captureFileScreenshot(screenshotDriver, result);
                }
            } catch (IOException e) {
                LOG.error(
                        "Unable to save screenshot for failed test: {}",
                        result.getName(),
                        e);
            }
        } finally {
            Report.removeTest();
        }
    }

    private void captureBase64Screenshot(TakesScreenshot screenshotDriver) {
        String base64Screenshot =
                screenshotDriver.getScreenshotAs(OutputType.BASE64);

        Report.getTest().addScreenCaptureFromBase64String(
                base64Screenshot,
                "Failure Screenshot");
    }

    private void captureFileScreenshot(
            TakesScreenshot screenshotDriver,
            ITestResult result) throws IOException {

        String timestamp =
                LocalDateTime.now().format(SCREENSHOT_TIMESTAMP);

        String screenshotName =
                result.getName()
                        + "_thread-"
                        + Thread.currentThread().getId()
                        + "_"
                        + timestamp
                        + ".png";

        Path screenshotDirectory =
                ReportPathManager.getScreenshotDirectory();

        Files.createDirectories(screenshotDirectory);

        Path destination =
                screenshotDirectory.resolve(screenshotName);

        File source =
                screenshotDriver.getScreenshotAs(OutputType.FILE);

        Files.copy(
                source.toPath(),
                destination,
                StandardCopyOption.REPLACE_EXISTING);

        Report.getTest().addScreenCaptureFromPath(
                "screenshots/" + screenshotName,
                "Failure Screenshot");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        try {
            LOG.info("Test Skipped: {}", result.getName());
            Report.log(
                    Status.SKIP,
                    "\tTest Skipped " + result.getThrowable(),
                    result.getName());
        } finally {
            Report.removeTest();
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        Report.removeTest();
    }

    @Override
    public void onStart(ITestContext context) {
        LOG.info("---------------- TEST EXECUTION STARTED ----------------");
    }

    @Override
    public void onFinish(ITestContext context) {
        Report.flush();
        LOG.info("---------------- TEST EXECUTION FINISHED ----------------");
    }
}
