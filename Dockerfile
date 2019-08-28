FROM openjdk:11.0-jdk as build
WORKDIR /workdir
COPY ["gradlew", "settings.gradle.kts", "build.gradle.kts", "./"]
COPY gradle gradle
RUN ./gradlew -Dorg.gradle.daemon=false dependencies

COPY src src
RUN ./gradlew --version
RUN ./gradlew bootJar

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build "/workdir/build/libs/igorprj-0.1.0.jar" .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "igorprj-0.1.0.jar"]
