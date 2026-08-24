# TasteGo Login & Register Setup Guide

## ✅ Fixed Issues

### 1. **Frontend URLs Updated**
- Changed from `https://tastego-production.up.railway.app/` to `http://localhost:8081/`
- Updated files:
  - ✅ `frontend/register.html`
  - ✅ `frontend/login.html`

### 2. **Backend Setup Required**

Before login/register will work, you need to:

#### Option A: Run with Maven (Recommended)
```bash
cd tastego
mvn spring-boot:run
```

#### Option B: Run the Built JAR
```bash
cd tastego/target
java -jar tastego-0.0.1-SNAPSHOT.jar
```

### 3. **MongoDB Connection**

The app uses MongoDB Atlas Cloud. Ensure:
- ✅ MongoDB URI is configured in `application.properties`
- Internet connection is available
- Network whitelist includes your IP on MongoDB Atlas dashboard

**Current Configuration:**
- Database: `tastego`
- Collections: `user`, `restaurant`, `order`, etc.

---

## 📋 Testing Checklist

1. **Start Backend Server**
   ```bash
   cd c:\xampp\htdocs\Tastego\tastego
   mvn spring-boot:run
   ```
   Wait for message: `Started TasteGoApplication in X seconds`

2. **Open Frontend**
   - Go to: `http://localhost/Tastego/frontend/register.html`
   - Or use: `file:///c:/xampp/htdocs/Tastego/frontend/register.html`

3. **Test Registration**
   - Email: `test@example.com`
   - Password: `password123`
   - Role: `Student`
   - Click Register

4. **Test Login**
   - Use the same email/password
   - Role: `Student`
   - Click Login

---

## 🔧 API Endpoints

### User Registration
```
POST http://localhost:8081/api/user/register
Body: { "email": "...", "password": "...", "role": "student|admin" }
```

### User Login
```
POST http://localhost:8081/api/user/login
Body: { "email": "...", "password": "..." }
```

---

## ⚠️ Troubleshooting

### Error: "Cannot connect to MongoDB"
- Check internet connection
- Verify MongoDB Atlas credentials in `application.properties`
- Check IP whitelist on MongoDB Atlas

### Error: "Connection refused localhost:8081"
- Backend server is not running
- Start it with: `mvn spring-boot:run`

### Error: "User not found" or "Invalid password"
- Ensure you registered first
- Use exact email and password from registration

---

## 📁 Project Structure

```
Tastego/
├── frontend/               # HTML/CSS/JS files
│   ├── register.html      ✅ FIXED
│   ├── login.html         ✅ FIXED
│   └── ...
├── tastego/               # Spring Boot Backend
│   ├── pom.xml           # Maven configuration
│   └── src/
│       ├── main/java/    # Backend code
│       │   └── com/svecw/tastego/
│       │       ├── controller/UserController.java
│       │       ├── service/UserService.java
│       │       ├── model/User.java
│       │       └── repository/UserRepository.java
│       └── resources/
│           └── application.properties  # MongoDB config
└── SETUP_GUIDE.md        # This file
```
