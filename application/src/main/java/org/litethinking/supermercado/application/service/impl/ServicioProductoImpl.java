package org.litethinking.supermercado.application.service.impl;

import org.litethinking.supermercado.application.ServicioProducto;
import org.litethinking.supermercado.application.cqrs.handlers.commands.CreateProductCommandHandler;
import org.litethinking.supermercado.application.cqrs.handlers.commands.DeleteProductCommandHandler;
import org.litethinking.supermercado.application.cqrs.handlers.commands.UpdateProductCommandHandler;
import org.litethinking.supermercado.application.cqrs.handlers.queries.GetAllProductsQueryHandler;
import org.litethinking.supermercado.application.cqrs.handlers.queries.GetProductByIdQueryHandler;
import org.litethinking.supermercado.application.cqrs.handlers.queries.GetProductsByFilterQueryHandler;
import org.litethinking.supermercado.shareddto.cqrs.commands.CreateProductCommand;
import org.litethinking.supermercado.shareddto.cqrs.commands.DeleteProductCommand;
import org.litethinking.supermercado.shareddto.cqrs.commands.UpdateProductCommand;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetAllProductsQuery;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetProductByIdQuery;
import org.litethinking.supermercado.shareddto.cqrs.queries.GetProductsByFilterQuery;
import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of ServicioProducto using CQRS pattern.
 */
@Service
public class ServicioProductoImpl implements ServicioProducto {

    private final CreateProductCommandHandler createProductCommandHandler;
    private final UpdateProductCommandHandler updateProductCommandHandler;
    private final DeleteProductCommandHandler deleteProductCommandHandler;
    private final GetProductByIdQueryHandler getProductByIdQueryHandler;
    private final GetAllProductsQueryHandler getAllProductsQueryHandler;
    private final GetProductsByFilterQueryHandler getProductsByFilterQueryHandler;

    public ServicioProductoImpl(
            CreateProductCommandHandler createProductCommandHandler,
            UpdateProductCommandHandler updateProductCommandHandler,
            DeleteProductCommandHandler deleteProductCommandHandler,
            GetProductByIdQueryHandler getProductByIdQueryHandler,
            GetAllProductsQueryHandler getAllProductsQueryHandler,
            GetProductsByFilterQueryHandler getProductsByFilterQueryHandler) {
        this.createProductCommandHandler = createProductCommandHandler;
        this.updateProductCommandHandler = updateProductCommandHandler;
        this.deleteProductCommandHandler = deleteProductCommandHandler;
        this.getProductByIdQueryHandler = getProductByIdQueryHandler;
        this.getAllProductsQueryHandler = getAllProductsQueryHandler;
        this.getProductsByFilterQueryHandler = getProductsByFilterQueryHandler;
    }

    @Override
    public ProductoDto crearProducto(ProductoDto productoDto) {
        CreateProductCommand command = new CreateProductCommand(
                productoDto.nombre(),
                productoDto.descripcion(),
                productoDto.precio(),
                productoDto.categoria(),
                productoDto.codigoBarras()
        );
        return createProductCommandHandler.handle(command);
    }

    @Override
    public ProductoDto actualizarProducto(Long id, ProductoDto productoDto) {
        UpdateProductCommand command = new UpdateProductCommand(
                id,
                productoDto.nombre(),
                productoDto.descripcion(),
                productoDto.precio(),
                productoDto.categoria(),
                productoDto.codigoBarras()
        );
        return updateProductCommandHandler.handle(command);
    }

    @Override
    public Optional<ProductoDto> obtenerProductoPorId(Long id) {
        GetProductByIdQuery query = new GetProductByIdQuery(id);
        return getProductByIdQueryHandler.handle(query);
    }

    @Override
    public List<ProductoDto> obtenerTodosLosProductos() {
        GetAllProductsQuery query = new GetAllProductsQuery();
        return getAllProductsQueryHandler.handle(query);
    }

    @Override
    public void eliminarProducto(Long id) {
        DeleteProductCommand command = new DeleteProductCommand(id);
        deleteProductCommandHandler.handle(command);
    }

    @Override
    public List<ProductoDto> obtenerProductosPorCategoria(String categoria) {
        GetProductsByFilterQuery query = new GetProductsByFilterQuery(categoria, null, null, null);
        return getProductsByFilterQueryHandler.handle(query);
    }

    @Override
    public List<ProductoDto> obtenerProductosPorRangoDePrecio(BigDecimal precioMinimo, BigDecimal precioMaximo) {
        GetProductsByFilterQuery query = new GetProductsByFilterQuery(null, precioMinimo, precioMaximo, null);
        return getProductsByFilterQueryHandler.handle(query);
    }

    @Override
    public Optional<ProductoDto> obtenerProductoPorCodigoBarras(String codigoBarras) {
        GetProductsByFilterQuery query = new GetProductsByFilterQuery(null, null, null, codigoBarras);
        List<ProductoDto> productos = getProductsByFilterQueryHandler.handle(query);
        return productos.isEmpty() ? Optional.empty() : Optional.of(productos.get(0));
    }
}
