package org.litethinking.supermercado.restapi.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.litethinking.supermercado.application.ServicioCliente;
import org.litethinking.supermercado.shareddto.supermercado.ClienteDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST pa' las operaciones de Cliente, manín.
 */
@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "API pa' manejar to' los clientes del supermercado, ¡tú 'ta claro!")
public class ControladorCliente {

    private final ServicioCliente servicioCliente;

    public ControladorCliente(ServicioCliente servicioCliente) {
        this.servicioCliente = servicioCliente;
    }

    /**
     * Crea un cliente nuevo, ¡así mismo!
     *
     * @param clienteDto el cliente que vamo' a crear
     * @return el cliente ya creao'
     */
    @Operation(
        summary = "Crear un cliente nuevecito",
        description = "Mete un cliente nuevo en el sistema con to' la información que le dimo'"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "¡Cliente creao' exitosamente, manín!",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClienteDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Los datos del cliente 'tán malos, arréglalo",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Se rompió el servidor, ¡qué lío!",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<ClienteDto> crearCliente(@Parameter(description = "Datos del cliente a crear", required = true) @RequestBody ClienteDto clienteDto) {
        ClienteDto clienteCreado = servicioCliente.crearCliente(clienteDto);
        return new ResponseEntity<>(clienteCreado, HttpStatus.CREATED);
    }

    /**
     * Actualiza un cliente que ya ta' en el sistema.
     *
     * @param id el ID del cliente que vamo' a actualizar
     * @param clienteDto los datos nuevecitos del cliente
     * @return el cliente ya actualizao'
     */
    @Operation(
        summary = "Actualizar un cliente, ¡al toque!",
        description = "Cambia la información de un cliente que ya 'tá en el sistema, tú sabe"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "¡Cliente actualizao' exitosamente, qué bueno!",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClienteDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "No encontramo' ese cliente, ¿seguro existe?",
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
    public ResponseEntity<ClienteDto> actualizarCliente(
            @Parameter(description = "ID del cliente a actualizar", required = true) @PathVariable Long id, 
            @Parameter(description = "Datos actualizados del cliente", required = true) @RequestBody ClienteDto clienteDto) {
        ClienteDto clienteActualizado = servicioCliente.actualizarCliente(id, clienteDto);
        return ResponseEntity.ok(clienteActualizado);
    }

    /**
     * Busca un cliente por su ID, ¿tú 'ta claro?
     *
     * @param id el ID del cliente que queremo' encontrar
     * @return el cliente si aparece, si no un 404 pa' que sepa
     */
    @Operation(
        summary = "Buscar un cliente por su ID, ¡rapidito!",
        description = "Busca y te trae un cliente por el ID que le dimo'"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "¡Lo encontramo'! Aquí 'tá tu cliente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClienteDto.class)
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
    public ResponseEntity<ClienteDto> obtenerClientePorId(@Parameter(description = "ID del cliente a buscar", required = true) @PathVariable Long id) {
        Optional<ClienteDto> cliente = servicioCliente.obtenerClientePorId(id);
        return cliente.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Trae to' los clientes que hay, ¡completo!
     *
     * @return la lista con to' los clientes
     */
    @Operation(
        summary = "Traer to' los clientes, ¡completo!",
        description = "Te busca la lista completa de to' los clientes que hay en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "¡Ahí te van to' los clientes, manín!",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClienteDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "El servidor se fue a dormir, ¡despiértalo!",
            content = @Content
        )
    })
    @GetMapping
    public ResponseEntity<List<ClienteDto>> obtenerTodosLosClientes() {
        List<ClienteDto> clientes = servicioCliente.obtenerTodosLosClientes();
        return ResponseEntity.ok(clientes);
    }

    /**
     * Borra un cliente del sistema, ¡fuera!
     *
     * @param id el ID del cliente que vamo' a eliminar
     * @return 204 No Content, ya no hay na' que ver aquí
     */
    @Operation(
        summary = "Borrar un cliente, ¡pa' fuera!",
        description = "Saca un cliente del sistema, ya no lo queremo'"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "¡Cliente eliminao', ya no hay na' que ver aquí!",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Ese cliente ya no existe o nunca existió, ¿qué tú quiere' borrar?",
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
    public ResponseEntity<Void> eliminarCliente(@Parameter(description = "ID del cliente a eliminar", required = true) @PathVariable Long id) {
        servicioCliente.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca un cliente por su email, rapidito.
     *
     * @param email el email que tamo' buscando
     * @return el cliente si lo encontramo', si no un 404
     */
    @Operation(
        summary = "Buscar cliente por email, ¡al toque!",
        description = "Busca un cliente por su correo electrónico, tú le pasas el email y él te lo encuentra"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "¡Lo encontramo'! Aquí 'tá tu cliente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClienteDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Ese email no existe, revisa bien o búscate otro",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "El servidor se cayó, ¡qué vaina!",
            content = @Content
        )
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteDto> obtenerClientePorEmail(@Parameter(description = "Email del cliente a buscar", required = true) @PathVariable String email) {
        Optional<ClienteDto> cliente = servicioCliente.obtenerClientePorEmail(email);
        return cliente.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Busca clientes que tengan ese nombre o parte de él, tú sabe.
     *
     * @param nombre el pedacito del nombre que tamo' buscando
     * @return la lista de clientes que coinciden con ese nombre
     */
    @Operation(
        summary = "Buscar clientes por nombre, ¡a lo rápido!",
        description = "Busca to' los clientes que tengan ese nombre o parte de él, tú le das un pedacito y él te busca"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "¡Aquí 'tán to' los clientes con ese nombre, manín!",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClienteDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "El servidor se enredó, ¡qué problema!",
            content = @Content
        )
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<ClienteDto>> obtenerClientesPorNombreContaining(@Parameter(description = "Nombre o parte del nombre a buscar", required = true) @PathVariable String nombre) {
        List<ClienteDto> clientes = servicioCliente.obtenerClientesPorNombreContaining(nombre);
        return ResponseEntity.ok(clientes);
    }
}
