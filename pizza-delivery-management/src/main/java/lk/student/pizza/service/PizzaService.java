
package lk.student.pizza.service;

import lk.student.pizza.model.MenuItem;
import lk.student.pizza.model.Pizza;
import lk.student.pizza.repository.PizzaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class PizzaService {

    private final PizzaRepository pizzaRepository;

    // Constructor
    public PizzaService(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;

        // Insert default pizzas if the database is empty
        if (pizzaRepository.count() == 0) {

            pizzaRepository.saveAll(List.of(
                    new Pizza("P001", "Margherita",
                            "Tomato, mozzarella and fresh basil",
                            1800, true, "Medium"),

                    new Pizza("P002", "Pepperoni",
                            "Mozzarella with classic pepperoni",
                            2200, true, "Medium"),

                    new Pizza("P003", "Four Cheese",
                            "A blend of four delicious cheeses",
                            2400, true, "Medium"),

                    new Pizza("P004", "Veggie Supreme",
                            "Bell peppers, olives, onion and mushrooms",
                            2100, true, "Medium"),

                    new Pizza("P005", "BBQ Chicken",
                            "BBQ chicken, onion and mozzarella",
                            2500, true, "Medium")
            ));
        }
    }

    // Find all pizzas
    public List<MenuItem> findAll() {
        return pizzaRepository.findAll()
                .stream()
                .map(pizza -> (MenuItem) pizza)
                .toList();
    }

    // Find pizza by ID
    public Optional<MenuItem> findById(String id) {
        return pizzaRepository.findById(id)
                .map(pizza -> (MenuItem) pizza);
    }

    // Add or update pizza
    public void save(Pizza pizza) {

        if (pizza.getId() == null || pizza.getId().isBlank()) {
            pizza.setId("P" +
                    String.format("%03d", new Random().nextInt(900) + 100));
        }

        pizzaRepository.save(pizza);
    }

    // Delete pizza
    public void delete(String id) {
        pizzaRepository.deleteById(id);
    }
}
