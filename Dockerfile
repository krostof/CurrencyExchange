## Etap budowy
#FROM maven:3.8.4-openjdk-17 AS build
#WORKDIR /app
#COPY pom.xml .
#COPY src ./src
#RUN mvn clean install
#RUN ls -al /app/target
#
## Etap produkcji
#FROM openjdk:17-alpine
#WORKDIR /app
#COPY --from=build /app/target/CurrencyExchange-0.0.1-SNAPSHOT.jar ./currency-exchange.jar
#EXPOSE 8080
#CMD ["java", "-jar", "currency-exchange.jar"]

FROM eclipse-temurin:17-jre-alpine
COPY target/CurrencyExchange.jar /currency-exchange.jar
ENTRYPOINT ["java","-jar","/currency-exchange.jar"]
