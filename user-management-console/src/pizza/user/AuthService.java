package pizza.user;

/**
 * Handles login/logout and remembers who is logged in.
 * Other components (cart, orders, payments...) call getCurrentUser() to know the customer ID.
 */
public class AuthService {
    private final UserRepository repository;
    private User currentUser; // null when nobody is logged in

    public AuthService(UserRepository repository) {
        this.repository = repository;
    }

    public User login(String email, String password) throws InvalidCredentialsException {
        // Same message for "no such email" and "wrong password" so attackers can't tell which was wrong.
        User user = repository.findByEmail(email == null ? "" : email.trim())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password."));
        if (!user.verifyPassword(password == null ? "" : password)) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }
        this.currentUser = user;
        return user;
    }

    public void logout() { this.currentUser = null; }
    public boolean isLoggedIn() { return currentUser != null; }
    public User getCurrentUser() { return currentUser; }
    public boolean isAdmin() { return currentUser instanceof Admin; }
}
