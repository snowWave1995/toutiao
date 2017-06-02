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

        Message message = new Message();
        message.setFromId(3);  //登录异常由信息发给账户异常用户;
        message.setToId(event.getEntityOwnerId());
        User affectedUser = userService.getUser(event.getEntityOwnerId());
        System.out.println("==============" + event.getExt("datetime"));
        message.setContent( "[系统提示] " + affectedUser.getName() + ",您的账户于" + event.getExt("datetime") + "发生登录异常, 异常信息是: " + event.getExt("ExceptionInfo") + ". 请您注意账号安全!");
        message.setHasRead(0);
        message.setCreatedDate(new Date());
        message.setConversationId(3<event.getEntityOwnerId()?
                3+"_"+event.getEntityOwnerId():event.getEntityOwnerId()+"_"+3);
        messageService.addMessage(message);
        Map<String, Object> mailInfoMap = new HashMap<String, Object>();
        mailInfoMap.put("username", affectedUser.getName());
        mailSender.sendWithHTMLTemplate(event.getExt("email"), "账户异常提醒", "mails/loginException.html", mailInfoMap);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
