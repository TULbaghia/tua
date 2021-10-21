FROM maven:3.8.3-openjdk-11

# RUN mvn dependency:go-offline

CMD cd /usr/app && \
    mvn package && \
    chmod 777 /usr/app/target