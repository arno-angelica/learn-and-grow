package com.arno.grow.user.web.management;

import com.arno.learn.grow.tiny.web.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.Serializable;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/15 下午9:02
 * @version:
 */
@Configuration
public class JMXDataSource implements Serializable {
    private static final long serialVersionUID = -1422655261537479579L;
    private String driver;
    private String url;
    private String userName;
    private String password;

    @PostConstruct
    public void init() {
        this.driver = "org.apache.derby.jdbc.EmbeddedDriver";
        this.url = "jdbc:derby:db/UserPlatformDB;create=true";
        this.userName = "user_platform";
        this.password = "123456";
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
