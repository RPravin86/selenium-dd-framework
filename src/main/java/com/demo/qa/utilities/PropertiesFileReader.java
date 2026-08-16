package com.demo.qa.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class responsible for loading Java properties files.
 *
 * <p>The reader does not maintain shared mutable state. A new
 * {@link Properties} instance is created and returned for every
 * successful read operation.</p>
 *
 * <p>Configuration-loading failures are treated as fatal because
 * continuing without required configuration can cause misleading
 * failures later in the test execution.</p>
 */
public final class PropertiesFileReader {

    /**
     * Loads properties from the specified file.
     *
     * @param fileName absolute or relative path of the properties file
     * @return loaded properties
     * @throws IllegalArgumentException when the file path is null or blank
     * @throws IllegalStateException when the file cannot be read
     */
    public static Properties read(String fileName) {

        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException(
                    "Properties file path must not be null or blank");
        }

        Properties properties = new Properties();

        try (FileInputStream inputStream = new FileInputStream(fileName)) {

            properties.load(inputStream);

            return properties;

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Unable to load properties file: " + fileName,
                    e);
        }
    }

    /**
     * Prevents accidental instantiation of this utility class.
     */
    private PropertiesFileReader() {
        // Utility class - no instantiation.
    }
}
