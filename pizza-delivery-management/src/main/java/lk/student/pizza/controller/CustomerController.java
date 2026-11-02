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
    public CustomerController(CustomerService service){this.service=service;}
    @GetMapping public String list(@RequestParam(required=false) String q,Model model){
        var customers=service.findAll();
        if(q!=null&&!q.isBlank()) customers=customers.stream().filter(c->c.getName().toLowerCase().contains(q.toLowerCase())||c.getEmail().toLowerCase().contains(q.toLowerCase())).toList();
        model.addAttribute("customers",customers);model.addAttribute("q",q);return "customers";
    }
    @GetMapping("/new") public String form(Model model){model.addAttribute("customer",new Customer());return "customer-form";}
    @GetMapping("/edit/{id}") public String edit(@PathVariable String id,Model model){model.addAttribute("customer",service.findById(id).orElseThrow());return "customer-form";}
    @PostMapping("/save") public String save(@ModelAttribute Customer customer){service.save(customer);return "redirect:/customers";}
    @PostMapping("/delete/{id}") public String delete(@PathVariable String id){service.delete(id);return "redirect:/customers";}
}
