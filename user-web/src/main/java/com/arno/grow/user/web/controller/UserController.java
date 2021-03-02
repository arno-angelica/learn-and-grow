package com.arno.grow.user.web.controller;

import com.arno.grow.user.web.model.BaseResult;
import com.arno.grow.user.web.model.req.UserRegisterRequest;
import com.arno.grow.user.web.server.UserInfoService;
import com.arno.grow.web.mvc.annotation.Autowired;
import com.arno.grow.web.mvc.annotation.WebController;
import com.arno.grow.web.mvc.annotation.WebRequestMapping;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/2/28 下午4:40
 * @version:
 */
@WebController
@WebRequestMapping("user")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;


    @GET
    @WebRequestMapping(value = "register/page", page = true)
    public String registerPage() {
        return "register-form.jsp";
    }

    @GET
    @POST
    @WebRequestMapping(value = "register", page = true)
    public String register(UserRegisterRequest request) {
        return "success.jsp";
    }

    @GET
    @POST
    @Path("register2")
    public BaseResult register2(UserRegisterRequest request) {
        userInfoService.test();
        return new BaseResult();
    }

    @POST
    @Path("query")
    public BaseResult query(UserRegisterRequest request) {
        return new BaseResult();
    }
}
