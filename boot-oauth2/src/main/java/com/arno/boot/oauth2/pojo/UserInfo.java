package com.arno.boot.oauth2.pojo;

import lombok.Data;

/**
 * @Author: lingquan.liu@quvideo.com
 * @Date: 2021/4/21 13:31
 * @Description:
 */
@Data
public class UserInfo {
    private Integer id;
    private String avatarUrl;
    private String login;
    private String bio;
    private String createdAt;
    private String htmlUrl;
}
