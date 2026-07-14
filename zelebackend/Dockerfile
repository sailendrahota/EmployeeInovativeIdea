# ==========================================
# STAGE 1: The "Kitchen" (Build Stage)
# ==========================================
# We use a heavy image that includes Maven and the full JDK.
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy your pom.xml and source code into the container
COPY pom.xml .
COPY src ./src

# Build the jar (we skip tests here just to make the Docker build faster)
RUN mvn clean package -DskipTests

# ==========================================
# STAGE 2: The "Dining Room" (Run Stage)
# ==========================================
# We throw away the Kitchen and use a tiny, secure Alpine Linux image with ONLY the JRE.
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# [BONUS SECURITY]: Never run a container as the 'root' user!
# We create a restricted 'spring' user and switch to it.
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy ONLY the final compiled .jar file from the 'builder' stage above.
# The wildcard *.jar automatically grabs whatever your jar is named!
COPY --from=builder /app/target/*.jar app.jar

# Expose the standard Spring Boot port
EXPOSE 8080

# Run the app with dynamic, Docker-aware memory limits
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]