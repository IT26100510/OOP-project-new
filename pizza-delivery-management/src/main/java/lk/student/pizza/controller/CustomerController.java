
package lk.student.pizza.controller;

import lk.student.pizza.model.Customer;
import lk.student.pizza.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;
    private final lk.student.pizza.usermanagement.UserService userService;

    public CustomerController(CustomerService service, lk.student.pizza.usermanagement.UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public String list(
            @RequestParam(required = false) String q,
            Model model) {

        var customers = service.findAll();

        if (q != null && !q.isBlank()) {

            customers = customers.stream()
                    .filter(c ->
                            c.getName().toLowerCase().contains(q.toLowerCase())
                                    || c.getEmail().toLowerCase().contains(q.toLowerCase())
                    )
                    .toList();
        }

        model.addAttribute("customers", customers);
        model.addAttribute("q", q);

        return "customers";
    }

    @GetMapping("/new")
    public String form(Model model) {

        model.addAttribute("customer", new Customer());

        return "customer-form";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable String id,
            Model model) {

        model.addAttribute(
                "customer",
                service.findById(id).orElseThrow()
        );

        return "customer-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Customer customer, org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        try {
            if (customer.getId() != null && !customer.getId().isBlank()) {
                var oldProfile = service.findById(customer.getId());
                if (oldProfile.isPresent()) {
                    try {
                        var account = userService.findUser(oldProfile.get().getEmail());
                        if (account instanceof lk.student.pizza.usermanagement.Customer) {
                            userService.updateName(account.getId(), customer.getName());
                            userService.updateEmail(account.getId(), customer.getEmail());
                            userService.updateCustomerContact(account.getId(), customer.getPhone(), customer.getAddress());
                        }
                    } catch (lk.student.pizza.usermanagement.ValidationException ignored) { }
                }
            }
            service.save(customer);
            ra.addFlashAttribute("message", "Customer profile saved.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/customers";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id, org.springframework.web.servlet.mvc.support.RedirectAttributes ra) {
        try {
            var profile = service.findById(id);
            if (profile.isPresent()) {
                try {
                    var account = userService.findUser(profile.get().getEmail());
                    if (account instanceof lk.student.pizza.usermanagement.Customer) userService.deleteUser(account.getId());
                } catch (lk.student.pizza.usermanagement.ValidationException ignored) { }
            }
            service.delete(id);
            ra.addFlashAttribute("message", "Customer profile deleted.");
        } catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/customers";
    }

}
