package lk.student.pizza.model;

public class Delivery {
    private String id;
    private String orderId;
    private String customerName;
    private String riderName;
    private String riderPhone;
    private String status;
    private String eta;
    private String notes;

    public Delivery() { }

    public Delivery(String id, String orderId, String customerName, String riderName,
                    String riderPhone, String status, String eta, String notes) {
        this.id = id;
        this.orderId = orderId;
        this.customerName = customerName;
        this.riderName = riderName;
        this.riderPhone = riderPhone;
        this.status = status;
        this.eta = eta;
        this.notes = notes;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getRiderName() { return riderName; }
    public void setRiderName(String riderName) { this.riderName = riderName; }
    public String getRiderPhone() { return riderPhone; }
    public void setRiderPhone(String riderPhone) { this.riderPhone = riderPhone; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getEta() { return eta; }
    public void setEta(String eta) { this.eta = eta; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
