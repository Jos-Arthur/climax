FROM maven:3-openjdk-17  AS build

WORKDIR /app
ADD pom.xml /app

COPY . /app
RUN mvn clean verify -DskipTests -Pdev
EXPOSE 8080
FROM openjdk:17
COPY --from=build /app/target/climax-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
