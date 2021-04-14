package pl.lodz.p.it.ssbd2021.ssbd06.config;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd06authDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "<user>",
        password = "<password>",
        serverName = "<server>",
        portNumber = 5432,
        databaseName = "<dbname>",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED,
        transactional = true)

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd06mokDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "<user>",
        password = "<password>>",
        serverName = "<server>",
        portNumber = 5432,
        databaseName = "<dbname>",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED,
        transactional = true)

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd06mohDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "<user>",
        password = "<password>",
        serverName = "<server>",
        portNumber = 5432,
        databaseName = "<dbname>",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED,
        transactional = true)

@Stateless
public class JDBCConfig {

}
