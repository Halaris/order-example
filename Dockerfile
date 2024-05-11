FROM amazoncorretto:22
LABEL authors="Al"
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]