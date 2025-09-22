package org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.litethinking.reto1cleanarchapicrud.domain.model.Inventory;
import org.litethinking.reto1cleanarchapicrud.domain.model.Product;
import org.litethinking.reto1cleanarchapicrud.domain.repository.InventoryRepository;
import org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.entity.InventoryJpaEntity;
import org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.entity.ProductJpaEntity;
import org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.repository.InventoryJpaRepository;
import org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.repository.ProductJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador pa' conectar el dominio con la base de datos de inventario
 * Esto hace que el sistema no dependa directamente de la base de datos, tú sabe!
 */
@Component
@RequiredArgsConstructor
public class InventoryRepositoryAdapter implements InventoryRepository {

    private final InventoryJpaRepository inventoryJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final ProductRepositoryAdapter productRepositoryAdapter;

    @Override
    public Inventory save(Inventory inventory) {
        InventoryJpaEntity entity = mapToEntity(inventory);
        InventoryJpaEntity savedEntity = inventoryJpaRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    @Override
    public Optional<Inventory> findById(Long id) {
        return inventoryJpaRepository.findById(id)
                .map(this::mapToDomain);
    }

    @Override
    public List<Inventory> findAll() {
        return inventoryJpaRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        inventoryJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Inventory> findByProduct(Product product) {
        // Primero necesitamos encontrar la entidad ProductJpaEntity correspondiente
        Optional<ProductJpaEntity> productEntity = productJpaRepository.findById(product.getId());
        
        if (productEntity.isPresent()) {
            return inventoryJpaRepository.findByProduct(productEntity.get())
                    .map(this::mapToDomain);
        }
        
        return Optional.empty();
    }

    @Override
    public List<Inventory> findLowStock() {
        return inventoryJpaRepository.findLowStock().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Inventory> findByLocation(String location) {
        return inventoryJpaRepository.findByLocation(location).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    /**
     * Convierte un inventario del dominio a una entidad JPA
     * @param inventory el inventario del dominio
     * @return la entidad JPA
     */
    private InventoryJpaEntity mapToEntity(Inventory inventory) {
        // Primero necesitamos encontrar o crear la entidad ProductJpaEntity correspondiente
        ProductJpaEntity productEntity;
        if (inventory.getProduct().getId() != null) {
            productEntity = productJpaRepository.findById(inventory.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + inventory.getProduct().getId()));
        } else {
            // Si el producto no tiene ID, lo guardamos primero
            Product savedProduct = productRepositoryAdapter.save(inventory.getProduct());
            productEntity = productJpaRepository.findById(savedProduct.getId())
                    .orElseThrow(() -> new RuntimeException("Error al guardar el producto"));
        }
        
        return InventoryJpaEntity.builder()
                .id(inventory.getId())
                .product(productEntity)
                .quantity(inventory.getQuantity())
                .minimumStock(inventory.getMinimumStock())
                .maximumStock(inventory.getMaximumStock())
                .lastRestockDate(inventory.getLastRestockDate())
                .location(inventory.getLocation())
                .build();
    }

    /**
     * Convierte una entidad JPA a un inventario del dominio
     * @param entity la entidad JPA
     * @return el inventario del dominio
     */
    private Inventory mapToDomain(InventoryJpaEntity entity) {
        // Convertimos la entidad ProductJpaEntity a un objeto Product del dominio
        Product product = productRepositoryAdapter.findById(entity.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Error al mapear el producto del inventario"));
        
        return Inventory.builder()
                .id(entity.getId())
                .product(product)
                .quantity(entity.getQuantity())
                .minimumStock(entity.getMinimumStock())
                .maximumStock(entity.getMaximumStock())
                .lastRestockDate(entity.getLastRestockDate())
                .location(entity.getLocation())
                .build();
    }
}