FROM adoptopenjdk/openjdk15
ADD target/service-registry-1.0-SNAPSHOT.jar /
CMD java -Dspring.profiles.active=production -jar service-registry-1.0-SNAPSHOT.jar