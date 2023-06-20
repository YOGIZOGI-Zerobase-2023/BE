FROM openjdk:11

# Gradle 설치
RUN wget https://services.gradle.org/distributions/gradle-7.6.1-bin.zip -P /tmp
RUN unzip -d /opt/gradle /tmp/gradle-7.6.1-bin.zip
ENV GRADLE_HOME=/opt/gradle/gradle-7.6.1
ENV PATH=$PATH:$GRADLE_HOME/bin

WORKDIR /app
COPY . /app

CMD ["java", "-jar", "app.jar"]