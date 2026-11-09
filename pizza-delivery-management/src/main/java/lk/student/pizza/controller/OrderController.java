
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
    private final lk.student.pizza.service.DeliveryService deliveries;

    public OrderController(OrderService orders, PizzaService pizzas, CustomerService customers,
                           lk.student.pizza.service.DeliveryService deliveries) {
        this.orders = orders;
        this.pizzas = pizzas;
        this.customers = customers;
        this.deliveries = deliveries;
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
            HttpSession session,
            RedirectAttributes ra) {

        @SuppressWarnings("unchecked")
        Map<String, String> user = (Map<String, String>) session.getAttribute("user");
        var customer = customers.findByEmail(user.get("email"));
        if (customer.isPresent()) order.setCustomerId(customer.get().getId());
        var pizza = pizzas.findById(order.getPizzaName());

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

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        var order = orders.findById(id).orElseThrow();
        model.addAttribute("order", order);
        model.addAttribute("pizzas", pizzas.findAll());
        return "order-edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable String id, @ModelAttribute CustomerOrder form, RedirectAttributes ra) {
        var existing = orders.findById(id);
        var pizza = pizzas.findById(form.getPizzaName());
        if (existing.isEmpty() || pizza.isEmpty() || form.getQuantity() < 1) {
            ra.addFlashAttribute("error", "Choose a valid pizza and quantity.");
            return "redirect:/orders";
        }
        CustomerOrder order = existing.get();
        order.setPizzaName(pizza.get().getName());
        order.setTotal(pizza.get().getPrice() * form.getQuantity());
        order.setQuantity(form.getQuantity());
        orders.update(order);
        ra.addFlashAttribute("message", "Order updated.");
        return "redirect:/orders";
    }

    @PostMapping("/status/{id}")
    public String status(
            @PathVariable String id,
            @RequestParam String status) {

        if (!java.util.Set.of("Received", "Preparing", "Out for delivery", "Delivered", "Cancelled").contains(status)) {
            return "redirect:/orders";
        }
        orders.updateStatus(id, status);
        return "redirect:/orders";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id) {

        orders.delete(id);

        return "redirect:/orders";
    }
}
