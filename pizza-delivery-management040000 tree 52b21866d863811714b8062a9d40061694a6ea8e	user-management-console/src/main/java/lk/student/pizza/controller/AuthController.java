
package lk.student.pizza.controller;

import lk.student.pizza.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @GetMapping("/login")
    public String login(
            @RequestParam(required = false) String next,
            Model model) {

        model.addAttribute("next", next == null ? "" : next);

        return "login";
    }

    @PostMapping("/login")
    public String doLogin(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam(defaultValue = "CUSTOMER") String accountType,
            @RequestParam(required = false) String next,
            HttpSession session,
            Model model) {

        var account = auth.authenticate(email, password);

        if (account.isEmpty() ||
                !account.get().get("role").equals(accountType)) {

            model.addAttribute("error",
                    "Invalid credentials or account type.");

            model.addAttribute("next", next == null ? "" : next);

            return "login";
        }

        session.setAttribute("user", account.get());

        if ("ADMIN".equals(account.get().get("role"))) {
            return "redirect:/menu";
        }

        return "redirect:" +
                (next != null && next.startsWith("/") && !next.startsWith("//")
                        ? next
                        : "/menu");
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String address,
            @RequestParam String password,
            Model model) {

        if (!auth.register(name, email, phone, address, password)) {

            model.addAttribute("error",
                    "That email is already registered, or the password is too short (minimum 6 characters).");

            return "register";
        }

        model.addAttribute("message",
                "Account created. Please sign in.");

        return "login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }
}
