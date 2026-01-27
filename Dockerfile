# --- STAGE 1: THE BUILD (The Factory) ---
# Start with a base image that has Maven and Java 21 pre-installed.
# "AS build" names this stage so it can be referenced later.
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Copy all source code from the current folder (.) into the container (.)
COPY . .

# Run the Maven command to compile code and package it into a .jar file.
# "-DskipTests" is used to speed up deployment (tests are skipped here).
RUN mvn clean package -DskipTests

# --- STAGE 2: THE RUN (The Showroom) ---
# Start fresh with a lightweight version of Linux (Alpine) + Java 21.
# This discards all the heavy Maven tools from the previous stage.
FROM eclipse-temurin:21-jdk-alpine

# Copy ONLY the compiled .jar file from the 'build' stage.
# This keeps the final image small and clean.
COPY --from=build /target/scheduler-api-0.0.1-SNAPSHOT.jar app.jar

# Define the command that runs automatically when the container starts.
ENTRYPOINT ["java", "-jar", "/app.jar"]
