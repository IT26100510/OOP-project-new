
package lk.student.pizza.controller;

import lk.student.pizza.model.CustomerOrder;
import lk.student.pizza.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orders;
    private final PizzaService pizzas;
    private final CustomerService customers;

    public OrderController(
            OrderService orders,
            PizzaService pizzas,
            CustomerService customers) {

        this.orders = orders;
        this.pizzas = pizzas;
        this.customers = customers;
    }

    @GetMapping
    public String list(Model model, HttpSession session) {

        @SuppressWarnings("unchecked")
        Map<String, String> user = (Map<String, String>) session.getAttribute("user");

        boolean admin = "ADMIN".equals(user.get("role"));
        if (admin) {
            model.addAttribute("orders", orders.findAll());
        } else {
            var customer = customers.findByEmail(user.get("email"));
            model.addAttribute("orders", customer
                    .map(c -> orders.findByCustomerId(c.getId()))
                    .orElseGet(java.util.List::of));
        }
        model.addAttribute("isAdmin", admin);

        return "orders";
    }

    @GetMapping("/new")
    public String form(
            @RequestParam(required = false) String pizzaId,
            Model model,
            HttpSession session) {

        @SuppressWarnings("unchecked")
        Map<String, String> user =
                (Map<String, String>) session.getAttribute("user");

        var customer = customers.findByEmail(user.get("email"));

        if (customer.isEmpty()) {
            return "redirect:/register";
        }

        var selected = pizzaId == null
                ? java.util.Optional.<lk.student.pizza.model.MenuItem>empty()
                : pizzas.findById(pizzaId);

        model.addAttribute("order", new CustomerOrder());
        model.addAttribute("pizzas", pizzas.findAll());
        model.addAttribute("customer", customer.get());
        model.addAttribute("selectedPizza", selected.orElse(null));

        return "order-form";
    }

    @PostMapping("/place")
    public String place(
            @ModelAttribute CustomerOrder order,
            RedirectAttributes ra) {

        var pizza = pizzas.findById(order.getPizzaName());
        var customer = customers.findById(order.getCustomerId());

        if (pizza.isEmpty() || customer.isEmpty()
                || order.getQuantity() < 1) {

            ra.addFlashAttribute(
                    "error",
                    "Choose a customer, a pizza and a quantity."
            );

            return "redirect:/orders/new";
        }

        order.setPizzaName(pizza.get().getName());
        order.setCustomerName(customer.get().getName());

        order.setTotal(
                pizza.get().getPrice() * order.getQuantity()
        );

        orders.place(order);

        ra.addFlashAttribute(
                "message",
                "Payment successful and order placed! Order reference: "
                        + order.getId()
        );

        return "redirect:/menu";
    }

    @PostMapping("/status/{id}")
    public String status(
            @PathVariable String id,
            @RequestParam String status) {

        orders.updateStatus(id, status);

        return "redirect:/orders";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id) {

        orders.delete(id);

        return "redirect:/orders";
    }
}
