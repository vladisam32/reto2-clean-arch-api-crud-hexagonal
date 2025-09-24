package org.litethinking.supermercado.web.service;

import org.litethinking.supermercado.shareddto.supermercado.CajeroDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service for interacting with the Cashier REST API.
 */
@Service
public class CajeroService {

    private final RestTemplate restTemplate;
    private final String apiBaseUrl;

    @Autowired
    public CajeroService(RestTemplate restTemplate, String apiBaseUrl) {
        this.restTemplate = restTemplate;
        this.apiBaseUrl = apiBaseUrl;
    }

    /**
     * Get all cashiers.
     *
     * @return a list of all cashiers
     */
    public List<CajeroDto> obtenerTodosLosCajeros() {
        try {
            ResponseEntity<List<CajeroDto>> response = restTemplate.exchange(
                    apiBaseUrl + "/cajeros",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CajeroDto>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Get a cashier by its ID.
     *
     * @param id the ID of the cashier to get
     * @return the cashier if found, empty otherwise
     */
    public Optional<CajeroDto> obtenerCajeroPorId(Long id) {
        try {
            ResponseEntity<CajeroDto> response = restTemplate.getForEntity(
                    apiBaseUrl + "/cajeros/" + id,
                    CajeroDto.class
            );
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Get a cashier by its code.
     *
     * @param codigo the code of the cashier to get
     * @return the cashier if found, empty otherwise
     */
    public Optional<CajeroDto> obtenerCajeroPorCodigo(String codigo) {
        try {
            ResponseEntity<CajeroDto> response = restTemplate.getForEntity(
                    apiBaseUrl + "/cajeros/codigo/" + codigo,
                    CajeroDto.class
            );
            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Create a new cashier.
     *
     * @param cajeroDto the cashier to create
     * @return the created cashier
     */
    public CajeroDto crearCajero(CajeroDto cajeroDto) {
        return restTemplate.postForObject(
                apiBaseUrl + "/cajeros",
                cajeroDto,
                CajeroDto.class
        );
    }

    /**
     * Update an existing cashier.
     *
     * @param id the ID of the cashier to update
     * @param cajeroDto the updated cashier data
     */
    public void actualizarCajero(Long id, CajeroDto cajeroDto) {
        restTemplate.put(
                apiBaseUrl + "/cajeros/" + id,
                cajeroDto
        );
    }

    /**
     * Delete a cashier.
     *
     * @param id the ID of the cashier to delete
     */
    public void eliminarCajero(Long id) {
        restTemplate.delete(apiBaseUrl + "/cajeros/" + id);
    }

    /**
     * Get cashiers by shift.
     *
     * @param turno the shift to search for
     * @return a list of cashiers in the shift
     */
    public List<CajeroDto> obtenerCajerosPorTurno(String turno) {
        try {
            ResponseEntity<List<CajeroDto>> response = restTemplate.exchange(
                    apiBaseUrl + "/cajeros/turno/" + turno,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<CajeroDto>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}