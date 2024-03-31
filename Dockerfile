# Step 1: Use the official OpenJDK image with tag for Java 17
FROM openjdk:17-jdk-slim as build

# Step 2: Set the working directory in the Docker image
WORKDIR /app

# Step 3: Copy the Maven/Gradle build file to the image
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Step 4: Copy the source code to the image
COPY src src

# Step 5: Build the application using Maven wrapper
RUN ./mvnw package -DskipTests

# Step 6: Unpack the built application JAR to prepare for the layered JAR
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Step 7: Use the official OpenJDK image to create a smaller runtime image
FROM openjdk:17-jdk-slim

ARG DEPENDENCY=/app/target/dependency

# Step 8: Copy the unpacked application layers from the build stage
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Step 9: Set the entry point to run the application
ENTRYPOINT ["java","-cp","app:app/lib/*","mediaSoft.storage.StorageApplication"]
