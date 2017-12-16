FROM java:8

ADD server/target/classes /app/server/classes
ADD server/target/dependency /app/server/dependency
ADD ui /app/ui

EXPOSE 8080

WORKDIR /app
CMD java -cp server/classes:server/dependency/* com.romanstolper.rateeverything.startup.Main