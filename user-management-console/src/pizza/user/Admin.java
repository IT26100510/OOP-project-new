package pizza.user;

/** INHERITANCE: Admin is a User with a staff code. */
public class Admin extends User {
    private String staffCode;

    public Admin(int id, String name, String email, String salt, String passwordHash, String staffCode) {
        super(id, name, email, salt, passwordHash);
        this.staffCode = staffCode;
    }

    public String getStaffCode() { return staffCode; }
    public void setStaffCode(String staffCode) { this.staffCode = staffCode; }

    @Override
    public String getRole() { return "ADMIN"; }

    @Override
    public String displayProfile() {
        return "Admin Profile\n  ID: " + getId() + "\n  Name: " + getName()
                + "\n  Email: " + getEmail() + "\n  Staff Code: " + staffCode;
    }

    @Override
    public String[] getExtraFields() { return new String[]{staffCode, ""}; }
}
