class PizzaMenuManager {

    private ArrayList<Pizza> pizzaList;

    // Constructor
    public PizzaMenuManager() {

        pizzaList = new ArrayList<>();

        // Add sample pizzas
        pizzaList.add(new Pizza(
                "P001", "Margherita",
                "Medium", "Vegetarian", 1800.00
        ));

        pizzaList.add(new Pizza(
                "P002", "Chicken BBQ",
                "Large", "Non-Vegetarian", 3200.00
        ));

        pizzaList.add(new Pizza(
                "P003", "Seafood Pizza",
                "Large", "Non-Vegetarian", 3800.00
        ));

        pizzaList.add(new Pizza(
                "P004", "Veggie Supreme",
                "Small", "Vegetarian", 1500.00
        ));
    }

    // Find a pizza using its ID
    public Pizza findPizza(String pizzaId) {

        for (Pizza pizza : pizzaList) {

            if (pizza.getPizzaId().equalsIgnoreCase(pizzaId)) {
                return pizza;
            }
        }

        return null;
    }

    // Add a new pizza
    public boolean addPizza(Pizza pizza) {

        if (findPizza(pizza.getPizzaId()) != null) {
            System.out.println(
                    "Error: Pizza ID already exists."
            );

            return false;
        }

        pizzaList.add(pizza);

        System.out.println(
                "Pizza added successfully!"
        );

        return true;
    }

    // Display all pizzas
    public void displayAllPizzas() {

        if (pizzaList.isEmpty()) {
            System.out.println("The pizza menu is empty.");
            return;
        }

        System.out.println("\n================ PIZZA MENU ================");

        System.out.printf(
                "%-8s %-20s %-10s %-18s %-15s %-12s%n",
                "ID", "Pizza Name", "Size",
                "Category", "Price", "Availability"
        );

        System.out.println(
                "--------------------------------------------------------------------------"
        );

        for (Pizza pizza : pizzaList) {
            pizza.displayPizza();
        }

        System.out.println(
                "=========================================================================="
        );
    }

    // Search for a pizza
    public void searchPizza(String pizzaId) {

        Pizza pizza = findPizza(pizzaId);

        if (pizza != null) {

            System.out.println("\nPizza found:");

            System.out.printf(
                    "%-8s %-20s %-10s %-18s %-15s %-12s%n",
                    "ID", "Pizza Name", "Size",
                    "Category", "Price", "Availability"
            );

            pizza.displayPizza();

        } else {

            System.out.println(
                    "Pizza not found."
            );
        }
    }

    // Update pizza details
    public boolean updatePizza(
            String pizzaId,
            String newName,
            String newSize,
            String newCategory,
            double newPrice) {

        Pizza pizza = findPizza(pizzaId);

        if (pizza == null) {
            System.out.println("Pizza not found.");
            return false;
        }

        // Validate all new values before changing the object.
        // This prevents a partial update if validation fails.
        Pizza validatedPizza = new Pizza(
                pizza.getPizzaId(),
                newName,
                newSize,
                newCategory,
                newPrice
        );

        // Apply validated values
        pizza.setPizzaName(validatedPizza.getPizzaName());
        pizza.setSize(validatedPizza.getSize());
        pizza.setCategory(validatedPizza.getCategory());
        pizza.setPrice(validatedPizza.getPrice());

        System.out.println(
                "Pizza details updated successfully!"
        );

        return true;
    }

    // Delete a pizza
    public boolean deletePizza(String pizzaId) {

        Pizza pizza = findPizza(pizzaId);

        if (pizza == null) {
            System.out.println("Pizza not found.");
            return false;
        }

        pizzaList.remove(pizza);

        System.out.println(
                "Pizza deleted successfully!"
        );

        return true;
    }

    // Change pizza availability
    public boolean changeAvailability(
            String pizzaId, boolean available) {

        Pizza pizza = findPizza(pizzaId);

        if (pizza == null) {
            System.out.println("Pizza not found.");
            return false;
        }

        pizza.setAvailable(available);

        System.out.println(
                "Pizza availability updated successfully!"
        );

        return true;
    }
}
