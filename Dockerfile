FROM maven:3.6.3-jdk-11-slim as builder
WORKDIR /build
COPY . .
RUN mvn clean package

FROM paper:1.16.1
WORKDIR /opt/spigot
COPY --from=builder /build/target/hcmc-0.1.0-jar-with-dependencies.jar /opt/spigot/plugins/hcmc.jar
