package feedback;

/** Inheritance: a review about a delivery/order. */
public class DeliveryReview extends Feedback {
    private final int orderId;
    private final String deliveryPersonName;

    public DeliveryReview(int customerId, int orderId, String deliveryPersonName, int rating, String comment) {
        super(customerId, rating, comment);
        this.orderId = orderId;
        this.deliveryPersonName = deliveryPersonName;
    }

    public int getOrderId() { return orderId; }
    public String getDeliveryPersonName() { return deliveryPersonName; }

    public DeliveryReview(int feedbackId, int customerId, int orderId, String deliveryPersonName,
                          int rating, String comment, java.time.LocalDate date, String adminReply) {
        super(feedbackId, customerId, rating, comment, date, adminReply);
        this.orderId = orderId;
        this.deliveryPersonName = deliveryPersonName;
    }

    @Override public String getFeedbackType() { return "Delivery Review"; }
    @Override public String getTargetName()   { return "Order #" + orderId + " - Rider: " + deliveryPersonName; }
}
