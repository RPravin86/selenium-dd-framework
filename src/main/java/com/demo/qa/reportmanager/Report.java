package com.demo.qa.reportmanager;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import java.util.HashMap;
import java.util.Map;


public class Report {
	static Map<Integer, ExtentTest> extentTestMap = new HashMap<>();
	private static final ExtentReports extent = ExtentManager.getExtentReports();
	static ExtentTest test;

	public static synchronized ExtentTest getTest() {
		return extentTestMap.get((int) (Thread.currentThread().getId()));
	}

	public static synchronized void startTest(String testName, String desc) {
		test = extent.createTest(testName, desc);
		extentTestMap.put((int) (Thread.currentThread().getId()), test);
	}

	public static synchronized void endTest() {
		extent.flush();
	}

	public static synchronized void log(Status status, String desc, String methodName) {
		System.out.println(methodName + " : " + desc);
		Report.getTest().log(status, desc);
	}
	
	public static synchronized void log(Status status, String desc) {
		System.out.println("Thread Id : " + Thread.currentThread().getId() + " " + desc);
		Report.getTest().log(status, desc);
	}
	
	public static synchronized void log(Status status, Exception e) {
		Report.getTest().log(status, e);
	}
}
