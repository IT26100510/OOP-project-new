
package lk.student.pizza.service;

import lk.student.pizza.model.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CustomerService {

    private final JdbcTemplate jdbc;

    public CustomerService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Customer> findAll() {
        return jdbc.query(
                "SELECT * FROM customers ORDER BY id",
                (rs, rowNum) -> new Customer(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address")
                )
        );
    }

    public Optional<Customer> findById(String id) {
        return jdbc.query(
                "SELECT * FROM customers WHERE id = ?",
                (rs, rowNum) -> new Customer(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address")
                ),
                id
        ).stream().findFirst();
    }

    public Optional<Customer> findByEmail(String email) {
        return jdbc.query(
                "SELECT * FROM customers WHERE LOWER(email) = LOWER(?)",
                (rs, rowNum) -> new Customer(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address")
                ),
                email
        ).stream().findFirst();
    }

    public void save(Customer c) {
        if (c.getId() == null || c.getId().isBlank()) {
            c.setId("C" + String.format(
                    "%03d", new Random().nextInt(900) + 100
            ));
        }

        jdbc.update(
                """
                INSERT INTO customers (id, name, email, phone, address)
                VALUES (?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                    name = VALUES(name),
                    email = VALUES(email),
                    phone = VALUES(phone),
                    address = VALUES(address)
                """,
                c.getId(),
                c.getName(),
                c.getEmail(),
                c.getPhone(),
                c.getAddress()
        );
    }

    public void delete(String id) {
        jdbc.update("DELETE FROM customers WHERE id = ?", id);
    }
}
