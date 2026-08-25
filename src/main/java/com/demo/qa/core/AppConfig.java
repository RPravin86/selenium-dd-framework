package com.demo.qa.core;

import com.demo.qa.utilities.PropertiesFileReader;
import org.openqa.selenium.PageLoadStrategy;

import java.util.Properties;

/**
 * Centralized configuration provider for the automation framework.
 *
 * <p>Application and framework configuration is loaded from the
 * {@code config.properties} file and exposed through immutable constants.</p>
 *
 * <p>This class is intentionally non-instantiable because configuration
 * values are consumed at framework level rather than through object state.</p>
 */
public final class AppConfig {

    private static final String ROOT = System.getProperty("user.dir");

    private static final String CONFIG_FILE_PATH =
            ROOT + "/config.properties";

    private static final Properties CONFIG =
            PropertiesFileReader.read(CONFIG_FILE_PATH);

    /** Base URL of the application under test. */
    public static final String BASE_URL =
            getRequiredProperty("baseUrl");

    /** Default browser used when no browser is supplied through TestNG. */
    public static final String BROWSER_NAME =
            getRequiredProperty("browser");

    /** Whether supported browsers should run in headless mode. */
    public static final boolean HEADLESS =
            getBooleanProperty("headless");

    /** Whether browser sessions should accept insecure TLS certificates. */
    public static final boolean ACCEPT_INSECURE_CERTS =
            getBooleanProperty("acceptInsecureCerts");

    /** Browser page-load strategy used during navigation. */
    public static final PageLoadStrategy PAGE_LOAD_STRATEGY =
            getPageLoadStrategy("pageLoadStrategy");

    /** Title displayed in the test execution report. */
    public static final String REPORT_TITLE =
            getRequiredProperty("reportTitle");

    /** Base path used for generated test reports. */
    public static final String REPORT_PATH =
            getRequiredProperty("reportPath");

    /** Location of JSON and other test-data resources. */
    public static final String TEST_RESOURCE_PATH =
            ROOT + "/src/test/resources/test-data";

    /**
     * Retrieves a mandatory string configuration property.
     *
     * @param propertyName name of the required property
     * @return trimmed property value
     * @throws IllegalStateException when the property is missing or blank
     */
    private static String getRequiredProperty(String propertyName) {
        String value = CONFIG.getProperty(propertyName);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required configuration property '"
                            + propertyName
                            + "' is missing or blank in "
                            + CONFIG_FILE_PATH
            );
        }

        return value.trim();
    }

    /**
     * Retrieves a mandatory boolean configuration property.
     *
     * <p>Only {@code true} or {@code false} are accepted so configuration
     * mistakes fail fast instead of silently falling back to false.</p>
     *
     * @param propertyName name of the required boolean property
     * @return parsed boolean value
     * @throws IllegalStateException when the property is missing or invalid
     */
    private static boolean getBooleanProperty(String propertyName) {
        String value = getRequiredProperty(propertyName);

        if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
            throw new IllegalStateException(
                    "Configuration property '"
                            + propertyName
                            + "' must be either true or false"
            );
        }

        return Boolean.parseBoolean(value);
    }

    /**
     * Retrieves and validates the Selenium page-load strategy.
     *
     * @param propertyName name of the page-load strategy property
     * @return configured Selenium PageLoadStrategy
     * @throws IllegalStateException when the configured value is unsupported
     */
    private static PageLoadStrategy getPageLoadStrategy(String propertyName) {
        String value = getRequiredProperty(propertyName);

        return switch (value.toLowerCase()) {
            case "normal" -> PageLoadStrategy.NORMAL;
            case "eager" -> PageLoadStrategy.EAGER;
            case "none" -> PageLoadStrategy.NONE;
            default -> throw new IllegalStateException(
                    "Configuration property '"
                            + propertyName
                            + "' must be normal, eager, or none"
            );
        };
    }

    /** Prevents accidental instantiation of the configuration class. */
    private AppConfig() {
        // Utility/configuration class - no instantiation.
    }
}
