# 🏆 Customer Reward Points Application

A Spring Boot application to calculate customer reward points based on transaction history over the past 3 months. The system also provides endpoints to save transactions and retrieve monthly and total rewards.

---

## 🚀 Features

- RESTful API to:
    - Save customer transactions
    - Calculate reward points based on amount spent
- Monthly and total reward breakdown
- Validation using Spring Validator (`jakarta.validation`)
- In-memory H2 database (easy to run and test)
- ModelMapper for DTO ↔ Entity mapping

---

## 🛠️ Tech Stack

- Java 17+
- Spring Boot 3.x
- Spring Web, Spring Validation
- H2 Database
- JPA/Hibernate
- Lombok
- ModelMapper

---

## 🧮 Reward Calculation Logic

- For every dollar spent over **$100**: 2 points
- For every dollar spent between **$50 and $100**: 1 point
- No rewards for purchases $50 or below

**Example**:  
Transaction of `$120` yields:  
→ `(120 - 100) * 2 + (100 - 50) * 1 = 40 + 50 = 90 points`

---
