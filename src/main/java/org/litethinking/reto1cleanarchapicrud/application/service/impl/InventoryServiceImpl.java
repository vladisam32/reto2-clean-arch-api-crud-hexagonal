package org.litethinking.reto1cleanarchapicrud.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.litethinking.reto1cleanarchapicrud.application.service.InventoryService;
import org.litethinking.reto1cleanarchapicrud.domain.model.Inventory;
import org.litethinking.reto1cleanarchapicrud.domain.model.Product;
import org.litethinking.reto1cleanarchapicrud.domain.repository.InventoryRepository;
import org.litethinking.reto1cleanarchapicrud.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de inventario, aquí tá la lógica de negocio
 * Esto maneja to' lo que tiene que ver con el inventario, tú sabe!
 */
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Override
    public Inventory createInventory(Inventory inventory) {
        // Validaciones básicas
        if (inventory.getProduct() == null || inventory.getProduct().getId() == null) {
            throw new IllegalArgumentException("El producto es obligatorio para crear un inventario");
        }
        
        // Verificamos que el producto exista
        Optional<Product> product = productRepository.findById(inventory.getProduct().getId());
        if (!product.isPresent()) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + inventory.getProduct().getId());
        }
        
        // Verificamos que no exista ya un inventario para este producto
        Optional<Inventory> existingInventory = inventoryRepository.findByProduct(inventory.getProduct());
        if (existingInventory.isPresent()) {
            throw new IllegalArgumentException("Ya existe un inventario para el producto con ID: " + inventory.getProduct().getId());
        }
        
        // Validamos la cantidad
        if (inventory.getQuantity() == null || inventory.getQuantity() < 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor o igual a cero");
        }
        
        // Establecemos la fecha de reposición si no viene
        if (inventory.getLastRestockDate() == null) {
            inventory.setLastRestockDate(LocalDateTime.now());
        }
        
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory updateInventory(Long id, Inventory inventory) {
        // Verificamos que el inventario exista
        Optional<Inventory> existingInventory = inventoryRepository.findById(id);
        if (!existingInventory.isPresent()) {
            throw new RuntimeException("Inventario no encontrado con ID: " + id);
        }
        
        // Validaciones básicas
        if (inventory.getProduct() == null || inventory.getProduct().getId() == null) {
            throw new IllegalArgumentException("El producto es obligatorio para actualizar un inventario");
        }
        
        // Verificamos que el producto exista
        Optional<Product> product = productRepository.findById(inventory.getProduct().getId());
        if (!product.isPresent()) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + inventory.getProduct().getId());
        }
        
        // Si cambia el producto, verificamos que no exista ya un inventario para el nuevo producto
        if (!inventory.getProduct().getId().equals(existingInventory.get().getProduct().getId())) {
            Optional<Inventory> inventoryWithProduct = inventoryRepository.findByProduct(inventory.getProduct());
            if (inventoryWithProduct.isPresent() && !inventoryWithProduct.get().getId().equals(id)) {
                throw new IllegalArgumentException("Ya existe un inventario para el producto con ID: " + inventory.getProduct().getId());
            }
        }
        
        // Validamos la cantidad
        if (inventory.getQuantity() == null || inventory.getQuantity() < 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor o igual a cero");
        }
        
        // Aseguramos que el ID sea el correcto
        inventory.setId(id);
        
        return inventoryRepository.save(inventory);
    }

    @Override
    public Optional<Inventory> getInventoryById(Long id) {
        return inventoryRepository.findById(id);
    }

    @Override
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @Override
    public void deleteInventory(Long id) {
        // Verificamos que el inventario exista
        if (!inventoryRepository.findById(id).isPresent()) {
            throw new RuntimeException("Inventario no encontrado con ID: " + id);
        }
        
        inventoryRepository.deleteById(id);
    }

    @Override
    public Optional<Inventory> getInventoryByProduct(Product product) {
        return inventoryRepository.findByProduct(product);
    }

    @Override
    public List<Inventory> getLowStockInventory() {
        return inventoryRepository.findLowStock();
    }

    @Override
    public List<Inventory> getInventoryByLocation(String location) {
        return inventoryRepository.findByLocation(location);
    }

    @Override
    public Inventory updateInventoryQuantity(Long id, Integer quantity) {
        // Verificamos que el inventario exista
        Optional<Inventory> existingInventory = inventoryRepository.findById(id);
        if (!existingInventory.isPresent()) {
            throw new RuntimeException("Inventario no encontrado con ID: " + id);
        }
        
        // Validamos la cantidad
        if (quantity == null || quantity < 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor o igual a cero");
        }
        
        // Actualizamos solo la cantidad y la fecha de reposición
        Inventory inventory = existingInventory.get();
        inventory.setQuantity(quantity);
        inventory.setLastRestockDate(LocalDateTime.now());
        
        return inventoryRepository.save(inventory);
    }
}