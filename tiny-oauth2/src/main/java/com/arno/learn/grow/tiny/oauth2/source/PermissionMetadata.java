package com.arno.learn.grow.tiny.oauth2.source;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author: lingquan.liu@quvideo.com
 * @Date: 2021/4/20 13:59
 * @Description:
 */
public class PermissionMetadata implements Serializable {
    private static final long serialVersionUID = -891346513492635813L;
    private List<String> paths;
    private List<Method> methods;

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(List<Method> methods) {
        this.methods = methods;
    }

    static class RolePermission implements Serializable {
        private static final long serialVersionUID = -3167961034378546569L;
        private String role;
        private List<String> paths;
        private List<Method> methods;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public List<String> getPaths() {
            return paths;
        }

        public void setPaths(List<String> paths) {
            this.paths = paths;
        }

        public List<Method> getMethods() {
            return methods;
        }

        public void setMethods(List<Method> methods) {
            this.methods = methods;
        }
    }

    static class UserRole implements Serializable {
        private List<String> role;
        private String user;

        public List<String> getRole() {
            return role;
        }

        public void setRole(List<String> role) {
            this.role = role;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }
    }

}
