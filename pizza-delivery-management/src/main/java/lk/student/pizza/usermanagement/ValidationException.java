package lk.student.pizza.usermanagement;

/** Thrown when input is invalid (bad email, weak password, duplicate email, user not found...). */
public class ValidationException extends Exception {
    public ValidationException(String message) { super(message); }
}
