package com.toutiao.util;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.velocity.VelocityProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeUtility;

import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;

/**
 * Created by snowWave on 2017/6/2
 */
@Service
public class MailSender implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;

    @Autowired
    private VelocityEngine velocityEngine;


    /**
     * 发送html邮件
     * @param to
     * @param subject
     * @param template
     * @param model
     * @return
     */
    public boolean sendWithHTMLTemplate(String to, String subject,
                                        String template, Map<String, Object> model) {
        try {
            //昵称
            String nick = MimeUtility.encodeText("头条管理站");
            //发件人
            InternetAddress from = new InternetAddress(nick + "<2450756004@qq.com>");
            //构造邮件
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            //填充邮件内容
            String result = VelocityEngineUtils
                    .mergeTemplateIntoString(velocityEngine, template, "UTF-8", model);

            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);//“登录异常”
            mimeMessageHelper.setText(result, true);
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }

    @Override //bean初始化时调用此方法
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();

        // 设置个人邮箱信息（收件者）
        mailSender.setUsername("2450756004@qq.com");
        mailSender.setPassword("hsbykusgcnaidiei");
        mailSender.setHost("smtp.qq.com");//发送的服务器
        mailSender.setPort(465);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);
    }
}
