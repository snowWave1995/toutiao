package com.toutiao.service;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.toutiao.util.ToutiaoUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
/**
 * Created by snowWave.
 */
@Service
public class QiniuService {

    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);

    private static String QINIU_IMAGE_DOMAIN = "http://oqm8cnjt1.bkt.clouddn.com/";

    Configuration cfg = new Configuration(Zone.zone1());
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "ep9wUBE6MhXxUYS0lEEpug5_Fn2pXIuttVi3BbXO";
    String SECRET_KEY = "dGItqQ4uEHD03I7IRAu3OLSdCNVBAh22gndRIzt_";
    //要上传的空间
    String bucketname = "toutiao";

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //创建上传对象
    UploadManager uploadManager = new UploadManager(cfg);


    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }


    /**
     * 保存图片到七牛云
     * @param file
     * @return
     * @throws IOException
     */
    public String saveImage(MultipartFile file) throws IOException {
        try {
            //判断文件名合法
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();//记得toLowerCase()
            if (!ToutiaoUtil.isFileAllowed(fileExt)) {
                return null;
            }

            //改文件名  和传本地方法一样
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;

            System.out.println(getUpToken());
            System.out.println(uploadManager);

            //调用put方法上传
            Response res = uploadManager.put(file.getBytes(), fileName, getUpToken());
            //打印返回的信息
            if (res.isOK() && res.isJson()) {
                return QINIU_IMAGE_DOMAIN + JSONObject.parseObject(res.bodyString()).get("key");
            } else {
                logger.error("七牛异常1:" + res.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            // 请求失败时打印的异常的信息
            logger.error("七牛异常2:" + e.getMessage());
            return null;
        }
    }
}
