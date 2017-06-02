package com.toutiao.async;

import com.alibaba.fastjson.JSONObject;
import com.toutiao.util.JedisAdapter;
import com.toutiao.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by snowWave on 2017/6/2
 */
@Service
public class EventProducer {

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel) {       //事件的生产者
        try {
            String json = JSONObject.toJSONString(eventModel);//json作为value
            String key = RedisKeyUtil.getEventQueueKey(); //获取事件key
            jedisAdapter.lpush(key, json); //放到队列里 list
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

