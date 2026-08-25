package com.demo.qa.reportmanager;

import com.demo.qa.core.AppConfig;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Centralizes report artifact paths for a single framework execution.
 *
 * <p>The execution identifier is generated once per JVM run so the Extent
 * HTML report and any file-based screenshots always share the same dated
 * execution directory.</p>
 */
public final class ReportPathManager {

    private static final DateTimeFormatter EXECUTION_TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private static final String EXECUTION_ID =
            LocalDateTime.now().format(EXECUTION_TIMESTAMP);

    private static final Path EXTENT_EXECUTION_DIRECTORY =
            Path.of(AppConfig.REPORT_ROOT, "extent", EXECUTION_ID);

    private ReportPathManager() {
        // Utility class - no instantiation.
    }

    public static Path getExecutionDirectory() {
        return EXTENT_EXECUTION_DIRECTORY;
    }

    public static Path getExtentReportPath() {
        return EXTENT_EXECUTION_DIRECTORY.resolve("ExtentReport.html");
    }

    public static Path getScreenshotDirectory() {
        return EXTENT_EXECUTION_DIRECTORY.resolve("screenshots");
    }
}
