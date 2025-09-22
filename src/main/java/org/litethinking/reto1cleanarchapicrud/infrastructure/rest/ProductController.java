package org.litethinking.reto1cleanarchapicrud.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.litethinking.reto1cleanarchapicrud.application.service.ProductService;
import org.litethinking.reto1cleanarchapicrud.domain.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST pa' los productos, aquí exponemo' los endpoints pa' que la gente los use
 * Con esto se puede crear, buscar, actualizar y borrar productos, tú sabe!
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Crea un producto nuevo
     * @param product el producto que vamo' a crear
     * @return el producto creado con su ID
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    /**
     * Busca un producto por su ID
     * @param id el ID del producto que tamo' buscando
     * @return el producto si lo encontramo', si no, error 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Busca to' los productos
     * @return la lista de productos
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Busca productos por nombre
     * @param name el nombre que tamo' buscando
     * @return la lista de productos que coinciden
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
        // Como no tenemos un método específico para buscar por nombre,
        // obtenemos todos los productos y filtramos por nombre
        List<Product> allProducts = productService.getAllProducts();
        List<Product> filteredProducts = allProducts.stream()
                .filter(p -> p.getName() != null && p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
        return new ResponseEntity<>(filteredProducts, HttpStatus.OK);
    }

    /**
     * Busca productos por categoría
     * @param category la categoría que tamo' buscando
     * @return la lista de productos de esa categoría
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * Busca un producto por código de barras
     * @param barcode el código de barras que tamo' buscando
     * @return el producto si lo encontramo', si no, error 404
     */
    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<Product> getProductByBarcode(@PathVariable String barcode) {
        return productService.getProductByBarcode(barcode)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Actualiza un producto existente
     * @param id el ID del producto que vamo' a actualizar
     * @param product los nuevos datos del producto
     * @return el producto actualizado, o error 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        if (!productService.getProductById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        product.setId(id);
        Product updatedProduct = productService.updateProduct(id, product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    /**
     * Borra un producto
     * @param id el ID del producto que vamo' a borrar
     * @return OK si se borró, o error 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!productService.getProductById(id).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
