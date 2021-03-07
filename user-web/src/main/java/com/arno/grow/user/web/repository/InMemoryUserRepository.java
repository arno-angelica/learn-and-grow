package com.arno.grow.user.web.repository;


import com.arno.grow.user.web.repository.domain.User;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存型 {@link UserRepository} 实现
 *
 * @since 1.0s
 */
@Deprecated
public class InMemoryUserRepository implements UserRepository {

    private Map<Long, User> repository = new ConcurrentHashMap<>();

    @Override
    public boolean save(User user) {
        return repository.put(user.getId(), user) == null;
    }

    @Override
    public boolean saveTransaction(User user) {
        return false;
    }

    @Override
    public boolean deleteById(Long userId) {
        return repository.remove(userId) != null;
    }

    @Override
    public boolean update(User user) {
        save(user);
        return true;
    }

    @Override
    public User getById(Long userId) {
        return repository.get(userId);
    }

    @Override
    public User getByNameAndPassword(String userName, String password) {
        return repository.values()
                .stream()
                .filter(user -> Objects.equals(userName, user.getName())
                        && Objects.equals(password, user.getPassword()))
                .findFirst()
                .get();
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public boolean deleteAll() {
        return false;
    }
}
