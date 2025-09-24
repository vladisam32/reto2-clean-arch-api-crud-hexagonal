package org.litethinking.supermercado.cli.service.report.impl;

import org.litethinking.supermercado.cli.service.report.ReportGenerator;
import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;
import org.litethinking.supermercado.shareddto.supermercado.inventario.InventarioDto;
import org.litethinking.supermercado.shareddto.supermercado.venta.VentaDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the ReportGenerator interface that generates inventory reports.
 * This class follows the Single Responsibility Principle by focusing solely on report generation.
 * It also follows the Open/Closed Principle as it can be extended without modification.
 * It follows the Liskov Substitution Principle as it can be substituted for any ReportGenerator.
 * It follows the Interface Segregation Principle as it implements only the methods it needs.
 * It follows the Dependency Inversion Principle as it depends on abstractions (ReportGenerator) not concretions.
 */
@Service
public class InventoryReportGenerator implements ReportGenerator {

    private final RestTemplate restTemplate;
    private final String apiBaseUrl;

    public InventoryReportGenerator(RestTemplate restTemplate, @Value("${api.base-url}") String apiBaseUrl) {
        this.restTemplate = restTemplate;
        this.apiBaseUrl = apiBaseUrl;
    }

    @Override
    public void generateReport() {
        try {
            // Get all products
            ResponseEntity<List<ProductoDto>> productResponse = restTemplate.exchange(
                    apiBaseUrl + "/productos",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ProductoDto>>() {}
            );
            List<ProductoDto> productos = productResponse.getBody();

            // Get all inventory
            ResponseEntity<List<InventarioDto>> inventoryResponse = restTemplate.exchange(
                    apiBaseUrl + "/inventario",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<InventarioDto>>() {}
            );
            List<InventarioDto> inventarios = inventoryResponse.getBody();

            // Get all sales
            ResponseEntity<List<VentaDto>> saleResponse = restTemplate.exchange(
                    apiBaseUrl + "/ventas",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<VentaDto>>() {}
            );
            List<VentaDto> ventas = saleResponse.getBody();

            if (productos == null || inventarios == null || ventas == null) {
                System.out.println("Error: Could not retrieve data for report.");
                return;
            }

            // Generate inventory value report
            generateInventoryValueReport(productos, inventarios);

            // Generate low stock report
            generateLowStockReport(inventarios);

            // Generate sales by category report
            generateSalesByCategoryReport(ventas, productos);

            // Generate sales trend report
            generateSalesTrendReport(ventas);

        } catch (Exception e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }

    /**
     * Generate a report showing the total value of inventory by category.
     */
    private void generateInventoryValueReport(List<ProductoDto> productos, List<InventarioDto> inventarios) {
        System.out.println("\n===== INVENTORY VALUE REPORT =====");

        Map<String, BigDecimal> categoryValues = new HashMap<>();
        BigDecimal totalValue = BigDecimal.ZERO;

        for (InventarioDto inventario : inventarios) {
            ProductoDto producto = inventario.producto();
            if (producto != null) {
                // Find the full product details
                ProductoDto fullProducto = productos.stream()
                        .filter(p -> p.id().equals(producto.id()))
                        .findFirst()
                        .orElse(null);

                if (fullProducto != null) {
                    BigDecimal itemValue = fullProducto.precio().multiply(new BigDecimal(inventario.cantidad()));
                    totalValue = totalValue.add(itemValue);

                    String categoria = fullProducto.categoria();
                    if (categoria != null) {
                        BigDecimal categoryValue = categoryValues.getOrDefault(categoria, BigDecimal.ZERO);
                        categoryValues.put(categoria, categoryValue.add(itemValue));
                    }
                }
            }
        }

        // Print category values
        System.out.println("Inventory Value by Category:");
        for (Map.Entry<String, BigDecimal> entry : categoryValues.entrySet()) {
            System.out.printf("  %s: $%.2f%n", entry.getKey(), entry.getValue());
        }

        // Print total value
        System.out.printf("Total Inventory Value: $%.2f%n", totalValue);
    }

    /**
     * Generate a report showing items with low stock.
     */
    private void generateLowStockReport(List<InventarioDto> inventarios) {
        System.out.println("\n===== LOW STOCK REPORT =====");

        List<InventarioDto> lowStockItems = inventarios.stream()
                .filter(inventario -> inventario.cantidad() <= inventario.stockMinimo())
                .collect(Collectors.toList());

        if (lowStockItems.isEmpty()) {
            System.out.println("No items with low stock.");
            return;
        }

        System.out.println("Items with Low Stock:");
        for (InventarioDto inventario : lowStockItems) {
            System.out.printf("  Product ID: %d, Quantity: %d, Minimum Stock: %d%n",
                    inventario.producto().id(),
                    inventario.cantidad(),
                    inventario.stockMinimo());
        }
    }

    /**
     * Generate a report showing sales by product category.
     */
    private void generateSalesByCategoryReport(List<VentaDto> ventas, List<ProductoDto> productos) {
        System.out.println("\n===== SALES BY CATEGORY REPORT =====");

        Map<String, BigDecimal> categorySales = new HashMap<>();
        BigDecimal totalSales = BigDecimal.ZERO;

        for (VentaDto venta : ventas) {
            if (venta.items() != null) {
                venta.items().forEach(item -> {
                    ProductoDto producto = item.producto();
                    if (producto != null) {
                        // Find the full product details
                        ProductoDto fullProducto = productos.stream()
                                .filter(p -> p.id().equals(producto.id()))
                                .findFirst()
                                .orElse(null);

                        if (fullProducto != null) {
                            String categoria = fullProducto.categoria();
                            if (categoria != null) {
                                BigDecimal categorySale = categorySales.getOrDefault(categoria, BigDecimal.ZERO);
                                categorySales.put(categoria, categorySale.add(item.subtotal()));
                            }
                        }
                    }
                });
            }

            totalSales = totalSales.add(venta.montoTotal());
        }

        // Print category sales
        System.out.println("Sales by Category:");
        for (Map.Entry<String, BigDecimal> entry : categorySales.entrySet()) {
            System.out.printf("  %s: $%.2f%n", entry.getKey(), entry.getValue());
        }

        // Print total sales
        System.out.printf("Total Sales: $%.2f%n", totalSales);
    }

    /**
     * Generate a report showing sales trends over time.
     */
    private void generateSalesTrendReport(List<VentaDto> ventas) {
        System.out.println("\n===== SALES TREND REPORT =====");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<String, BigDecimal> dailySales = new HashMap<>();

        for (VentaDto venta : ventas) {
            LocalDateTime fechaVenta = venta.fechaVenta();
            if (fechaVenta != null) {
                String dateStr = fechaVenta.format(formatter);
                BigDecimal dailySale = dailySales.getOrDefault(dateStr, BigDecimal.ZERO);
                dailySales.put(dateStr, dailySale.add(venta.montoTotal()));
            }
        }

        // Print daily sales
        System.out.println("Daily Sales:");
        dailySales.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.printf("  %s: $%.2f%n", entry.getKey(), entry.getValue()));
    }
}
