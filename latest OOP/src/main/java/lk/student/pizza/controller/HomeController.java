
package lk.student.pizza.controller;

import lk.student.pizza.service.PizzaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final PizzaService pizzas;

    public HomeController(PizzaService pizzas) {
        this.pizzas = pizzas;
    }

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("pizzas", pizzas.findAll());

        return "index";
    }
}
