package org.litethinking.supermercado.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the home page.
 */
@Controller
public class HomeController {

    /**
     * Display the home page.
     *
     * @param model the model to add attributes to
     * @return the name of the view to render
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("titulo", "Sistema de Gestión de Supermercado");
        return "home";
    }
}