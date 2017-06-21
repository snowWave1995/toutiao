package com.toutiao.async;

import com.alibaba.fastjson.JSON;
import com.toutiao.util.JedisAdapter;
import com.toutiao.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by snowWave on 2017/6/2
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {   //消费者


    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);


    private Map<EventType, List<EventHandler>> config = new HashMap<>();//一个eventType对应几个handler

    private ApplicationContext applicationContext;  //记录上下文


    @Autowired
    private JedisAdapter jedisAdapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        //获取所有注册的handler
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);

        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {

                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();

                //把每个eventType作为map的key
                for (EventType type : eventTypes) {
                    if (!config.containsKey(type)) {
                        config.put(type, new ArrayList<EventHandler>());
                    }

                    // 注册每个事件的处理函数
                    config.get(type).add(entry.getValue());
                }
            }
        }

        // 启动线程去消费事件
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // 从队列一直消费
                while (true) {
                    String key = RedisKeyUtil.getEventQueueKey();

                    //从右边取出（从左边加入的）,这里list里存的是一个或几个map(一个String代表一个map)
                    List<String> messages = jedisAdapter.brpop(0, key);
                    // 第一个元素是队列名字（String的第一个元素是type，也就是map的key）
                    for (String message : messages) {
                        if (message.equals(key)) {
                            continue;
                        }

                        //将从右边取出的message转化为可识别的eventModel
                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        // 找到这个事件的处理handler列表
                        if (!config.containsKey(eventModel.getType())) {
                            logger.error("不能识别的事件");
                            continue;
                        }

                        for (EventHandler handler : config.get(eventModel.getType())) {
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();
    }



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
