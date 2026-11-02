# Class diagram (PlantUML)

```plantuml
@startuml
abstract class MenuItem {
  - id: String
  - name: String
  - description: String
  - price: double
  - available: boolean
  + getCategory(): String
}
class Pizza {
  - size: String
  + getCategory(): String
}
class Customer {
  - id: String
  - name: String
  - email: String
  - phone: String
  - address: String
}
class CustomerOrder {
  - id: String
  - customerId: String
  - customerName: String
  - pizzaName: String
  - quantity: int
  - total: double
  - status: String
}
class FileDataStore
class PizzaService
class CustomerService
class OrderService
class PizzaController
class CustomerController
class OrderController
MenuItem <|-- Pizza
PizzaService --> FileDataStore
CustomerService --> FileDataStore
OrderService --> FileDataStore
PizzaController --> PizzaService
CustomerController --> CustomerService
OrderController --> OrderService
OrderController --> CustomerService
OrderController --> PizzaService
@enduml
```

## Viva explanation
The controllers receive browser requests. Services apply application rules. `FileDataStore` reads and writes text files. Model classes represent pizzas, customers and orders. `Pizza` inherits from `MenuItem`, demonstrating inheritance. Services use the abstract parent type `MenuItem`, demonstrating polymorphism.
