package org.litethinking.supermercado.restapi.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.litethinking.supermercado.application.ServicioProducto;
import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador REST pa' las operaciones de Producto, ¡con to'!
 */
@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "API pa' manejar to' los productos del supermercado, ¡con to'!")
public class ControladorProducto {

    private static final Logger logger = LogManager.getLogger(ControladorProducto.class);
    private final ServicioProducto servicioProducto;

    public ControladorProducto(ServicioProducto servicioProducto) {
        this.servicioProducto = servicioProducto;
        logger.info("ControladorProducto inicializado");
    }

    /**
     * Crea un producto nuevo, pa' venderlo.
     *
     * @param productoDto el producto que vamo' a crear
     * @return el producto ya creao'
     */
    @Operation(
        summary = "Crear un producto nuevecito",
        description = "Mete un producto nuevo en el sistema con to' la información que le dimo'"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "¡Producto creao' exitosamente, manín!",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductoDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Los datos del producto 'tán malos, arréglalo",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Se rompió el servidor, ¡qué lío!",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<ProductoDto> crearProducto(
            @Parameter(description = "Datos del producto a crear", required = true)
            @RequestBody ProductoDto productoDto) {
        logger.info("Creando nuevo producto: {}", productoDto.nombre());
        try {
            ProductoDto productoCreado = servicioProducto.crearProducto(productoDto);
            logger.info("Producto creado con ID: {}", productoCreado.id());
            return new ResponseEntity<>(productoCreado, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error al crear producto: {}", productoDto.nombre(), e);
            throw e;
        }
    }

    @Operation(
        summary = "Actualizar un producto, ¡al toque!",
        description = "Cambia la información de un producto que ya 'tá en el sistema, tú sabe"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "¡Producto actualizao' exitosamente, qué bueno!",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductoDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "No encontramo' ese producto, ¿seguro existe?",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Esos datos 'tán malos, revísalos bien",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "El servidor se volvió loco, ¡qué vaina!",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDto> actualizarProducto(
            @Parameter(description = "ID del producto a actualizar", required = true)
            @PathVariable Long id, 
            @Parameter(description = "Datos actualizados del producto", required = true)
            @RequestBody ProductoDto productoDto) {
        ProductoDto productoActualizado = servicioProducto.actualizarProducto(id, productoDto);
        return ResponseEntity.ok(productoActualizado);
    }

    /**
     * Busca un producto por su ID, a ver si lo tenemo'.
     *
     * @param id el ID del producto que tamo' buscando
     * @return el producto si lo encontramo', si no un 404
     */
    @Operation(
        summary = "Buscar un producto por su ID, ¡rapidito!",
        description = "Busca y te trae un producto por el ID que le dimo'"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "¡Lo encontramo'! Aquí 'tá tu producto",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductoDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "No hay na' con ese ID, búscate otro",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "El servidor explotó, ¡qué desastre!",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDto> obtenerProductoPorId(
            @Parameter(description = "ID del producto a buscar", required = true)
            @PathVariable Long id) {
        logger.debug("Buscando producto con ID: {}", id);
        try {
            Optional<ProductoDto> producto = servicioProducto.obtenerProductoPorId(id);
            if (producto.isPresent()) {
                logger.info("Producto encontrado: ID={}, Nombre={}", id, producto.get().nombre());
                return ResponseEntity.ok(producto.get());
            } else {
                logger.warn("Producto no encontrado con ID: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error al buscar producto con ID: {}", id, e);
            throw e;
        }
    }

    @Operation(
        summary = "Traer to' los productos, ¡completo!",
        description = "Te busca la lista completa de to' los productos que hay en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "¡Ahí te van to' los productos, manín!",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductoDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "El servidor se fue a dormir, ¡despiértalo!",
            content = @Content
        )
    })
    @GetMapping
    public ResponseEntity<List<ProductoDto>> obtenerTodosLosProductos() {
        logger.debug("Obteniendo lista de todos los productos");
        try {
            List<ProductoDto> productos = servicioProducto.obtenerTodosLosProductos();
            logger.info("Se encontraron {} productos en total", productos.size());

            // Si es necesario, podemos loguear más detalles a nivel de debug
            if (logger.isDebugEnabled() && !productos.isEmpty()) {
                logger.debug("Categorías encontradas: {}", 
                    productos.stream()
                        .map(ProductoDto::categoria)
                        .distinct()
                        .collect(Collectors.joining(", ")));
            }

            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            logger.error("Error al obtener la lista de productos", e);
            throw e;
        }
    }

    /**
     * Borra un producto del sistema, ¡pa' fuera!
     *
     * @param id el ID del producto que vamo' a eliminar
     * @return 204 No Content, ya no hay na' que ver aquí
     */
    @Operation(
        summary = "Borrar un producto, ¡pa' fuera!",
        description = "Saca un producto del sistema, ya no lo queremo'"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "¡Producto eliminao', ya no hay na' que ver aquí!",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Ese producto ya no existe o nunca existió, ¿qué tú quiere' borrar?",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "El servidor 'tá dando problemas, ¡qué lío!",
            content = @Content
        )
    })
    @DeleteMapping("/{id}")
    @Hidden
    public ResponseEntity<Void> eliminarProducto(
            @Parameter(description = "ID del producto a eliminar", required = true)
            @PathVariable Long id) {
        logger.info("Eliminando producto con ID: {}", id);
        try {
            // Opcionalmente, podríamos obtener el producto primero para registrar más detalles
            Optional<ProductoDto> producto = servicioProducto.obtenerProductoPorId(id);
            if (producto.isPresent()) {
                logger.debug("Eliminando producto: ID={}, Nombre={}", id, producto.get().nombre());
            }

            servicioProducto.eliminarProducto(id);
            logger.info("Producto eliminado con ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error al eliminar producto con ID: {}", id, e);
            throw e;
        }
    }

    @Operation(
        summary = "Buscar productos por categoría, ¡al toque!",
        description = "Te trae to' los productos que son de una categoría que tú escoja"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "¡Aquí 'tán to' los productos de esa categoría, manín!",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductoDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "El servidor se volvió loco, ¡qué problema!",
            content = @Content
        )
    })
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoDto>> obtenerProductosPorCategoria(
            @Parameter(description = "Categoría de productos a buscar", required = true)
            @PathVariable String categoria) {
        List<ProductoDto> productos = servicioProducto.obtenerProductosPorCategoria(categoria);
        return ResponseEntity.ok(productos);
    }

    /**
     * Busca un producto por su código de barras, ¡pim pam!
     *
     * @param codigoBarras el código de barras que tamo' buscando
     * @return el producto si lo encontramo', si no un 404
     */
    @Operation(
        summary = "Buscar producto por código de barras, ¡pim pam!",
        description = "Busca un producto por su código de barras, tú le pasas el código y él te lo encuentra"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "¡Lo encontramo'! Aquí 'tá tu producto",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductoDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Ese código no existe, revisa bien o búscate otro",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "El servidor se cayó, ¡qué vaina!",
            content = @Content
        )
    })
    @GetMapping("/codigo-barras/{codigoBarras}")
    public ResponseEntity<ProductoDto> obtenerProductoPorCodigoBarras(
            @Parameter(description = "Código de barras del producto a buscar", required = true)
            @PathVariable String codigoBarras) {
        Optional<ProductoDto> producto = servicioProducto.obtenerProductoPorCodigoBarras(codigoBarras);
        return producto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Genera un archivo CSV con todos los productos.
     *
     * @return un archivo CSV con todos los productos
     */
    @Operation(
        summary = "Hacer un CSV con to' los productos, ¡pa' llevártelo!",
        description = "Te prepara un archivo CSV con to' los productos que hay, pa' que lo descargues"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "¡CSV listo, manín! Ya te lo puedes bajar",
            content = @Content(
                mediaType = "text/plain"
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "El servidor se enredó, ¡qué lío!",
            content = @Content
        )
    })
    @GetMapping("/csv")
    public ResponseEntity<String> generarCsvProductos() {
        List<ProductoDto> productos = servicioProducto.obtenerTodosLosProductos();

        // Crear el contenido del CSV
        StringBuilder csv = new StringBuilder();
        csv.append("id,nombre,descripcion,precio,categoria,codigoBarras\n");

        for (ProductoDto producto : productos) {
            csv.append(producto.id()).append(",")
               .append(producto.nombre()).append(",")
               .append(producto.descripcion()).append(",")
               .append(producto.precio()).append(",")
               .append(producto.categoria()).append(",")
               .append(producto.codigoBarras()).append("\n");
        }

        // Configurar las cabeceras para la descarga del archivo
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "productos.csv");

        return new ResponseEntity<>(csv.toString(), headers, HttpStatus.OK);
    }
}
