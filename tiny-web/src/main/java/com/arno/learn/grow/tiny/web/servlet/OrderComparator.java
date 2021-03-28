package com.arno.learn.grow.tiny.web.servlet;

import java.util.Comparator;
import java.util.List;

/**
 * @Author: angelica
 * @Date: 2021/3/24 10:48
 * @Description:
 */
public class OrderComparator implements Comparator<ServletStartupInitializer> {
    public static final OrderComparator INSTANCE = new OrderComparator();
    @Override
    public int compare(ServletStartupInitializer o1, ServletStartupInitializer o2) {
        return Integer.compare(o1.order(), o2.order());
    }

    public static void sort(List<ServletStartupInitializer> list) {
        if (list.size() > 1) {
            list.sort(INSTANCE);
        }
    }
}
