FROM adoptopenjdk/openjdk15
ADD target/anonymous-coderun-service-1.0-SNAPSHOT.jar /
CMD java -Dspring.profiles.active=production -jar anonymous-coderun-service-1.0-SNAPSHOT.jar
EXPOSE 8005