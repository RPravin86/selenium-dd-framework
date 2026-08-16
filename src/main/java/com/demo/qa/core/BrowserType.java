package com.demo.qa.core;

import java.util.Locale;

/**
 * Supported browser types for local WebDriver execution.
 */
public enum BrowserType {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge"),
    SAFARI("safari");

    private final String configValue;

    BrowserType(String configValue) {
        this.configValue = configValue;
    }

    /**
     * Converts the browser value from TestNG or properties configuration
     * into a validated browser type.
     *
     * @param value configured browser name
     * @return matching browser type
     * @throws IllegalArgumentException when the value is unsupported
     */
    public static BrowserType from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Browser name must not be null or blank");
        }

        String normalized = value.trim().toLowerCase(Locale.ROOT);

        for (BrowserType browserType : values()) {
            if (browserType.configValue.equals(normalized)) {
                return browserType;
            }
        }

        throw new IllegalArgumentException("Unsupported browser: " + value);
    }
}
