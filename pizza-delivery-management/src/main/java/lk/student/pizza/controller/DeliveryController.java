package lk.student.pizza.controller;

import jakarta.servlet.http.HttpSession;
import lk.student.pizza.model.Delivery;
import lk.student.pizza.service.DeliveryService;
import lk.student.pizza.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/deliveries")
public class DeliveryController {
    private final DeliveryService deliveries;
    private final OrderService orders;
    public DeliveryController(DeliveryService deliveries, OrderService orders) {
        this.deliveries = deliveries;
        this.orders = orders;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("deliveries", deliveries.findAll());
        return "deliveries";
    }
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("delivery", new Delivery());
        model.addAttribute("orders", orders.findAll());
        return "delivery-form";
    }
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute("delivery", deliveries.findById(id).orElseThrow());
        model.addAttribute("orders", orders.findAll());
        return "delivery-form";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute Delivery delivery, RedirectAttributes ra) {
        var order = orders.findById(delivery.getOrderId());
        if (order.isEmpty() || blank(delivery.getRiderName()) || blank(delivery.getRiderPhone())
                || blank(delivery.getEta()) || !java.util.Set.of("Assigned", "Picked up", "In transit", "Delivered", "Cancelled").contains(delivery.getStatus())) {
            ra.addFlashAttribute("error", "Select an order and enter rider details, ETA, and a valid status.");
            return "redirect:/deliveries";
        }
        delivery.setCustomerName(order.get().getCustomerName());
        if (delivery.getNotes() == null) delivery.setNotes("");
        deliveries.save(delivery);
        ra.addFlashAttribute("message", "Delivery saved.");
        return "redirect:/deliveries";
    }
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        deliveries.delete(id);
        ra.addFlashAttribute("message", "Delivery assignment removed.");
        return "redirect:/deliveries";
    }
    private boolean blank(String value) { return value == null || value.isBlank(); }
}

