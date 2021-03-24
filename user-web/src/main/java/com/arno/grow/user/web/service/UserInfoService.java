package com.arno.grow.user.web.service;

import com.arno.grow.user.web.constant.ErrorCode;
import com.arno.grow.user.web.model.BaseResult;
import com.arno.grow.user.web.model.req.ConverterRequest;
import com.arno.grow.user.web.model.req.UserRegisterRequest;
import com.arno.grow.user.web.model.resp.ConverterResponse;
import com.arno.grow.user.web.model.resp.UserResponse;
import com.arno.grow.user.web.repository.DatabaseUserRepository;
import com.arno.grow.user.web.repository.domain.User;
import com.arno.learn.grow.tiny.core.util.StringUtils;
import com.arno.learn.grow.tiny.web.annotation.Autowired;
import com.arno.learn.grow.tiny.web.annotation.Service;
import org.eclipse.microprofile.config.Config;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @Author: lingquan.liu@quvideo.com
 * @Date: 2021/3/2 13:11
 * @Description:
 */
@Service("bean/UserService")
public class UserInfoService {

    private static final Map<String, Class<?>> classTypeMap = new HashMap<>();
    private static final String NAME = "application.name";

    private Logger logger = Logger.getLogger(UserInfoService.class.getName());

    static {
        classTypeMap.put("string", String.class);
        classTypeMap.put("integer", Integer.class);
        classTypeMap.put("byte", Byte.class);
        classTypeMap.put("bigdecimal", BigDecimal.class);
        classTypeMap.put("biginteger", BigInteger.class);
        classTypeMap.put("character", Character.class);
        classTypeMap.put("double", Double.class);
        classTypeMap.put("float", Float.class);
        classTypeMap.put("long", Long.class);
        classTypeMap.put("short", Short.class);
    }

    @Autowired("bean/DatabaseUserRepository")
    private DatabaseUserRepository databaseUserRepository;

    @Autowired("bean/Validator")
    private Validator validator;

    @Autowired
    private Config config;


    public BaseResult<Void> saveUser(UserRegisterRequest request) {
        User user = databaseUserRepository.getByNameAndPassword(request.getName(), request.getPassword());
        if (user != null) {
            return new BaseResult<>();
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
            String message = obj.getMessage();
            return BaseResult.createFail(ErrorCode.PARAM_ERROR, message);
        }
        databaseUserRepository.saveTransaction(user);
        return new BaseResult<>();
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

    public BaseResult<List<ConverterResponse>> getConfigValue(ConverterRequest request) {
        if (StringUtils.hasText(request.getType())) {

            Class<?> clazz = classTypeMap.get(request.getType().toLowerCase());
            Object value = this.config.getValue(NAME + "." + request.getType().toLowerCase(), clazz);
            logger.info("配置结果为" + value);
            ConverterResponse resp = new ConverterResponse();
            resp.setKey(NAME);
            resp.setValue(value);

            Object application = this.config.getValue("servletContext.application.name", String.class);
            logger.info("servletContext.application.name" + application);
            ConverterResponse respServlet = new ConverterResponse();
            respServlet.setKey("context-param servletContext.application.name");
            respServlet.setValue(application);

            List<ConverterResponse> configs = new ArrayList<>();
            configs.add(resp);
            configs.add(respServlet);
            return new BaseResult<>(configs);
        }
        return BaseResult.createFail(ErrorCode.PARAM_ERROR, "type 不能为空");

    }

}
