package org.litethinking.reto1cleanarchapicrud.application.service;

import org.litethinking.reto1cleanarchapicrud.domain.model.Sale;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing sales.
 */
public interface SaleService {
    
    /**
     * Create a new sale.
     *
     * @param sale the sale to create
     * @return the created sale
     */
    Sale createSale(Sale sale);
    
    /**
     * Update an existing sale.
     *
     * @param id the id of the sale to update
     * @param sale the updated sale data
     * @return the updated sale
     * @throws RuntimeException if the sale is not found
     */
    Sale updateSale(Long id, Sale sale);
    
    /**
     * Get a sale by its id.
     *
     * @param id the id of the sale
     * @return the sale if found, empty otherwise
     */
    Optional<Sale> getSaleById(Long id);
    
    /**
     * Get all sales.
     *
     * @return the list of all sales
     */
    List<Sale> getAllSales();
    
    /**
     * Delete a sale by its id.
     *
     * @param id the id of the sale to delete
     * @throws RuntimeException if the sale is not found
     */
    void deleteSale(Long id);
    
    /**
     * Get sales by date range.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return the list of sales in the date range
     */
    List<Sale> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Get sales by customer name.
     *
     * @param customerName the customer name to search for
     * @return the list of sales for the customer
     */
    List<Sale> getSalesByCustomerName(String customerName);
    
    /**
     * Get sales by cashier name.
     *
     * @param cashierName the cashier name to search for
     * @return the list of sales by the cashier
     */
    List<Sale> getSalesByCashierName(String cashierName);
    
    /**
     * Get a sale by receipt number.
     *
     * @param receiptNumber the receipt number to search for
     * @return the sale if found, empty otherwise
     */
    Optional<Sale> getSaleByReceiptNumber(String receiptNumber);
}