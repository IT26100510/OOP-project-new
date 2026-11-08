
package lk.student.pizza.usermanagement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbc;

    public JdbcUserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public int nextId() {
        Integer nextId = jdbc.queryForObject(
                "SELECT COALESCE(MAX(user_id), 0) + 1 FROM users",
                Integer.class
        );
        return nextId != null ? nextId : 1;
    }

    @Override
    public void add(User user) {
        String staffCode = user instanceof Admin
                ? ((Admin) user).getStaffCode() : null;

        String phone = user instanceof Customer
                ? ((Customer) user).getPhone() : null;

        String address = user instanceof Customer
                ? ((Customer) user).getAddress() : null;

        jdbc.update(
                """
                INSERT INTO users
                (user_id, name, email, password_hash, salt, role,
                 staff_code, phone, address)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """,
                user.getId(), user.getName(), user.getEmail(),
                user.getPasswordHash(), user.getSalt(), user.getRole(),
                staffCode, phone, address
        );
    }

    @Override
    public void update(User user) {
        String staffCode = user instanceof Admin
                ? ((Admin) user).getStaffCode() : null;

        String phone = user instanceof Customer
                ? ((Customer) user).getPhone() : null;

        String address = user instanceof Customer
                ? ((Customer) user).getAddress() : null;

        jdbc.update(
                """
                UPDATE users
                SET name = ?, email = ?, password_hash = ?, salt = ?,
                    role = ?, staff_code = ?, phone = ?, address = ?
                WHERE user_id = ?
                """,
                user.getName(), user.getEmail(),
                user.getPasswordHash(), user.getSalt(), user.getRole(),
                staffCode, phone, address, user.getId()
        );
    }

    @Override
    public boolean delete(int id) {
        return jdbc.update(
                "DELETE FROM users WHERE user_id = ?", id
        ) > 0;
    }

    @Override
    public Optional<User> findById(int id) {
        return jdbc.query(
                "SELECT * FROM users WHERE user_id = ?",
                (rs, rowNum) -> mapUser(rs),
                id
        ).stream().findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jdbc.query(
                "SELECT * FROM users WHERE LOWER(email) = LOWER(?)",
                (rs, rowNum) -> mapUser(rs),
                email
        ).stream().findFirst();
    }

    @Override
    public List<User> findAll() {
        return jdbc.query(
                "SELECT * FROM users ORDER BY user_id",
                (rs, rowNum) -> mapUser(rs)
        );
    }

    private User mapUser(java.sql.ResultSet rs)
            throws java.sql.SQLException {

        int id = rs.getInt("user_id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String salt = rs.getString("salt");
        String hash = rs.getString("password_hash");
        String role = rs.getString("role");

        if ("ADMIN".equals(role)) {
            return new Admin(
                    id, name, email, salt, hash,
                    rs.getString("staff_code")
            );
        }

        return new Customer(
                id, name, email, salt, hash,
                rs.getString("phone"),
                rs.getString("address")
        );
    }
}
