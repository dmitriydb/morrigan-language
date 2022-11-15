FROM adoptopenjdk/openjdk15
ADD target/coderun-load-balancer-1.0-SNAPSHOT.jar /
CMD java -Dspring.profiles.active=production -jar coderun-load-balancer-1.0-SNAPSHOT.jar
EXPOSE 8866