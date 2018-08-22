FROM openjdk:8-slim


ADD build/libs/ /app

RUN mkdir /app/logs && chmod 777 /app/logs

CMD ["java", "-jar", "/app/playground.jar"]