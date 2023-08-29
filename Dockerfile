FROM openjdk:17-alpine

RUN apk add --no-cache bash

WORKDIR /app

COPY  /home/runner/work/sprint-boot-login/sprint-boot-login/target/spring-boot-login-0.0.1-SNAPSHOT.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
