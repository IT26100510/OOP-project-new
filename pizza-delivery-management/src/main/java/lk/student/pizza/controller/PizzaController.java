package lk.student.pizza.controller;

import lk.student.pizza.model.Pizza;
import lk.student.pizza.service.PizzaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/pizzas")
public class PizzaController {

    private final PizzaService service;

    public PizzaController(PizzaService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pizzas", service.findAll());
        return "pizzas";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("pizza", new Pizza());
        return "pizza-form";
    }

    @GetMapping("/edit/{id}")
    public String
