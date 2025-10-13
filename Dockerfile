FROM gradle:8-jdk24 AS builder
WORKDIR /app

COPY build.gradle settings.gradle gradle* ./
RUN gradle --version

COPY src ./src

RUN gradle clean bootJar --no-daemon

FROM eclipse-temurin:24-jdk-alpine

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]