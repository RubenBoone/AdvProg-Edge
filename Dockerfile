FROM openjdk:11.0.13-jdk-oracle
EXPOSE 8050
ARG JAR_FILE=target/*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]