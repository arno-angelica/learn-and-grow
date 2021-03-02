package com.arno.grow.user.web.service;

import com.arno.grow.user.web.model.BaseResult;
import com.arno.grow.user.web.model.req.UserRegisterRequest;
import com.arno.grow.user.web.model.resp.UserResponse;
import com.arno.grow.user.web.repository.DatabaseUserRepository;
import com.arno.grow.user.web.repository.domain.User;
import com.arno.grow.web.mvc.annotation.Autowired;
import com.arno.grow.web.mvc.annotation.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lingquan.liu@quvideo.com
 * @Date: 2021/3/2 13:11
 * @Description:
 */
@Service
public class UserInfoService {

    @Autowired
    private DatabaseUserRepository databaseUserRepository;

    public void saveUser(UserRegisterRequest request) {
        User user = databaseUserRepository.getByNameAndPassword(request.getName(), request.getPassword());
        if (user != null) {
            return;
        }
        user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(request.getPassword());
        databaseUserRepository.save(user);
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
