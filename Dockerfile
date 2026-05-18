## BUILD

FROM eclipse-temurin:21-jdk AS build

WORKDIR /app
COPY . .
RUN ./gradlew clean bootJar

## DEPLOY

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY build/libs/igloo.jar igloo.jar

VOLUME /tmp

ENTRYPOINT ["java","-jar","igloo.jar"]