package com.demo.qa.utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonFileReader {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Reads a JSON file and returns the root JSON object as a tree node.
     *
     * @param fileName path of the JSON file
     * @return root JSON node
     * @throws IOException if the file cannot be read or parsed
     */
    public static JsonNode readJson(String fileName) throws IOException {
        return OBJECT_MAPPER.readTree(new File(fileName));
    }

    /**
     * Reads a JSON array from the specified root-level key and converts each
     * array element into a map of string key/value pairs.
     *
     * @param fileName path of the JSON file
     * @param jsonKey root-level key containing the JSON array
     * @return list of JSON objects represented as string maps
     * @throws IOException if the file cannot be read or parsed
     */
    public static List<Map<String, String>> readJson(
            String fileName,
            String jsonKey) throws IOException {

        JsonNode rootNode = OBJECT_MAPPER.readTree(new File(fileName));
        JsonNode dataNode = rootNode.get(jsonKey);

        if (dataNode == null || !dataNode.isArray()) {
            throw new IOException("JSON key '" + jsonKey + "' does not contain an array: " + fileName);
        }

        return OBJECT_MAPPER.convertValue(
                dataNode,
                new TypeReference<List<Map<String, String>>>() {
                });
    }

    /**
     * Reads a root-level JSON array and converts each nested array into a
     * TestNG-compatible two-dimensional object array.
     *
     * @param filePath path of the JSON file
     * @return two-dimensional object array, or null when the JSON cannot be read
     */
    public static Object[][] readJSONAs2DArray(String filePath) {
        try {
            JsonNode rootNode = OBJECT_MAPPER.readTree(new File(filePath));

            if (!rootNode.isArray()) {
                System.err.println("Error: Root object is not a JSONArray.");
                return null;
            }

            Object[][] array = new Object[rootNode.size()][];
            for (int i = 0; i < rootNode.size(); i++) {
                JsonNode rowNode = rootNode.get(i);
                if (!rowNode.isArray()) {
                    System.err.println("Error: JSON row at index " + i + " is not an array.");
                    return null;
                }
                array[i] = OBJECT_MAPPER.convertValue(rowNode, Object[].class);
            }
            return array;
        } catch (IOException e) {
            return null;
        }
    }
}
