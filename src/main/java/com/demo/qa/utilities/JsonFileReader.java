package com.demo.qa.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
     * array element into the requested data type.
     *
     * @param fileName path of the JSON file
     * @param jsonKey root-level key containing the JSON array
     * @param dataType target type for each JSON array element
     * @param <T> target data type
     * @return list of typed JSON objects
     * @throws IOException if the file cannot be read, parsed, or the key
     *                     does not contain an array
     */
    public static <T> List<T> readJson(
            String fileName,
            String jsonKey,
            Class<T> dataType) throws IOException {

        JsonNode rootNode = OBJECT_MAPPER.readTree(new File(fileName));
        JsonNode dataNode = rootNode.get(jsonKey);

        if (dataNode == null || !dataNode.isArray()) {
            throw new IOException(
                    "JSON key '" + jsonKey
                            + "' does not contain an array: " + fileName
            );
        }

        return OBJECT_MAPPER.readerForListOf(dataType)
                .readValue(dataNode);
    }

    /**
     * Reads a root-level JSON array and converts each nested array into a
     * TestNG-compatible two-dimensional object array.
     *
     * @param filePath path of the JSON file
     * @return two-dimensional object array
     * @throws IOException if the file cannot be read, parsed, or contains
     *                     an invalid JSON array structure
     */
    public static Object[][] readJSONAs2DArray(String filePath) throws IOException {
        JsonNode rootNode = OBJECT_MAPPER.readTree(new File(filePath));

        if (!rootNode.isArray()) {
            throw new IOException("Expected JSON root to be an array: " + filePath);
        }

        Object[][] array = new Object[rootNode.size()][];
        for (int i = 0; i < rootNode.size(); i++) {
            JsonNode rowNode = rootNode.get(i);
            if (!rowNode.isArray()) {
                throw new IOException(
                        "Expected JSON row at index " + i + " to be an array: " + filePath);
            }
            array[i] = OBJECT_MAPPER.convertValue(rowNode, Object[].class);
        }
        return array;
    }
}
