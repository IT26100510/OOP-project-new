package lk.student.pizza.usermanagement;

/** Thrown when login fails (wrong email or password). */
public class InvalidCredentialsException extends Exception {
    public InvalidCredentialsException(String message) { super(message); }
}
