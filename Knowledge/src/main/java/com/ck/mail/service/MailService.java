package com.ck.mail.service;

import com.ck.exercise.properties.ExerciseMailProperties;
import org.quartz.spi.ThreadExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Title: MailService
 * @Author: Chengkai
 * @Date: 2019/9/19 13:31
 * @Version: 1.0
 */
@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ExerciseMailProperties mailProperties;

    private static final String APP_NAME = "知识管理";

    public void sendHTMLMail(String title, String html) throws MessagingException {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true, "utf-8");
        mimeMessageHelper.setFrom(new InternetAddress(APP_NAME+"<"+mailProperties.getSender()+">"));
        mimeMessageHelper.setTo(mailProperties.getReciever());
        mimeMessageHelper.setSubject(title);
        mimeMessageHelper.setText(html, true);
        javaMailSender.send(mimeMailMessage);
    }

    public void sendHTMLMail(String title, String html, String filePath) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true, "utf-8");
        mimeMessageHelper.setSubject(title);
        Pattern p = Pattern.compile("\\<img src=\\\"[\\S]+\\\"");
        Matcher m = p.matcher(html);
        while (m.find()) {
            String temp = m.group();
            String fileName = temp.substring(10, temp.length() - 1);
            html = html.replaceAll(fileName, "cid:" + fileName);
            mimeMessageHelper.addInline(fileName, new FileSystemResource(new File(filePath + File.separator + fileName)));
        }
        mimeMessageHelper.addInline("whatever", new ClassPathResource(""));
        mimeMessageHelper.setText(html, true);
        mimeMessageHelper.setSentDate(new Date());
        mimeMessageHelper.setFrom(new InternetAddress(mailProperties.getSender(),APP_NAME,"utf-8"));
        mimeMessageHelper.setTo(mailProperties.getReciever());
        javaMailSender.send(mimeMailMessage);
    }
}
