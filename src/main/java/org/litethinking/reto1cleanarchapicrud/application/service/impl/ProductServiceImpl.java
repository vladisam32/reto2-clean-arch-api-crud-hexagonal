package org.litethinking.reto1cleanarchapicrud.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.litethinking.reto1cleanarchapicrud.application.service.ProductService;
import org.litethinking.reto1cleanarchapicrud.domain.model.Product;
import org.litethinking.reto1cleanarchapicrud.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de productos, aquí tá la lógica de negocio
 * Esto maneja to' lo que tiene que ver con productos, tú sabe!
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        // Validaciones básicas
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        
        if (product.getPrice() == null || product.getPrice().doubleValue() <= 0) {
            throw new IllegalArgumentException("El precio del producto debe ser mayor que cero");
        }
        
        // Si ya existe un producto con el mismo código de barras, no lo creamos
        if (product.getBarcode() != null && !product.getBarcode().trim().isEmpty()) {
            Optional<Product> existingProduct = productRepository.findByBarcode(product.getBarcode());
            if (existingProduct.isPresent()) {
                throw new IllegalArgumentException("Ya existe un producto con el código de barras: " + product.getBarcode());
            }
        }
        
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        // Verificamos que el producto exista
        Optional<Product> existingProduct = productRepository.findById(id);
        if (!existingProduct.isPresent()) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        
        // Validaciones básicas
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }
        
        if (product.getPrice() == null || product.getPrice().doubleValue() <= 0) {
            throw new IllegalArgumentException("El precio del producto debe ser mayor que cero");
        }
        
        // Si cambia el código de barras, verificamos que no exista otro producto con ese código
        if (product.getBarcode() != null && !product.getBarcode().equals(existingProduct.get().getBarcode())) {
            Optional<Product> productWithBarcode = productRepository.findByBarcode(product.getBarcode());
            if (productWithBarcode.isPresent() && !productWithBarcode.get().getId().equals(id)) {
                throw new IllegalArgumentException("Ya existe un producto con el código de barras: " + product.getBarcode());
            }
        }
        
        // Aseguramos que el ID sea el correcto
        product.setId(id);
        
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void deleteProduct(Long id) {
        // Verificamos que el producto exista
        if (!productRepository.findById(id).isPresent()) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        
        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public Optional<Product> getProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode);
    }
}