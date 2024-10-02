package com.demo.qa.reportmanager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.ExtentSparkReporterConfig;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.demo.qa.core.AppConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentManager implements AppConfig {

	private static ExtentReports extentReport;

	/**
	 * This is a static method that creates and configures ExtentReports object.
	 * This method sets the theme of the report to STANDARD and title to "Test Report".
	 * 
	 * @return ExtentReports
	 */
	public synchronized static ExtentReports getExtentReports() {
		if (extentReport == null) {
			String date = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
			ExtentSparkReporter htmlReporter = new ExtentSparkReporter(REPORT_PATH + date);
			extentReport = new ExtentReports();
			extentReport.attachReporter(htmlReporter);
			htmlReporter.config(
					ExtentSparkReporterConfig.builder().theme(Theme.STANDARD).documentTitle(REPORT_TITLE).build());
		}
		return extentReport;
	}
}
