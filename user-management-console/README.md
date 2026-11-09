# User Management (Member 01) - Pizza Ordering System

## Run
    mkdir out
    javac -d out src/pizza/user/*.java
    java -cp out pizza.user.Main

First run creates a default admin: admin@pizza.com / Admin1234
Users are stored in users.txt (id|ROLE|name|email|salt|hash|extra1|extra2).

## Classes
- User (abstract) -> Customer, Admin        : abstraction, inheritance, encapsulation
- UserRepository (interface) -> FileUserRepository : abstraction, file handling
- PasswordHasher                            : salted SHA-256
- UserService (CRUD + validation), AuthService (login/logout/current user)
- InvalidCredentialsException, ValidationException : custom exceptions
- Main                                      : console menu
