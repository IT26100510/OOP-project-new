package lk.student.pizza.service;

import lk.student.pizza.model.Delivery;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeliveryService {
    private final JdbcTemplate jdbc;
    public DeliveryService(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public List<Delivery> findAll() {
        return jdbc.query("SELECT * FROM deliveries ORDER BY created_at DESC", (rs, n) -> map(rs));
    }
    public Optional<Delivery> findById(String id) {
        return jdbc.query("SELECT * FROM deliveries WHERE id = ?", (rs, n) -> map(rs), id).stream().findFirst();
    }
    public void save(Delivery d) {
        if (d.getId() == null || d.getId().isBlank()) {
            d.setId("D" + UUID.randomUUID().toString().substring(0, 7).toUpperCase());
            jdbc.update("INSERT INTO deliveries (id, order_id, customer_name, rider_name, rider_phone, status, eta, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    d.getId(), d.getOrderId(), d.getCustomerName(), d.getRiderName(), d.getRiderPhone(), d.getStatus(), d.getEta(), d.getNotes());
        } else {
            jdbc.update("UPDATE deliveries SET order_id=?, customer_name=?, rider_name=?, rider_phone=?, status=?, eta=?, notes=? WHERE id=?",
                    d.getOrderId(), d.getCustomerName(), d.getRiderName(), d.getRiderPhone(), d.getStatus(), d.getEta(), d.getNotes(), d.getId());
        }
    }
    public void delete(String id) { jdbc.update("DELETE FROM deliveries WHERE id = ?", id); }

    private Delivery map(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new Delivery(rs.getString("id"), rs.getString("order_id"), rs.getString("customer_name"),
                rs.getString("rider_name"), rs.getString("rider_phone"), rs.getString("status"),
                rs.getString("eta"), rs.getString("notes"));
    }
}
