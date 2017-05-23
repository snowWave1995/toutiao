package com.toutiao;

import com.toutiao.dao.NewsDAO;
import com.toutiao.dao.UserDAO;
import com.toutiao.model.News;

import com.toutiao.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
//@WebAppConfiguration  这行会修改默认的启动路径需要注释掉
@Sql({"/init-schema.sql"})
public class InitDatabaseTests {

    @Autowired
    NewsDAO newsDAO;

    @Autowired
    UserDAO userDAO;

    @Test
    public void InitDataBase() {
        Random r = new Random();
        News news = new News();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setName(String.format("USER%d", i));
            user.setHeadUrl(String.format("http://images.toutiao.com/head/%dt.png", r.nextInt(1000)));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);

            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * 5 * i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.toutiao.com/head/%dm.png", r.nextInt(1000)));
            news.setLikeCount(i + 1);
            news.setLink(String.format("http://www.toutiao.com/link/{%d}.html", i));
            news.setTitle(String.format("Title {%d} ", i));
            news.setUserId(i+1);
            newsDAO.addNews(news);
            System.out.println(news.getId());

            user.setPassword("newpassword");
            userDAO.updatePassword(user);
        }

        Assert.assertEquals("newpassword", userDAO.selectById(1).getPassword());
        userDAO.deleteById(1);
        Assert.assertNull(userDAO.selectById(1));
    }
}
