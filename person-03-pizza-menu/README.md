# Person 3 — Pizza Menu

## Assigned module

Add pizzas, view the menu, edit pizza details/price/availability, delete pizzas.

## Included files

- `src/main/java/lk/student/pizza/controller/PizzaController.java`
- `src/main/java/lk/student/pizza/controller/MenuController.java`
- `src/main/java/lk/student/pizza/service/PizzaService.java`
- `src/main/java/lk/student/pizza/repository/PizzaRepository.java`
- `src/main/java/lk/student/pizza/model/MenuItem.java`
- `src/main/java/lk/student/pizza/model/Pizza.java`
- `src/main/resources/templates/pizzas.html`
- `src/main/resources/templates/pizza-form.html`
- `data/pizzas.txt`

## Dependencies and coordination

Shared application/database configuration. Person 4 uses PizzaService to validate order items and calculate totals.

## Shared project files

These are already present in the integrated project and should be merged only once by the team coordinator: `pom.xml`, `application.properties`, `schema.sql`, `LegacyDataImporter.java`, `fragments.html` (navigation), and shared `style.css`. `AuthInterceptor.java` is included with Person 1 because it contains the account routes, but its authorization rules cover multiple modules; merge it only once after coordinating the module routes. This ZIP contains your module contribution files, not a separately runnable application. Preserve the existing folder paths when copying these files into the main `pizza-delivery-management` project.

## Git contribution

Work on a branch for this module, commit the owned files, and open a pull request. Coordinate edits to shared integration files with the relevant module owners.
