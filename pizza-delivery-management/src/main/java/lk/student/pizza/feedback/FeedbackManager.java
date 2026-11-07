package lk.student.pizza.feedback;

import java.util.ArrayList;
import java.util.List;

/** Handles all feedback operations (CRUD + reports). */
public class FeedbackManager implements Manageable<Feedback> {
    private final List<Feedback> feedbackList = new ArrayList<>();

    @Override
    public void add(Feedback f) { feedbackList.add(f); }

    @Override
    public Feedback findById(int id) {
        for (Feedback f : feedbackList)
            if (f.getFeedbackId() == id) return f;
        return null;
    }

    @Override
    public boolean update(int id, int newRating, String newComment) {
        Feedback f = findById(id);
        if (f == null) return false;
        f.setRating(newRating);
        f.setComment(newComment);
        return true;
    }

    @Override
    public boolean delete(int id) {
        Feedback f = findById(id);
        return f != null && feedbackList.remove(f);
    }

    @Override
    public List<Feedback> getAll() { return new ArrayList<>(feedbackList); }

    public List<Feedback> getByCustomer(int customerId) {
        List<Feedback> result = new ArrayList<>();
        for (Feedback f : feedbackList)
            if (f.getCustomerId() == customerId) result.add(f);
        return result;
    }

    public List<Feedback> getByRating(int rating) {
        List<Feedback> result = new ArrayList<>();
        for (Feedback f : feedbackList)
            if (f.getRating() == rating) result.add(f);
        return result;
    }

    public double averageRatingForPizza(int pizzaId) {
        int total = 0, count = 0;
        for (Feedback f : feedbackList)
            if (f instanceof PizzaReview && ((PizzaReview) f).getPizzaId() == pizzaId) {
                total += f.getRating();
                count++;
            }
        return count == 0 ? 0 : (double) total / count;
    }

    public double overallAverage() {
        if (feedbackList.isEmpty()) return 0;
        int total = 0;
        for (Feedback f : feedbackList) total += f.getRating();
        return (double) total / feedbackList.size();
    }

    public boolean replyToFeedback(int id, String reply) {
        Feedback f = findById(id);
        if (f == null) return false;
        f.setAdminReply(reply);
        return true;
    }
}
