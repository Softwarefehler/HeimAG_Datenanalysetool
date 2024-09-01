# Stage 1: Build the application and Vue.js project
FROM gradle:8.8.0-jdk21-alpine AS kotlin_build
ARG ROOT_FOLDER="."
RUN apk add --no-cache nodejs npm
WORKDIR "/source"
COPY "$ROOT_FOLDER" "."
RUN gradle \
    -Ddevelopment=false \
    -Dorg.gradle.vfs.watch=false \
    -Dorg.gradle.parallel=false \
    install-vue build-vue buildFatJar

# Stage 2: Prepare the runtime environment
FROM alpine:3.20 AS runtime
ENV DISABLE_HTTPS="false"
RUN apk add --no-cache openjdk21-jre
WORKDIR "/app"

# Copy the built Vue.js project and the Fat JAR from the build stage
COPY --from=kotlin_build "/source/src/main/vue-project/dist" "/app/src/main/vue-project/dist"
COPY --from=kotlin_build "/source/build/libs/Datenanalysetool-all.jar" "/app/Datenanalysetool-all.jar"

# Copy resources like images, configuration files, etc.
COPY --from=kotlin_build "/source/src/main/resources" "/app/resources"

# Expose the ports for HTTP and HTTPS
EXPOSE 8080 8443

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/Datenanalysetool-all.jar"]
