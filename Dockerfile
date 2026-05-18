FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY build/libs/cloud-native-msa-attendance-1.jar cloud-native-msa-attendance.jar

VOLUME /tmp

ENTRYPOINT ["java","-jar","cloud-native-msa-attendance.jar"]