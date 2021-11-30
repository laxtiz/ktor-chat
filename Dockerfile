FROM openjdk:11-jre-slim
EXPOSE 8080

WORKDIR /app
COPY ./build/install/ktor-chat/ /app

CMD [ "./bin/ktor-chat" ]