FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app
COPY . .
RUN mvn package -DskipTests

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/target/vulnarebel.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]