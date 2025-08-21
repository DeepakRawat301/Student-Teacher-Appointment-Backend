# Stage 1: Build the application
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Maven wrapper and pom.xml first (for dependency caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application (skip tests to speed up)
RUN ./mvnw clean package -DskipTests

# Stage 2: Run the application with a smaller image
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy only the built JAR file
COPY --from=build /app/target/*.jar app.jar

# Render will pass the port as $PORT
EXPOSE 8080

# Run Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
