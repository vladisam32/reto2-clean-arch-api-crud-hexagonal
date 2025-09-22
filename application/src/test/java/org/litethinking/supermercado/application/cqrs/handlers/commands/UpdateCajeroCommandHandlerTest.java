package org.litethinking.supermercado.application.cqrs.handlers.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.litethinking.supermercado.domain.model.Cajero;
import org.litethinking.supermercado.domain.ports.output.RepositorioCajeroPort;
import org.litethinking.supermercado.shareddto.cqrs.commands.UpdateCajeroCommand;
import org.litethinking.supermercado.shareddto.supermercado.CajeroDto;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateCajeroCommandHandlerTest {

    @Mock
    private RepositorioCajeroPort repositorioCajeroPort;

    private UpdateCajeroCommandHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new UpdateCajeroCommandHandler(repositorioCajeroPort);
    }

    @Test
    void handle_ShouldUpdateCajero_WhenCajeroExists() {
        // Arrange
        Long id = 1L;
        String nombre = "Juan Perez Actualizado";
        String codigo = "C001-UPD";
        String turno = "Tarde";

        UpdateCajeroCommand command = new UpdateCajeroCommand(id, nombre, codigo, turno);

        Cajero cajeroExistente = new Cajero();
        cajeroExistente.setId(id);
        cajeroExistente.setNombre("Juan Perez");
        cajeroExistente.setCodigo("C001");
        cajeroExistente.setTurno("Mañana");

        Cajero cajeroActualizado = new Cajero();
        cajeroActualizado.setId(id);
        cajeroActualizado.setNombre(nombre);
        cajeroActualizado.setCodigo(codigo);
        cajeroActualizado.setTurno(turno);

        when(repositorioCajeroPort.findById(id)).thenReturn(Optional.of(cajeroExistente));
        when(repositorioCajeroPort.save(any(Cajero.class))).thenReturn(cajeroActualizado);

        // Act
        CajeroDto result = handler.handle(command);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.id());
        assertEquals(nombre, result.nombre());
        assertEquals(codigo, result.codigo());
        assertEquals(turno, result.turno());

        verify(repositorioCajeroPort).findById(id);
        verify(repositorioCajeroPort).save(any(Cajero.class));
    }

    @Test
    void handle_ShouldThrowException_WhenCajeroDoesNotExist() {
        // Arrange
        Long id = 999L;
        UpdateCajeroCommand command = new UpdateCajeroCommand(id, "Nombre", "Codigo", "Turno");

        when(repositorioCajeroPort.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            handler.handle(command);
        });

        assertEquals("Cajero no encontrado con ID: " + id, exception.getMessage());
        verify(repositorioCajeroPort).findById(id);
    }
}
