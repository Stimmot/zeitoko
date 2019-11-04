FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY target/odra-test.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]