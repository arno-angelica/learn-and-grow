package com.arno.grow.user.web.management;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/15 下午8:52
 * @version:
 */
 public interface JMXDatasourceManagerMXBean {

     String getDriver();

     void setDriver(String driver);

     String getUrl();

     void setUrl(String url);

     String getUserName();

     void setUserName(String userName);

     String getPassword();

     void setPassword(String password);
}
