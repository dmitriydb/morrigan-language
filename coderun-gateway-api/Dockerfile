FROM adoptopenjdk/openjdk15
ADD target/coderun-gateway-api-1.0-SNAPSHOT.jar /
CMD java -Dspring.profiles.active=production -jar coderun-gateway-api-1.0-SNAPSHOT.jar
EXPOSE 8181