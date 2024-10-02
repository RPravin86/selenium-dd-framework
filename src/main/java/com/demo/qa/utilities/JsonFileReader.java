package com.demo.qa.utilities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class JsonFileReader {

	/**
	 * Reads JSON file and returns JSONArray
	 * 
	 * @param fileName: Name of JSON file
	 * @return JSONObject
	 * @throws IOException, ParseException
	 */

	public static JSONObject readJson(String fileName) throws IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
        try (FileReader filereader = new FileReader(fileName)) {
            return (JSONObject) jsonParser.parse(filereader);
        }
    }

	/**
	 * Reads JSON file and returns JSONArray with specific sets of JSON key
	 * 
	 * @param filename: Name of JSON file
	 * @param Jsonkey: Key to search for in JSON
	 * @throws IOException, ParseException
	 */
	public static JSONArray readJson(String filename, String Jsonkey) throws IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
		FileReader filereader = new FileReader((filename));
		JSONObject obj = (JSONObject) jsonParser.parse(filereader);
        return (JSONArray) obj.get(Jsonkey);
	}

	public static Object[][] readJSONAs2DArray(String filePath) {
		try {
			// Create a JSONParser object
			JSONParser parser = new JSONParser();

			// Read the JSON file
			Object obj = parser.parse(new FileReader(filePath));

			// Check if the root object is a JSONArray
			if (obj instanceof JSONArray) {
				JSONArray jsonArray = (JSONArray) obj;

				// Convert the JSONArray to a 2D array
				Object[][] array = new Object[jsonArray.size()][];
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONArray rowArray = (JSONArray) jsonArray.get(i);
					array[i] = rowArray.toArray();
				}
				return array;
			} else {
				System.err.println("Error: Root object is not a JSONArray.");
				return null;
			}
		} catch (IOException | ParseException e) {
			return null;
		}
	}

}
