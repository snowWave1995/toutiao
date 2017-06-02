package com.toutiao.async;

import java.util.List;

/**
 * Created by snowWave on 2017/6/2
 */
public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();
}
