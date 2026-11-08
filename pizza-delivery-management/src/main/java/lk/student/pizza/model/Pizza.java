
package lk.student.pizza.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "pizzas")
public class Pizza extends MenuItem {

    @Column(length = 30)
    private String size;

    // Default Constructor
    public Pizza() {
    }

    // Parameterized Constructor
    public Pizza(
            String id,
            String name,
            String description,
            double price,
            boolean available,
            String size) {

        super(id, name, description, price, available);

        this.size = size;
    }

    // Override Abstract Method
    @Override
    public String getCategory() {
        return "Pizza";
    }

    // Getter and Setter
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
