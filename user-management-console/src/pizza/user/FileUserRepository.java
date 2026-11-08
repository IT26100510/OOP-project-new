package pizza.user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Stores users in a text file, one user per line:
 * id|ROLE|name|email|salt|hash|extra1|extra2
 * Loads everything into memory at start-up and rewrites the file after each change.
 */
public class FileUserRepository implements UserRepository {
    private static final String SEP = "|";
    private final Path filePath;
    private final Map<Integer, User> users = new LinkedHashMap<>();

    public FileUserRepository(String fileName) throws IOException {
        this.filePath = Paths.get(fileName);
        load();
    }

    private void load() throws IOException {
        if (!Files.exists(filePath)) {
            return; // first run: no file yet, start with an empty list
        }
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                User user = parse(line);
                if (user != null) users.put(user.getId(), user);
            }
        }
    }

    /** Converts one file line back into a Customer or Admin object. */
    private User parse(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 8) return null; // skip corrupted lines
        try {
            int id = Integer.parseInt(p[0]);
            switch (p[1]) {
                case "CUSTOMER": return new Customer(id, p[2], p[3], p[4], p[5], p[6], p[7]);
                case "ADMIN":    return new Admin(id, p[2], p[3], p[4], p[5], p[6]);
                default:         return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void saveAll() throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
            for (User u : users.values()) {
                String[] extra = u.getExtraFields();
                writer.write(String.join(SEP, String.valueOf(u.getId()), u.getRole(), u.getName(),
                        u.getEmail(), u.getSalt(), u.getPasswordHash(), extra[0], extra[1]));
                writer.newLine();
            }
        }
    }

    @Override
    public int nextId() {
        return users.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
    }

    @Override
    public void add(User user) throws IOException {
        users.put(user.getId(), user);
        saveAll();
    }

    @Override
    public void update(User user) throws IOException {
        users.put(user.getId(), user);
        saveAll();
    }

    @Override
    public boolean delete(int id) throws IOException {
        boolean removed = users.remove(id) != null;
        if (removed) saveAll();
        return removed;
    }

    @Override
    public Optional<User> findById(int id) { return Optional.ofNullable(users.get(id)); }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    @Override
    public List<User> findAll() { return new ArrayList<>(users.values()); }
}
