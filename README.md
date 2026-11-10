# Person 5 — Feedback and Reviews

## Assigned module

Submit and list reviews, update/delete own reviews, allow admin review management/replies. FeedbackManager persists reviews while retaining the OOP CRUD/report methods.

## Included files

- `src/main/java/feedback/DeliveryReview.java`
- `src/main/java/feedback/Feedback.java`
- `src/main/java/feedback/FeedbackManager.java`
- `src/main/java/feedback/Manageable.java`
- `src/main/java/feedback/PizzaReview.java`
- `src/main/java/lk/student/pizza/controller/FeedbackController.java`
- `src/main/resources/templates/feedback.html`
- `src/main/resources/templates/feedback-edit.html`

## Dependencies and coordination

Person 1 merges the FeedbackManager bean into UserManagementConfig. Uses PizzaService, CustomerService, and OrderService to validate review targets. Shared navigation and styling are integrated once by the team.

## Shared project files

These are already present in the integrated project and should be merged only once by the team coordinator: `pom.xml`, `application.properties`, `schema.sql`, `LegacyDataImporter.java`, `fragments.html` (navigation), and shared `style.css`. `AuthInterceptor.java` is included with Person 1 because it contains the account routes, but its authorization rules cover multiple modules; merge it only once after coordinating the module routes. This ZIP contains your module contribution files, not a separately runnable application. Preserve the existing folder paths when copying these files into the main `pizza-delivery-management` project.

## Git contribution

Work on a branch for this module, commit the owned files, and open a pull request. Coordinate edits to shared integration files with the relevant module owners.
