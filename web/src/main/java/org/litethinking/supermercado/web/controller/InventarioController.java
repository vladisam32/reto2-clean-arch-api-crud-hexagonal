package org.litethinking.supermercado.web.controller;

import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;
import org.litethinking.supermercado.shareddto.supermercado.inventario.InventarioDto;
import org.litethinking.supermercado.web.service.InventarioService;
import org.litethinking.supermercado.web.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Controller for inventory operations in the web interface.
 */
@Controller
@RequestMapping("/inventario")
public class InventarioController {

    private final InventarioService inventarioService;
    private final ProductoService productoService;

    @Autowired
    public InventarioController(InventarioService inventarioService, ProductoService productoService) {
        this.inventarioService = inventarioService;
        this.productoService = productoService;
    }

    /**
     * Display the list of all inventory items.
     *
     * @param model the model to add attributes to
     * @return the name of the view to render
     */
    @GetMapping
    public String listarInventario(Model model) {
        List<InventarioDto> inventario = inventarioService.obtenerTodoElInventario();
        model.addAttribute("inventario", inventario);
        return "inventario/lista";
    }

    /**
     * Display the form to create a new inventory item.
     *
     * @param model the model to add attributes to
     * @return the name of the view to render
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoInventario(Model model) {
        // Get products that don't have inventory yet
        List<ProductoDto> productosSinInventario = inventarioService.obtenerProductosSinInventario();
        
        if (productosSinInventario.isEmpty()) {
            model.addAttribute("error", "No hay productos disponibles para agregar al inventario.");
            return "redirect:/inventario";
        }
        
        model.addAttribute("productosSinInventario", productosSinInventario);
        model.addAttribute("inventarioForm", new InventarioForm());
        return "inventario/formulario";
    }

    /**
     * Process the form submission to create a new inventory item.
     *
     * @param inventarioForm the inventory form data
     * @param redirectAttributes attributes to add to the redirect
     * @return a redirect to the inventory list
     */
    @PostMapping
    public String crearInventario(@ModelAttribute InventarioForm inventarioForm, RedirectAttributes redirectAttributes) {
        try {
            // Get the product
            Optional<ProductoDto> producto = productoService.obtenerProductoPorId(inventarioForm.getProductoId());
            
            if (producto.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Producto no encontrado.");
                return "redirect:/inventario/nuevo";
            }
            
            // Create the inventory item
            InventarioDto inventarioDto = new InventarioDto(
                    null, // ID will be assigned by the server
                    producto.get(),
                    inventarioForm.getCantidad(),
                    inventarioForm.getStockMinimo(),
                    inventarioForm.getStockMaximo(),
                    LocalDate.now(),
                    inventarioForm.getUbicacion()
            );
            
            InventarioDto nuevoInventario = inventarioService.crearInventario(inventarioDto);
            redirectAttributes.addFlashAttribute("mensaje", "Inventario creado exitosamente con ID: " + nuevoInventario.id());
            return "redirect:/inventario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el inventario: " + e.getMessage());
            return "redirect:/inventario/nuevo";
        }
    }

    /**
     * Display the form to edit an existing inventory item.
     *
     * @param id the ID of the inventory item to edit
     * @param model the model to add attributes to
     * @param redirectAttributes attributes to add to the redirect
     * @return the name of the view to render
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarInventario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<InventarioDto> inventario = inventarioService.obtenerInventarioPorId(id);
        
        if (inventario.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Inventario no encontrado con ID: " + id);
            return "redirect:/inventario";
        }
        
        InventarioDto inventarioDto = inventario.get();
        InventarioForm inventarioForm = new InventarioForm();
        inventarioForm.setProductoId(inventarioDto.producto().id());
        inventarioForm.setCantidad(inventarioDto.cantidad());
        inventarioForm.setStockMinimo(inventarioDto.stockMinimo());
        inventarioForm.setStockMaximo(inventarioDto.stockMaximo());
        inventarioForm.setUbicacion(inventarioDto.ubicacion());
        
        model.addAttribute("inventarioForm", inventarioForm);
        model.addAttribute("inventario", inventarioDto);
        model.addAttribute("editar", true);
        return "inventario/formulario";
    }

    /**
     * Process the form submission to update an existing inventory item.
     *
     * @param id the ID of the inventory item to update
     * @param inventarioForm the updated inventory form data
     * @param redirectAttributes attributes to add to the redirect
     * @return a redirect to the inventory list
     */
    @PostMapping("/{id}")
    public String actualizarInventario(@PathVariable Long id, @ModelAttribute InventarioForm inventarioForm, RedirectAttributes redirectAttributes) {
        try {
            Optional<InventarioDto> inventarioExistente = inventarioService.obtenerInventarioPorId(id);
            
            if (inventarioExistente.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Inventario no encontrado con ID: " + id);
                return "redirect:/inventario";
            }
            
            InventarioDto inventarioDto = inventarioExistente.get();
            
            // Update the inventory item
            InventarioDto inventarioActualizado = new InventarioDto(
                    id,
                    inventarioDto.producto(),
                    inventarioForm.getCantidad(),
                    inventarioForm.getStockMinimo(),
                    inventarioForm.getStockMaximo(),
                    LocalDate.now(), // Update the restock date
                    inventarioForm.getUbicacion()
            );
            
            inventarioService.actualizarInventario(id, inventarioActualizado);
            redirectAttributes.addFlashAttribute("mensaje", "Inventario actualizado exitosamente");
            return "redirect:/inventario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el inventario: " + e.getMessage());
            return "redirect:/inventario/editar/" + id;
        }
    }

    /**
     * Delete an inventory item.
     *
     * @param id the ID of the inventory item to delete
     * @param redirectAttributes attributes to add to the redirect
     * @return a redirect to the inventory list
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarInventario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            inventarioService.eliminarInventario(id);
            redirectAttributes.addFlashAttribute("mensaje", "Inventario eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el inventario: " + e.getMessage());
        }
        return "redirect:/inventario";
    }

    /**
     * Display the details of an inventory item.
     *
     * @param id the ID of the inventory item to display
     * @param model the model to add attributes to
     * @param redirectAttributes attributes to add to the redirect
     * @return the name of the view to render
     */
    @GetMapping("/{id}")
    public String verDetallesInventario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<InventarioDto> inventario = inventarioService.obtenerInventarioPorId(id);
        
        if (inventario.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Inventario no encontrado con ID: " + id);
            return "redirect:/inventario";
        }
        
        model.addAttribute("inventario", inventario.get());
        return "inventario/detalles";
    }

    /**
     * Form object for inventory creation and updates.
     */
    public static class InventarioForm {
        private Long productoId;
        private Integer cantidad;
        private Integer stockMinimo;
        private Integer stockMaximo;
        private String ubicacion;

        public Long getProductoId() {
            return productoId;
        }

        public void setProductoId(Long productoId) {
            this.productoId = productoId;
        }

        public Integer getCantidad() {
            return cantidad;
        }

        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }

        public Integer getStockMinimo() {
            return stockMinimo;
        }

        public void setStockMinimo(Integer stockMinimo) {
            this.stockMinimo = stockMinimo;
        }

        public Integer getStockMaximo() {
            return stockMaximo;
        }

        public void setStockMaximo(Integer stockMaximo) {
            this.stockMaximo = stockMaximo;
        }

        public String getUbicacion() {
            return ubicacion;
        }

        public void setUbicacion(String ubicacion) {
            this.ubicacion = ubicacion;
        }
    }
}