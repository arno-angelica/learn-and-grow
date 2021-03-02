package com.arno.grow.user.web.controller;

import com.arno.grow.user.web.model.BaseResult;
import com.arno.grow.user.web.model.req.UserRegisterRequest;
import com.arno.grow.user.web.model.resp.UserResponse;
import com.arno.grow.user.web.service.UserInfoService;
import com.arno.grow.web.mvc.annotation.Autowired;
import com.arno.grow.web.mvc.annotation.WebController;
import com.arno.grow.web.mvc.annotation.WebRequestMapping;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

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
    @WebRequestMapping(value = "home", page = true)
    public String registerPage() {
        return "home.jsp";
    }

    @GET
    @POST
    @WebRequestMapping(value = "register", page = true)
    public String register(UserRegisterRequest request) {
        userInfoService.saveUser(request);
        return "success.jsp";
    }

    @GET
    @POST
    @WebRequestMapping(value = "goRegister", page = true)
    public String goRegister(UserRegisterRequest request) {
        return "register-form.jsp";
    }

    @GET
    @POST
    @Path("getAll")
    public BaseResult<List<UserResponse>> getAll() {
        return userInfoService.getAllUser();
    }

    @GET
    @POST
    @Path("deleteAll")
    public BaseResult<Object> deleteAll() {
        return userInfoService.deleteAll();
    }

}
