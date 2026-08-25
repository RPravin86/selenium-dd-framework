package com.demo.qa.core;

import java.util.Locale;

/**
 * Supported screenshot capture modes for failed tests.
 */
public enum ScreenshotMode {
    BASE64("base64"),
    FILE("file");

    private final String configValue;

    ScreenshotMode(String configValue) {
        this.configValue = configValue;
    }

    /**
     * Converts the configured screenshot value into a validated mode.
     *
     * @param value configured screenshot mode
     * @return matching screenshot mode
     * @throws IllegalArgumentException when the value is unsupported
     */
    public static ScreenshotMode from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Screenshot mode must not be null or blank");
        }

        String normalized = value.trim().toLowerCase(Locale.ROOT);

        for (ScreenshotMode mode : values()) {
            if (mode.configValue.equals(normalized)) {
                return mode;
            }
        }

        throw new IllegalArgumentException(
                "Unsupported screenshot mode: " + value
                        + ". Supported values: base64, file");
    }
}
