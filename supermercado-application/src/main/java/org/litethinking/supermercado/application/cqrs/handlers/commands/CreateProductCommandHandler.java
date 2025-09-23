package org.litethinking.supermercado.application.cqrs.handlers.commands;

import org.litethinking.supermercado.application.mapper.ProductoMapper;
import org.litethinking.supermercado.domain.model.Producto;
import org.litethinking.supermercado.domain.ports.output.RepositorioProductoPort;
import org.litethinking.supermercado.shareddto.cqrs.commands.CreateProductCommand;
import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;
import org.springframework.stereotype.Component;

/**
 * Handler for CreateProductCommand.
 * This class is responsible for processing the command to create a new product.
 */
@Component
public class CreateProductCommandHandler {

    private final RepositorioProductoPort repositorioProductoPort;

    public CreateProductCommandHandler(RepositorioProductoPort repositorioProductoPort) {
        this.repositorioProductoPort = repositorioProductoPort;
    }

    /**
     * Handle the command to create a new product.
     *
     * @param command the command to create a product
     * @return the created product DTO
     */
    public ProductoDto handle(CreateProductCommand command) {
        Producto producto = Producto.builder()
                .nombre(command.nombre())
                .descripcion(command.descripcion())
                .precio(command.precio())
                .categoria(command.categoria())
                .codigoBarras(command.codigoBarras())
                .build();

        Producto productoGuardado = repositorioProductoPort.save(producto);
        return ProductoMapper.toDto(productoGuardado);
    }
}
