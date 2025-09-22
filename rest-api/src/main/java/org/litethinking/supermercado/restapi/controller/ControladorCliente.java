package org.litethinking.supermercado.restapi.controller;

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
    @PostMapping
    public ResponseEntity<ClienteDto> crearCliente(@RequestBody ClienteDto clienteDto) {
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
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDto> actualizarCliente(@PathVariable Long id, @RequestBody ClienteDto clienteDto) {
        ClienteDto clienteActualizado = servicioCliente.actualizarCliente(id, clienteDto);
        return ResponseEntity.ok(clienteActualizado);
    }

    /**
     * Busca un cliente por su ID, ¿tú 'ta claro?
     *
     * @param id el ID del cliente que queremo' encontrar
     * @return el cliente si aparece, si no un 404 pa' que sepa
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> obtenerClientePorId(@PathVariable Long id) {
        Optional<ClienteDto> cliente = servicioCliente.obtenerClientePorId(id);
        return cliente.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Trae to' los clientes que hay, ¡completo!
     *
     * @return la lista con to' los clientes
     */
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        servicioCliente.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca un cliente por su email, rapidito.
     *
     * @param email el email que tamo' buscando
     * @return el cliente si lo encontramo', si no un 404
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ClienteDto> obtenerClientePorEmail(@PathVariable String email) {
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
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<ClienteDto>> obtenerClientesPorNombreContaining(@PathVariable String nombre) {
        List<ClienteDto> clientes = servicioCliente.obtenerClientesPorNombreContaining(nombre);
        return ResponseEntity.ok(clientes);
    }
}
