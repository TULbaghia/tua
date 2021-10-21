FROM jboss/wildfly

USER root

COPY wait-for-it.sh /usr/wait-for-it.sh
RUN chmod +x /usr/wait-for-it.sh


USER jboss
RUN /bin/bash -c '/opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0 &' && \
    /usr/wait-for-it.sh 127.0.0.1:9990 && \
    /opt/jboss/wildfly/bin/jboss-cli.sh -c command='/subsystem=undertow/application-security-domain=other:write-attribute(name=integrated-jaspi, value=false)' && \
    /opt/jboss/wildfly/bin/jboss-cli.sh -c command=:reload && \
    /usr/wait-for-it.sh 127.0.0.1:9990

CMD /opt/jboss/wildfly/bin/standalone.sh -b 0.0.0.0