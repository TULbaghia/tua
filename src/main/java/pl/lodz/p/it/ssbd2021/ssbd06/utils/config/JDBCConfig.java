package pl.lodz.p.it.ssbd2021.ssbd06.utils.config;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
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
        transactional = true,
        maxPoolSize = 32,
        minPoolSize = 8)

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd06mokDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "<user>",
        password = "<password>",
        serverName = "<server>",
        portNumber = 5432,
        databaseName = "<dbname>",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED,
        transactional = true,
        maxPoolSize = 32,
        minPoolSize = 8)

@DataSourceDefinition(
        name = "java:app/jdbc/ssbd06mohDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "<user>",
        password = "<password>",
        serverName = "<server>",
        portNumber = 5432,
        databaseName = "<dbname>",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED,
        transactional = true,
        maxPoolSize = 32,
        minPoolSize = 8)

@Stateless
public class JDBCConfig {

}
