FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java","-Xms128m","-Xmx512m","-jar","/app/app.jar"]
