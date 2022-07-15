FROM openjdk:11-oracle
COPY "./target/movement-service-1.0.0.jar" "/app/movement-service-1.0.0.jar"
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "/app/movement-service-1.0.0.jar"]