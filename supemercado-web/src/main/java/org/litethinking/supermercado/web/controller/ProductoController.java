package org.litethinking.supermercado.web.controller;

import org.litethinking.supermercado.shareddto.supermercado.ProductoDto;
import org.litethinking.supermercado.web.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Controller for product operations in the web interface.
 */
@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * Display the list of all products.
     *
     * @param model the model to add attributes to
     * @return the name of the view to render
     */
    @GetMapping
    public String listarProductos(Model model) {
        List<ProductoDto> productos = productoService.obtenerTodosLosProductos();
        model.addAttribute("productos", productos);
        return "productos/lista";
    }

    /**
     * Display the form to create a new product.
     *
     * @param model the model to add attributes to
     * @return the name of the view to render
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoProducto(Model model) {
        model.addAttribute("producto", new ProductoDto(null, "", "", BigDecimal.ZERO, "", ""));
        return "productos/formulario";
    }

    /**
     * Process the form submission to create a new product.
     *
     * @param productoDto the product data from the form
     * @param redirectAttributes attributes to add to the redirect
     * @return a redirect to the product list
     */
    @PostMapping
    public String crearProducto(@ModelAttribute ProductoDto productoDto, RedirectAttributes redirectAttributes) {
        try {
            ProductoDto nuevoProducto = productoService.crearProducto(productoDto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto creado exitosamente con ID: " + nuevoProducto.id());
            return "redirect:/productos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el producto: " + e.getMessage());
            return "redirect:/productos/nuevo";
        }
    }

    /**
     * Display the form to edit an existing product.
     *
     * @param id the ID of the product to edit
     * @param model the model to add attributes to
     * @param redirectAttributes attributes to add to the redirect
     * @return the name of the view to render
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarProducto(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<ProductoDto> producto = productoService.obtenerProductoPorId(id);
        if (producto.isPresent()) {
            model.addAttribute("producto", producto.get());
            model.addAttribute("editar", true);
            return "productos/formulario";
        } else {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado con ID: " + id);
            return "redirect:/productos";
        }
    }

    /**
     * Process the form submission to update an existing product.
     *
     * @param id the ID of the product to update
     * @param productoDto the updated product data from the form
     * @param redirectAttributes attributes to add to the redirect
     * @return a redirect to the product list
     */
    @PostMapping("/{id}")
    public String actualizarProducto(@PathVariable Long id, @ModelAttribute ProductoDto productoDto, RedirectAttributes redirectAttributes) {
        try {
            productoService.actualizarProducto(id, productoDto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto actualizado exitosamente");
            return "redirect:/productos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el producto: " + e.getMessage());
            return "redirect:/productos/editar/" + id;
        }
    }

    /**
     * Delete a product.
     *
     * @param id the ID of the product to delete
     * @param redirectAttributes attributes to add to the redirect
     * @return a redirect to the product list
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productoService.eliminarProducto(id);
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el producto: " + e.getMessage());
        }
        return "redirect:/productos";
    }

    /**
     * Display the details of a product.
     *
     * @param id the ID of the product to display
     * @param model the model to add attributes to
     * @param redirectAttributes attributes to add to the redirect
     * @return the name of the view to render
     */
    @GetMapping("/{id}")
    public String verDetallesProducto(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<ProductoDto> producto = productoService.obtenerProductoPorId(id);
        if (producto.isPresent()) {
            model.addAttribute("producto", producto.get());
            return "productos/detalles";
        } else {
            redirectAttributes.addFlashAttribute("error", "Producto no encontrado con ID: " + id);
            return "redirect:/productos";
        }
    }
}