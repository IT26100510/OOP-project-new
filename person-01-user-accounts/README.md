# Person 1 — User Accounts

## Assigned module

Create/register accounts, view accounts, edit profile/role-specific data and passwords, delete accounts. Admin accounts cannot delete their own active session.

## Included files

- `src/main/java/lk/student/pizza/controller/UserAdminController.java`
- `src/main/java/lk/student/pizza/controller/AuthController.java`
- `src/main/java/lk/student/pizza/controller/AuthInterceptor.java`
- `src/main/java/lk/student/pizza/service/AuthService.java`
- `src/main/java/lk/student/pizza/usermanagement/Admin.java`
- `src/main/java/lk/student/pizza/usermanagement/Customer.java`
- `src/main/java/lk/student/pizza/usermanagement/InvalidCredentialsException.java`
- `src/main/java/lk/student/pizza/usermanagement/JdbcUserRepository.java`
- `src/main/java/lk/student/pizza/usermanagement/PasswordHasher.java`
- `src/main/java/lk/student/pizza/usermanagement/User.java`
- `src/main/java/lk/student/pizza/usermanagement/UserRepository.java`
- `src/main/java/lk/student/pizza/usermanagement/UserService.java`
- `src/main/java/lk/student/pizza/usermanagement/ValidationException.java`
- `src/main/java/lk/student/pizza/config/UserManagementConfig.java`
- `src/main/resources/templates/users.html`
- `src/main/resources/templates/user-form.html`
- `src/main/resources/templates/login.html`
- `src/main/resources/templates/register.html`

## Dependencies and coordination

Shared database configuration and schema; customer-profile module for keeping registered accounts in sync; Person 5 adds the FeedbackManager bean to UserManagementConfig (merge that shared config once).

## Shared project files

These are already present in the integrated project and should be merged only once by the team coordinator: `pom.xml`, `application.properties`, `schema.sql`, `LegacyDataImporter.java`, `fragments.html` (navigation), and shared `style.css`. `AuthInterceptor.java` is included with Person 1 because it contains the account routes, but its authorization rules cover multiple modules; merge it only once after coordinating the module routes. This ZIP contains your module contribution files, not a separately runnable application. Preserve the existing folder paths when copying these files into the main `pizza-delivery-management` project.

## Git contribution

Work on a branch for this module, commit the owned files, and open a pull request. Coordinate edits to shared integration files with the relevant module owners.
