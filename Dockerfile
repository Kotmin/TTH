FROM eclipse-temurin:17-jdk-jammy AS deps
WORKDIR /app
COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle/ gradle/
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon -q

FROM deps AS build
COPY src/ src/
RUN ./gradlew installDist --no-daemon -q

FROM build AS test
RUN ./gradlew test --no-daemon

FROM eclipse-temurin:17-jre-jammy AS runtime
WORKDIR /app
COPY --from=build /app/build/install/alert-rule-engine/ ./
ENTRYPOINT ["./bin/alert-rule-engine"]
