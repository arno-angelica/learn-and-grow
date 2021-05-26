package com.arno.mybatis.boot.start.auto.entry;

import java.io.Serializable;

/**
 * @author Arno
 * @since
 */
public class Person implements Serializable {

    private static final long serialVersionUID = 7268289228492149983L;
    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
