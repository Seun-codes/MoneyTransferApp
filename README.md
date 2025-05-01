
# 💸 Money Transfer App

A Spring Boot application that simulates money transfers between bank accounts, supporting account creation, secure transfers with transaction fees and commissions, daily summaries, and JWT-based authentication.

---

## 🚀 Features

* ✅ Create and deposit into user accounts
* 🔐 JWT authentication with `/auth/register` and `/auth/login`
* 💰 Money transfer with transaction fee (0.5%, capped at 100)
* 💸 Commission evaluation (20% of fee) via scheduled job
* 📊 Daily summary report of total amount, fees, and commission
* 📦 Redis caching support (optional)
* 🧪 JUnit 5 and MockMvc integration tests

---

## 🛠 Tech Stack

* Java 17+
* Spring Boot
* Spring Security (JWT)
* Spring Data JPA (with MySQL)
* Redis (optional caching)
* Maven
* JUnit 5 + MockMvc

---

## 🔧 Setup Instructions


### 1. Configure MySQL (or use Docker)

Update `application.yml` or `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/money_transfer
spring.datasource.username=root
spring.datasource.password=yourpassword
```

### 2. Build and Run the App

```bash
mvn clean install
mvn spring-boot:run
```

App will start at `http://localhost:8080`

---

## 🔐 Authentication

### Register:

```http
POST /api/auth/register
{
  "username": "admin",
  "password": "admin123"
}
```

### Login:

```http
POST /api/auth/login
{
  "username": "admin",
  "password": "admin123"
}
```

Response contains:

```json
{
  "token": "<JWT_TOKEN>"
}
```

Add this token as a Bearer header for all subsequent requests:

```
Authorization: Bearer <JWT_TOKEN>
```

---

## 📨 API Endpoints

### 🔐 Auth

* `POST /api/auth/register` — register new user
* `POST /api/auth/login` — get JWT token

### 💼 Account

* `POST /api/accounts/create` — create account
* `POST /api/accounts/deposit` — deposit into account

### 💸 Transaction

* `POST /api/transactions/transfer` — transfer funds
* `GET  /api/transactions` — filter transactions
* `GET  /api/transactions/summary?date=yyyy-MM-dd` — get daily summary

---

## 🧪 Running Tests

```bash
mvn test
```

* Integration tests use `MockMvc`
* Authentication is handled automatically during tests

---

## ⏱ Scheduled Jobs

* **Commission Evaluation:** Marks transactions with 20% commission of their fee (if successful)
* **Summary Generation:** Aggregates total transactions, amounts, fees, and commissions per day

These are triggered via `@Scheduled` methods and can be configured using cron expressions.

---

## 📬 Postman Collection

A ready-to-use Postman collection is available in the `/postman` folder (https://blue-crater-713039.postman.co/workspace/My-Workspace~b10aa947-52c6-4bc4-8931-b46fb1d98815/collection/23085713-72f18a55-3fef-4fea-b4f1-38341e720e36?action=share&creator=23085713).


---

## 🙋‍♂️ Author

**Adeniyi Oluwaseun**
