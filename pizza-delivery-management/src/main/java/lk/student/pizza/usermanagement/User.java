package lk.student.pizza.usermanagement;

/**
 * ABSTRACTION: base class for every kind of account. It cannot be instantiated.
 * ENCAPSULATION: all fields are private; the password is only stored as salt + hash.
 */
public abstract class User {
    private final int id;
    private String name;
    private String email;
    private String salt;
    private String passwordHash;

    protected User(int id, String name, String email, String salt, String passwordHash) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.salt = salt;
        this.passwordHash = passwordHash;
    }

    // ---- Abstract methods: every subclass MUST provide its own version (polymorphism) ----
    public abstract String getRole();
    public abstract String displayProfile();
    /** Extra fields of the subclass, used by the file repository to save the user. */
    public abstract String[] getExtraFields();

    // ---- Getters ----
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    // ---- Setters (validation happens in UserService) ----
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }

    // ---- Information hiding: password data is never exposed directly ----
    /** Checks a plain password against the stored hash without revealing the hash. */
    public boolean verifyPassword(String plainPassword) {
        return PasswordHasher.hash(plainPassword, salt).equals(passwordHash);
    }

    /** Replaces the password: generates a new salt and stores the new hash. */
    public void changePassword(String newPlainPassword) {
        this.salt = PasswordHasher.generateSalt();
        this.passwordHash = PasswordHasher.hash(newPlainPassword, salt);
    }

    /** Package-private: only the repository (same package) may read these to save to file. */
    String getSalt() { return salt; }
    String getPasswordHash() { return passwordHash; }

    @Override
    public String toString() {
        return getRole() + " #" + id + " - " + name + " (" + email + ")";
    }
}
