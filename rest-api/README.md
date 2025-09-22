# REST API Module - Logging Implementation

## Overview
This module includes a Log4j2 implementation for traceability of information and errors. Logs are printed to both the console and log files.

## Configuration

### Dependencies
The following dependencies have been added to the `pom.xml`:
```xml
<!-- Log4j2 dependencies -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
</dependency>
```

### Log4j2 Configuration
The logging configuration is defined in `src/main/resources/log4j2.xml`. Key features:

- Console logging for all levels
- File logging for all levels in `logs/application.log`
- Separate error logging in `logs/error.log`
- Rolling file strategy based on size (10MB) and time (daily)
- Debug level logging for `org.litethinking` packages
- Info level logging for other packages

### Application Properties
The `application.properties` file includes:
```properties
# Logging configuration
logging.level.org.springframework=INFO
logging.level.org.litethinking=DEBUG
logging.config=classpath:log4j2.xml

# Log file location
app.log.dir=logs
```

## Usage

### Basic Logging
To use logging in your classes:

```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class YourClass {
    private static final Logger logger = LogManager.getLogger(YourClass.class);
    
    public void yourMethod() {
        logger.debug("Debug message");
        logger.info("Info message");
        logger.warn("Warning message");
        logger.error("Error message", exception);
    }
}
```

### Best Practices

1. **Use parameterized logging** to avoid string concatenation:
   ```java
   // Good
   logger.info("Processing user: {}", username);
   
   // Avoid
   logger.info("Processing user: " + username);
   ```

2. **Include exceptions in error logs**:
   ```java
   try {
       // code that might throw an exception
   } catch (Exception e) {
       logger.error("Error occurred while processing", e);
   }
   ```

3. **Use appropriate log levels**:
   - ERROR: For errors that need immediate attention
   - WARN: For potential issues that don't prevent the application from working
   - INFO: For important application events
   - DEBUG: For detailed information useful during development

4. **Add traceability with operation IDs** for complex operations:
   ```java
   String operationId = generateUniqueId();
   logger.info("Starting operation: {}", operationId);
   // ... operation steps ...
   logger.info("Operation {} completed", operationId);
   ```

## Example
See `LoggingDemoController.java` for examples of how to implement logging in controllers.