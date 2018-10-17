FROM openjdk:8-jdk-alpine
RUN apk add --update \
    curl \
    && rm -rf /var/cache/apk/*
RUN mkdir /root/app
COPY target/happy-mypet-0.0.1.jar /root/app/app.jar
WORKDIR /root/app
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]