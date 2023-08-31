FROM eclipse-temurin:17

WORKDIR /app

COPY  staging/spring-boot-login-0.0.1-SNAPSHOT.jar /app/spring-boot-login.jar

ENTRYPOINT ["java", "-jar", "spring-boot-login.jar"]
