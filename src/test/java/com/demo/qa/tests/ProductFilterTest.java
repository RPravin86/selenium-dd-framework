package com.demo.qa.tests;

import com.demo.qa.core.AppConfig;
import com.demo.qa.core.BaseTest;
import com.demo.qa.utilities.JsonFileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Listeners(com.demo.qa.utilities.TestListener.class)

public class ProductFilterTest extends BaseTest {

	@Test(dataProvider = "filterData", description = "Verify perfume filter")
	public void filter_test(Map<String, String> testDataMap) {
		p.getHomePage().verifyHomepageLoaded();
		p.getHomePage().navigateToPerfumePage();
		p.getPerfumePage().applyFilter(testDataMap);
		p.getPerfumePage().fetchListing();
	}

	@DataProvider
	public Object[][] filterData() throws Exception {
		JSONArray jsonArray = JsonFileReader.readJson(AppConfig.TEST_RESOURCE_PATH + "/filter.json"
				, "Criteria");
		Object[][] dataObj = new Object[jsonArray.size()][1];

		for (int i = 0; i < jsonArray.size(); i++) {
			Map<String, String> map = new HashMap<>();
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			for(Object key : jsonObject.keySet()){
				map.put((String)key, (String)jsonObject.get(key));
			}
			dataObj[i][0] = map;
		}

		System.out.println(Arrays.toString(dataObj));
		return dataObj;
	}
}
