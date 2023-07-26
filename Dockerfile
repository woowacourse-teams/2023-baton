FROM arm64v8/amazoncorretto:17

WORKDIR /app

COPY ./build/libs/baton-0.0.1-SNAPSHOT.jar /app/baton.jar

CMD ["java", "-jar", "baton.jar"]
