# Pizza House – Pizza Delivery Management System

A first-year SE1020 Java OOP group project. It uses Spring Boot MVC, Thymeleaf HTML templates, CSS, minimal JavaScript and text-file storage. No database is required.

## Requirements
- IntelliJ IDEA (Community or Ultimate)
- JDK 17
- Maven (IntelliJ can use the Maven wrapper/imported Maven project)
- Internet connection only for the optional Google Fonts / Unsplash hero background

## Run in IntelliJ
1. Extract the project folder.
2. Open IntelliJ → **File → Open** → select this folder (the folder containing `pom.xml`).
3. Set Project SDK to **Java 17**.
4. Wait for Maven dependencies to finish downloading.
5. Open `src/main/java/lk/student/pizza/PizzaDeliveryApplication.java`.
6. Click the green Run icon beside `main`.
7. Open `http://localhost:8080` in your browser.

Spring Boot runs on its embedded Tomcat server. You do not need to deploy a WAR manually or install a separate Tomcat for this setup.

## Features
- Home page and five-pizza menu.
- Pizza management: create, read, update and delete.
- Customer management: create, read, search, update and delete.
- Orders: place an order, view orders, update status and delete.
- Data is saved in `data/pizzas.txt`, `data/customers.txt` and `data/orders.txt`.
- Sample customer record is included.

## File storage format
Each record is one line. Fields are separated by `|`.
- pizzas.txt: id | name | description | price | available | size
- customers.txt: id | name | email | phone | address
- orders.txt: id | customerId | customerName | pizzaName | quantity | total | status

The application writes to these files while it is running. Keep the `data` folder in the project root. Back up these files before testing destructive actions.

## Six-person team split
All six people work in the **same repository** and on the same project structure. Do not create six unrelated projects. Each member should own the files listed below, commit their work on their own branch, then open a pull request into `main`.

| Member | Part / responsibility | Files to own |
|---|---|---|
| 1 | UI and home/menu | `templates/index.html`, `templates/fragments.html`, `static/css/style.css`, `static/js/app.js` |
| 2 | Pizza management CRUD | `controller/PizzaController.java`, `service/PizzaService.java`, `templates/pizzas.html`, `templates/pizza-form.html` |
| 3 | Customer management CRUD | `model/Customer.java`, `controller/CustomerController.java`, `service/CustomerService.java`, `templates/customers.html`, `templates/customer-form.html` |
| 4 | Order management | `model/CustomerOrder.java`, `controller/OrderController.java`, `service/OrderService.java`, `templates/orders.html`, `templates/order-form.html` |
| 5 | OOP model and file handling | `model/MenuItem.java`, `model/Pizza.java`, `repository/FileDataStore.java`, sample files in `data/` |
| 6 | Integration, application setup and documentation | `PizzaDeliveryApplication.java`, `HomeController.java`, `application.properties`, `pom.xml`, `README.md`, `docs/` |

**Coordination rule:** Member 5 should agree the model and file format with Members 2–4 before everyone edits their services. Member 6 integrates and checks the whole app. If a shared file needs changing, discuss it first to avoid merge conflicts.

## GitHub workflow for each member
1. One person creates an empty GitHub repository and adds the other five as collaborators.
2. One person pushes the starter project to `main`.
3. Each member clones the same repository.
4. Each member creates a branch, for example `feature/pizza-crud` or `feature/customer-management`.
5. Work only on the assigned files; commit frequently with meaningful messages.
6. Push the branch and create a Pull Request.
7. Review and merge PRs into `main` one at a time.
8. Everyone pulls the updated `main` and tests the integrated project.

Example commands:

```bash
git clone https://github.com/YOUR-TEAM/YOUR-REPOSITORY.git
cd YOUR-REPOSITORY
git switch -c feature/pizza-crud
git add .
git commit -m "Add pizza CRUD management"
git push -u origin feature/pizza-crud
```

After the pull request is merged:

```bash
git switch main
git pull origin main
```

Do not all push directly to `main`. Pull requests make contributions and commit history clear for the final report and viva.

## OOP concepts to explain in the viva
- **Encapsulation:** model fields are private and accessed through getters/setters.
- **Inheritance:** `Pizza` extends the abstract `MenuItem` class.
- **Polymorphism:** `PizzaService.findAll()` returns a `List<MenuItem>` containing `Pizza` objects; the common parent type is used to handle them.
- **Abstraction:** `MenuItem` is abstract and requires subclasses to implement `getCategory()`.

## CRUD evidence for the viva
- Create: add a pizza or customer, or place an order.
- Read: view menu, customers and orders.
- Update: edit a pizza/customer or update an order status.
- Delete: delete a pizza/customer/order.

## Important limitations
This is a classroom demonstration, not a production delivery platform. It has no login/security, real payment processing, live delivery tracking, stock control or database. The text-file approach is intentionally simple and matches the project brief. For a real multi-user deployment, file locking and a database would be preferable.

## Authentication and checkout

- Customer accounts can be created from **Login → Create customer account**.
- Admin sign-in is available from the same login screen by selecting **Admin**.
- Demo admin credentials: `admin@pizzahouse.lk` / `Admin@123` (change these before deployment).
- Customers can view the menu after signing in and submit orders through the demo checkout page.
- The checkout currently simulates payment; it does not connect to a real payment gateway or process card payments.
- Account records are stored in `data/users.txt`; customer passwords are stored as salted PBKDF2 hashes.
