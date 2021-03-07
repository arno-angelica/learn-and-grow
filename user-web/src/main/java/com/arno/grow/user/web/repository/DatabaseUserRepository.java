package com.arno.grow.user.web.repository;

import com.arno.grow.user.web.db.DbManager;
import com.arno.grow.user.web.repository.domain.User;
import com.arno.learn.grow.tiny.web.annotation.Autowired;
import com.arno.learn.grow.tiny.web.annotation.Service;
import com.arno.learn.grow.tiny.web.function.ThrowableFunction;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.commons.lang.ClassUtils.wrapperToPrimitive;


@Service("bean/DatabaseUserRepository")
public class DatabaseUserRepository implements UserRepository {

    private static final Logger logger = Logger.getLogger(DatabaseUserRepository.class.getName());

    /**
     * 通用处理方式
     */
    private static final Consumer<Throwable> COMMON_EXCEPTION_HANDLER = e -> logger.log(Level.SEVERE, e.getMessage());

    public static final String INSERT_USER_DML_SQL = "INSERT INTO users(name,password,email,phoneNumber) VALUES " +
                    "(?,?,?,?)";
    public static final String DELETE_USER_DML_SQL = "DELETE FROM users";

    public static final String QUERY_ALL_USERS_DML_SQL = "SELECT id,name,password,email,phoneNumber FROM users";

    @Autowired("bean/DbManager")
    private DbManager dbManager;

    @Autowired("bean/EntityManager")
    private EntityManager entityManager;
    @Override
    public boolean save(User user) {
        try {
            PreparedStatement statement = dbManager.getConnection().prepareStatement(INSERT_USER_DML_SQL);
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhoneNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            return false;
        } finally {
            dbManager.release();
        }
        return true;
    }

    public boolean saveTransaction(User user) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            entityManager.persist(user);
//            System.out.println(1/0);
        } catch (Exception e) {
            transaction.rollback();
            return false;
        }
        transaction.commit();
        return true;
    }

    @Override
    public boolean deleteById(Long userId) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public User getById(Long userId) {
        return null;
    }

    @Override
    public User getByNameAndPassword(String userName, String password) {
        return executeQuery("SELECT id,name,password,email,phoneNumber FROM users WHERE name=? and password=?",
                resultSet -> {
                    BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class, Object.class);
                    User user = null;
                    while (resultSet.next()) {
                        user = new User(); // 如果存在并且游标滚动 // SQLException
                        for (PropertyDescriptor propertyDescriptor : userBeanInfo.getPropertyDescriptors()) {
                            String fieldName = propertyDescriptor.getName();
                            Class fieldType = propertyDescriptor.getPropertyType();
                            String methodName = resultSetMethodMappings.get(fieldType);
                            // 可能存在映射关系（不过此处是相等的）
                            String columnLabel = mapColumnLabel(fieldName);
                            Method resultSetMethod = ResultSet.class.getMethod(methodName, String.class);
                            // 通过放射调用 getXXX(String) 方法
                            Object resultValue = resultSetMethod.invoke(resultSet, columnLabel);
                            // 获取 User 类 Setter方法
                            // PropertyDescriptor ReadMethod 等于 Getter 方法
                            // PropertyDescriptor WriteMethod 等于 Setter 方法
                            Method setterMethodFromUser = propertyDescriptor.getWriteMethod();
                            // 以 id 为例，  user.setId(resultSet.getLong("id"));
                            setterMethodFromUser.invoke(user, resultValue);
                        }
                    }
                    return user;
                }, COMMON_EXCEPTION_HANDLER, userName, password);
    }

    @Override
    public List<User> getAll() {
        return executeQuery("SELECT id,name,password,email,phoneNumber FROM users", resultSet -> {
            // BeanInfo -> IntrospectionException
            BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class, Object.class);
            List<User> users = new ArrayList<>();
            while (resultSet.next()) { // 如果存在并且游标滚动 // SQLException
                User user = new User();
                for (PropertyDescriptor propertyDescriptor : userBeanInfo.getPropertyDescriptors()) {
                    String fieldName = propertyDescriptor.getName();
                    Class fieldType = propertyDescriptor.getPropertyType();
                    String methodName = resultSetMethodMappings.get(fieldType);
                    // 可能存在映射关系（不过此处是相等的）
                    String columnLabel = mapColumnLabel(fieldName);
                    Method resultSetMethod = ResultSet.class.getMethod(methodName, String.class);
                    // 通过放射调用 getXXX(String) 方法
                    Object resultValue = resultSetMethod.invoke(resultSet, columnLabel);
                    // 获取 User 类 Setter方法
                    // PropertyDescriptor ReadMethod 等于 Getter 方法
                    // PropertyDescriptor WriteMethod 等于 Setter 方法
                    Method setterMethodFromUser = propertyDescriptor.getWriteMethod();
                    // 以 id 为例，  user.setId(resultSet.getLong("id"));
                    setterMethodFromUser.invoke(user, resultValue);
                }
                users.add(user);
            }
            return users;
        }, e -> {
            // 异常处理
        });
    }

    @Override
    public boolean deleteAll() {
        try {
            PreparedStatement statement = dbManager.getConnection().prepareStatement(DELETE_USER_DML_SQL);
            statement.executeUpdate();
        } catch (SQLException e) {
            return false;
        } finally {
            dbManager.release();
        }
        return false;
    }

    /**
     * @param sql
     * @param function
     * @param <T>
     * @return
     */
    protected <T> T executeQuery(String sql, ThrowableFunction<ResultSet, T> function,
                                 Consumer<Throwable> exceptionHandler, Object... args) {
        Connection connection = dbManager.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                Class<?> argType = arg.getClass();

                Class<?> wrapperType = wrapperToPrimitive(argType);

                if (wrapperType == null) {
                    wrapperType = argType;
                }

                // Boolean -> boolean
                String methodName = preparedStatementMethodMappings.get(argType);
                Method method = PreparedStatement.class.getMethod(methodName, wrapperType);
                int j = i + 1;
                method.invoke(preparedStatement, j, arg);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            // 返回一个 POJO List -> ResultSet -> POJO List
            // ResultSet -> T
            return function.apply(resultSet);
        } catch (Throwable e) {
            exceptionHandler.accept(e);
        } finally {
            dbManager.release();
        }
        return null;
    }


    private static String mapColumnLabel(String fieldName) {
        return fieldName;
    }

    /**
     * 数据类型与 ResultSet 方法名映射
     */
    static Map<Class<?>, String> resultSetMethodMappings = new HashMap<>();

    static Map<Class<?>, String> preparedStatementMethodMappings = new HashMap<>();

    static {
        resultSetMethodMappings.put(Long.class, "getLong");
        resultSetMethodMappings.put(String.class, "getString");
        preparedStatementMethodMappings.put(Long.class, "setLong"); // long
        preparedStatementMethodMappings.put(String.class, "setString"); //

    }
}