
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
    public String editForm(
            @PathVariable String id,
            Model model) {

        Pizza p = (Pizza) service.findById(id).orElseThrow();

        model.addAttribute("pizza", p);

        return "pizza-form";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute Pizza pizza,
            RedirectAttributes ra) {

        if (pizza.getName() == null
                || pizza.getName().isBlank()
                || pizza.getPrice() <= 0) {

            ra.addFlashAttribute(
                    "error",
                    "Enter a name and a price greater than zero."
            );

            return "redirect:/admin/pizzas";
        }

        service.save(pizza);

        ra.addFlashAttribute(
                "message",
                "Pizza saved successfully."
        );

        return "redirect:/admin/pizzas";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id) {

        service.delete(id);

        return "redirect:/admin/pizzas";
    }
}

