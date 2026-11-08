package feedback;

import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** CRUD manager for reviews. The no-argument constructor remains for the console OOP demo. */
public class FeedbackManager implements Manageable<Feedback> {
    private final List<Feedback> feedbackList = new ArrayList<>();
    private final JdbcTemplate jdbc;

    public FeedbackManager() { this.jdbc = null; }
    public FeedbackManager(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        loadSavedReviews();
    }

    @Override public void add(Feedback f) {
        feedbackList.add(f);
        if (jdbc != null) insert(f);
    }
    @Override public Feedback findById(int id) {
        for (Feedback f : feedbackList) if (f.getFeedbackId() == id) return f;
        return null;
    }
    @Override public boolean update(int id, int newRating, String newComment) {
        Feedback f = findById(id);
        if (f == null) return false;
        f.setRating(newRating); f.setComment(newComment);
        if (jdbc != null) jdbc.update("UPDATE feedback SET rating=?, review_text=? WHERE id=?", newRating, newComment.trim(), id);
        return true;
    }
    @Override public boolean delete(int id) {
        Feedback f = findById(id);
        if (f == null || !feedbackList.remove(f)) return false;
        if (jdbc != null) jdbc.update("DELETE FROM feedback WHERE id=?", id);
        return true;
    }
    @Override public List<Feedback> getAll() { return new ArrayList<>(feedbackList); }
    public List<Feedback> getByCustomer(int customerId) {
        List<Feedback> result = new ArrayList<>();
        for (Feedback f : feedbackList) if (f.getCustomerId() == customerId) result.add(f);
        return result;
    }
    public List<Feedback> getByRating(int rating) {
        List<Feedback> result = new ArrayList<>();
        for (Feedback f : feedbackList) if (f.getRating() == rating) result.add(f);
        return result;
    }
    public double averageRatingForPizza(int pizzaId) {
        int total=0,count=0;
        for (Feedback f : feedbackList) if (f instanceof PizzaReview && ((PizzaReview)f).getPizzaId()==pizzaId) { total+=f.getRating(); count++; }
        return count==0 ? 0 : (double)total/count;
    }
    public double overallAverage() {
        if (feedbackList.isEmpty()) return 0;
        int total=0; for (Feedback f:feedbackList) total+=f.getRating();
        return (double)total/feedbackList.size();
    }
    public boolean replyToFeedback(int id, String reply) {
        Feedback f=findById(id); if(f==null) return false;
        f.setAdminReply(reply);
        if(jdbc!=null) jdbc.update("UPDATE feedback SET admin_reply=? WHERE id=?", f.getAdminReply(), id);
        return true;
    }

    private void loadSavedReviews() {
        jdbc.query("SELECT * FROM feedback ORDER BY id", rs -> {
            int id=rs.getInt("id"), customer=rs.getInt("customer_id"), rating=rs.getInt("rating");
            String comment=rs.getString("review_text"), reply=rs.getString("admin_reply");
            LocalDate date=rs.getDate("review_date").toLocalDate();
            Feedback f;
            if("PIZZA".equals(rs.getString("review_type"))) {
                f=new PizzaReview(id, customer, rs.getInt("pizza_id"), rs.getString("pizza_name"), rating, comment, date, reply);
            } else {
                f=new DeliveryReview(id, customer, rs.getInt("order_id"), rs.getString("delivery_person_name"), rating, comment, date, reply);
            }
            feedbackList.add(f);
        });
    }
    private void insert(Feedback f) {
        String type=f instanceof PizzaReview ? "PIZZA" : "DELIVERY";
        Integer pizzaId=f instanceof PizzaReview ? ((PizzaReview)f).getPizzaId() : null;
        String pizzaName=f instanceof PizzaReview ? ((PizzaReview)f).getPizzaName() : null;
        Integer orderId=f instanceof DeliveryReview ? ((DeliveryReview)f).getOrderId() : null;
        String rider=f instanceof DeliveryReview ? ((DeliveryReview)f).getDeliveryPersonName() : null;
        jdbc.update("INSERT INTO feedback (id, review_type, customer_id, rating, review_text, review_date, admin_reply, pizza_id, pizza_name, order_id, delivery_person_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                f.getFeedbackId(), type, f.getCustomerId(), f.getRating(), f.getComment(), java.sql.Date.valueOf(f.getDate()), f.getAdminReply(), pizzaId, pizzaName, orderId, rider);
    }
}
