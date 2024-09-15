# First stage, build the custom JRE
FROM eclipse-temurin:17-jdk-alpine AS jre-builder

RUN mkdir /opt/app
COPY . /opt/app

WORKDIR /opt/app

# Install Gradle and dependencies
ENV GRADLE_VERSION 8.10.1
ENV GRADLE_HOME /usr/lib/gradle
ENV PATH $GRADLE_HOME/bin:$PATH

RUN apk update && \
    apk add --no-cache tar binutils wget unzip bash

RUN wget https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip && \
    unzip gradle-$GRADLE_VERSION-bin.zip && \
    rm gradle-$GRADLE_VERSION-bin.zip && \
    mv gradle-$GRADLE_VERSION /usr/lib/gradle

# Build the app
RUN gradle build -x test --no-daemon

# Unpack the JAR
RUN jar xvf build/libs/eureka-server-0.0.1-SNAPSHOT.jar
RUN jdeps --ignore-missing-deps -q  \
    --recursive  \
    --multi-release 17  \
    --print-module-deps  \
    --class-path 'BOOT-INF/lib/*'  \
    build/libs/eureka-server-0.0.1-SNAPSHOT.jar > modules.txt

# Build small JRE image
RUN $JAVA_HOME/bin/jlink \
         --verbose \
         --add-modules $(cat modules.txt) \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /optimized-jdk-17

# Second stage, Use the custom JRE and build the app image
FROM alpine:latest
ENV JAVA_HOME=/opt/jdk/jdk-17
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Copy JRE from the first stage
COPY --from=jre-builder /optimized-jdk-17 $JAVA_HOME

# Add app user
ARG APPLICATION_USER=spring

# Create a user to run the application, don't run as root
RUN addgroup --system $APPLICATION_USER &&  adduser --system $APPLICATION_USER --ingroup $APPLICATION_USER

# Create the application directory
RUN mkdir /app && chown -R $APPLICATION_USER /app

# Copy the built jar file from the first stage
COPY --from=jre-builder /opt/app/build/libs/eureka-server-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

USER $APPLICATION_USER

EXPOSE 8761
ENTRYPOINT [ "java", "-jar", "/app/app.jar" ]
