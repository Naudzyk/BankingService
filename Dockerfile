FROM openjdk:17-jdk-slim-buster
MAINTAINER Mr.Zhenya
ARG JAR_FILE=target/*.jar
COPY ./target/bank-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
