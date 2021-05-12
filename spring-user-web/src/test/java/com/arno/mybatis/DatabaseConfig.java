package com.arno.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Arno
 * @since
 */
@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource demoDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        // 连接信息
        druidDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/mybatis_demo");
        druidDataSource.setDriverClassName("com.mysql.jdbc.Driver");
        druidDataSource.setPassword("1258989");
        druidDataSource.setUsername("root");
        druidDataSource.setDefaultAutoCommit(true);
        druidDataSource.setMaxActive(20);
        druidDataSource.setValidationQuery("SELECT 'x'");
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        druidDataSource.setPoolPreparedStatements(true);
        return druidDataSource;
    }
}
