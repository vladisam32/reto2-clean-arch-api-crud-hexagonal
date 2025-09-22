package org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.adapter;

import lombok.RequiredArgsConstructor;
import org.litethinking.reto1cleanarchapicrud.domain.model.Product;
import org.litethinking.reto1cleanarchapicrud.domain.repository.ProductRepository;
import org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.entity.ProductJpaEntity;
import org.litethinking.reto1cleanarchapicrud.infrastructure.persistence.repository.ProductJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador pa' conectar el dominio con la base de datos de productos
 * Esto hace que el sistema no dependa directamente de la base de datos, tú sabe!
 */
@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = mapToEntity(product);
        ProductJpaEntity savedEntity = productJpaRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id)
                .map(this::mapToDomain);
    }

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByCategory(String category) {
        return productJpaRepository.findByCategory(category).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Product> findByBarcode(String barcode) {
        return productJpaRepository.findByBarcode(barcode)
                .map(this::mapToDomain);
    }

    @Override
    public void deleteById(Long id) {
        productJpaRepository.deleteById(id);
    }

    /**
     * Convierte un producto del dominio a una entidad JPA
     * @param product el producto del dominio
     * @return la entidad JPA
     */
    private ProductJpaEntity mapToEntity(Product product) {
        return ProductJpaEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory())
                .barcode(product.getBarcode())
                .build();
    }

    /**
     * Convierte una entidad JPA a un producto del dominio
     * @param entity la entidad JPA
     * @return el producto del dominio
     */
    private Product mapToDomain(ProductJpaEntity entity) {
        return Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .category(entity.getCategory())
                .barcode(entity.getBarcode())
                .build();
    }
}
