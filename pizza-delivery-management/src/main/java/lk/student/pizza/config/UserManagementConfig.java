
package lk.student.pizza.config;

import lk.student.pizza.usermanagement.UserRepository;
import lk.student.pizza.usermanagement.UserService;
import lk.student.pizza.usermanagement.ValidationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserManagementConfig {

    @Bean
    public UserService userService(UserRepository repository) {
        return new UserService(repository);
    }

    @Bean
    public String seedDefaultAdmin(UserService users,
                                   UserRepository repository) throws Exception {
        if (repository.findAll().isEmpty()) {
            try {
                users.registerAdmin(
                        "Administrator",
                        "admin@pizzahouse.lk",
                        "Admin@123",
                        "ADM001"
                );
            } catch (ValidationException e) {
                throw new IllegalStateException(
                        "Could not create the default administrator.", e
                );
            }
        }
        return "user-management-ready";
    }
}
