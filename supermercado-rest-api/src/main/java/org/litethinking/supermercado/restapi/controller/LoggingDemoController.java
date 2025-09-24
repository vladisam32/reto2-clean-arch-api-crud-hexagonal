package org.litethinking.supermercado.restapi.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to demonstrate logging functionality.
 */
@RestController
@RequestMapping("/api/logging")
public class LoggingDemoController {

    private static final Logger logger = LogManager.getLogger(LoggingDemoController.class);

    /**
     * Endpoint to demonstrate different log levels.
     * 
     * @param message Optional message to log
     * @param error Flag to simulate an error
     * @return Response with status message
     */
    @GetMapping("/test")
    public ResponseEntity<String> testLogging(
            @RequestParam(required = false, defaultValue = "Test message") String message,
            @RequestParam(required = false, defaultValue = "false") boolean error) {
        
        logger.debug("Debug log: Received request with message: {}", message);
        logger.info("Info log: Processing logging test request");
        
        if (error) {
            try {
                // Simulate an error
                throw new RuntimeException("Simulated error for logging demonstration");
            } catch (Exception e) {
                logger.error("Error log: An error occurred during request processing", e);
                return ResponseEntity.internalServerError().body("Error logged. Check logs for details.");
            }
        }
        
        logger.info("Info log: Request processed successfully");
        return ResponseEntity.ok("Logging test successful. Message: " + message);
    }
    
    /**
     * Endpoint to demonstrate traceability with logging.
     * 
     * @return Response with status message
     */
    @GetMapping("/trace")
    public ResponseEntity<String> traceOperation() {
        String operationId = generateOperationId();
        logger.info("Starting operation: {}", operationId);
        
        try {
            // Step 1
            logger.debug("Step 1: Initializing operation {}", operationId);
            // Simulate processing
            Thread.sleep(100);
            
            // Step 2
            logger.debug("Step 2: Processing data for operation {}", operationId);
            // Simulate processing
            Thread.sleep(100);
            
            // Step 3
            logger.debug("Step 3: Finalizing operation {}", operationId);
            // Simulate processing
            Thread.sleep(100);
            
            logger.info("Operation {} completed successfully", operationId);
            return ResponseEntity.ok("Operation completed. Operation ID: " + operationId);
        } catch (Exception e) {
            logger.error("Error in operation {}: {}", operationId, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Operation failed. Check logs for details.");
        }
    }
    
    private String generateOperationId() {
        return "OP-" + System.currentTimeMillis();
    }
}