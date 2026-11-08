package feedback;

import java.time.LocalDate;

/** Abstract base class: common data/behaviour for every kind of feedback. */
public abstract class Feedback {
    private static int idCounter = 1;          // auto-generates unique IDs

    private final int feedbackId;
    private final int customerId;
    private int rating;                        // 1 - 5
    private String comment;
    private final LocalDate date;
    private String adminReply = "";

    public Feedback(int customerId, int rating, String comment) {
        this.feedbackId = idCounter++;
        this.customerId = customerId;
        setRating(rating);
        setComment(comment);
        this.date = LocalDate.now();
    }

    // ---- Getters ----
    public int getFeedbackId()    { return feedbackId; }
    public int getCustomerId()    { return customerId; }
    public int getRating()        { return rating; }
    public String getComment()    { return comment; }
    public LocalDate getDate()    { return date; }
    public String getAdminReply() { return adminReply; }

    // ---- Setters with validation (encapsulation) ----
    public void setRating(int rating) {
        if (rating < 1 || rating > 5)
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        this.rating = rating;
    }

    public void setComment(String comment) {
        if (comment == null || comment.trim().isEmpty())
            throw new IllegalArgumentException("Comment cannot be empty.");
        this.comment = comment.trim();
    }

    public void setAdminReply(String reply) { this.adminReply = reply == null ? "" : reply.trim(); }

    // ---- Abstract methods: each subclass defines its own (polymorphism) ----
    public abstract String getFeedbackType();
    public abstract String getTargetName();    // what is being reviewed

    public String stars() { return "*".repeat(rating) + "-".repeat(5 - rating); }

    public void display() {
        System.out.println("--------------------------------------------");
        System.out.println("Feedback ID : " + feedbackId + "   [" + getFeedbackType() + "]");
        System.out.println("Customer ID : " + customerId);
        System.out.println("Reviewing   : " + getTargetName());
        System.out.println("Rating      : " + stars() + " (" + rating + "/5)");
        System.out.println("Comment     : " + comment);
        System.out.println("Date        : " + date);
        if (!adminReply.isEmpty()) System.out.println("Admin Reply : " + adminReply);
    }
}
