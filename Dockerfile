FROM gradle:4.3.0-jdk8-alpine
VOLUME ["/tmp"]
ADD build/libs/product-api-0.0.1-SNAPSHOT.jar /tmp/product-api-0.0.1-SNAPSHOT.jar
RUN sh -c 'touch /tmp/product-api-0.0.1-SNAPSHOT.jar'
EXPOSE 8080
ENTRYPOINT ["sh", "-c",  "java -Xmx512m -Xms256m -jar -Dspring.profiles.active=local /tmp/product-api-0.0.1-SNAPSHOT.jar"]