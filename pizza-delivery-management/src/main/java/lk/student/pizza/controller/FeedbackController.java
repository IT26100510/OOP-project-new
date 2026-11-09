package lk.student.pizza.controller;

import feedback.*;
import jakarta.servlet.http.HttpSession;
import lk.student.pizza.service.CustomerService;
import lk.student.pizza.service.OrderService;
import lk.student.pizza.service.PizzaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackManager feedback;
    private final PizzaService pizzas;
    private final OrderService orders;
    private final CustomerService customers;

    public FeedbackController(FeedbackManager feedback,
                              PizzaService pizzas,
                              OrderService orders,
                              CustomerService customers) {
        this.feedback = feedback;
        this.pizzas = pizzas;
        this.orders = orders;
        this.customers = customers;
    }

    @GetMapping
    public String list(Model model, HttpSession session) {
        model.addAttribute("feedback", feedback.getAll());
        model.addAttribute("isAdmin", isAdmin(session));
        model.addAttribute("pizzas", pizzas.findAll());
        model.addAttribute("orders", customerOrders(session));
        model.addAttribute("currentUserId",
                Integer.parseInt(currentUser(session).get("id")));

        return "feedback";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model,
                       HttpSession session, RedirectAttributes ra) {

        Feedback item = feedback.findById(id);

        if (item == null || (!isAdmin(session) && !isOwner(item, session))) {
            ra.addFlashAttribute("error",
                    "Review not found or access denied.");
            return "redirect:/feedback";
        }

        model.addAttribute("item", item);
        return "feedback-edit";
    }

    @PostMapping("/create")
    public String create(
            @RequestParam String reviewType,
            @RequestParam int rating,
            @RequestParam String comment,
            @RequestParam(required = false) String pizzaId,
            @RequestParam(required = false) String orderId,
            HttpSession session,
            RedirectAttributes ra) {

        try {
            int customerId =
                    Integer.parseInt(currentUser(session).get("id"));

            if (rating < 1 || rating > 5 ||
                    comment == null || comment.isBlank()) {
                throw new IllegalArgumentException(
                        "Choose a rating and enter a comment.");
            }

            if ("PIZZA".equals(reviewType)) {

                var pizza = pizzas.findById(pizzaId).orElseThrow();

                feedback.add(new PizzaReview(
                        customerId,
                        numericId(pizza.getId()),
                        pizza.getName(),
                        rating,
                        comment
                ));

            } else if ("DELIVERY".equals(reviewType)) {

                var order = orders.findById(orderId).orElseThrow();

                String profileId = customers
                        .findByEmail(currentUser(session).get("email"))
                        .orElseThrow().getId();

                if (!order.getCustomerId().equals(profileId)) {
                    throw new IllegalArgumentException(
                            "You can review only your own order.");
                }

                feedback.add(new DeliveryReview(
                        customerId,
                        numericId(order.getId()),
                        "Delivery team",
                        rating,
                        comment
                ));

            } else {
                throw new IllegalArgumentException(
                        "Select a review type.");
            }

            ra.addFlashAttribute("message", "Review submitted.");

        } catch (Exception e) {
            ra.addFlashAttribute("error",
                    e.getMessage() == null
                            ? "Could not submit review."
                            : e.getMessage());
        }

        return "redirect:/feedback";
    }

    @PostMapping("/update/{id}")
    public String update(
            @PathVariable int id,
            @RequestParam int rating,
            @RequestParam String comment,
            HttpSession session,
            RedirectAttributes ra) {

        Feedback item = feedback.findById(id);

        if (item == null ||
                (!isAdmin(session) && !isOwner(item, session))) {

            ra.addFlashAttribute("error",
                    "Review not found or access denied.");

        } else {
            try {
                feedback.update(id, rating, comment);
                ra.addFlashAttribute("message", "Review updated.");

            } catch (IllegalArgumentException e) {
                ra.addFlashAttribute("error", e.getMessage());
            }
        }

        return "redirect:/feedback";
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable int id,
            HttpSession session,
            RedirectAttributes ra) {

        Feedback item = feedback.findById(id);

        if (item == null ||
                (!isAdmin(session) && !isOwner(item, session))) {

            ra.addFlashAttribute("error",
                    "Review not found or access denied.");

        } else {
            feedback.delete(id);
            ra.addFlashAttribute("message", "Review deleted.");
        }

        return "redirect:/feedback";
    }

    @PostMapping("/reply/{id}")
    public String reply(
            @PathVariable int id,
            @RequestParam String reply,
            RedirectAttributes ra) {

        if (reply == null || reply.isBlank()
                || !feedback.replyToFeedback(id, reply)) {

            ra.addFlashAttribute("error",
                    "Enter a reply for an existing review.");

        } else {
            ra.addFlashAttribute("message", "Admin reply saved.");
        }

        return "redirect:/feedback";
    }

    private boolean isOwner(Feedback f, HttpSession session) {
        return String.valueOf(f.getCustomerId())
                .equals(currentUser(session).get("id"));
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(currentUser(session).get("role"));
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> currentUser(HttpSession s) {
        return (Map<String, String>) s.getAttribute("user");
    }

    private java.util.List<lk.student.pizza.model.CustomerOrder>
    customerOrders(HttpSession session) {

        String email = currentUser(session).get("email");

        return customers.findByEmail(email)
                .map(c -> orders.findByCustomerId(c.getId()))
                .orElseGet(java.util.List::of);
    }

    private int numericId(String id) {
        return Integer.parseInt(id.replaceAll("\\D", ""));
    }
}