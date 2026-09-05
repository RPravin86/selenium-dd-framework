package com.demo.qa.testdata;

import java.util.UUID;

public final class UserDataFactory {

    private UserDataFactory() {
        // Utility class - no instantiation.
    }

    public static UserData createUniqueUser() {
        String uniqueId = UUID.randomUUID()
                .toString()
                .substring(0, 8);

        return new UserData(
                "Automation User",
                "automation.user." + uniqueId + "@example.com",
                "Test@12345",
                "Mr",
                "15",
                "June",
                "1990",
                "Automation",
                "User",
                "OpenAI Test Company",
                "123 Automation Street",
                "QA District",
                "India",
                "Maharashtra",
                "Pune",
                "411001",
                "9876543210"
        );
    }
}
