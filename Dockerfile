FROM openjdk:17
ADD build/libs/springboot-mysql-docker.jar springboot-mysql-docker.jar
ENTRYPOINT ["java", "-jar", "/springboot-mysql-docker.jar"]

