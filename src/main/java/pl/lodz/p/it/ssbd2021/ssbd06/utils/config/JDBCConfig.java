package pl.lodz.p.it.ssbd2021.ssbd06.utils.config;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;
import java.sql.Connection;

/**
 * Zawiera dane potrzebne do uwierzytelnienia w bazie danych
 */

@DataSourceDefinition(
        name = "java:jboss/jdbc/ssbd06authDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "root",
        password = "root",
        serverName = "localhost",
        portNumber = 5432,
        databaseName = "ssbd06",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED,
        transactional = true,
        maxPoolSize = 32,
        minPoolSize = 8)

@DataSourceDefinition(
        name = "java:jboss/jdbc/ssbd06mokDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "root",
        password = "root",
        serverName = "localhost",
        portNumber = 5432,
        databaseName = "ssbd06",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED,
        transactional = true,
        maxPoolSize = 32,
        minPoolSize = 8)

@DataSourceDefinition(
        name = "java:jboss/jdbc/ssbd06mohDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        user = "root",
        password = "root",
        serverName = "localhost",
        portNumber = 5432,
        databaseName = "ssbd06",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED,
        transactional = true,
        maxPoolSize = 32,
        minPoolSize = 8)

@Stateless
public class JDBCConfig {

}
