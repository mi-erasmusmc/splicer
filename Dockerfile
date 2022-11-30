FROM maven:3.8-eclipse-temurin-17-alpine AS builder
RUN mkdir /project
COPY . /project
WORKDIR /project
RUN mvn dependency:go-offline -B
RUN mvn clean package assembly:single -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN mkdir /app/data
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
COPY --from=builder /project/target/splicer-1.0-SNAPSHOT-jar-with-dependencies.jar /app/java-application.jar
COPY data /app/data
RUN chown -R javauser:javauser /app
USER javauser
CMD "java" "-Ddb_conn=$DB_CONN" "-Ddb_user=$DB_USER" "-Ddb_pass=$DB_PASS" "-jar" "java-application.jar"
