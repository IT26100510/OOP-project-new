package pizza.user;

import java.io.IOException;
import java.util.Scanner;

/** Console menu to demonstrate the User Management component. */
public class Main {
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            UserRepository repo = new FileUserRepository("users.txt");
            UserService userService = new UserService(repo);
            AuthService auth = new AuthService(repo);

            // First run: create a default admin so someone can log in.
            if (repo.findAll().isEmpty()) {
                userService.registerAdmin("Default Admin", "admin@pizza.com", "Admin1234", "ADM001");
                System.out.println("Default admin created: admin@pizza.com / Admin1234");
            }

            while (true) {
                if (!auth.isLoggedIn()) guestMenu(userService, auth);
                else userMenu(userService, auth);
            }
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        } catch (ValidationException e) {
            System.out.println("Setup error: " + e.getMessage());
        }
    }

    private static void guestMenu(UserService us, AuthService auth) {
        System.out.println("\n=== PIZZA SHOP - USER MANAGEMENT ===");
        System.out.println("1. Register  2. Login  0. Exit");
        switch (ask("Choose: ")) {
            case "1":
                try {
                    Customer c = us.registerCustomer(ask("Name: "), ask("Email: "), ask("Password: "),
                            ask("Phone (digits): "), ask("Address: "));
                    System.out.println("Registered! Your ID is " + c.getId());
                } catch (ValidationException | IOException e) { System.out.println("Error: " + e.getMessage()); }
                break;
            case "2":
                try {
                    User u = auth.login(ask("Email: "), ask("Password: "));
                    System.out.println("Welcome, " + u.getName() + " (" + u.getRole() + ")");
                } catch (InvalidCredentialsException e) { System.out.println("Error: " + e.getMessage()); }
                break;
            case "0":
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            default: System.out.println("Invalid option.");
        }
    }

    private static void userMenu(UserService us, AuthService auth) {
        User me = auth.getCurrentUser();
        System.out.println("\n--- Logged in as " + me.getName() + " [" + me.getRole() + "] ---");
        System.out.println("1. View my profile  2. Update name  3. Update email  4. Update phone/address (customer)");
        System.out.println("5. Change password  6. Delete my account");
        if (auth.isAdmin()) System.out.println("7. List all users  8. Create admin  9. Delete a user");
        System.out.println("0. Logout");
        try {
            switch (ask("Choose: ")) {
                case "1": System.out.println(me.displayProfile()); break; // polymorphism
                case "2": us.updateName(me.getId(), ask("New name: ")); System.out.println("Updated."); break;
                case "3": us.updateEmail(me.getId(), ask("New email: ")); System.out.println("Updated."); break;
                case "4": us.updateCustomerContact(me.getId(), ask("Phone: "), ask("Address: ")); System.out.println("Updated."); break;
                case "5": us.changePassword(me.getId(), ask("Current password: "), ask("New password: ")); System.out.println("Password changed."); break;
                case "6":
                    if (ask("Type YES to confirm: ").equals("YES")) {
                        us.deleteUser(me.getId());
                        auth.logout();
                        System.out.println("Account deleted.");
                    }
                    break;
                case "7":
                    if (auth.isAdmin()) us.getAllUsers().forEach(System.out::println);
                    break;
                case "8":
                    if (auth.isAdmin()) {
                        us.registerAdmin(ask("Name: "), ask("Email: "), ask("Password: "), ask("Staff code: "));
                        System.out.println("Admin created.");
                    }
                    break;
                case "9":
                    if (auth.isAdmin()) {
                        us.deleteUser(Integer.parseInt(ask("User ID to delete: ")));
                        System.out.println("User deleted.");
                    }
                    break;
                case "0": auth.logout(); System.out.println("Logged out."); break;
                default: System.out.println("Invalid option.");
            }
        } catch (ValidationException | IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: please enter a number.");
        }
    }

    private static String ask(String prompt) {
        System.out.print(prompt);
        return in.nextLine().trim();
    }
}
