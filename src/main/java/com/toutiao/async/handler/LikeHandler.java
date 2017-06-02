package com.toutiao.async.handler;

import com.toutiao.async.EventHandler;
import com.toutiao.async.EventModel;
import com.toutiao.async.EventType;
import com.toutiao.model.Message;
import com.toutiao.model.User;
import com.toutiao.service.MessageService;
import com.toutiao.service.UserService;
import com.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by snowWave on 2017/6/2
 */
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        //给被点赞用户发消息
        Message message = new Message();
        message.setFromId(3);
        message.setCreatedDate(new Date());
        int fromUid = model.getActorId();
        int toUid = model.getEntityOwnerId();
        message.setContent("用户" + (userService.getUser(fromUid)).getName() +  "赞了你发布的资讯," +
                " http://127.0.0.1:8080/news/"
                + model.getEntityId());
        message.setToId(model.getEntityOwnerId());
        message.setConversationId( 3<toUid ? 3+"_"+toUid : toUid+"_"+3);
        message.setHasRead(0);
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {  //只关心点赞行为
        return Arrays.asList(EventType.LIKE);
    }
}

