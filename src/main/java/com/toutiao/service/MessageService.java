package com.toutiao.service;

import com.toutiao.dao.MessageDAO;
import com.toutiao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by snowWave on 2017/6/1
 */
@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;


    /**
     * 增加一个对话
     * @param message
     * @return
     */
    public int addMessage(Message message) {
        return messageDAO.addMessage(message);
    }


    /**
     *获取会话列表
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> getConversationList(int userId, int offset, int limit) {
        // conversation的总条数存在id里
        return messageDAO.getConversationList(userId, offset, limit);
    }


    /**
     * 获取一个对话的所有内容
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        // conversation的总条数存在id里
        return messageDAO.getConversationDetail(conversationId, offset, limit);
    }


    /**
     * 获取未读条数
     * @param userId
     * @param conversationId
     * @return
     */
    public int getUnreadCount(int userId, String conversationId) {
        return messageDAO.getConversationUnReadCount(userId, conversationId);
    }
}
