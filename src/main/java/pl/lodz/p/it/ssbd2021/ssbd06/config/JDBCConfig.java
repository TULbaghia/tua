package pl.lodz.p.it.ssbd2021.ssbd06.config;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd06authDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "example",
        password = "example",
        serverName = "localhost",
        portNumber = 5432,
        databaseName = "example")

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd06mokDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "example",
        password = "example",
        serverName = "localhost",
        portNumber = 5432,
        databaseName = "example")

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd06mohDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "example",
        password = "example",
        serverName = "localhost",
        portNumber = 5432,
        databaseName = "example")

@Stateless
public class JDBCConfig {

    @PersistenceContext(unitName = "ssbd06mokPU")
    private EntityManager em1;

}
