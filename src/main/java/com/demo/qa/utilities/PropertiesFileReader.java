package com.demo.qa.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFileReader {
	public static Properties properties;

	/**
	 * Reads properties file and returns instance of Properties class
	 * 
	 * @param fileName: File to read
	 * @return Properties
	 */
	public static Properties read(String fileName) {
		try {
			properties = new Properties();
			FileInputStream ip = new FileInputStream(fileName);
			properties.load(ip);
		} catch (FileNotFoundException e) {
			System.out.println("Exception: File not found");
		} catch (IOException e) {
			System.out.println("Exception: IO Exception");
		}
		return properties;
	}
}
