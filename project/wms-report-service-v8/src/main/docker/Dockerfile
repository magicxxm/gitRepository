FROM java:8
MAINTAINER "hanjianmin"<jianmin.han@mushiny.com>
VOLUME /tmp
ADD wms-report-service-v8.jar app.jar
RUN sh -c 'touch /app.jar'
EXPOSE 11006
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]