# Use Java 17
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy project files
COPY . .

# Build jar
RUN ./mvnw clean package -DskipTests

# Run app
CMD ["java", "-jar", "target/*.jar"]