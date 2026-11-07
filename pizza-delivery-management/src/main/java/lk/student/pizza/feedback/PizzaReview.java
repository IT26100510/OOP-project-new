package lk.student.pizza.feedback;

/** Inheritance: a review about a specific pizza. */
public class PizzaReview extends Feedback {
    private final int pizzaId;
    private final String pizzaName;

    public PizzaReview(int customerId, int pizzaId, String pizzaName, int rating, String comment) {
        super(customerId, rating, comment);
        this.pizzaId = pizzaId;
        this.pizzaName = pizzaName;
    }

    public int getPizzaId() { return pizzaId; }

    @Override public String getFeedbackType() { return "Pizza Review"; }
    @Override public String getTargetName()   { return pizzaName + " (Pizza #" + pizzaId + ")"; }
}
