# Spring Boot Login  with Spring Security, PostgreSQL and JWT
This code has been created following the tutorial of Bezcoder, the post called Spring Boot Token based Authentication with Spring Security & JWT which is available [here](https://www.bezkoder.com/spring-boot-jwt-authentication/)
## User Registration, Login and Authorization process.

![spring-boot-login-flow](spring-boot-login-flow.png)

## Spring Boot Server Architecture with Spring Security
You can have an overview of our Spring Boot Server with the diagram below:

![spring-boot-login-architecture](spring-boot-login-architecture.png)

## Run Spring Boot application
```
mvn clean install (for skipping test, add -Dmaven.test.skip=true)
mvn spring-boot:run
```

## Run following SQL insert statements
```
INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
```
