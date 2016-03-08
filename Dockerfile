FROM java:8-jre
ADD ["dvesta-gateway-${project.version}.jar", "/dvesta-gateway.jar"]
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "dvesta-gateway.jar"]
