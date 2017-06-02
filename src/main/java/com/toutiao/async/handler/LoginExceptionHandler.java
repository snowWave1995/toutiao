package com.toutiao.async.handler;

import com.toutiao.async.EventHandler;
import com.toutiao.async.EventModel;
import com.toutiao.async.EventType;
import com.toutiao.controller.IndexController;
import com.toutiao.model.Message;
import com.toutiao.model.User;
import com.toutiao.service.MessageService;
import com.toutiao.service.UserService;
import com.toutiao.util.MailSender;
import com.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * Created by snowWave on 2017/6/2
 */
@Component
public class LoginExceptionHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    MailSender mailSender;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel event) {

        // 判断是否有异常登陆
        Message message = new Message();

        message.setToId(event.getActorId());
        message.setFromId(3);

        message.setContent("你上次的登陆ip异常");

        message.setHasRead(0);
        message.setCreatedDate(new Date());
        message.setConversationId(3 < event.getEntityOwnerId()? 3+"_"+event.getEntityOwnerId():event.getEntityOwnerId()+"_"+3);

        messageService.addMessage(message);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", event.getExt("username"));
        mailSender.sendWithHTMLTemplate(event.getExt("email"), "登陆异常", "mails/welcome.html",
                map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
