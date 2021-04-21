package com.arno.boot.oauth2.service;

import com.arno.boot.oauth2.auth.GiteeOAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Author: lingquan.liu@quvideo.com
 * @Date: 2021/4/21 13:27
 * @Description:
 */
@Service
public class GiteeServiceImpl implements BaseOauthService {
    @Autowired
    private GiteeOAuth giteeOAuth;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String authorizeUri() {
        return giteeOAuth.authorize();
    }

    @Override
    public String getAccessToken(String code) {
        String token = giteeOAuth.accessToken(code);
        ResponseEntity<Object> entity = restTemplate.postForEntity(token, httpEntity(), Object.class);
        Object body = entity.getBody();
        assert body != null;
        return body.toString();
    }

    public static HttpEntity httpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

        HttpEntity<HttpHeaders> request = new HttpEntity<>(headers);
        return request;
    }
}
