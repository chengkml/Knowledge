package com.ck.knowledge.service;

import com.ck.knowledge.properties.ExerciseMailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

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

    public void sendHTMLMail(String title, String html) {
        MimeMessage mimeMailMessage = null;
        try {
            mimeMailMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);
            mimeMessageHelper.setFrom(mailProperties.getSender());
            mimeMessageHelper.setTo(mailProperties.getReciever());
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(html, true);
            javaMailSender.send(mimeMailMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
