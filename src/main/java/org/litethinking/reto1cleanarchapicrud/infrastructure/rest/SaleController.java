package org.litethinking.reto1cleanarchapicrud.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.litethinking.reto1cleanarchapicrud.application.service.SaleService;
import org.litethinking.reto1cleanarchapicrud.domain.model.Sale;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST pa' las ventas, aquí exponemo' los endpoints pa' que la gente los use
 * Con esto se puede crear, buscar, actualizar y borrar ventas, tú sabe!
 */
@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    /**
     * Crea una venta nueva
     * @param sale la venta que vamo' a crear
     * @return la venta creada con su ID
     */
    @PostMapping
    public ResponseEntity<?> createSale(@RequestBody Sale sale) {
        try {
            Sale createdSale = saleService.createSale(sale);
            return new ResponseEntity<>(createdSale, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Busca una venta por su ID
     * @param id el ID de la venta que tamo' buscando
     * @return la venta si la encontramo', si no, error 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        return saleService.getSaleById(id)
                .map(sale -> new ResponseEntity<>(sale, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Busca to' las ventas
     * @return la lista de ventas
     */
    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales() {
        List<Sale> sales = saleService.getAllSales();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    /**
     * Busca ventas por rango de fechas
     * @param startDate desde cuándo queremo' ver
     * @param endDate hasta cuándo queremo' ver
     * @return la lista de ventas en ese período
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Sale>> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Sale> sales = saleService.getSalesByDateRange(startDate, endDate);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    /**
     * Busca ventas por nombre del cliente
     * @param customerName el nombre del cliente que tamo' buscando
     * @return la lista de ventas de ese cliente
     */
    @GetMapping("/customer/{customerName}")
    public ResponseEntity<List<Sale>> getSalesByCustomerName(@PathVariable String customerName) {
        List<Sale> sales = saleService.getSalesByCustomerName(customerName);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    /**
     * Busca ventas por nombre del cajero
     * @param cashierName el nombre del cajero que tamo' buscando
     * @return la lista de ventas de ese cajero
     */
    @GetMapping("/cashier/{cashierName}")
    public ResponseEntity<List<Sale>> getSalesByCashierName(@PathVariable String cashierName) {
        List<Sale> sales = saleService.getSalesByCashierName(cashierName);
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    /**
     * Busca una venta por número de recibo
     * @param receiptNumber el número de recibo que tamo' buscando
     * @return la venta si la encontramo', si no, error 404
     */
    @GetMapping("/receipt/{receiptNumber}")
    public ResponseEntity<Sale> getSaleByReceiptNumber(@PathVariable String receiptNumber) {
        return saleService.getSaleByReceiptNumber(receiptNumber)
                .map(sale -> new ResponseEntity<>(sale, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Actualiza una venta existente
     * @param id el ID de la venta que vamo' a actualizar
     * @param sale los nuevos datos de la venta
     * @return la venta actualizada, o error 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateSale(@PathVariable Long id, @RequestBody Sale sale) {
        try {
            if (!saleService.getSaleById(id).isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            sale.setId(id);
            Sale updatedSale = saleService.updateSale(id, sale);
            return new ResponseEntity<>(updatedSale, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Borra una venta
     * @param id el ID de la venta que vamo' a borrar
     * @return OK si se borró, o error 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        try {
            saleService.deleteSale(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}