package com.arno.grow.user.web.configuration;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @desc:
 * @author: Arno.KV
 * @date: 2021/3/11 下午9:08
 * @version:
 */
public class ConfigurationDemo {
    public static void main(String[] args) throws BackingStoreException {
        Preferences preferences = Preferences.userRoot();
        preferences.put("my-key", "hello");
        preferences.flush();
        System.out.println(preferences.get("my-key", null));
        preferences.remove("my-key");
    }
}
