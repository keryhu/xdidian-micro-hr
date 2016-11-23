FROM java:8
MAINTAINER keryhu@hotmail.com
VOLUME /tmp
LABEL can-autoremove="true"
RUN mkdir /app
EXPOSE 9999
ADD auth-server-1.0-SNAPSHOT.jar /app/app.jar
COPY runboot.sh /app/
RUN bash -c 'touch  /app/app.jar'
WORKDIR /app
RUN chmod a+x runboot.sh
CMD /app/runboot.sh
