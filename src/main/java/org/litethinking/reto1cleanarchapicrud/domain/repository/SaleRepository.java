package org.litethinking.reto1cleanarchapicrud.domain.repository;

import org.litethinking.reto1cleanarchapicrud.domain.model.Sale;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Sale entity.
 */
public interface SaleRepository {
    
    /**
     * Save a sale.
     *
     * @param sale the sale to save
     * @return the saved sale
     */
    Sale save(Sale sale);
    
    /**
     * Find a sale by its id.
     *
     * @param id the id of the sale
     * @return the sale if found, empty otherwise
     */
    Optional<Sale> findById(Long id);
    
    /**
     * Find all sales.
     *
     * @return the list of sales
     */
    List<Sale> findAll();
    
    /**
     * Delete a sale by its id.
     *
     * @param id the id of the sale to delete
     */
    void deleteById(Long id);
    
    /**
     * Find sales by date range.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return the list of sales in the date range
     */
    List<Sale> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find sales by customer name.
     *
     * @param customerName the customer name to search for
     * @return the list of sales for the customer
     */
    List<Sale> findByCustomerName(String customerName);
    
    /**
     * Find sales by cashier name.
     *
     * @param cashierName the cashier name to search for
     * @return the list of sales by the cashier
     */
    List<Sale> findByCashierName(String cashierName);
    
    /**
     * Find a sale by receipt number.
     *
     * @param receiptNumber the receipt number to search for
     * @return the sale if found, empty otherwise
     */
    Optional<Sale> findByReceiptNumber(String receiptNumber);
}