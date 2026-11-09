package pizza.user;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * ABSTRACTION (interface): the services only know WHAT can be done with stored users,
 * not HOW. We could swap the file implementation for a database without changing the services.
 */
public interface UserRepository {
    int nextId();
    void add(User user) throws IOException;
    void update(User user) throws IOException;
    boolean delete(int id) throws IOException;
    Optional<User> findById(int id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
}
