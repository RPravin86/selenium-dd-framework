# Selenium Data-Driven Automation Framework

> Production-oriented UI test automation framework built with Java 17, Selenium 4 and TestNG, designed for maintainability, scalability and parallel execution.

![Java](https://img.shields.io/badge/Java-17-blue)
![Selenium](https://img.shields.io/badge/Selenium-4-green)
![TestNG](https://img.shields.io/badge/TestNG-7.12-orange)
![Maven](https://img.shields.io/badge/Maven-Build-red)
![Jackson](https://img.shields.io/badge/Jackson-2.21.5-blue)

## ⭐ Project Highlights

- **Thread-safe WebDriver management** using `ThreadLocal<WebDriver>`
- **Cross-browser execution** across Chrome, Firefox, Edge and Safari
- **Configuration-driven execution** for headless mode and insecure certificates
- **Page Object Model** for maintainable and reusable UI automation
- **JSON data-driven testing** using Jackson and TestNG `DataProvider`
- **Explicit-wait synchronization** without dependency on implicit waits
- **ExtentReports + Log4j2** integration for reporting and diagnostics
- **Modern Java 17 + Selenium 4 architecture** using Selenium Manager

---

## 🚀 Overview

This project demonstrates a production-oriented Selenium automation framework designed around clean architecture, reusable components and configuration-driven execution.

The framework focuses on solving common challenges in UI automation such as:

- Maintainable Page Object architecture
- Thread-safe WebDriver management
- Data-driven test execution
- Cross-browser execution
- Centralized configuration
- Structured logging
- Automated reporting
- CI/CD readiness

---

## 🛠 Technology Stack

| Technology | Purpose |
|---|---|
| Java 17 | Programming language |
| Selenium 4 | Web UI automation |
| TestNG | Test execution |
| Maven | Build & dependency management |
| Jackson | JSON test-data processing |
| Log4j 2 | Logging |
| ExtentReports | Test reporting |
| Git | Version control |

---

## 🏗 Framework Architecture

```text
                    TestNG Test Classes
                            │
                            ▼
                        BaseTest
                            │
                            ▼
                       ObjectRepo
                            │
                 ┌──────────┴──────────┐
                 ▼                     ▼
             HomePage             PerfumePage
                 │                     │
                 └──────────┬──────────┘
                            ▼
                      DriverManager
                            │
                            ▼
                     Selenium WebDriver
                            │
              ┌─────────────┼─────────────┐
              ▼             ▼             ▼
           Chrome        Firefox          Edge
```

### Core design principles

- Separation of test, framework and page-object responsibilities
- Thread-local WebDriver management
- Configuration-driven browser execution
- Reusable page components
- Explicit synchronization
- Centralized reporting and logging

---

## 📁 Project Structure

```text
selenium-dd-framework
│
├── config.properties
├── pom.xml
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.demo.qa
│   │   │       ├── core
│   │   │       ├── pageobjects
│   │   │       └── utilities
│   │   │
│   │   └── resources
│   │       └── log4j2.properties
│   │
│   └── test
│       ├── java
│       │   └── com.demo.qa
│       │       ├── core
│       │       └── tests
│       │
│       └── resources
│           └── test-data
│
└── reports
```

---

## ⚙️ Configuration

Framework execution is controlled through `config.properties`.

```properties
baseUrl = https://www.douglas.de/de
browser = chrome
headless = false
acceptInsecureCerts = false
```

### Supported browsers

- Chrome
- Firefox
- Edge
- Safari

### Execution options

| Configuration | Description |
|---|---|
| `browser` | Browser used for execution |
| `headless` | Enables headless browser execution |
| `acceptInsecureCerts` | Allows insecure SSL certificates |

---

## 🧪 Data-Driven Testing

Test data is maintained separately from test logic.

Example:

```json
{
  "Criteria": [
    {
      "Highlights": "Sale",
      "Für Wen": "Unisex",
      "Marke": "4711 Acqua Colonia",
      "Produktart": "Eau de Cologne"
    }
  ]
}
```

The TestNG `DataProvider` converts the JSON data into test parameters, allowing the same test flow to execute against multiple datasets.

This keeps **test logic** and **test data** separated.

---

## 🌐 Cross-Browser Execution

The framework supports:

```text
Chrome
Firefox
Edge
Safari
```

Browser-specific Selenium options are isolated inside `DriverManager`, while browser behavior is controlled through configuration.

This prevents browser-specific implementation details from leaking into test classes.

---

## 🧵 Parallel Execution

`ThreadLocal<WebDriver>` is used to maintain an independent browser session for each execution thread.

```text
Thread 1 → WebDriver 1
Thread 2 → WebDriver 2
Thread 3 → WebDriver 3
```

This allows the framework to support parallel TestNG execution without sharing browser state between tests.

---

## 📊 Reporting & Logging

The framework integrates:

- ExtentReports for execution reporting
- Log4j 2 for structured framework logging

Reports and logs are generated outside the test implementation, keeping reporting concerns separated from business test logic.

---

## 🔧 Framework Modernization

This project is being modernized from an older automation implementation toward a current Java/Selenium ecosystem.

Modernization includes:

- Java 17
- Selenium 4
- TestNG 7
- Jackson for JSON processing
- ExtentReports 5
- Log4j 2
- Selenium Manager
- Centralized browser configuration
- Thread-safe driver management
- Removal of obsolete dependencies

The modernization focuses on improving maintainability, reliability and long-term framework support rather than simply upgrading dependency versions.

---

## 💡 Engineering Decisions

### Why ThreadLocal WebDriver?

To provide isolated WebDriver instances when tests execute in parallel.

### Why centralized DriverManager?

To keep browser creation and lifecycle management separate from test classes.

### Why configuration-driven execution?

To allow execution behavior to change without modifying test implementation.

### Why separate test data?

To allow the same automation flow to execute against multiple datasets while keeping test logic reusable.

---

## ▶️ Running the Tests

Compile the project:

```bash
mvn clean compile
```

Compile test sources without executing tests:

```bash
mvn clean test -DskipTests
```

Execute the test suite:

```bash
mvn clean test
```

---

## 📌 Current Scope

The current framework demonstrates:

- Selenium UI automation
- Page Object Model
- Data-driven testing
- TestNG execution
- Cross-browser support
- Parallel execution architecture
- Configuration management
- Logging
- Reporting

---

## 🚧 Roadmap

Planned improvements include:

- GitHub Actions CI pipeline
- Docker-based execution
- Selenium Grid / remote execution
- Enhanced test reporting artifacts
- API + UI workflow integration
- Additional automated scenarios

---

## 👨‍💻 Author

**Pravin Ranjane**  
Senior SDET | Test Automation Engineer

Focused on building scalable test automation solutions across UI, API and mobile platforms.

[LinkedIn](https://www.linkedin.com/in/pravin-ranjane)  
[GitHub](https://github.com/RPravin86)
