package com.arno.grow.web.mvc.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.arno.grow.user.web.db.DBConnectionManager.CREATE_USERS_TABLE_DDL_SQL;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/2/28 下午10:17
 * @version:
 */
public class ServletInitListener implements ServletContextListener {
    private static final Logger log = Logger
            .getLogger(ServletInitListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            log.info("Loading Derby Driver...");
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            initializeDatabase();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Could not load Derby Embedded Driver!", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("Servlet Context Destroyed");
        try {
            log.info("Shutting down Derby ...");
            DriverManager.getConnection("jdbc:derby:Databases/UserPlatformDB;shutdown=true");
        } catch (SQLException e) {
            log.info(e.getMessage());
        }
    }

    private void initializeDatabase() throws SQLException {
        Connection connection = null;
        try {
            log.info("Starting up Derby DB...");
            connection = DriverManager
                    .getConnection("jdbc:derby:Databases/UserPlatformDB;create=true");
            if (!schemaHasBeenInitialized(connection)) {
                initializeSchema(connection);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Could not connect to Derby Embedded DB!", e);
            throw e;
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private void initializeSchema(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(CREATE_USERS_TABLE_DDL_SQL);
    }


    private boolean schemaHasBeenInitialized(Connection con) throws SQLException {
        final DatabaseMetaData metaData = con.getMetaData();

        try (ResultSet tablesResultSet = metaData.getTables(
                null, null, null,
                new String[]{"TABLE"})) {
            while (tablesResultSet.next()) {
                final String tableName = tablesResultSet.getString("TABLE_NAME");
                if ("users".equalsIgnoreCase(tableName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
