FROM java:8
MAINTAINER "hanjianmin"<jianmin.han@mushiny.com>
VOLUME /tmp
ADD wms-registryservice-v8-1.0.0-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8761 8762
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
