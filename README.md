# Spring Boot MongoDB + Email Integration Project

This is a Spring Boot application that connects to MongoDB Atlas and enables email functionality using SMTP with app password authentication.

## Features

- Connects to a cloud-hosted MongoDB Atlas database
- Sends emails using JavaMailSender (Gmail SMTP)
- Uses environment variables for sensitive configuration (Mongo URI, email credentials)
- Example use cases: User registration, email verification, appointment booking, etc.

---

## Technologies Used

- Java 17+
- Spring Boot 3.x
- Spring Data MongoDB
- Spring Mail (JavaMailSender)
- MongoDB Atlas
- Maven

---

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/your-repo-name.git
cd your-repo-name

MONGO_DB_URI=your_mongodb_atlas_uri
EMAIL_USERNAME=your_email@gmail.com
EMAIL_PASSWORD=your_app_password


spring.data.mongodb.uri=${MONGO_DB_URI}

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${EMAIL_USERNAME}
spring.mail.password=${EMAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true



./mvnw spring-boot:run

 
