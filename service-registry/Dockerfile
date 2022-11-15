FROM adoptopenjdk/openjdk15
ADD target/service-registry-1.0-SNAPSHOT.jar /
CMD java -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=10002 -Dcom.sun.management.jmxremote.rmi.port=10002 -Djava.rmi.server.hostname=localhost -Dcom.sun.management.jmxremote.local.only=false -Dspring.profiles.active=production -jar service-registry-1.0-SNAPSHOT.jar
EXPOSE 9004
EXPOSE 10002