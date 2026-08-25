package com.demo.qa.reportmanager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ExtentSparkReporterConfig;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.demo.qa.core.AppConfig;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Creates and configures the framework's Extent report instance.
 */
public final class ExtentManager {

    private static ExtentReports extentReport;

    private ExtentManager() {
        // Utility class - no instantiation.
    }

    public static synchronized ExtentReports getExtentReports() {
        if (extentReport == null) {
            createExecutionDirectory();

            ExtentSparkReporter htmlReporter =
                    new ExtentSparkReporter(
                            ReportPathManager.getExtentReportPath().toString());

            extentReport = new ExtentReports();
            extentReport.attachReporter(htmlReporter);

            htmlReporter.config(
                    ExtentSparkReporterConfig.builder()
                            .theme(Theme.STANDARD)
                            .documentTitle(AppConfig.REPORT_TITLE)
                            .build());
        }

        return extentReport;
    }

    private static void createExecutionDirectory() {
        try {
            Files.createDirectories(
                    ReportPathManager.getExecutionDirectory());
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to create report execution directory: "
                            + ReportPathManager.getExecutionDirectory(),
                    e);
        }
    }
}
