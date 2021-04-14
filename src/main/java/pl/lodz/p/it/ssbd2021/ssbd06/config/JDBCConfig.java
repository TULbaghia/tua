package pl.lodz.p.it.ssbd2021.ssbd06.config;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd06authDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "example",
        password = "example",
        serverName = "<password>",
        portNumber = 5432,
        databaseName = "example",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED,
        transactional = true)

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd06mokDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "postgres",
        password = "-pl,MKO)",
        serverName = "localhost",
        portNumber = 5432,
        databaseName = "example",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED,
        transactional = true)

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd06mohDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "example",
        password = "example",
        serverName = "localhost",
        portNumber = 5432,
        databaseName = "example",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED,
        transactional = true)

@Stateless
public class JDBCConfig {

    @PersistenceContext(unitName = "ssbd06mokPU")
    private EntityManager em1;

}
