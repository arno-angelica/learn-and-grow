package com.arno.boot.oauth2.service;

import com.arno.boot.oauth2.auth.GitHubOAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static com.arno.boot.oauth2.service.GiteeServiceImpl.httpEntity;

/**
 * @Author: lingquan.liu@quvideo.com
 * @Date: 2021/4/21 13:28
 * @Description:
 */
@Service
public class GithubServiceImpl implements BaseOauthService {
    @Autowired
     private GitHubOAuth gitHubOAuth;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String authorizeUri() {
        return gitHubOAuth.authorize();
    }

    @Override
    public String getAccessToken(String code) {
        String token = gitHubOAuth.accessToken(code);
        ResponseEntity<Object> forEntity = restTemplate.exchange(token, HttpMethod.GET, httpEntity(), Object.class);
        return Objects.requireNonNull(forEntity.getBody()).toString();
    }

}
