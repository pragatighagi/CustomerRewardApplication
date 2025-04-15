# 🏆 Customer Reward Points Application

A Spring Boot application to calculate customer reward points based on transaction history over the past 3 months. The system also provides endpoints to save transactions and retrieve monthly and total rewards.

---

## 🚀 Features

✅ Exposes RESTful APIs to:
- Save customer transactions
- Calculate reward points based on amount spent
- Return month-wise and total rewards

✅ Built-in capabilities:
- Input validation using `jakarta.validation`
- Exception handling via `@ControllerAdvice`
- DTO ↔ Entity conversion using **ModelMapper**
- In-memory **H2 database** for easy setup and testing

---

## 🛠️ Tech Stack

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Web**
- **Spring Validation (jakarta.validation)**
- **Spring Data JPA (Hibernate)**
- **H2 Database**
- **Lombok**
- **ModelMapper**

---
## 🧮 Reward Calculation Logic

- For every dollar spent over **$100**: 2 points
- For every dollar spent between **$50 and $100**: 1 point
- No rewards for purchases $50 or below
- 
### 🧾 Example:

A transaction of **$120** earns:
- (120 - 100) * 2 = 40 points
- (100 - 50) * 1 = 50 points  
  ✅ **Total = 90 points**

---
🧱 Project Structure
src
├── main
│   ├── java
│   │   └── com.SpringbootApplication.CustomerRewardApplication
│   │       ├── controller                 --> REST API Controller
│   │       ├── entity                     --> Entity class (e.g., Transaction)
│   │       ├── exception                  --> Custom exceptions & global handler
│   │       │   ├── CustomerNotFoundException
│   │       │   └── GlobalExceptionHandler
│   │       ├── payload                    --> DTOs
│   │       │   ├── RewardsDTO
│   │       │   └── TransactionDTO
│   │       ├── repository                 --> JPA repository interface
│   │       │   └── TransactionRepository
│   │       ├── service                    --> Business logic
│   │       │   ├── RewardsService
│   │       │   └── RewardsServiceImpl
│   │       └── CustomerRewardApplication  --> Main application class
│   └── resources
│       └── application.properties         --> Application configuration
├── test
│   └── java
│       └── com.SpringbootApplication.CustomerRewardApplication
│           ├── CustomerRewardApplicationTests  --> Main test class
│           ├── RewardsIntegrationTest          --> Integration tests
│           └── RewardsServiceImplTest          --> Unit tests for service logic

---
## 📤 POST /api/transactions
Saves a list of customer transactions to the system. Each transaction includes the customerId, transactionAmount, and transactionDate.

📥 Sample Request Body:
json
[
{
"customerId": 103,
"transactionAmount": 35.0,
"transactionDate": "2025-03-20T00:00:00.000+00:00"
},
{
"customerId": 103,
"transactionAmount": 75.0,
"transactionDate": "2025-03-15T00:00:00.000+00:00"
},
{
"customerId": 103,
"transactionAmount": 120.0,
"transactionDate": "2025-03-10T00:00:00.000+00:00"
},
{
"customerId": 103,
"transactionAmount": 200.0,
"transactionDate": "2025-02-22T00:00:00.000+00:00"
},
{
"customerId": 103,
"transactionAmount": 60.0,
"transactionDate": "2025-02-10T00:00:00.000+00:00"
},
{
"customerId": 103,
"transactionAmount": 110.0,
"transactionDate": "2025-01-25T00:00:00.000+00:00"
},
{
"customerId": 103,
"transactionAmount": 130.0,
"transactionDate": "2025-01-05T00:00:00.000+00:00"
}
]
---
## ✅ Successful Response
json

{
"message": "Transactions saved successfully!"
}

---
## 📥 GET /api/rewards
Returns a JSON map of customer reward points, broken down by month and total.

📤 Sample Response:
{
"customerId": 103,
"monthlyRewards": {
"March": 230,
"February": 520,
"January": 360
},
"totalRewards": 1110
}

---
## ▶️ Run the Application
Clone the repository

Open in your favorite IDE

Run CustomerRewardApplication.java

Access APIs at: http://localhost:9090/api

---

