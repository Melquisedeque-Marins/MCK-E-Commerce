FROM openjdk:17
MAINTAINER MCK
WORKDIR /app
ENV APP_PROFILE=dev
COPY ./target/product-service-*.jar ./product-service.jar
EXPOSE 8083
ENTRYPOINT java -jar product-service.jar