package lk.student.pizza.service;

import lk.student.pizza.model.Customer;
import lk.student.pizza.usermanagement.User;
import lk.student.pizza.usermanagement.UserRepository;
import lk.student.pizza.usermanagement.UserService;
import lk.student.pizza.usermanagement.ValidationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository users;
    private final UserService userService;
    private final CustomerService customers;

    public AuthService(UserRepository users, UserService userService, CustomerService customers) {
        this.users = users;
        this.userService = userService;
        this.customers = customers;
    }

    public Optional<Map<String, String>> authenticate(String email, String password) {
        if (email == null || password == null) return Optional.empty();

        return users.findByEmail(email.trim().toLowerCase(Locale.ROOT))
                .filter(user -> user.verifyPassword(password))
                .map(this::toSessionUser);
    }

    public boolean register(String name, String email, String phone, String address, String password) {
        try {
            lk.student.pizza.usermanagement.Customer account = userService.registerCustomer(name, email, password, phone, address);
            // Keep the website's existing customer-management page in sync.
            customers.save(new Customer(
                    "C" + String.format("%03d", account.getId()),
                    account.getName(),
                    account.getEmail(),
                    account.getPhone(),
                    account.getAddress()
            ));
            return true;
        } catch (ValidationException | IOException | RuntimeException e) {
            return false;
        }
    }

    private Map<String, String> toSessionUser(User user) {
        Map<String, String> sessionUser = new LinkedHashMap<>();
        sessionUser.put("id", String.valueOf(user.getId()));
        sessionUser.put("role", user.getRole());
        sessionUser.put("name", user.getName());
        sessionUser.put("email", user.getEmail());

        if (user instanceof lk.student.pizza.usermanagement.Customer customer) {
            sessionUser.put("phone", customer.getPhone());
            sessionUser.put("address", customer.getAddress());
        }
        return Map.copyOf(sessionUser);
    }
}
