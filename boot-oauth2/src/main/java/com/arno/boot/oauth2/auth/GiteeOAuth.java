package com.arno.boot.oauth2.auth;

import org.springframework.stereotype.Component;

/**
 * @Author: lingquan.liu@quvideo.com
 * @Date: 2021/4/21 13:24
 * @Description:
 */
@Component
public class GiteeOAuth implements OAuthBase {
    private static final String GITEE_CLIENT_ID = "86e24f65eb90310d7d2730c525e82a197710a5f8c0b170015facca5d72ad5ec3";
    private static final String GITEE_CLIENT_SECRET = "85fd50887809b82b35a5fb91914a5f51f08dcdc31f7effc3abbcb037adddb9f3";

    private static final String REDIRECT_URI = "http://localhost:8080/gitee/callback";

    @Override
    public String authorize() {
        return "https://gitee.com/oauth/authorize?client_id=" + GITEE_CLIENT_ID + "&response_type=code&redirect_uri=" + REDIRECT_URI;
    }

    @Override
    public String accessToken(String code) {
        return "https://gitee.com/oauth/token?grant_type=authorization_code&code=" + code + "&client_id=" + GITEE_CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&client_secret=" + GITEE_CLIENT_SECRET;
    }

    @Override
    public String userInfo(String accessToken) {
        return "https://gitee.com/api/v5/user?access_token=" + accessToken;
    }
}
