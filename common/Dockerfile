FROM azul/zulu-openjdk:17

ARG JAR_FILE=/build/libs/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 80

ENV TZ Asia/Seoul

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "-Djava.net.preferIPv4Stack=true", "/app.jar"]