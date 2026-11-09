import feedback.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final FeedbackManager manager = new FeedbackManager();

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n===== FEEDBACK & REVIEW MANAGEMENT =====");
            System.out.println("1. Add Pizza Review");
            System.out.println("2. Add Delivery Review");
            System.out.println("3. View All Feedback");
            System.out.println("4. Search Feedback by ID");
            System.out.println("5. View My Feedback (by Customer ID)");
            System.out.println("6. Update Feedback");
            System.out.println("7. Delete Feedback");
            System.out.println("8. Filter by Rating");
            System.out.println("9. Pizza Average Rating");
            System.out.println("10. Admin Reply");
            System.out.println("0. Exit");
            choice = readInt("Choose: ");
            try {
                switch (choice) {
                    case 1 -> addPizzaReview();
                    case 2 -> addDeliveryReview();
                    case 3 -> show(manager.getAll());
                    case 4 -> searchById();
                    case 5 -> show(manager.getByCustomer(readInt("Customer ID: ")));
                    case 6 -> updateFeedback();
                    case 7 -> System.out.println(manager.delete(readInt("Feedback ID: ")) ? "Deleted." : "Not found.");
                    case 8 -> show(manager.getByRating(readInt("Rating (1-5): ")));
                    case 9 -> pizzaAverage();
                    case 10 -> adminReply();
                    case 0 -> System.out.println("Goodbye!");
                    default -> System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (choice != 0);
    }

    private static void addPizzaReview() {
        int cid = readInt("Customer ID: ");
        int pid = readInt("Pizza ID: ");
        String name = readLine("Pizza name: ");
        int rating = readInt("Rating (1-5): ");
        String comment = readLine("Comment: ");
        manager.add(new PizzaReview(cid, pid, name, rating, comment));
        System.out.println("Pizza review added. Thank you!");
    }

    private static void addDeliveryReview() {
        int cid = readInt("Customer ID: ");
        int oid = readInt("Order ID: ");
        String rider = readLine("Rider name: ");
        int rating = readInt("Rating (1-5): ");
        String comment = readLine("Comment: ");
        manager.add(new DeliveryReview(cid, oid, rider, rating, comment));
        System.out.println("Delivery review added. Thank you!");
    }

    private static void searchById() {
        Feedback f = manager.findById(readInt("Feedback ID: "));
        if (f == null) System.out.println("Not found."); else f.display();
    }

    private static void updateFeedback() {
        int id = readInt("Feedback ID: ");
        int rating = readInt("New rating (1-5): ");
        String comment = readLine("New comment: ");
        System.out.println(manager.update(id, rating, comment) ? "Updated." : "Not found.");
    }

    private static void pizzaAverage() {
        int pid = readInt("Pizza ID: ");
        System.out.printf("Average rating: %.2f / 5%n", manager.averageRatingForPizza(pid));
    }

    private static void adminReply() {
        int id = readInt("Feedback ID: ");
        String reply = readLine("Reply: ");
        System.out.println(manager.replyToFeedback(id, reply) ? "Reply saved." : "Not found.");
    }

    private static void show(List<Feedback> list) {
        if (list.isEmpty()) { System.out.println("No feedback found."); return; }
        for (Feedback f : list) f.display();   // polymorphism in action
        System.out.printf("%nTotal: %d | Overall average: %.2f%n", list.size(), manager.overallAverage());
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(sc.nextLine().trim()); }
            catch (NumberFormatException e) { System.out.println("Enter a valid number."); }
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }
}
