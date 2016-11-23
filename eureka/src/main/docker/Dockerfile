FROM java:8
MAINTAINER keryhu@hotmail.com
VOLUME /tmp
LABEL can-autoremove="true"
EXPOSE 8761
ADD eureka-1.0-SNAPSHOT.jar /app.jar
RUN bash -c 'touch  /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]