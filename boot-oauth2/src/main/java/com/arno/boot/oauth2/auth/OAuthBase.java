package com.arno.boot.oauth2.auth;

/**
 * @Author: lingquan.liu@quvideo.com
 * @Date: 2021/4/21 13:23
 * @Description:
 */
public interface OAuthBase {
    /**
     * 授权地址
     *
     * @return 授权地址
     */
    String authorize();

    /**
     * 获取accessToken
     *
     * @param code 请求编码
     * @return accessToken
     */
    String accessToken(String code);

    /**
     * 获取用户信息
     *
     * @param accessToken token
     * @return user
     */
    String userInfo(String accessToken);
}
