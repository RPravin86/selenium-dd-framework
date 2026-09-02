package com.demo.qa.testdata;

/**
 * Typed test data used by product search scenarios.
 *
 * @param searchTerm product search term
 * @param expectedProduct product expected in the search results
 */
public record ProductSearchData(
        String searchTerm,
        String expectedProduct
) {
}
