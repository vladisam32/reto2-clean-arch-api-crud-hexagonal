package org.litethinking.supermercado.cli.service.report;

/**
 * Interface for generating reports.
 * Following the Single Responsibility Principle, this interface is responsible only for report generation.
 */
public interface ReportGenerator {
    
    /**
     * Generate and display a report.
     */
    void generateReport();
}