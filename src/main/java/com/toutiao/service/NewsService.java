package com.toutiao.service;

import com.toutiao.dao.NewsDAO;
import com.toutiao.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by snowWave.
 */
@Service
public class NewsService {

    @Autowired
    private NewsDAO newsDAO;

    public List<News> getLatestNews(int userId, int offset,int limit){
        return newsDAO.selectByUserIdAndOffset(userId,offset,limit);
    }

}
