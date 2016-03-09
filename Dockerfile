FROM java:8-jre
ADD ["target/dvesta-gateway.jar", "/dvesta-gateway.jar"]
EXPOSE 8093
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "dvesta-gateway.jar"]
