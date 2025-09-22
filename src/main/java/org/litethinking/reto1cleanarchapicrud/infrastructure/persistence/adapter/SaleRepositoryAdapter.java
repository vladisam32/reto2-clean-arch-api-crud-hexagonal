package org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.litethinking.reto1cleanarchapicrud.domain.model.Product;
import org.litethinking.reto1cleanarchapicrud.domain.model.Sale;
import org.litethinking.reto1cleanarchapicrud.domain.model.SaleItem;
import org.litethinking.reto1cleanarchapicrud.domain.repository.SaleRepository;
import org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.entity.ProductJpaEntity;
import org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.entity.SaleItemJpaEntity;
import org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.entity.SaleJpaEntity;
import org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.repository.ProductJpaRepository;
import org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.repository.SaleJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador pa' conectar el dominio con la base de datos de ventas
 * Esto hace que el sistema no dependa directamente de la base de datos, tú sabe!
 */
@Component
@RequiredArgsConstructor
public class SaleRepositoryAdapter implements SaleRepository {

    private final SaleJpaRepository saleJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final ProductRepositoryAdapter productRepositoryAdapter;

    @Override
    public Sale save(Sale sale) {
        SaleJpaEntity entity = mapToEntity(sale);
        SaleJpaEntity savedEntity = saleJpaRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    @Override
    public Optional<Sale> findById(Long id) {
        return saleJpaRepository.findById(id)
                .map(this::mapToDomain);
    }

    @Override
    public List<Sale> findAll() {
        return saleJpaRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        saleJpaRepository.deleteById(id);
    }

    @Override
    public List<Sale> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleJpaRepository.findBySaleDateBetween(startDate, endDate).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Sale> findByCustomerName(String customerName) {
        return saleJpaRepository.findByCustomerNameContainingIgnoreCase(customerName).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Sale> findByCashierName(String cashierName) {
        return saleJpaRepository.findByCashierNameContainingIgnoreCase(cashierName).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Sale> findByReceiptNumber(String receiptNumber) {
        return saleJpaRepository.findByReceiptNumber(receiptNumber)
                .map(this::mapToDomain);
    }

    /**
     * Convierte una venta del dominio a una entidad JPA
     * @param sale la venta del dominio
     * @return la entidad JPA
     */
    private SaleJpaEntity mapToEntity(Sale sale) {
        SaleJpaEntity entity = SaleJpaEntity.builder()
                .id(sale.getId())
                .saleDate(sale.getSaleDate())
                .customerName(sale.getCustomerName())
                .totalAmount(sale.getTotalAmount())
                .paymentMethod(sale.getPaymentMethod())
                .cashierName(sale.getCashierName())
                .receiptNumber(sale.getReceiptNumber())
                .build();
        
        // Mapeamos los items de la venta
        if (sale.getItems() != null) {
            for (SaleItem item : sale.getItems()) {
                SaleItemJpaEntity itemEntity = mapSaleItemToEntity(item);
                entity.addItem(itemEntity);
            }
        }
        
        return entity;
    }

    /**
     * Convierte un item de venta del dominio a una entidad JPA
     * @param item el item de venta del dominio
     * @return la entidad JPA
     */
    private SaleItemJpaEntity mapSaleItemToEntity(SaleItem item) {
        // Buscamos el producto en la base de datos
        ProductJpaEntity productEntity = null;
        if (item.getProduct().getId() != null) {
            productEntity = productJpaRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + item.getProduct().getId()));
        } else {
            // Si el producto no tiene ID, lo buscamos por código de barras
            Optional<ProductJpaEntity> productOpt = productJpaRepository.findByBarcode(item.getProduct().getBarcode());
            if (productOpt.isPresent()) {
                productEntity = productOpt.get();
            } else {
                throw new RuntimeException("Producto no encontrado con código de barras: " + item.getProduct().getBarcode());
            }
        }
        
        return SaleItemJpaEntity.builder()
                .id(item.getId())
                .product(productEntity)
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .subtotal(item.getSubtotal())
                .discount(item.getDiscount())
                .build();
    }

    /**
     * Convierte una entidad JPA a una venta del dominio
     * @param entity la entidad JPA
     * @return la venta del dominio
     */
    private Sale mapToDomain(SaleJpaEntity entity) {
        List<SaleItem> items = new ArrayList<>();
        
        // Mapeamos los items de la venta
        if (entity.getItems() != null) {
            items = entity.getItems().stream()
                    .map(this::mapSaleItemToDomain)
                    .collect(Collectors.toList());
        }
        
        return Sale.builder()
                .id(entity.getId())
                .saleDate(entity.getSaleDate())
                .items(items)
                .totalAmount(entity.getTotalAmount())
                .paymentMethod(entity.getPaymentMethod())
                .customerName(entity.getCustomerName())
                .cashierName(entity.getCashierName())
                .receiptNumber(entity.getReceiptNumber())
                .build();
    }

    /**
     * Convierte una entidad JPA a un item de venta del dominio
     * @param entity la entidad JPA
     * @return el item de venta del dominio
     */
    private SaleItem mapSaleItemToDomain(SaleItemJpaEntity entity) {
        // Convertimos la entidad ProductJpaEntity a un objeto Product del dominio
        Product product = productRepositoryAdapter.findById(entity.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Error al mapear el producto del item de venta"));
        
        return SaleItem.builder()
                .id(entity.getId())
                .product(product)
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .subtotal(entity.getSubtotal())
                .discount(entity.getDiscount())
                .build();
    }
}