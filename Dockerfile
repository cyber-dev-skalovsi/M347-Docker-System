FROM openjdk:21-jdk-slim AS build

# Maven installieren
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Dependencies zuerst laden
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Source kopieren und builden
COPY src ./src
RUN mvn clean package -DskipTests -B

# Runtime Stage
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/terminkalender-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]