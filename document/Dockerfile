FROM java:openjdk-8-jre-alpine
VOLUME /tmp
ADD wms-internaltool-service-v8.jar wms-internaltool-service-v8.jar
RUN sh -c 'touch /wms-internaltool-service-v8.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/wms-internaltool-service-v8.jar"]
