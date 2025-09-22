package org.litethinking.supermercado.application.cqrs.handlers.commands;

import org.litethinking.supermercado.application.mapper.ProductoMapper;
import org.litethinking.supermercado.domain.model.Producto;
import org.litethinking.supermercado.domain.ports.output.RepositorioProductoPort;
import org.litethinking.supermercado.shareddto.cqrs.commands.UpdateProductCommand;
import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Handler for UpdateProductCommand.
 * This class is responsible for processing the command to update an existing product.
 */
@Component
public class UpdateProductCommandHandler {

    private final RepositorioProductoPort repositorioProductoPort;

    public UpdateProductCommandHandler(RepositorioProductoPort repositorioProductoPort) {
        this.repositorioProductoPort = repositorioProductoPort;
    }

    /**
     * Handle the command to update an existing product.
     *
     * @param command the command to update a product
     * @return the updated product DTO
     * @throws IllegalArgumentException if the product is not found
     */
    public ProductoDto handle(UpdateProductCommand command) {
        Optional<Producto> productoExistente = repositorioProductoPort.findById(command.id());
        if (productoExistente.isPresent()) {
            Producto producto = Producto.builder()
                    .id(command.id())
                    .nombre(command.nombre())
                    .descripcion(command.descripcion())
                    .precio(command.precio())
                    .categoria(command.categoria())
                    .codigoBarras(command.codigoBarras())
                    .build();

            Producto productoActualizado = repositorioProductoPort.save(producto);
            return ProductoMapper.toDto(productoActualizado);
        } else {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + command.id());
        }
    }
}
