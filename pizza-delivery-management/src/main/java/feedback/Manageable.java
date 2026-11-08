package feedback;

import java.util.List;

/** Interface (abstraction): contract any management class must follow. */
public interface Manageable<T> {
    void add(T item);
    T findById(int id);
    boolean update(int id, int newRating, String newComment);
    boolean delete(int id);
    List<T> getAll();
}
