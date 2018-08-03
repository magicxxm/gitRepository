FROM java:8
MAINTAINER "hanjianmin"<jianmin.han@mushiny.com>
VOLUME /tmp
ADD wms-internaltool-service-v8-1.0.SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
EXPOSE 11007
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]