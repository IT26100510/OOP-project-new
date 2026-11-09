package lk.student.pizza.usermanagement;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

/** Business logic + validation for the CRUD operations on users. */
public class UserService {
    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private static final Pattern PHONE = Pattern.compile("^\\d{9,12}$");
    private final UserRepository repository; // depends on the interface, not the file class

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    // ---------------- CREATE ----------------
    public Customer registerCustomer(String name, String email, String password,
                                     String phone, String address) throws ValidationException, IOException {
        validateCommon(name, email, password);
        if (!PHONE.matcher(phone).matches()) throw new ValidationException("Phone must be 9-12 digits.");
        requireNoPipe(address);
        if (address.isBlank()) throw new ValidationException("Address cannot be empty.");

        String salt = PasswordHasher.generateSalt();
        Customer c = new Customer(repository.nextId(), name.trim(), email.trim().toLowerCase(),
                salt, PasswordHasher.hash(password, salt), phone, address.trim());
        repository.add(c);
        return c;
    }

    public Admin registerAdmin(String name, String email, String password,
                               String staffCode) throws ValidationException, IOException {
        validateCommon(name, email, password);
        requireNoPipe(staffCode);
        if (staffCode.isBlank()) throw new ValidationException("Staff code cannot be empty.");

        String salt = PasswordHasher.generateSalt();
        Admin a = new Admin(repository.nextId(), name.trim(), email.trim().toLowerCase(),
                salt, PasswordHasher.hash(password, salt), staffCode.trim());
        repository.add(a);
        return a;
    }

    // ---------------- READ (method OVERLOADING = compile-time polymorphism) ----------------
    public User findUser(int id) throws ValidationException {
        return repository.findById(id).orElseThrow(() -> new ValidationException("No user with ID " + id));
    }

    public User findUser(String email) throws ValidationException {
        return repository.findByEmail(email.trim())
                .orElseThrow(() -> new ValidationException("No user with email " + email));
    }

    public List<User> getAllUsers() { return repository.findAll(); }

    // ---------------- UPDATE ----------------
    public void updateName(int id, String newName) throws ValidationException, IOException {
        requireText(newName, "Name");
        User u = findUser(id);
        u.setName(newName.trim());
        repository.update(u);
    }

    public void updateEmail(int id, String newEmail) throws ValidationException, IOException {
        if (!EMAIL.matcher(newEmail.trim()).matches()) throw new ValidationException("Invalid email format.");
        User u = findUser(id);
        if (repository.findByEmail(newEmail.trim()).filter(o -> o.getId() != id).isPresent())
            throw new ValidationException("That email is already registered.");
        u.setEmail(newEmail.trim().toLowerCase());
        repository.update(u);
    }

    public void updateCustomerContact(int id, String phone, String address) throws ValidationException, IOException {
        User u = findUser(id);
        if (!(u instanceof Customer)) throw new ValidationException("Only customers have phone/address.");
        if (!PHONE.matcher(phone).matches()) throw new ValidationException("Phone must be 9-12 digits.");
        requireNoPipe(address);
        if (address.isBlank()) throw new ValidationException("Address cannot be empty.");
        Customer c = (Customer) u;
        c.setPhone(phone);
        c.setAddress(address.trim());
        repository.update(c);
    }

    public void changePassword(int id, String oldPassword, String newPassword)
            throws ValidationException, IOException {
        User u = findUser(id);
        if (!u.verifyPassword(oldPassword)) throw new ValidationException("Current password is incorrect.");
        checkPasswordStrength(newPassword);
        u.changePassword(newPassword);
        repository.update(u);
    }

    // ---------------- DELETE ----------------
    public void updateAdminStaffCode(int id, String staffCode) throws ValidationException, IOException {
        if (staffCode == null || staffCode.isBlank() || staffCode.contains("|")) throw new ValidationException("Staff code cannot be empty.");
        User u = findUser(id);
        if (!(u instanceof Admin admin)) throw new ValidationException("Only administrators have a staff code.");
        admin.setStaffCode(staffCode.trim());
        repository.update(admin);
    }

    /** Admin-assisted password reset used by the account management screen. */
    public void setPasswordByAdmin(int id, String newPassword) throws ValidationException, IOException {
        checkPasswordStrength(newPassword);
        User u = findUser(id);
        u.changePassword(newPassword);
        repository.update(u);
    }

    public void deleteUser(int id) throws ValidationException, IOException {
        findUser(id); // throws if it does not exist
        repository.delete(id);
    }

    // ---------------- Validation helpers ----------------
    private void validateCommon(String name, String email, String password) throws ValidationException {
        requireText(name, "Name");
        if (!EMAIL.matcher(email.trim()).matches()) throw new ValidationException("Invalid email format.");
        if (repository.findByEmail(email.trim()).isPresent())
            throw new ValidationException("That email is already registered.");
        checkPasswordStrength(password);
    }

    private void checkPasswordStrength(String password) throws ValidationException {
        if (password == null || password.length() < 8
                || !password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*"))
            throw new ValidationException("Password must be 8+ characters with at least one letter and one digit.");
    }

    private void requireText(String value, String field) throws ValidationException {
        if (value == null || value.isBlank()) throw new ValidationException(field + " cannot be empty.");
        requireNoPipe(value);
    }

    /** '|' is our file separator, so it is not allowed inside text fields. */
    private void requireNoPipe(String value) throws ValidationException {
        if (value != null && value.contains("|")) throw new ValidationException("The '|' character is not allowed.");
    }
}
