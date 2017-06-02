package com.toutiao.async;

/**
 * Created by snowWave on 2017/6/2
 */
public enum EventType {
    LIKE(0),        //喜欢
    COMMENT(1),     //评论
    LOGIN(2),       //登录
    MAIL(3);        //邮件

    private int value;
    EventType(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
