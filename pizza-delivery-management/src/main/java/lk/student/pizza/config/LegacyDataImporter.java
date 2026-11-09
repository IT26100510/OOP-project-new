package lk.student.pizza.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

/** Imports the project's existing text-file records once, without overwriting database data. */
@Component
public class LegacyDataImporter implements ApplicationRunner {
    private final JdbcTemplate jdbc;
    public LegacyDataImporter(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @Override public void run(ApplicationArguments args) throws Exception {
        importCustomers();
        importOrders();
    }

    private void importCustomers() throws Exception {
        if (count("customers") != 0) return;
        Path file = Path.of("data", "customers.txt");
        if (!Files.exists(file)) return;
        for (String line : Files.readAllLines(file)) {
            String[] v = line.split("\\|", -1);
            if (v.length == 5 && !v[0].isBlank()) {
                jdbc.update("INSERT INTO customers (id,name,email,phone,address) VALUES (?,?,?,?,?)",
                        v[0].trim(), v[1].trim(), v[2].trim(), v[3].trim(), v[4].trim());
            }
        }
    }

    private void importOrders() throws Exception {
        if (count("orders") != 0) return;
        Path file = Path.of("data", "orders.txt");
        if (!Files.exists(file)) return;
        for (String line : Files.readAllLines(file)) {
            String[] v = line.split("\\|", -1);
            if (v.length == 7 && !v[0].isBlank()) {
                jdbc.update("INSERT INTO orders (id,customer_id,customer_name,pizza_name,quantity,total,status) VALUES (?,?,?,?,?,?,?)",
                        v[0].trim(), v[1].trim(), v[2].trim(), v[3].trim(), Integer.parseInt(v[4].trim()),
                        Double.parseDouble(v[5].trim()), v[6].trim());
            }
        }
    }

    private int count(String table) {
        // Table names here are fixed constants, never user input.
        return jdbc.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class);
    }
}
