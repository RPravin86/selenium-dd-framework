package com.demo.qa.core;

import com.demo.qa.utilities.PropertiesFileReader;
import org.openqa.selenium.PageLoadStrategy;

import java.util.Properties;

/**
 * Centralized configuration provider for the automation framework.
 *
 * <p>Configuration values are resolved using the following precedence:
 * system property, environment variable, then {@code config.properties}.</p>
 *
 * <p>This allows CI/CD pipelines to override execution settings without
 * modifying repository configuration while preserving convenient local
 * defaults in {@code config.properties}.</p>
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

    /** Browser used for the test execution. */
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

    /** Screenshot capture mode used for failed tests. */
    public static final ScreenshotMode SCREENSHOT_MODE =
            ScreenshotMode.from(getRequiredProperty("screenshotMode"));

    /** Root directory used for all generated report types. */
    public static final String REPORT_ROOT =
            getRequiredProperty("reportRoot");

    /** Title displayed in the test execution report. */
    public static final String REPORT_TITLE =
            getRequiredProperty("reportTitle");

    /** Location of JSON and other test-data resources. */
    public static final String TEST_RESOURCE_PATH =
            ROOT + "/src/test/resources/test-data";

    /**
     * Resolves a required configuration value using runtime overrides first.
     *
     * <p>Resolution order:</p>
     * <ol>
     *     <li>Java system property, for example {@code -Dbrowser=firefox}</li>
     *     <li>Environment variable, for example {@code BROWSER=firefox}</li>
     *     <li>{@code config.properties}</li>
     * </ol>
     *
     * @param propertyName configuration property name
     * @return resolved non-blank configuration value
     */
    private static String getRequiredProperty(String propertyName) {
        String value = System.getProperty(propertyName);

        if (value == null || value.isBlank()) {
            value = System.getenv(toEnvironmentKey(propertyName));
        }

        if (value == null || value.isBlank()) {
            value = CONFIG.getProperty(propertyName);
        }

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required configuration property '"
                            + propertyName
                            + "' is missing. Supply it as a system property, "
                            + "environment variable, or in "
                            + CONFIG_FILE_PATH
            );
        }

        return value.trim();
    }

    /**
     * Converts a camelCase property name to an uppercase environment key.
     *
     * <p>Examples: {@code baseUrl -> BASE_URL} and
     * {@code acceptInsecureCerts -> ACCEPT_INSECURE_CERTS}.</p>
     */
    private static String toEnvironmentKey(String propertyName) {
        return propertyName
                .replaceAll("([a-z0-9])([A-Z])", "$1_$2")
                .toUpperCase();
    }

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

    private AppConfig() {
        // Utility/configuration class - no instantiation.
    }
}
