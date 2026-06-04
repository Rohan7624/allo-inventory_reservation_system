# Inventory Reservation System

## Overview

This project is an implementation of the **Allo Engineering Take-Home Exercise**. The system addresses a common e-commerce challenge: preventing overselling during the checkout process when multiple customers attempt to purchase the same inventory simultaneously.

Instead of reducing inventory immediately when a customer adds items to a cart, the application creates a temporary reservation. The reserved inventory is held for a fixed duration (10 minutes), allowing the customer time to complete payment. If the payment succeeds, the reservation is confirmed. If the payment fails or expires, the reserved inventory is released back into available stock.

The solution focuses on **correctness under concurrency**, **inventory consistency**, and a smooth user experience.

---

# Live Application

**Live URL**

https://allo-inventory-reservation-system-pxnb.onrender.com/

The deployed application is pre-seeded with sample products, warehouses, inventory records, and reservations, allowing reviewers to test the complete workflow without any additional setup.

---

# Features

## Inventory Management

* Product management
* Warehouse management
* Inventory tracking per warehouse
* Available stock calculation

Available Stock = Total Stock − Reserved Stock

## Reservation Management

* Create reservation
* Confirm reservation
* Release reservation manually
* Automatic expiration handling
* Reservation status tracking

### Reservation States

* PENDING
* CONFIRMED
* RELEASED

## User Interface

* Product inventory listing
* Warehouse-wise stock visibility
* Reservation creation
* Live reservation countdown timer
* Reservation confirmation
* Reservation cancellation
* Real-time UI updates without page refresh
* Error handling for expired reservations and insufficient stock

---

# Concurrency Handling

The most critical requirement of this exercise is preventing overselling when multiple users attempt to reserve the same inventory simultaneously.

To ensure correctness, the application uses **database-level pessimistic locking**.

### Approach

During reservation creation:

1. The inventory row is locked using:

```sql
SELECT ... FOR UPDATE
```

2. Available stock is calculated inside a transaction.
3. If sufficient stock exists:

   * Reserved stock is increased.
   * Reservation is created.
4. If insufficient stock exists:

   * HTTP 409 Conflict is returned.

### Result

If two requests attempt to reserve the last available unit at the same time:

* Exactly one request succeeds.
* The second request receives a 409 Conflict response.

This guarantees inventory consistency and eliminates race conditions.

---

# Technology Stack

## Backend

* Java 17
* Spring Boot 3
* Spring Data JPA
* Hibernate
* Maven

## Database

* PostgreSQL

## Frontend

* HTML
* CSS
* JavaScript

## Deployment

* Render
* PostgreSQL (Hosted)

---

# API Endpoints

## Products

### Get Products

```http
GET /api/products
```

Returns all products along with warehouse inventory information.

---

## Warehouses

### Get Warehouses

```http
GET /api/warehouses
```

Returns all available warehouses.

---

## Reservations

### Create Reservation

```http
POST /api/reservations
```

Creates a reservation if sufficient stock is available.

Responses:

* 201 Created
* 409 Conflict (Insufficient Stock)

---

### Confirm Reservation

```http
POST /api/reservations/{id}/confirm
```

Confirms a reservation after successful payment.

Responses:

* 200 OK
* 410 Gone (Reservation Expired)

---

### Release Reservation

```http
POST /api/reservations/{id}/release
```

Releases reserved inventory back into stock.

Responses:

* 200 OK

---

# Database Design

## Products

| Column      | Type    |
| ----------- | ------- |
| id          | BIGINT  |
| name        | VARCHAR |
| description | TEXT    |

---

## Warehouses

| Column   | Type    |
| -------- | ------- |
| id       | BIGINT  |
| name     | VARCHAR |
| location | VARCHAR |

---

## Inventory

| Column         | Type    |
| -------------- | ------- |
| id             | BIGINT  |
| product_id     | BIGINT  |
| warehouse_id   | BIGINT  |
| total_stock    | INTEGER |
| reserved_stock | INTEGER |

---

## Reservations

| Column       | Type      |
| ------------ | --------- |
| id           | BIGINT    |
| inventory_id | BIGINT    |
| quantity     | INTEGER   |
| status       | VARCHAR   |
| created_at   | TIMESTAMP |
| expires_at   | TIMESTAMP |

---

# Reservation Expiry Strategy

The application currently uses a **lazy expiration approach**.

When a reservation is confirmed:

1. The reservation is loaded.
2. The current timestamp is compared with the reservation expiry time.
3. If the reservation has expired:

   * Reserved inventory is released automatically.
   * Reservation status changes to RELEASED.
   * HTTP 410 Gone is returned.
4. Otherwise:

   * The reservation is confirmed successfully.

### Production Considerations

For a production-scale system, the following enhancements would be recommended:

* Scheduled cleanup jobs
* Background workers
* Event-driven expiration processing
* Redis-based distributed locking
* Message queues (Kafka/RabbitMQ)

---

# Running Locally

## Prerequisites

* Java 17+
* Maven 3.9+
* PostgreSQL

---

## Clone Repository

```bash
git clone https://github.com/Rohan7624/allo-inventory_reservation_system.git

cd allo-inventory_reservation_system
```

---

## Configure Database

Create a PostgreSQL database and update your application configuration.

Example:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/inventory_db
spring.datasource.username=postgres
spring.datasource.password=password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## Build and Run

```bash
mvn clean install

mvn spring-boot:run
```

Application will start at:

```text
http://localhost:8081
```

---

# Seed Data

The database contains sample:

* Products
* Warehouses
* Inventory records

This allows immediate testing of the reservation workflow after startup.

---

# Trade-offs

To keep the implementation focused and aligned with the exercise scope, the following trade-offs were made:

* Lazy expiration instead of scheduled cleanup jobs
* Single application instance assumption
* No distributed locking
* No Redis integration
* No idempotency implementation
* Simplified frontend styling

These decisions allowed more focus on inventory correctness and concurrency safety.

---

# Future Improvements

Given additional development time, I would implement:

* Redis-based distributed locking
* Scheduled reservation cleanup
* Idempotency-Key support
* Background worker processing
* Docker containerization
* Kubernetes deployment
* Audit logging
* Metrics and monitoring
* Integration testing
* Load testing
* Event-driven architecture using Kafka

---

# Assumptions

* Inventory is maintained separately for each warehouse.
* Reservations remain valid for 10 minutes.
* Stock becomes unavailable immediately after reservation creation.
* Confirmed reservations represent successful purchases.
* Inventory consistency takes priority over throughput.

---

# Submission Details

## GitHub Repository

https://github.com/Rohan7624/allo-inventory_reservation_system

## Live Deployment

https://allo-inventory-reservation-system-pxnb.onrender.com/

---

# Author

**Jagadish Pradhan**

Java Developer

Skills:

* Java
* Spring Boot
* Hibernate
* JPA
* PostgreSQL
* SQL
* Maven

GitHub:
https://github.com/Rohan7624

---

# Conclusion

This project demonstrates a concurrency-safe inventory reservation system designed to prevent overselling during checkout. The implementation focuses on transactional consistency, proper stock reservation handling, and a clean end-to-end workflow while maintaining simplicity and readability.

Thank you for reviewing this submission. I look forward to discussing the implementation, design decisions, concurrency strategy, and potential production enhancements during the debrief session.
