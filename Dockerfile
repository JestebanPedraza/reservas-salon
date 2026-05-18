# Stage 1: Build stage with full SDK
FROM maven:3.9-eclipse-temurin-25 AS dependencies

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Stage 2: Build application
FROM maven:3.9-eclipse-temurin-25 AS builder

WORKDIR /app
COPY --from=dependencies /root/.m2 /root/.m2
COPY --from=dependencies /app/pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 3: Runtime stage with minimal footprint (Java 25)
FROM eclipse-temurin:25-jre-alpine AS runtime

# Install curl for health checks
RUN apk add --no-cache curl dumb-init

# Create non-root user
RUN addgroup -g 1001 -S appuser && \
    adduser -S appuser -G appuser -u 1001

WORKDIR /app

# Copy built artifact from builder stage
COPY --from=builder /app/target/reservas-0.0.1-SNAPSHOT.jar app.jar

# Set ownership to non-root user
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Configure JVM for container environment
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:+UseG1GC \
    -Djava.security.egd=file:/dev/./urandom \
    -Dserver.port=8080"

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

# Use dumb-init for proper signal handling
ENTRYPOINT ["dumb-init", "--"]
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
