package com.arno.grow.web.mvc.db;

import com.arno.grow.web.mvc.lock.CustomerAQSLock;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/2 下午7:42
 * @version:
 */
public class DbManager {


    private static final String JDBC_DRIVER = "jdbc.driver";
    private static final String JDBC_URL = "jdbc.url";
    private static final String TABLE_NAME = "table.name";
    private static final String CREATE_SQL = "create.table.sql";
    private static final CustomerAQSLock customerAQSLock;
    private Connection connection;

    static {
        customerAQSLock = new CustomerAQSLock();
    }

    public DbManager() {
    }

    public DbManager(Map<String, String> propertiesMap) {
        try {
            String driver = propertiesMap.get(JDBC_DRIVER);
            String url = propertiesMap.get(JDBC_URL);
            Class.forName(driver);
            this.connection = DriverManager
                    .getConnection(url);

            String tableName = propertiesMap.get(TABLE_NAME);
            String createSql = propertiesMap.get(CREATE_SQL);
            if (StringUtils.isNotBlank(tableName) && StringUtils.isNotBlank(createSql)
                    && !tableHasBeenInitialized(connection, tableName)) {
                initTable(createSql);
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("init jdbc connection error, cause " + e.getMessage());
        }
    }

    public Connection getConnection() {
        customerAQSLock.lock();
        return connection;
    }

    public void release() {
        customerAQSLock.unlock();
    }

    /**
     * 检测表是否存在
     *
     * @param con
     * @param table
     * @return
     * @throws SQLException
     */
    private boolean tableHasBeenInitialized(Connection con, String table) throws SQLException {
        final DatabaseMetaData metaData = con.getMetaData();

        try (ResultSet tablesResultSet = metaData.getTables(
                null, null, null,
                new String[]{"TABLE"})) {
            while (tablesResultSet.next()) {
                final String tableName = tablesResultSet.getString("TABLE_NAME");
                if (table.equalsIgnoreCase(tableName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 初始化表
     */
    private void initTable(String createSql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(createSql);
    }

}
