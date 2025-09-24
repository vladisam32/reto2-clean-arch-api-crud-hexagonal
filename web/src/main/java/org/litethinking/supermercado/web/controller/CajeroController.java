package org.litethinking.supermercado.web.controller;

import org.litethinking.supermercado.shareddto.supermercado.CajeroDto;
import org.litethinking.supermercado.web.service.CajeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

/**
 * Controller for cashier operations in the web interface.
 */
@Controller
@RequestMapping("/cajeros")
public class CajeroController {

    private final CajeroService cajeroService;

    @Autowired
    public CajeroController(CajeroService cajeroService) {
        this.cajeroService = cajeroService;
    }

    /**
     * Display the list of all cashiers.
     *
     * @param model the model to add attributes to
     * @return the name of the view to render
     */
    @GetMapping
    public String listarCajeros(Model model) {
        List<CajeroDto> cajeros = cajeroService.obtenerTodosLosCajeros();
        model.addAttribute("cajeros", cajeros);
        return "cajeros/lista";
    }

    /**
     * Display the form to create a new cashier.
     *
     * @param model the model to add attributes to
     * @return the name of the view to render
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoCajero(Model model) {
        model.addAttribute("cajero", new CajeroDto(null, "", "", ""));
        return "cajeros/formulario";
    }

    /**
     * Process the form submission to create a new cashier.
     *
     * @param cajeroDto the cashier data from the form
     * @param redirectAttributes attributes to add to the redirect
     * @return a redirect to the cashier list
     */
    @PostMapping
    public String crearCajero(@ModelAttribute CajeroDto cajeroDto, RedirectAttributes redirectAttributes) {
        try {
            CajeroDto nuevoCajero = cajeroService.crearCajero(cajeroDto);
            redirectAttributes.addFlashAttribute("mensaje", "Cajero creado exitosamente con ID: " + nuevoCajero.id());
            return "redirect:/cajeros";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el cajero: " + e.getMessage());
            return "redirect:/cajeros/nuevo";
        }
    }

    /**
     * Display the form to edit an existing cashier.
     *
     * @param id the ID of the cashier to edit
     * @param model the model to add attributes to
     * @param redirectAttributes attributes to add to the redirect
     * @return the name of the view to render
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarCajero(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<CajeroDto> cajero = cajeroService.obtenerCajeroPorId(id);
        if (cajero.isPresent()) {
            model.addAttribute("cajero", cajero.get());
            model.addAttribute("editar", true);
            return "cajeros/formulario";
        } else {
            redirectAttributes.addFlashAttribute("error", "Cajero no encontrado con ID: " + id);
            return "redirect:/cajeros";
        }
    }

    /**
     * Process the form submission to update an existing cashier.
     *
     * @param id the ID of the cashier to update
     * @param cajeroDto the updated cashier data from the form
     * @param redirectAttributes attributes to add to the redirect
     * @return a redirect to the cashier list
     */
    @PostMapping("/{id}")
    public String actualizarCajero(@PathVariable Long id, @ModelAttribute CajeroDto cajeroDto, RedirectAttributes redirectAttributes) {
        try {
            cajeroService.actualizarCajero(id, cajeroDto);
            redirectAttributes.addFlashAttribute("mensaje", "Cajero actualizado exitosamente");
            return "redirect:/cajeros";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el cajero: " + e.getMessage());
            return "redirect:/cajeros/editar/" + id;
        }
    }

    /**
     * Delete a cashier.
     *
     * @param id the ID of the cashier to delete
     * @param redirectAttributes attributes to add to the redirect
     * @return a redirect to the cashier list
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarCajero(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cajeroService.eliminarCajero(id);
            redirectAttributes.addFlashAttribute("mensaje", "Cajero eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el cajero: " + e.getMessage());
        }
        return "redirect:/cajeros";
    }

    /**
     * Display the details of a cashier.
     *
     * @param id the ID of the cashier to display
     * @param model the model to add attributes to
     * @param redirectAttributes attributes to add to the redirect
     * @return the name of the view to render
     */
    @GetMapping("/{id}")
    public String verDetallesCajero(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<CajeroDto> cajero = cajeroService.obtenerCajeroPorId(id);
        if (cajero.isPresent()) {
            model.addAttribute("cajero", cajero.get());
            return "cajeros/detalles";
        } else {
            redirectAttributes.addFlashAttribute("error", "Cajero no encontrado con ID: " + id);
            return "redirect:/cajeros";
        }
    }

    /**
     * Display the login form for cashiers.
     *
     * @param model the model to add attributes to
     * @return the name of the view to render
     */
    @GetMapping("/login")
    public String mostrarFormularioLogin(Model model) {
        model.addAttribute("codigo", "");
        return "cajeros/login";
    }

    /**
     * Process the login form submission.
     *
     * @param codigo the cashier code from the form
     * @param model the model to add attributes to
     * @param redirectAttributes attributes to add to the redirect
     * @return a redirect to the appropriate page
     */
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String codigo, Model model, RedirectAttributes redirectAttributes) {
        Optional<CajeroDto> cajero = cajeroService.obtenerCajeroPorCodigo(codigo);
        if (cajero.isPresent()) {
            // In a real application, you would store the logged-in cashier in the session
            // For simplicity, we'll just redirect to the cashier details page
            redirectAttributes.addFlashAttribute("mensaje", "Inicio de sesión exitoso como: " + cajero.get().nombre());
            return "redirect:/cajeros/" + cajero.get().id();
        } else {
            redirectAttributes.addFlashAttribute("error", "Código de cajero no válido");
            return "redirect:/cajeros/login";
        }
    }
}