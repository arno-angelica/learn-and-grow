package com.arno.learn.grow.tiny.monitor.bean.processor;

import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.RequiredModelMBean;

/**
 * @Author:
 * @Date: 2021/3/15 16:19
 * @Description:
 */
public class ModelMBeanCreator {

    public enum ManagedResource {
        MANAGED_OBJ("ObjectReference"),
        MANAGED_HANDLE("Handle"),
        MANAGED_IOR("IOR"),
        MANAGED_EJB("EJBHandle"),
        MANAGED_RMI("RMIReference"),
        ;
        private final String value;

        ManagedResource(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static RequiredModelMBean creatorModelBean(Object object, ManagedResource managedResource) throws Exception {
        RequiredModelMBean modelMBean = new RequiredModelMBean();
        modelMBean.setManagedResource(object, managedResource.value);
        ModelMBeanInfo info = assembleMBeanInfo(object);
        modelMBean.setModelMBeanInfo(info);
        return modelMBean;
    }

    private static ModelMBeanInfo assembleMBeanInfo(Object object) {

        return null;
    }

}
