
package lk.student.pizza.service;

import lk.student.pizza.model.CustomerOrder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class OrderService {

    private final JdbcTemplate jdbc;

    public OrderService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<CustomerOrder> findAll() {
        return jdbc.query(
                "SELECT * FROM orders ORDER BY id",
                (rs, rowNum) -> new CustomerOrder(
                        rs.getString("id"),
                        rs.getString("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("pizza_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("total"),
                        rs.getString("status")
                )
        );
    }
    public java.util.Optional<CustomerOrder> findById(String id) {
        List<CustomerOrder> result = jdbc.query(
                "SELECT * FROM orders WHERE id = ?",
                (rs, rowNum) -> new CustomerOrder(
                        rs.getString("id"),
                        rs.getString("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("pizza_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("total"),
                        rs.getString("status")
                ),
                id
        );

        return result.stream().findFirst();
    }

    public List<CustomerOrder> findByCustomerId(String customerId) {
        return jdbc.query(
                "SELECT * FROM orders WHERE customer_id = ? ORDER BY id",
                (rs, rowNum) -> new CustomerOrder(
                        rs.getString("id"),
                        rs.getString("customer_id"),
                        rs.getString("customer_name"),
                        rs.getString("pizza_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("total"),
                        rs.getString("status")
                ),
                customerId
        );
    }

    public void place(CustomerOrder o) {
        o.setId("O" + String.format(
                "%03d", new Random().nextInt(900) + 100
        ));
        o.setStatus("Received");

        jdbc.update(
                """
                INSERT INTO orders
                (id, customer_id, customer_name, pizza_name,
                 quantity, total, status)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """,
                o.getId(),
                o.getCustomerId(),
                o.getCustomerName(),
                o.getPizzaName(),
                o.getQuantity(),
                o.getTotal(),
                o.getStatus()
        );
    }

    public void updateStatus(String id, String status) {
        jdbc.update(
                "UPDATE orders SET status = ? WHERE id = ?",
                status, id
        );
    }

    public void delete(String id) {
        jdbc.update(
                "DELETE FROM orders WHERE id = ?",
                id
        );
    }
}
