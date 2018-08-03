FROM java:openjdk-8-jre-alpine
VOLUME /tmp
ADD wcs-1.0.jar wcs-1.0.jar
RUN sh -c 'touch /wcs-1.0.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/wcs-1.0.jar"]
