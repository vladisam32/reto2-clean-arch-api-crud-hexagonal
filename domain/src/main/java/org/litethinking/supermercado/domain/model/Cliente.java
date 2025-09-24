package org.litethinking.supermercado.domain.model;

import lombok.*;

/**
 * Entidad Cliente que representa un cliente en el sistema del supermercado, ¡así mismo!
 */

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Cliente {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;

    /*// Default constructor
    public Cliente() {
    }

    // All-args constructor
    public Cliente(Long id, String nombre, String email, String telefono, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Manual implementation of builder pattern
    public static ClienteBuilder builder() {
        return new ClienteBuilder();
    }

    public static class ClienteBuilder {
        private Long id;
        private String nombre;
        private String email;
        private String telefono;
        private String direccion;

        ClienteBuilder() {
        }

        public ClienteBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ClienteBuilder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public ClienteBuilder email(String email) {
            this.email = email;
            return this;
        }

        public ClienteBuilder telefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public ClienteBuilder direccion(String direccion) {
            this.direccion = direccion;
            return this;
        }

        public Cliente build() {
            return new Cliente(id, nombre, email, telefono, direccion);
        }
    }

    // Manual implementation of getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }*/
}
