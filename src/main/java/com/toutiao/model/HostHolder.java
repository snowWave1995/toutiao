package com.toutiao.model;

import org.springframework.stereotype.Component;

/**
 * 线程存一个用户，即一个线程
 * Created by snowWve.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();
    }
}
