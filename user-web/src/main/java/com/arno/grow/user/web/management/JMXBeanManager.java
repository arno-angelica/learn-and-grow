package com.arno.grow.user.web.management;

import com.arno.grow.user.web.repository.domain.User;
import com.arno.learn.grow.tiny.web.annotation.Autowired;
import com.arno.learn.grow.tiny.web.annotation.Service;

import javax.annotation.PostConstruct;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * @desc:
 * @author: angelica
 * @date: 2021/3/15 下午9:10
 * @version:
 */
@Service
public class JMXBeanManager {

    @Autowired
    private JMXDatasourceManager jmxDatasourceManager;

    @PostConstruct
    public void init() {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        // 为 UserMXBean 定义 ObjectName
        ObjectName objectName = null;
        try {
            objectName = new ObjectName("com.arno.grow.user.web.management:type=UserManager");
            // 创建 UserMBean 实例
            User user = new User();
            mBeanServer.registerMBean(createUserMBean(user), objectName);
            objectName = new ObjectName("com.arno.grow.user.web.management:type=JMXDatasourceManager");
            mBeanServer.registerMBean(jmxDatasourceManager, objectName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static Object createUserMBean(User user) throws Exception {
        return new UserManager(user);
    }

}
