package com.arno.grow.user.web.repository;


import com.arno.grow.user.web.repository.domain.User;

import java.util.Collection;
import java.util.List;

/**
 * 用户存储仓库
 *
 * @since 1.0
 */
public interface UserRepository {

    boolean save(User user);

    boolean deleteById(Long userId);

    boolean update(User user);

    User getById(Long userId);

    User getByNameAndPassword(String userName, String password);

    List<User> getAll();

    boolean deleteAll();
}
