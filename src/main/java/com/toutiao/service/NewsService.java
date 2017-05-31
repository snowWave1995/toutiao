package com.toutiao.service;

import com.toutiao.dao.NewsDAO;
import com.toutiao.model.News;
import com.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Created by snowWave.
 */
@Service
public class NewsService {

    @Autowired
    private NewsDAO newsDAO;


    /**
     * 获取最新新闻
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    public List<News> getLatestNews(int userId, int offset,int limit){
        return newsDAO.selectByUserIdAndOffset(userId,offset,limit);
    }

    /**
     * 增加新闻
     * @param news
     * @return
     */
    public int addNews(News news) {
        newsDAO.addNews(news);
        return news.getId();
    }

    /**
     * 获取新闻
     * @param newsId
     * @return
     */
    public News getById(int newsId) {
        return newsDAO.getById(newsId);
    }


    /**
     * 保存图片，成功就返回图片本地地址
     * @param file
     * @return
     * @throws IOException
     */
    public String saveImage(MultipartFile file) throws IOException {

        //判断文件是否合法，通过文件后缀
        int dotPos = file.getOriginalFilename().lastIndexOf(".");//"."的位置
        if (dotPos < 0) {
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();

        //判断后缀
        if (!ToutiaoUtil.isFileAllowed(fileExt)) {
            return null;
        }

        //重写文件名字
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
        //拷贝到本地
        Files.copy(file.getInputStream(), new File(ToutiaoUtil.IMAGE_DIR + fileName).toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        return ToutiaoUtil.TOUTIAO_DOMAIN + "image?name=" + fileName;
    }

    public int updateCommentCount(int id, int count) {
        return newsDAO.updateCommentCount(id, count);
    }

}
