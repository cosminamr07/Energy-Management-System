FROM maven:3.8.3-openjdk-17 AS builder

COPY ./src/ /root/src
COPY ./pom.xml /root/
WORKDIR /root
RUN mvn clean package -DskipTests

FROM openjdk:17

ENV TZ=UTC

COPY --from=builder /root/target/device-service-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8081:8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]