package org.litethinking.supermercado.cli.service;

import org.litethinking.supermercado.shareddto.supermercado.CajeroDto;

import java.util.Scanner;

/**
 * Service interface for CLI operations.
 */
public interface CliService {

    /**
     * Display and handle the cashier login menu.
     * 
     * @param scanner the scanner for user input
     * @return the logged-in cashier, or null if login failed
     */
    CajeroDto cajeroLogin(Scanner scanner);

    /**
     * Display and handle the product management menu.
     *
     * @param scanner the scanner for user input
     */
    void productMenu(Scanner scanner);

    /**
     * Display and handle the inventory management menu.
     *
     * @param scanner the scanner for user input
     */
    void inventoryMenu(Scanner scanner);

    /**
     * Display and handle the sales management menu.
     *
     * @param scanner the scanner for user input
     * @param cajero the logged-in cashier
     */
    void saleMenu(Scanner scanner, CajeroDto cajero);

    /**
     * Generate and display a report.
     */
    void generateReport();
}
