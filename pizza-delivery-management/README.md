# Pizza House — Pizza Delivery Management System

A Java 17, Spring Boot MVC project using Thymeleaf, CSS, JDBC, JPA, and MySQL. The existing project has been extended into six distinct CRUD modules so a six-person team can work on separate areas of the same application.

## Run the project

1. Install JDK 17, Maven, and MySQL 8.
2. Create a MySQL user that can create/use the `pizza_studio` database, or set the connection variables below.
3. Open the folder containing `pom.xml` in IntelliJ and run `PizzaDeliveryApplication`.
4. Open `http://localhost:8080`.

The default database URL creates `pizza_studio` if it does not already exist. Set credentials in the run configuration or shell rather than editing source files:

```bash
export DB_URL='jdbc:mysql://localhost:3306/pizza_studio?createDatabaseIfNotExist=true&serverTimezone=UTC'
export DB_USERNAME='root'
export DB_PASSWORD='your-local-mysql-password'
```

The app creates the required tables from `src/main/resources/schema.sql`. Pizza sample records are seeded by the existing `PizzaService`. Existing `data/customers.txt` and `data/orders.txt` records are imported once when their database tables are empty. The importer does not overwrite existing database rows.

First startup creates an administrator account:

- Email: `admin@pizzahouse.lk`
- Password: `Admin@123`

Change the demo password from **Accounts** before using the project beyond a local classroom demonstration.

## Six-person CRUD division

Each person owns one feature module and completes Create, Read, Update, and Delete for that feature. Work in the same repository and coordinate shared model/schema changes before merging.

| Person | Module | Main files and routes |
|---|---|---|
| 1 | User accounts | `UserAdminController`, `UserService`, `JdbcUserRepository`, `users.html`, `user-form.html`; `/users` (admin) |
| 2 | Customer profiles | `CustomerController`, `CustomerService`, `Customer`, `customers.html`, `customer-form.html`; `/customers` (admin) |
| 3 | Pizza menu | `PizzaController`, `PizzaService`, `Pizza`, `pizzas.html`, `pizza-form.html`; `/admin/pizzas` (admin), `/menu` (signed-in users) |
| 4 | Orders | `OrderController`, `OrderService`, `CustomerOrder`, `orders.html`, `order-form.html`, `order-edit.html`; `/orders` |
| 5 | Feedback and reviews | `FeedbackController`, `FeedbackManager`, `Feedback`, `PizzaReview`, `DeliveryReview`, feedback templates; `/feedback` |
| 6 | Delivery management | `DeliveryController`, `DeliveryService`, `Delivery`, `deliveries.html`, `delivery-form.html`; `/deliveries` (admin), tracking is shown beside each order |

Customer registration creates both a login account and a customer profile. Editing/deleting registered customer details keeps those records aligned. Customers manage their own reviews; administrators can review, delete, and reply to feedback. Order editing and delivery tracking are separate: orders hold the order details, while a delivery record holds rider, ETA, and delivery progress.

## CRUD routes at a glance

- **Accounts:** add, list/edit, update account details/password, delete (the signed-in admin cannot delete their own account).
- **Customers:** add, search/list, edit, delete.
- **Pizzas:** add, list, edit, delete.
- **Orders:** place, view, edit, update status, delete (admin actions).
- **Feedback:** create, list, update, delete; admin replies are also supported.
- **Deliveries:** assign, list, edit rider/status/ETA, delete; each order page displays its delivery status and ETA.

## Data and limitations

- The account, customer, order, delivery, and feedback records use MySQL tables. Pizza records use the existing JPA repository.
- Feedback is backed by the database while the existing `FeedbackManager` retains the project’s OOP CRUD and review-report methods.
- Existing text data files remain in the project. Customers and orders are imported only if the corresponding database table starts empty.
- Checkout is a classroom demo, not a real payment gateway. Delivery tracking is manually updated by an administrator; it is not live GPS tracking.
- This project is intended for coursework and local demonstration, not production use.

## Git workflow for the team

Each person should create a feature branch, work mainly in their assigned module, and open a pull request. Agree on schema and model changes together, then merge and run the integrated app as a group.

```bash
git switch -c feature/delivery-management
# make changes in the assigned module
git add .
git commit -m "Complete delivery management CRUD"
git push -u origin feature/delivery-management
```

## OOP concepts demonstrated

- **Encapsulation:** private fields and validated service/model updates.
- **Inheritance:** `Pizza` extends `MenuItem`; `PizzaReview` and `DeliveryReview` extend `Feedback`; account types extend `User`.
- **Polymorphism:** feedback is handled through the common `Feedback` type and review subclasses.
- **Abstraction:** `MenuItem`, `User`, and `Manageable` define shared contracts.
