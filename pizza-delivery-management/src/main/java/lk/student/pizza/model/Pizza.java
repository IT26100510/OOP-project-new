package lk.student.pizza.model;

public class Pizza extends MenuItem {
    private String size;

    public Pizza() {}
    public Pizza(String id, String name, String description, double price, boolean available, String size) {
        super(id, name, description, price, available);
        this.size = size;
    }

    @Override
    public String getCategory() { return "Pizza"; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
}
