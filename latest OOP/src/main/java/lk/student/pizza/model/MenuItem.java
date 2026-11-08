
package lk.student.pizza.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class MenuItem {

    @Id
    @Column(length = 20)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    private double price;

    private boolean available;

    // Default Constructor
    protected MenuItem() {
    }

    // Parameterized Constructor
    protected MenuItem(
            String id,
            String name,
            String description,
            double price,
            boolean available) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = available;
    }

    // Abstract Method
    public abstract String getCategory();

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
