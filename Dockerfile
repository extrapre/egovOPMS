FROM egovio/apline-jre:8u121

EXPOSE 8009
# copy pre-built JAR into image
# INSTRUCTIONS ON HOW TO BUILD JAR:
# Move to the location where pom.xml is exist in project and build project using below command
# "mvn clean package"

COPY /target/egov-opms.jar /opt/egov/egov-opms.jar

COPY start.sh /usr/bin/start.sh

RUN chmod +x /usr/bin/start.sh

CMD ["/usr/bin/start.sh"]
