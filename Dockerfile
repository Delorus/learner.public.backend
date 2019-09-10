FROM openjdk:11.0-jdk as build
WORKDIR /workdir
COPY ["gradlew", "settings.gradle.kts", "build.gradle.kts", "./"]
COPY gradle gradle
RUN ./gradlew --parallel --no-daemon --refresh-dependencies dependencies

COPY src src
RUN ./gradlew --parallel --no-daemon --no-rebuild bootJar

FROM openjdk:11-jre-slim
WORKDIR /app
COPY vmoptions vmoptions
COPY --from=build "/workdir/build/libs/igorprj-0.1.0.jar" .
EXPOSE 8080
ENTRYPOINT ["java", "@vmoptions", "-jar", "igorprj-0.1.0.jar"]
