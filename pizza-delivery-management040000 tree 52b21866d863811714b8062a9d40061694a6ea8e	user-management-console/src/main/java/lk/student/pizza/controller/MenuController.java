
package lk.student.pizza.controller;

import lk.student.pizza.service.PizzaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    private final PizzaService pizzas;

    public MenuController(PizzaService pizzas) {
        this.pizzas = pizzas;
    }

    @GetMapping("/menu")
    public String menu(Model model) {

        model.addAttribute("pizzas", pizzas.findAll());

        return "pizzas";
    }
}
