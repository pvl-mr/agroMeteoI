FROM openjdk:17-oracle
EXPOSE 8080
ADD target/*.jar agroserver.jar
ENTRYPOINT ["java", "-jar", "agroserver.jar"]