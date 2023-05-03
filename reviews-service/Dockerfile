FROM openjdk:17
MAINTAINER MCK
WORKDIR /app
COPY ./target/reviews-service-*.jar ./review-service.jar
EXPOSE 8084
ENTRYPOINT java -jar review-service.jar