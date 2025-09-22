package org.litethinking.reto1cleanarchapicrud.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.litethinking.reto1cleanarchapicrud.application.service.InventoryService;
import org.litethinking.reto1cleanarchapicrud.application.service.ProductService;
import org.litethinking.reto1cleanarchapicrud.application.service.SaleService;
import org.litethinking.reto1cleanarchapicrud.domain.model.Inventory;
import org.litethinking.reto1cleanarchapicrud.domain.model.Product;
import org.litethinking.reto1cleanarchapicrud.domain.model.Sale;
import org.litethinking.reto1cleanarchapicrud.domain.model.SaleItem;
import org.litethinking.reto1cleanarchapicrud.domain.repository.SaleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementación del servicio de ventas, aquí tá la lógica de negocio
 * Esto maneja to' lo que tiene que ver con las ventas, tú sabe!
 */
@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final ProductService productService;
    private final InventoryService inventoryService;

    @Override
    @Transactional
    public Sale createSale(Sale sale) {
        // Validaciones básicas
        if (sale.getItems() == null || sale.getItems().isEmpty()) {
            throw new IllegalArgumentException("Una venta debe tener al menos un producto");
        }
        
        // Establecemos la fecha de venta si no viene
        if (sale.getSaleDate() == null) {
            sale.setSaleDate(LocalDateTime.now());
        }
        
        // Generamos un número de recibo único si no viene
        if (sale.getReceiptNumber() == null || sale.getReceiptNumber().trim().isEmpty()) {
            sale.setReceiptNumber(generateReceiptNumber());
        } else {
            // Verificamos que no exista ya una venta con ese número de recibo
            Optional<Sale> existingSale = saleRepository.findByReceiptNumber(sale.getReceiptNumber());
            if (existingSale.isPresent()) {
                throw new IllegalArgumentException("Ya existe una venta con el número de recibo: " + sale.getReceiptNumber());
            }
        }
        
        // Validamos y actualizamos cada item de la venta
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (SaleItem item : sale.getItems()) {
            // Verificamos que el producto exista
            Product product = validateAndGetProduct(item);
            item.setProduct(product);
            
            // Verificamos que haya suficiente inventario
            validateAndUpdateInventory(product, item.getQuantity());
            
            // Calculamos el subtotal del item
            if (item.getUnitPrice() == null) {
                item.setUnitPrice(product.getPrice());
            }
            
            BigDecimal subtotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            if (item.getDiscount() != null) {
                subtotal = subtotal.subtract(item.getDiscount());
            }
            item.setSubtotal(subtotal);
            
            // Sumamos al total de la venta
            totalAmount = totalAmount.add(subtotal);
        }
        
        // Establecemos el total de la venta
        sale.setTotalAmount(totalAmount);
        
        return saleRepository.save(sale);
    }

    @Override
    @Transactional
    public Sale updateSale(Long id, Sale sale) {
        // Verificamos que la venta exista
        Optional<Sale> existingSale = saleRepository.findById(id);
        if (!existingSale.isPresent()) {
            throw new RuntimeException("Venta no encontrada con ID: " + id);
        }
        
        // Validaciones básicas
        if (sale.getItems() == null || sale.getItems().isEmpty()) {
            throw new IllegalArgumentException("Una venta debe tener al menos un producto");
        }
        
        // Si cambia el número de recibo, verificamos que no exista ya una venta con ese número
        if (sale.getReceiptNumber() != null && !sale.getReceiptNumber().equals(existingSale.get().getReceiptNumber())) {
            Optional<Sale> saleWithReceiptNumber = saleRepository.findByReceiptNumber(sale.getReceiptNumber());
            if (saleWithReceiptNumber.isPresent() && !saleWithReceiptNumber.get().getId().equals(id)) {
                throw new IllegalArgumentException("Ya existe una venta con el número de recibo: " + sale.getReceiptNumber());
            }
        }
        
        // Aseguramos que el ID sea el correcto
        sale.setId(id);
        
        // Recalculamos el total de la venta
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (SaleItem item : sale.getItems()) {
            // Verificamos que el producto exista
            Product product = validateAndGetProduct(item);
            item.setProduct(product);
            
            // Calculamos el subtotal del item
            if (item.getUnitPrice() == null) {
                item.setUnitPrice(product.getPrice());
            }
            
            BigDecimal subtotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            if (item.getDiscount() != null) {
                subtotal = subtotal.subtract(item.getDiscount());
            }
            item.setSubtotal(subtotal);
            
            // Sumamos al total de la venta
            totalAmount = totalAmount.add(subtotal);
        }
        
        // Establecemos el total de la venta
        sale.setTotalAmount(totalAmount);
        
        return saleRepository.save(sale);
    }

    @Override
    public Optional<Sale> getSaleById(Long id) {
        return saleRepository.findById(id);
    }

    @Override
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    @Override
    public void deleteSale(Long id) {
        // Verificamos que la venta exista
        if (!saleRepository.findById(id).isPresent()) {
            throw new RuntimeException("Venta no encontrada con ID: " + id);
        }
        
        saleRepository.deleteById(id);
    }

    @Override
    public List<Sale> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleRepository.findByDateRange(startDate, endDate);
    }

    @Override
    public List<Sale> getSalesByCustomerName(String customerName) {
        return saleRepository.findByCustomerName(customerName);
    }

    @Override
    public List<Sale> getSalesByCashierName(String cashierName) {
        return saleRepository.findByCashierName(cashierName);
    }

    @Override
    public Optional<Sale> getSaleByReceiptNumber(String receiptNumber) {
        return saleRepository.findByReceiptNumber(receiptNumber);
    }
    
    /**
     * Genera un número de recibo único
     * @return el número de recibo generado
     */
    private String generateReceiptNumber() {
        return "REC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Valida que el producto exista y lo devuelve
     * @param item el item de venta
     * @return el producto validado
     */
    private Product validateAndGetProduct(SaleItem item) {
        if (item.getProduct() == null || (item.getProduct().getId() == null && item.getProduct().getBarcode() == null)) {
            throw new IllegalArgumentException("El producto es obligatorio para cada item de la venta");
        }
        
        Product product;
        if (item.getProduct().getId() != null) {
            Optional<Product> productOpt = productService.getProductById(item.getProduct().getId());
            if (!productOpt.isPresent()) {
                throw new IllegalArgumentException("Producto no encontrado con ID: " + item.getProduct().getId());
            }
            product = productOpt.get();
        } else {
            Optional<Product> productOpt = productService.getProductByBarcode(item.getProduct().getBarcode());
            if (!productOpt.isPresent()) {
                throw new IllegalArgumentException("Producto no encontrado con código de barras: " + item.getProduct().getBarcode());
            }
            product = productOpt.get();
        }
        
        return product;
    }
    
    /**
     * Valida que haya suficiente inventario y lo actualiza
     * @param product el producto
     * @param quantity la cantidad a vender
     */
    private void validateAndUpdateInventory(Product product, Integer quantity) {
        Optional<Inventory> inventoryOpt = inventoryService.getInventoryByProduct(product);
        if (!inventoryOpt.isPresent()) {
            throw new IllegalArgumentException("No hay inventario para el producto: " + product.getName());
        }
        
        Inventory inventory = inventoryOpt.get();
        if (inventory.getQuantity() < quantity) {
            throw new IllegalArgumentException("No hay suficiente inventario para el producto: " + product.getName() + 
                    ". Disponible: " + inventory.getQuantity() + ", Solicitado: " + quantity);
        }
        
        // Actualizamos el inventario
        inventoryService.updateInventoryQuantity(inventory.getId(), inventory.getQuantity() - quantity);
    }
}