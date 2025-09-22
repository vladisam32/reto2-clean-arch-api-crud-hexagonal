package org.litethinking.supermercado.domain.model;

import lombok.*;

/**
 * Entidad Cajero que representa un cajero en el sistema del supermercado, ¡pa' cobrar!
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Cajero {
    private Long id;
    private String nombre;
    private String codigo;
    private String turno;

    // Manual implementation of builder pattern
    public static CajeroBuilder builder() {
        return new CajeroBuilder();
    }

    public static class CajeroBuilder {
        private Long id;
        private String nombre;
        private String codigo;
        private String turno;

        CajeroBuilder() {
        }

        public CajeroBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CajeroBuilder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public CajeroBuilder codigo(String codigo) {
            this.codigo = codigo;
            return this;
        }

        public CajeroBuilder turno(String turno) {
            this.turno = turno;
            return this;
        }

        public Cajero build() {
            return new Cajero(id, nombre, codigo, turno);
        }
    }

    // Manual implementation of getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getTurno() {
        return turno;
    }
}
