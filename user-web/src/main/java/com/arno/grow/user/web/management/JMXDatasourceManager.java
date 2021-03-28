package com.arno.grow.user.web.management;

import com.arno.learn.grow.tiny.web.annotation.Service;

import javax.annotation.Resource;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/3/15 下午8:50
 * @version:
 */
@Service
public class JMXDatasourceManager implements JMXDatasourceManagerMXBean {

    @Resource
    private JMXDataSource jmxDataSource;

    public String getDriver() {
        return jmxDataSource.getDriver();
    }

    public void setDriver(String driver) {
        jmxDataSource.setDriver(driver);
    }

    public String getUrl() {
        return jmxDataSource.getUrl();
    }

    public void setUrl(String url) {
        jmxDataSource.setUrl(url);
    }

    public String getUserName() {
        return jmxDataSource.getUserName();
    }

    public void setUserName(String userName) {
        jmxDataSource.setUserName(userName);
    }

    public String getPassword() {
        return jmxDataSource.getPassword();
    }

    public void setPassword(String password) {
        jmxDataSource.setPassword(password);
    }
}
