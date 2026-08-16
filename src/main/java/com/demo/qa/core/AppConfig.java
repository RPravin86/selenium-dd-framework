package com.demo.qa.core;

import com.demo.qa.utilities.PropertiesFileReader;

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

    /**
     * Base URL of the application under test.
     */
    public static final String BASE_URL =
            getRequiredProperty("baseUrl");

    /**
     * Default browser used when no browser is supplied through TestNG.
     */
    public static final String BROWSER_NAME =
            getRequiredProperty("browser");

    /**
     * Title displayed in the test execution report.
     */
    public static final String REPORT_TITLE =
            getRequiredProperty("reportTitle");

    /**
     * Base path used for generated test reports.
     */
    public static final String REPORT_PATH =
            getRequiredProperty("reportPath");

    /**
     * Location of JSON and other test-data resources.
     */
    public static final String TEST_RESOURCE_PATH =
            ROOT + "/src/test/resources/test-data";

    /**
     * Retrieves a mandatory configuration property.
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
     * Prevents accidental instantiation of the configuration class.
     */
    private AppConfig() {
        // Utility/configuration class - no instantiation.
    }
}
