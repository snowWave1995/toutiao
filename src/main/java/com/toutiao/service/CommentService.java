package com.toutiao.service;

import com.toutiao.dao.CommentDAO;
import com.toutiao.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by snowWave on 2017/6/1
 */
@Service
public class CommentService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);

    @Autowired
    CommentDAO commentDAO;


    /**
     * 获取评论列表
     * @param entityId
     * @param entityType
     * @return
     */
    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentDAO.selectByEntity(entityId, entityType);
    }

    /**
     * 增加评论
     * @param comment
     * @return
     */
    public int addComment(Comment comment) {
        return commentDAO.addComment(comment);
    }


    /**
     *获取评论数量
     * @param entityId
     * @param entityType
     * @return
     */
    public int getCommentCount(int entityId, int entityType) {
        return commentDAO.getCommentCount(entityId, entityType);
    }
}
