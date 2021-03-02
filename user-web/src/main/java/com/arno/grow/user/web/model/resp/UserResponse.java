package com.arno.grow.user.web.model.resp;

import java.io.Serializable;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/2 下午8:52
 * @version:
 */
public class UserResponse implements Serializable {

    private static final long serialVersionUID = 5772101474302802572L;
    private Long id;

    private String name;

    private String password;

    private String email;

    private String phoneNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
