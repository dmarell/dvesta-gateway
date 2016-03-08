FROM java:8-jre
ADD ["target/dvesta-gateway.jar", "/dvesta-gateway.jar"]
EXPOSE 10102
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "dvesta-gateway.jar"]
