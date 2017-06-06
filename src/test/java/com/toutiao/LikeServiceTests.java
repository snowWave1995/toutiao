package com.toutiao;

import com.toutiao.service.LikeService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by snowWave on 2017/6/6
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class LikeServiceTests {

    @Autowired
    LikeService likeService;



    @Test   //要跑的测试用例 2
    public void testLike(){
       likeService.like(123,1,1);
        Assert.assertEquals(1,likeService.getLikeStatus(123,1,1));
    }

    @Test   //要跑的测试用例 2
    public void testDislike(){
        likeService.disLike(123,1,1);
        Assert.assertEquals(-1,likeService.getLikeStatus(123,1,1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException(){

        throw new IllegalArgumentException("xxx");
    }

    @Before //初始化数据 1
    public void setUp(){

        System.out.println("setUp");

    }
    @After  //清理数据  3
    public void tearDown(){

        System.out.println("tearDown");

    }
    @BeforeClass  //跑所有测试用例之前，跑一次
    public static void beforeClass(){

        System.out.println("beforeClass");

    }
    @AfterClass     //跑所有测试用例之后，跑一次
    public static void afterClass(){

       System.out.println("afterClass");

    }

}
