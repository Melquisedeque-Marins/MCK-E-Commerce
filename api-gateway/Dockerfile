FROM maven:3.8.6-openjdk-18 as build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17
WORKDIR /app
ENV APP_PROFILE=docker
COPY --from=build ./build/target/*.jar ./api-gateway.jar
ENTRYPOINT [ "java", "-jar", "api-gateway.jar" ]
EXPOSE 8080
