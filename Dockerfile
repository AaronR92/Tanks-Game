FROM openjdk:17
ADD target/Tanks-Game-1.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]