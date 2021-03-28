package com.arno.grow.user.web.controller;

import com.arno.grow.user.web.model.BaseResult;
import com.arno.grow.user.web.model.req.ConverterRequest;
import com.arno.grow.user.web.model.req.UserRegisterRequest;
import com.arno.grow.user.web.model.resp.ConverterResponse;
import com.arno.grow.user.web.model.resp.UserResponse;
import com.arno.grow.user.web.service.UserInfoService;
import com.arno.learn.grow.tiny.web.annotation.Autowired;
import com.arno.learn.grow.tiny.web.annotation.WebController;
import com.arno.learn.grow.tiny.web.annotation.WebRequestMapping;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/2/28 下午4:40
 * @version:
 */
@WebController
@WebRequestMapping("user")
public class UserController {

    @Autowired("bean/UserService")
    private UserInfoService userInfoService;


    @GET
    @WebRequestMapping(value = "home")
    public String registerPage() {
        return "home.jsp";
    }

    @GET
    @POST
    @WebRequestMapping(value = "register")
    public BaseResult<Void> register(UserRegisterRequest request) {
        return userInfoService.saveUser(request);
    }

    @GET
    @POST
    @WebRequestMapping(value = "goRegister")
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

    @POST
    @Path("getApplication")
    public BaseResult<List<ConverterResponse>> getApplication(ConverterRequest request) {
        return userInfoService.getConfigValue(request);
    }

}
