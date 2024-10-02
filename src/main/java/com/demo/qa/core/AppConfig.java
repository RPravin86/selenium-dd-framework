package com.demo.qa.core;

import com.demo.qa.utilities.PropertiesFileReader;

import java.util.Properties;

/**
 * It is an interface contains application config values as constant for configuration of the application
 *
 * @author Pravin
 *
 */
public interface AppConfig {

	String ROOT = System.getProperty("user.dir");
	Properties CONFIG = PropertiesFileReader.read(ROOT + "/config.properties");

	String BASE_URL = CONFIG.getProperty("baseUrl");
	String BROWSER_NAME = CONFIG.getProperty("browser");
	String REPORT_TITLE = CONFIG.getProperty("reportTitle");
	String REPORT_PATH = CONFIG.getProperty("reportPath");

	String TEST_RESOURCE_PATH = ROOT + "/src/test/resources/test-data";
}
