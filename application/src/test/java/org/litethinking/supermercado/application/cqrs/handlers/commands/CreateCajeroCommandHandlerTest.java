package org.litethinking.supermercado.application.cqrs.handlers.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.litethinking.supermercado.domain.model.Cajero;
import org.litethinking.supermercado.domain.ports.output.RepositorioCajeroPort;
import org.litethinking.supermercado.shareddto.cqrs.commands.CreateCajeroCommand;
import org.litethinking.supermercado.shareddto.supermercado.CajeroDto;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateCajeroCommandHandlerTest {

    @Mock
    private RepositorioCajeroPort repositorioCajeroPort;

    private CreateCajeroCommandHandler handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new CreateCajeroCommandHandler(repositorioCajeroPort);
    }

    @Test
    void handle_ShouldCreateCajero_WhenCommandIsValid() {
        // Arrange
        String nombre = "Juan Perez";
        String codigo = "C001";
        String turno = "Mañana";
        
        CreateCajeroCommand command = new CreateCajeroCommand(nombre, codigo, turno);
        
        Cajero cajeroGuardado = new Cajero();
        cajeroGuardado.setId(1L);
        cajeroGuardado.setNombre(nombre);
        cajeroGuardado.setCodigo(codigo);
        cajeroGuardado.setTurno(turno);
        
        when(repositorioCajeroPort.save(any(Cajero.class))).thenReturn(cajeroGuardado);
        
        // Act
        CajeroDto result = handler.handle(command);
        
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals(nombre, result.nombre());
        assertEquals(codigo, result.codigo());
        assertEquals(turno, result.turno());
        
        verify(repositorioCajeroPort).save(any(Cajero.class));
    }
}