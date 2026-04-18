🍽️ TasteGo – Smart Food Ordering & Restaurant Management System

📌 Project Overview
TasteGo is a full-stack web application designed to streamline the food ordering process between students/customers and restaurants. It provides a complete digital solution for:

Ordering food online
Restaurant menu management
Order tracking system
Payment proof verification (UPI screenshots)
PDF-based reporting system for accepted orders

The project is built using Spring Boot (backend) and HTML, CSS, JavaScript (frontend) with REST API integration.

🎯 Objectives
Eliminate manual food ordering in college canteens/restaurants
Provide a structured digital ordering system
Enable restaurants to manage orders efficiently
Provide payment verification using UPI screenshots
Generate downloadable reports for business tracking

✨ Key Features

👨‍🎓 Student / Customer Features
View restaurant menus dynamically
Place food orders with quantity selection
Upload UPI payment screenshot as proof
Track order status:
⏳ Pending
✅ Accepted
❌ Rejected
View token number after acceptance

🏪 Restaurant Dashboard Features
🔐 Authentication
Login using unique restaurant code
Session stored using localStorage
🍽️ Menu Management
Add new menu items dynamically
Update existing menu items
Save menu to backend database
Load previous menu automatically
📦 Order Management
View all incoming orders
Separate sections for:
Pending Orders
Accepted Orders
Accept or Reject orders in real-time
Auto refresh after action
📄 Payment Verification
View UPI payment screenshot uploaded by student
Image modal popup for better viewing
Error handling for missing images
🧾 Token System
Each accepted order generates a token number
Used for order pickup identification
📊 PDF Download Feature (Important)

Restaurants can download all accepted orders in PDF format for record keeping.

📄 PDF Includes:
Student Email
Ordered Items
Quantity
Order Status
Token Number

📌 Benefits:
Offline record maintenance
Easy reporting
Business tracking
Daily order summary

🛠️ Tech Stack

🔙 Backend (Spring Boot)
Java 17+
Spring Boot
Spring Web (REST APIs)
Spring Data JPA
Hibernate
File Storage (Local system)
Maven

🎨 Frontend
HTML5
CSS3
JavaScript (Vanilla JS)
Fetch API for backend communication

🗄️ Database
MySQL / PostgreSQL (configurable)
Tables:
Orders
Menu
Scanner Images (UPI proofs)
Restaurant details
📁 File Storage System
UPI screenshots stored locally:
/uploads/scanners/
📄 PDF Library
jsPDF (Frontend library)

📂 Project Structure
Tastego/
│
├── backend/
│   ├── controller/
│   │   ├── OrderController.java
│   │   ├── MenuController.java
│   │   ├── ScannerImageController.java
│   │
│   ├── model/
│   │   ├── Order.java
│   │   ├── Menu.java
│   │   ├── ScannerImage.java
│   │
│   ├── repository/
│   │   ├── OrderRepository.java
│   │   ├── MenuRepository.java
│   │   ├── ScannerImageRepository.java
│   │
│   ├── service/
│   └── TastegoApplication.java
│
├── frontend/
│   ├── login.html
│   ├── dashboard.html
│   ├── script.js
│   ├── styles.css
│
├── uploads/
│   └── scanners/   (UPI payment screenshots)
│
└── README.md

🔌 API Endpoints
🏪 Restaurant APIs
GET  /api/restaurant/login?code={code}
🍽️ Menu APIs
GET  /api/menu/{restaurantName}
POST /api/menu/upload
📦 Order APIs
GET  /api/admin/orders/pending
GET  /api/admin/orders/accepted
PUT  /api/admin/order/accept?orderId=
PUT  /api/admin/order/reject?orderId=
📱 Payment / Scanner APIs
POST /api/scanner/upload
GET  /api/scanner/view/{fileName}
GET  /api/scanner/base64/{fileName}
