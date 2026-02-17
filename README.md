# PriceMatrix-backend

> PriceMatrix 企業級折扣管理系統 - 後端 | Backend for PriceMatrix Discount Management System

---

## 📖 專案簡介 | About

**繁體中文**

PriceMatrix 後端，使用 Java Spring Boot 建構，提供 RESTful API 給前端使用。  
負責業務邏輯、資料驗證、與 MySQL 資料庫的溝通。

**English**

Backend service for PriceMatrix, built with Java Spring Boot.  
Provides RESTful APIs for the frontend, handling business logic, data validation, and MySQL database operations.

---

## ✨ API 端點 | API Endpoints

| 方法 Method | 路徑 Path | 說明 Description |
|------------|-----------|-----------------|
| GET | `/categories` | 取得所有分類 |
| GET | `/customers/search?name=` | 搜尋客戶 |
| GET | `/discounts/customer/{id}` | 取得客戶折扣清單 |
| POST | `/discounts` | 新增折扣 |
| PUT | `/discounts/{id}` | 更新折扣 |
| DELETE | `/discounts/{id}` | 刪除折扣 |

---

## 🛠️ 技術棧 | Tech Stack

| 層級 | 技術 |
|------|------|
| 語言 Language | Java 17 |
| 框架 Framework | Spring Boot 3.x |
| ORM | Spring Data JPA / Hibernate |
| 資料庫 Database | MySQL 8.0 |
| 建構工具 Build Tool | Maven |

---

## 🏗️ 系統架構 | Architecture

```
Controller  （接收 HTTP 請求）
    ↓
Service     （業務邏輯）
    ↓
Repository  （資料庫操作）
    ↓
Entity      （資料結構定義）
    ↓
MySQL
```

---

## 📁 專案結構 | Project Structure

```
src/main/java/com/pricematrix/pricematrix/
├── entity/
│   ├── Customer.java
│   ├── Category.java
│   ├── Product.java
│   └── Discount.java
├── repository/
│   ├── CustomerRepository.java
│   ├── CategoryRepository.java
│   ├── ProductRepository.java
│   └── DiscountRepository.java
├── service/
│   ├── CustomerService.java
│   ├── CategoryService.java
│   ├── ProductService.java
│   └── DiscountService.java
├── controller/
│   ├── CustomerController.java
│   ├── CategoryController.java
│   ├── ProductController.java
│   └── DiscountController.java
└── PricematrixApplication.java
```

---

## 🚀 本地啟動 | Local Setup

### 前置需求 Prerequisites

- Java 17
- MySQL 8.0
- Maven

### 環境變數設定 Environment Variables

啟動前需設定以下環境變數 | Set the following environment variables before running:

```
DB_URL=jdbc:mysql://localhost:3306/pricematrix?useSSL=false&serverTimezone=Asia/Tokyo&allowPublicKeyRetrieval=true
DB_USERNAME=your_username
DB_PASSWORD=your_password
```

### 啟動 Run

```bash
./mvnw spring-boot:run
```

後端預設運行於 port 8080 | Runs on port 8080 by default.

---

## 🔗 前端 | Frontend

[PriceMatrix Frontend](https://github.com/shen-Private/PriceMatrix)
