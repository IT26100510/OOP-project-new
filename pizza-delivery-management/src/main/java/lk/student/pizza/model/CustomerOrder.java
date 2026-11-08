
package lk.student.pizza.model;

public class CustomerOrder {

    private String id;
    private String customerId;
    private String customerName;
    private String pizzaName;
    private int quantity;
    private double total;
    private String status;

    // Default Constructor
    public CustomerOrder() {
    }

    // Parameterized Constructor
    public CustomerOrder(
            String id,
            String customerId,
            String customerName,
            String pizzaName,
            int quantity,
            double total,
            String status) {

        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.pizzaName = pizzaName;
        this.quantity = quantity;
        this.total = total;
        this.status = status;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPizzaName() {
        return pizzaName;
    }

    public void setPizzaName(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
