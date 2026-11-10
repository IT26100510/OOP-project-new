# Person 2 — Customer Profiles

## Assigned module

Add profiles, list/search profiles, edit customer contact/address details, delete profiles. Keep login-account/profile sync coordinated with Person 1.

## Included files

- `src/main/java/lk/student/pizza/controller/CustomerController.java`
- `src/main/java/lk/student/pizza/service/CustomerService.java`
- `src/main/java/lk/student/pizza/model/Customer.java`
- `src/main/resources/templates/customers.html`
- `src/main/resources/templates/customer-form.html`

## Dependencies and coordination

UserService from Person 1 to synchronize registered customer account details; OrderService uses customer IDs when placing and listing orders.

## Shared project files

These are already present in the integrated project and should be merged only once by the team coordinator: `pom.xml`, `application.properties`, `schema.sql`, `LegacyDataImporter.java`, `fragments.html` (navigation), and shared `style.css`. `AuthInterceptor.java` is included with Person 1 because it contains the account routes, but its authorization rules cover multiple modules; merge it only once after coordinating the module routes. This ZIP contains your module contribution files, not a separately runnable application. Preserve the existing folder paths when copying these files into the main `pizza-delivery-management` project.

## Git contribution

Work on a branch for this module, commit the owned files, and open a pull request. Coordinate edits to shared integration files with the relevant module owners.
