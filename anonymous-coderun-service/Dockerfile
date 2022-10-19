FROM adoptopenjdk/openjdk15
ADD target/anonymous-coderun-service-1.0-SNAPSHOT.jar /
CMD java -jar anonymous-coderun-service-1.0-SNAPSHOT.jar -Dspring.profiles.active=production
EXPOSE 8005