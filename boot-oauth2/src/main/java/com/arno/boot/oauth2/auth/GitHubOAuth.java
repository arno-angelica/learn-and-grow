package com.arno.boot.oauth2.auth;

import org.springframework.stereotype.Component;

/**
 * @Author: lingquan.liu@quvideo.com
 * @Date: 2021/4/21 13:25
 * @Description:
 */
@Component
public class GitHubOAuth implements OAuthBase {
    private static final String GITHUB_CLIENT_ID = "52c1293ba0e617d1dc9d";
    private static final String GITHUB_CLIENT_SECRET = "e0ab6de536cd711ae0692fe9a7462e1fd01fbbce";

    /**
     * 登陆授权类型
     */
    @Override
    public String authorize() {
        return "https://github.com/login/oauth/authorize?client_id=" + GITHUB_CLIENT_ID + "&scope=user,public_repo";
    }

    @Override
    public String accessToken(String code) {
        return "https://github.com/login/oauth/access_token?client_id=" + GITHUB_CLIENT_ID + "&client_secret=" + GITHUB_CLIENT_SECRET + "&code=" + code;
    }

    @Override
    public String userInfo(String accessToken) {
        return "https://api.github.com/user?access_token=" + accessToken;
    }
}
