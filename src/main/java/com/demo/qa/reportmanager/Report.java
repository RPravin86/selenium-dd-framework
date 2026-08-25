package com.demo.qa.reportmanager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

/**
 * Thread-safe facade for framework reporting operations.
 *
 * <p>Each TestNG worker thread owns its own {@link ExtentTest} instance so
 * parallel test executions cannot write events to another test's report.</p>
 */
public final class Report {

    private static final ExtentReports EXTENT =
            ExtentManager.getExtentReports();

    private static final ThreadLocal<ExtentTest> EXTENT_TEST =
            new ThreadLocal<>();

    private Report() {
        // Utility class - no instantiation.
    }

    /**
     * Returns the Extent test associated with the current execution thread.
     *
     * @return current thread's ExtentTest, or null when not initialized
     */
    public static ExtentTest getTest() {
        return EXTENT_TEST.get();
    }

    /**
     * Creates and associates a report test with the current execution thread.
     *
     * @param testName test name displayed in the report
     * @param description test description displayed in the report
     */
    public static synchronized void startTest(
            String testName,
            String description) {

        ExtentTest test = EXTENT.createTest(testName, description);
        EXTENT_TEST.set(test);
    }

    /**
     * Removes the current thread's ExtentTest reference after result
     * processing has completed.
     */
    public static void removeTest() {
        EXTENT_TEST.remove();
    }

    /**
     * Writes all accumulated report data to the configured Extent report.
     */
    public static synchronized void flush() {
        EXTENT.flush();
    }

    public static void log(Status status, String description, String methodName) {
        System.out.println(methodName + " : " + description);
        getRequiredTest().log(status, description);
    }

    public static void log(Status status, String description) {
        System.out.println(
                "Thread Id : "
                        + Thread.currentThread().getId()
                        + " "
                        + description);
        getRequiredTest().log(status, description);
    }

    public static void log(Status status, Exception exception) {
        getRequiredTest().log(status, exception);
    }

    private static ExtentTest getRequiredTest() {
        ExtentTest test = getTest();

        if (test == null) {
            throw new IllegalStateException(
                    "ExtentTest has not been initialized for thread "
                            + Thread.currentThread().getId());
        }

        return test;
    }
}
