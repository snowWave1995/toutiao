package com.toutiao.controller;

import com.toutiao.async.EventModel;
import com.toutiao.async.EventProducer;
import com.toutiao.async.EventType;
import com.toutiao.model.EntityType;
import com.toutiao.model.HostHolder;
import com.toutiao.model.News;
import com.toutiao.service.LikeService;
import com.toutiao.service.NewsService;
import com.toutiao.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by snowWave on 2017/6/2
 */
@Controller
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);
    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    NewsService newsService;

    @Autowired
    EventProducer eventProducer;


    /**
     * 点赞
     * @param nid
     * @return
     */
    @RequestMapping(path = {"/like"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String like(@Param("nid") Integer nid) {
        //like数量
        int likeCount = (int)likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS, nid);
        // 更新喜欢数

        News news = newsService.getById(nid);
        newsService.updateLikeCount(nid, (int) likeCount);

        //异步解决 方法都是返回this，所以可以使用调用链
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                .setEntityOwnerId(news.getUserId())
                .setActorId(hostHolder.getUser().getId()).setEntityId(nid));

        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }


    /**
     * 点踩
     * @param nid
     * @return
     */
    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String dislike(@Param("nid") Integer nid) {

        int likeCount = (int)likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_NEWS, nid);
        // 更新喜欢数
        newsService.updateLikeCount(nid, (int) likeCount);
        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
