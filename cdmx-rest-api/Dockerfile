FROM openjdk:8-jre
COPY cdmx-rest-api-1.0-SNAPSHOT/ /loc
EXPOSE 9000 9443
RUN chmod -R 777 /loc/*
CMD /loc/bin/cdmx-rest-api -Dhttps.port=9443 -Dplay.crypto.secret=7sTQDxvurDYhAQ5W