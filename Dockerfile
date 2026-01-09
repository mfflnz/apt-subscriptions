FROM openjdk:27-ea-jdk

COPY /target/apt-subscriptions-1.0-SNAPSHOT-jar-with-dependencies.jar /app/app.jar

ENV DISPLAY=:0

CMD ["java", "-cp", "/app/app.jar", "org.blefuscu.apt.subscriptions.SubscriptionsSwingApp"]
