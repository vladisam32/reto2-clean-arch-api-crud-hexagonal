package org.litethinking.reto1cleanarchapicrud.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.litethinking.reto1cleanarchapicrud.application.service.InventoryService;
import org.litethinking.reto1cleanarchapicrud.application.service.ProductService;
import org.litethinking.reto1cleanarchapicrud.domain.model.Inventory;
import org.litethinking.reto1cleanarchapicrud.domain.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST pa' el inventario, aquí exponemo' los endpoints pa' que la gente los use
 * Con esto se puede crear, buscar, actualizar y borrar inventario, tú sabe!
 */
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final ProductService productService;

    /**
     * Crea un nuevo registro de inventario
     * @param inventory el inventario que vamo' a crear
     * @return el inventario creado con su ID
     */
    @PostMapping
    public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory) {
        try {
            Inventory createdInventory = inventoryService.createInventory(inventory);
            return new ResponseEntity<>(createdInventory, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Busca un inventario por su ID
     * @param id el ID del inventario que tamo' buscando
     * @return el inventario si lo encontramo', si no, error 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        return inventoryService.getInventoryById(id)
                .map(inventory -> new ResponseEntity<>(inventory, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Busca to' los inventarios
     * @return la lista de inventarios
     */
    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {
        List<Inventory> inventories = inventoryService.getAllInventory();
        return new ResponseEntity<>(inventories, HttpStatus.OK);
    }

    /**
     * Busca inventario por producto
     * @param productId el ID del producto que tamo' buscando
     * @return el inventario si lo encontramo', si no, error 404
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<Inventory> getInventoryByProduct(@PathVariable Long productId) {
        Optional<Product> product = productService.getProductById(productId);
        if (!product.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        return inventoryService.getInventoryByProduct(product.get())
                .map(inventory -> new ResponseEntity<>(inventory, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Busca inventario con poco stock
     * @return la lista de inventarios con poco stock
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<Inventory>> getLowStockInventory() {
        List<Inventory> inventories = inventoryService.getLowStockInventory();
        return new ResponseEntity<>(inventories, HttpStatus.OK);
    }

    /**
     * Busca inventario por ubicación
     * @param location la ubicación que tamo' buscando
     * @return la lista de inventarios en esa ubicación
     */
    @GetMapping("/location/{location}")
    public ResponseEntity<List<Inventory>> getInventoryByLocation(@PathVariable String location) {
        List<Inventory> inventories = inventoryService.getInventoryByLocation(location);
        return new ResponseEntity<>(inventories, HttpStatus.OK);
    }

    /**
     * Actualiza un inventario existente
     * @param id el ID del inventario que vamo' a actualizar
     * @param inventory los nuevos datos del inventario
     * @return el inventario actualizado, o error 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, @RequestBody Inventory inventory) {
        try {
            if (!inventoryService.getInventoryById(id).isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            inventory.setId(id);
            Inventory updatedInventory = inventoryService.updateInventory(id, inventory);
            return new ResponseEntity<>(updatedInventory, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Actualiza solo la cantidad de un inventario
     * @param id el ID del inventario que vamo' a actualizar
     * @param quantity la nueva cantidad
     * @return el inventario actualizado, o error 404 si no existe
     */
    @PatchMapping("/{id}/quantity/{quantity}")
    public ResponseEntity<Inventory> updateInventoryQuantity(@PathVariable Long id, @PathVariable Integer quantity) {
        try {
            Inventory updatedInventory = inventoryService.updateInventoryQuantity(id, quantity);
            return new ResponseEntity<>(updatedInventory, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Borra un inventario
     * @param id el ID del inventario que vamo' a borrar
     * @return OK si se borró, o error 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        try {
            inventoryService.deleteInventory(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}