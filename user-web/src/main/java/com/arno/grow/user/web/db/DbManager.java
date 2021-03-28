package com.arno.grow.user.web.db;

import com.arno.learn.grow.tiny.core.util.CustomerAQSLock;
import com.arno.learn.grow.tiny.web.annotation.Autowired;
import com.arno.learn.grow.tiny.web.annotation.DbRepository;
import org.apache.commons.lang.StringUtils;
import org.eclipse.microprofile.config.Config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/3/2 下午7:42
 * @version:
 */
@DbRepository("bean/DbManager")
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

    @Autowired
    private Config config;

    @PostConstruct
    public void init() {
        // 读取配置文件
        initFiled();
    }

    public void initFiled() {
        try {
            String driver = config.getValue(JDBC_DRIVER, String.class);
            String url = config.getValue(JDBC_URL, String.class);
            Class.forName(driver);
            this.connection = DriverManager
                    .getConnection(url);

            String tableName = config.getValue(TABLE_NAME, String.class);
            String createSql = config.getValue(CREATE_SQL, String.class);
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


    @PreDestroy
    public void destroy() throws SQLException {
        if (connection != null) {
            connection.close();
        }
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
