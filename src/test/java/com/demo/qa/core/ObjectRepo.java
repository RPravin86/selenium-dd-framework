package com.demo.qa.core;

import com.demo.qa.pageobjects.HomePage;
import com.demo.qa.pageobjects.PerfumePage;

public final class ObjectRepo {

    /**
     * Keeps the page-object repository isolated per test-execution thread so
     * parallel TestNG methods do not share mutable page-object state.
     */
    private static final ThreadLocal<ObjectRepo> THREAD_INSTANCE = new ThreadLocal<>();

    private HomePage homePage;
    private PerfumePage perfumePage;

    private ObjectRepo() {
        // Prevent direct instantiation; instances are managed per execution thread.
    }

    /**
     * Returns the ObjectRepo associated with the current execution thread.
     *
     * @return thread-local ObjectRepo instance
     */
    public static ObjectRepo getInstance() {
        if (THREAD_INSTANCE.get() == null) {
            THREAD_INSTANCE.set(new ObjectRepo());
        }
        return THREAD_INSTANCE.get();
    }

    /**
     * Lazily creates the HomePage using the WebDriver assigned to the current thread.
     *
     * @return current thread's HomePage instance
     */
    public HomePage getHomePage() {
        if (homePage == null) {
            homePage = new HomePage(DriverManager.getDriver());
        }
        return homePage;
    }

    /**
     * Lazily creates the PerfumePage using the WebDriver assigned to the current thread.
     *
     * @return current thread's PerfumePage instance
     */
    public PerfumePage getPerfumePage() {
        if (perfumePage == null) {
            perfumePage = new PerfumePage(DriverManager.getDriver());
        }
        return perfumePage;
    }
}
