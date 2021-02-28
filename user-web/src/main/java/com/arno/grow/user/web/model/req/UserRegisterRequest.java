package com.arno.grow.user.web.model.req;

import java.io.Serializable;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/2/28 下午4:49
 * @version:
 */
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 2022045741415351315L;
    private String name;

    private String password;

    private String email;

    private String phoneNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
