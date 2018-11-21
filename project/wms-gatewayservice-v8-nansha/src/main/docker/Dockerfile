FROM java:openjdk-8-jre-alpine
ADD wms-gatewayservice-v8-1.0.0-SNAPSHOT.jar app.jar
VOLUME /tmp
VOLUME /target
RUN sh -c 'touch /app.jar'
EXPOSE 8001
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
