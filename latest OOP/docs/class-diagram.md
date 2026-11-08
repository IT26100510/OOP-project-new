# Class diagram (PlantUML)

```plantuml
@startuml
abstract class MenuItem
class Pizza
class Customer
class CustomerOrder
class Delivery
abstract class User
class Admin
class AccountCustomer
abstract class Feedback
class PizzaReview
class DeliveryReview
interface Manageable<T>
class FeedbackManager

MenuItem <|-- Pizza
User <|-- Admin
User <|-- AccountCustomer
Feedback <|-- PizzaReview
Feedback <|-- DeliveryReview
Manageable <|.. FeedbackManager

PizzaController --> PizzaService
CustomerController --> CustomerService
OrderController --> OrderService
OrderController --> DeliveryService
DeliveryController --> DeliveryService
FeedbackController --> FeedbackManager
UserAdminController --> UserService
OrderService --> CustomerService
OrderService --> PizzaService
@enduml
```

## Layer responsibilities

Controllers accept web requests and return Thymeleaf views. Services apply validation and CRUD rules. The pizza service uses Spring Data JPA. Account, customer, order, delivery, and feedback records use JDBC with MySQL. Models represent the application records and shared OOP types.

## OOP concepts to explain in the viva

- **Encapsulation:** model fields are private and updated through validated service/model methods.
- **Inheritance:** `Pizza` extends `MenuItem`; account types extend `User`; `PizzaReview` and `DeliveryReview` extend `Feedback`.
- **Polymorphism:** the feedback manager handles both review subclasses through the shared `Feedback` type.
- **Abstraction:** `MenuItem`, `User`, and `Manageable` define shared contracts.
