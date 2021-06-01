FROM java:8
VOLUME /tmp
ADD *.jar eureka-server-0.0.1-SNAPSHOT.jar
RUN bash -c 'touch /eureka-server-0.0.1-SNAPSHOT.jar'
ENV JAVA_OPTS='-Xms800m -Xmx800m'
EXPOSE 8010
ENTRYPOINT exec java  $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /eureka-server-0.0.1-SNAPSHOT.jar