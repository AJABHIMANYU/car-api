FROM openjdk:17-oracle
COPY ./target/CarApiGateway-0.0.1-SNAPSHOT.jar CarApiGateway.jar
CMD ["java","-jar","CarApiGateway.jar"]