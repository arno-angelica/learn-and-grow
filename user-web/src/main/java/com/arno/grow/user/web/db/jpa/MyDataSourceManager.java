package com.arno.grow.user.web.db.jpa;

import com.arno.learn.grow.tiny.web.annotation.Configuration;
import com.arno.learn.grow.tiny.web.annotation.Value;
import org.apache.commons.dbcp.BasicDataSource;

import java.io.Serializable;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/7 下午7:17
 * @version:
 */
@Configuration("bean/MyDataSourceManager")
public class MyDataSourceManager implements Serializable {
    private static final long serialVersionUID = 9194842997994442526L;


    @Value("@jdbc.driver")
    private String driver;

    @Value("@jdbc.url")
    private String jdbcUrl;

    @Value("@jdbc.max.active")
    private String maxActive;

    @Value("@jdbc.max.idle")
    private String maxIdle;

    @Value("@jdbc.max.wait")
    private String maxWait;

    public BasicDataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(jdbcUrl);
        dataSource.setMaxActive(Integer.parseInt(maxActive));
        dataSource.setMaxIdle(Integer.parseInt(maxIdle));
        dataSource.setMaxWait(Integer.parseInt(maxWait));
        return dataSource;
    }
}
