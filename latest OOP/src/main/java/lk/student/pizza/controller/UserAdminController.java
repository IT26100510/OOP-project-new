package lk.student.pizza.controller;

import jakarta.servlet.http.HttpSession;
import lk.student.pizza.usermanagement.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserAdminController {
    private final UserService users;
    private final lk.student.pizza.service.CustomerService customerProfiles;
    public UserAdminController(UserService users, lk.student.pizza.service.CustomerService customerProfiles) {
        this.users = users; this.customerProfiles = customerProfiles;
    }

    @GetMapping
    public String list(Model model) { model.addAttribute("users", users.getAllUsers()); return "users"; }
    @GetMapping("/new")
    public String createForm(Model model) { model.addAttribute("account", new AccountForm()); return "user-form"; }
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable int id, Model model) throws ValidationException {
        User u = users.findUser(id);
        AccountForm form = new AccountForm();
        form.setId(id); form.setName(u.getName()); form.setEmail(u.getEmail()); form.setRole(u.getRole());
        if (u instanceof Customer c) { form.setPhone(c.getPhone()); form.setAddress(c.getAddress()); }
        if (u instanceof Admin a) form.setStaffCode(a.getStaffCode());
        model.addAttribute("account", form);
        return "user-form";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute AccountForm form, RedirectAttributes ra) {
        try {
            if (form.getId() == null) {
                if ("ADMIN".equals(form.getRole())) users.registerAdmin(form.getName(), form.getEmail(), form.getPassword(), form.getStaffCode());
                else {
                    Customer c = users.registerCustomer(form.getName(), form.getEmail(), form.getPassword(), form.getPhone(), form.getAddress());
                    String profileId = customerProfiles.findByEmail(c.getEmail()).map(lk.student.pizza.model.Customer::getId)
                            .orElse("C" + String.format("%03d", c.getId()));
                    customerProfiles.save(new lk.student.pizza.model.Customer(profileId, c.getName(), c.getEmail(), c.getPhone(), c.getAddress()));
                }
            } else {
                User existingAccount = users.findUser(form.getId());
                var profile = customerProfiles.findByEmail(existingAccount.getEmail());
                users.updateName(form.getId(), form.getName());
                users.updateEmail(form.getId(), form.getEmail());
                if ("CUSTOMER".equals(form.getRole())) {
                    users.updateCustomerContact(form.getId(), form.getPhone(), form.getAddress());
                    String profileId = profile.map(lk.student.pizza.model.Customer::getId)
                            .orElse("C" + String.format("%03d", form.getId()));
                    customerProfiles.save(new lk.student.pizza.model.Customer(profileId, form.getName(), form.getEmail(), form.getPhone(), form.getAddress()));
                }
                if (form.getRole().equals("ADMIN")) users.updateAdminStaffCode(form.getId(), form.getStaffCode());
                if (form.getPassword() != null && !form.getPassword().isBlank()) users.setPasswordByAdmin(form.getId(), form.getPassword());
            }
            ra.addFlashAttribute("message", "Account saved.");
        } catch (ValidationException | java.io.IOException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/users";
        }
        return "redirect:/users";
    }
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id, HttpSession session, RedirectAttributes ra) {
        try {
            @SuppressWarnings("unchecked") var current = (java.util.Map<String, String>) session.getAttribute("user");
            if (current != null && String.valueOf(id).equals(current.get("id"))) {
                ra.addFlashAttribute("error", "You cannot delete the account you are currently using.");
            } else {
                User account = users.findUser(id);
                if (account instanceof Customer) {
                    customerProfiles.findAll().stream().filter(c -> c.getEmail().equalsIgnoreCase(account.getEmail())).findFirst().ifPresent(c -> customerProfiles.delete(c.getId()));
                }
                users.deleteUser(id);
                ra.addFlashAttribute("message", "Account deleted.");
            }
        } catch (ValidationException | java.io.IOException e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/users";
    }

    public static class AccountForm {
        private Integer id; private String name=""; private String email=""; private String password="";
        private String role="CUSTOMER"; private String phone=""; private String address=""; private String staffCode="";
        public Integer getId(){return id;} public void setId(Integer v){id=v;}
        public String getName(){return name;} public void setName(String v){name=v;}
        public String getEmail(){return email;} public void setEmail(String v){email=v;}
        public String getPassword(){return password;} public void setPassword(String v){password=v;}
        public String getRole(){return role;} public void setRole(String v){role=v;}
        public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
        public String getAddress(){return address;} public void setAddress(String v){address=v;}
        public String getStaffCode(){return staffCode;} public void setStaffCode(String v){staffCode=v;}
    }
}
