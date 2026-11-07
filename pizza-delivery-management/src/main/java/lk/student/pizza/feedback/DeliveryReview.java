package lk.student.pizza.feedback;

/** Inheritance: a review about a delivery/order. */
public class DeliveryReview extends Feedback {
    private final int orderId;
    private final String deliveryPersonName;

    public DeliveryReview(int customerId, int orderId, String deliveryPersonName, int rating, String comment) {
        super(customerId, rating, comment);
        this.orderId = orderId;
        this.deliveryPersonName = deliveryPersonName;
    }

    @SuppressWarnings("unused")
    public int getOrderId() { return orderId; }

    @Override public String getFeedbackType() { return "Delivery Review"; }
    @Override public String getTargetName()   { return "Order #" + orderId + " - Rider: " + deliveryPersonName; }
}
