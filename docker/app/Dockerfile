FROM rancher-server:5000/openjdk:8-jre-alpine
MAINTAINER Rainer Burgstaller

ADD context/web.jar /opt/webshop/web.jar

WORKDIR /opt/webshop
CMD ["java", "-jar", "web.jar"]