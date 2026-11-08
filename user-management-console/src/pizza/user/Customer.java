package pizza.user;

/** INHERITANCE: Customer is a User with a phone number and delivery address. */
public class Customer extends User {
    private String phone;
    private String address;

    public Customer(int id, String name, String email, String salt, String passwordHash,
                    String phone, String address) {
        super(id, name, email, salt, passwordHash);
        this.phone = phone;
        this.address = address;
    }

    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }

    @Override
    public String getRole() { return "CUSTOMER"; }

    @Override
    public String displayProfile() {
        return "Customer Profile\n  ID: " + getId() + "\n  Name: " + getName()
                + "\n  Email: " + getEmail() + "\n  Phone: " + phone + "\n  Address: " + address;
    }

    @Override
    public String[] getExtraFields() { return new String[]{phone, address}; }
}
