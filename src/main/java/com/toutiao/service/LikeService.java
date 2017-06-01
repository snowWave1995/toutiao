package com.toutiao.service;

import com.toutiao.util.JedisAdapter;
import com.toutiao.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by snowWave on 2017/6/2
 */
@Service
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;


    /**
     * 判断某个用户是否喜欢某个元素
     * @param userId
     * @param entityType entity和entityId唯一确定一个评论
     * @param entityId
     * @return
     */
    public int getLikeStatus(int userId, int entityType, int entityId) {
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        if(jedisAdapter.sismember(likeKey, String.valueOf(userId))) {
            return 1;//喜欢返回1
        }
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        return jedisAdapter.sismember(disLikeKey, String.valueOf(userId)) ? -1 : 0;
    }


    /**
     *  点赞
     * @param userId
     * @param entityType
     * @param entityId
     * @return 喜欢人数
     */
    public long like(int userId, int entityType, int entityId) {
        // 在喜欢集合里增加
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));
        // 从反对里删除
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        jedisAdapter.srem(disLikeKey, String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }


    /**
     * 点踩
     * @param userId
     * @param entityType
     * @param entityId
     * @return 喜欢人数
     */
    public long disLike(int userId, int entityType, int entityId) {
        // 在反对集合里增加
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));
        // 从喜欢里删除
        String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
        jedisAdapter.srem(likeKey, String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }
}

