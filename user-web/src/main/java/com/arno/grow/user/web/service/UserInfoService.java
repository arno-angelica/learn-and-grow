package com.arno.grow.user.web.service;

import com.arno.grow.user.web.model.BaseResult;
import com.arno.grow.user.web.model.req.UserRegisterRequest;
import com.arno.grow.user.web.model.resp.UserResponse;
import com.arno.grow.user.web.repository.DatabaseUserRepository;
import com.arno.grow.user.web.repository.domain.User;
import com.arno.learn.grow.tiny.web.annotation.Autowired;
import com.arno.learn.grow.tiny.web.annotation.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: lingquan.liu@quvideo.com
 * @Date: 2021/3/2 13:11
 * @Description:
 */
@Service("bean/UserService")
public class UserInfoService {

    @Autowired("bean/DatabaseUserRepository")
    private DatabaseUserRepository databaseUserRepository;

    @Autowired("bean/Validator")
    private Validator validator;


    public String saveUser(UserRegisterRequest request) {
        User user = databaseUserRepository.getByNameAndPassword(request.getName(), request.getPassword());
        if (user != null) {
            return "success.jsp";
        }
        user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(request.getPassword());
//        databaseUserRepository.save(user);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations != null && violations.size() > 0) {
            ConstraintViolation<User> obj = new ArrayList<>(violations).get(0);
            return obj.getPropertyPath().toString() + obj.getMessage();
        }
        databaseUserRepository.saveTransaction(user);
        return "success.jsp";
    }

    public BaseResult<List<UserResponse>> getAllUser() {
        List<UserResponse> responses = null;
        List<User> users = databaseUserRepository.getAll();
        if (users != null && users.size() > 0) {
            responses = new ArrayList<>();
            for (User user : users) {
                UserResponse resp = new UserResponse();
                resp.setEmail(user.getEmail());
                resp.setPhoneNumber(user.getPhoneNumber());
                resp.setPassword(user.getPassword());
                resp.setName(user.getName());
                resp.setId(user.getId());
                responses.add(resp);
            }
        }
        return new BaseResult<>(responses);
    }

    public BaseResult<Object> deleteAll() {
        databaseUserRepository.deleteAll();
        return new BaseResult<>(null);
    }



}
