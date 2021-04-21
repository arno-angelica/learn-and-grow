package com.arno.boot.oauth2.service;


/**
 * @Author: lingquan.liu@quvideo.com
 * @Date: 2021/4/21 13:26
 * @Description:
 */
public interface BaseOauthService {
    String authorizeUri();

    String getAccessToken(String code);
}
