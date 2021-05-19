package com.arno.grow.user.web.hash;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arno
 * @since
 */
public class Node {
    private final String domain;

    private String ip;
    private int count = 0;

    private Map<String, Object> data = new HashMap<>();

    public Node(String domain) {
        this.domain= domain;
        //this.ip=ip;
    }
    public <T> void put(String key,String value) {
        data.put(key,value);
        count++;
    }

    public void remove(String key){
        data.remove(key);
        count--;
    }

    public <T> T get(String key) {
        return (T) data.get(key);
    }

    public int getCount() {
        return count;
    }

    public String getName() {
        return domain;
    }
}
